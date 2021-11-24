package org.wangpai.wchat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.server.external.api.ServerApi;
import org.wangpai.wchat.universal.protocol.internal.client.ClientConfig;
import org.wangpai.wchat.universal.protocol.internal.server.ContactCode;
import org.wangpai.wchat.universal.protocol.internal.server.DataUnit;
import org.wangpai.wchat.universal.protocol.remote.Packet;
import org.wangpai.wchat.universal.protocol.remote.Protocol;
import org.wangpai.wchat.universal.util.json.JsonUtil;

/**
 * @since 2021-12-1
 */
@Accessors(chain = true)
public class Client {
    @Setter
    @Getter
    private ClientConfig clientConfig;

    @Setter
    @Getter
    private ContactCode contactCode;

    @Setter
    @Getter
    private LocalDateTime dataTime;

    /**
     * Channel 是线程安全的，所以使用 Channel 的方法时无需上锁
     *
     * @since 2022-1-13
     */
    private Channel channel;

    private EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

    private boolean started = false;

    @Getter
    private boolean closed = false;

    public Client start() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(this.workerLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        // 设置接收端的 IP 和端口号，但实际上，自己作为发送端也会为自己自动生成一个端口号
        bootstrap.remoteAddress(this.clientConfig.getOtherPartyIp(),
                Integer.parseInt(this.clientConfig.getOtherPartyPort()));
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                // 最外层编码器。为了帮助接收端解决粘包、半包问题
                var pipeline = ch.pipeline();
                pipeline.addLast(new LengthFieldPrepender(Protocol.HEAD_LENGTH));
                // 将 String 数据转化为二进制数据
                pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                // 将 Java 对象转化为 String 数据（JSON 数据）
                pipeline.addLast(new MessageToMessageEncoder<Packet>() {
                    @Override
                    protected void encode(ChannelHandlerContext ctx, Packet message, List<Object> out) {
                        try {
                            out.add(JsonUtil.pojo2Json(message));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });
                pipeline.addLast(new MessageToMessageEncoder<Object>() {
                    @Override
                    protected void encode(ChannelHandlerContext ctx, Object data, List<Object> out) {
                        try {
                            var dataUnit = (DataUnit) data;
                            var packet = new Packet()
                                    .setContactCode(Client.this.getContactCode())
                                    .setPort(ServerApi.getServerPort())
                                    .setDataTime(Client.this.getDataTime())
                                    .setCarryingType(dataUnit.getIdentifier())
                                    .setCarrier(JsonUtil.pojo2Json(dataUnit.getData()));
                            out.add(packet);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });

                // 客户端的入站处理器，用于处理来自服务端的反馈
                pipeline.addLast(new ChannelInboundHandlerAdapter() {
                    /**
                     * 此方法将在断线时被触发
                     *
                     * @since 2022-1-17
                     */
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) {
                        ctx.close();
                        Client.this.close();

                        System.out.println("注意：客户端的方法 channelInactive 被调用。客户端连接关闭");
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        ctx.close();
                        Client.this.close();

                        System.out.println("注意：客户端的方法 exceptionCaught 被调用。客户端连接关闭");
                        cause.printStackTrace();
                    }
                });
            }
        });

        ChannelFuture future = bootstrap.connect();
        future.addListener((ChannelFuture futureListener) -> {
            if (futureListener.isSuccess()) {
                System.out.println("与服务端连接成功"); // FIXME：日志
            } else {
                System.out.println("与服务端连接失败。客户端连接关闭"); // FIXME：日志
                Client.this.close();
            }
        });
        try {
            future.sync();
        } catch (Exception exception) {
            exception.printStackTrace(); // FIXME：日志
        }
        this.channel = future.channel();
        this.started = true;

        return this;
    }

    public void send(DataUnit dataUnit, ContactCode contactCode, LocalDateTime dataTime) {
        this.setContactCode(contactCode)
                .setDataTime(dataTime);

        if (!this.started) {
            this.start();
            this.started = true;
        }

        this.channel.writeAndFlush(dataUnit);
    }

    public void close() {
        this.workerLoopGroup.shutdownGracefully();
        this.closed = true;
    }

    private Client() {
        super();
    }

    public static Client getInstance() {
        System.out.println("一个新的 client 被创建");

        return new Client();
    }
}