package org.wangpai.wchat.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import java.net.InetSocketAddress;
import java.util.List;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.server.external.driver.event.OnReceiveEvent;
import org.wangpai.wchat.server.external.hook.WchatServerOnHooks;
import org.wangpai.wchat.server.external.hook.WchatServerQueryHooks;
import org.wangpai.wchat.universal.protocol.internal.client.ClientConfig;
import org.wangpai.wchat.universal.protocol.internal.server.DataUnit;
import org.wangpai.wchat.universal.protocol.remote.Packet;
import org.wangpai.wchat.universal.protocol.remote.Protocol;
import org.wangpai.wchat.universal.util.json.JsonUtil;

/**
 * @since 2021-12-1
 */
@Accessors(chain = true)
@Lazy
@Scope("singleton")
@Service("server")
public class Server extends OnServerAction implements InitializingBean {
    @Autowired
    WchatServerOnHooks onHooks;

    @Autowired
    WchatServerQueryHooks queryHooks;

    @Setter
    private String port; // port 不要设置为整数类型，这会为编程带来很多麻烦

    private EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);

    private EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

    public Server start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(this.bossLoopGroup, this.workerLoopGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.localAddress(Integer.parseInt(this.port));
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        var server = this;
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                var pipeline = ch.pipeline();
                // 最外层解码器。可解决粘包、半包问题
                pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0,
                        Protocol.HEAD_LENGTH, 0, Protocol.HEAD_LENGTH));
                // 将二进制数据解码成 String 数据
                pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                // 将 String 数据（JSON 数据）解码成 Java 对象
                pipeline.addLast(new MessageToMessageDecoder<String>() {
                    @Override
                    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out)
                            throws JsonProcessingException {
                        Packet packet = null;
                        try {
                            packet = JsonUtil.json2Pojo(msg, Packet.class);
                        } catch (Exception exception) {
                            System.out.println(exception);
                        }
                        out.add(packet);
                    }
                });
                // 进行对转化后的最终的数据的处理
                pipeline.addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object obj) throws JsonProcessingException {
                        var packet = (Packet) obj;

                        var clientConfig = new ClientConfig();
                        var inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                        clientConfig.setOtherPartyPort(packet.getPort());
                        clientConfig.setOtherPartyIp(inetSocketAddress.getAddress().getHostAddress());

                        var dataUnit = new DataUnit()
                                .setIdentifier(packet.getCarryingType())
                                .setData(JsonUtil.json2Pojo(packet.getCarrier(), packet.getClassType()));

                        var onReceiveEvent = new OnReceiveEvent()
                                .setContactCode(packet.getContactCode())
                                .setClientConfig(clientConfig)
                                .setDateTime(packet.getDataTime())
                                .setDataUnit(dataUnit);
                        try {
                            server.onReceive(onReceiveEvent);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });

                pipeline.addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) {
                        System.out.println("注意：服务端的方法 channelInactive 被调用");
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        System.out.println("注意：服务端的方法 exceptionCaught 被调用");
                        cause.printStackTrace();
                    }
                });
            }
        });

        try {
            ChannelFuture channelFuture = bootstrap.bind().sync();
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception exception) {
            exception.printStackTrace(); // FIXME：日志
        } finally {
            this.close();
        }

        return this;
    }

    public void close() {
        this.workerLoopGroup.shutdownGracefully();
        this.bossLoopGroup.shutdownGracefully();
    }

    private Server() {
        super();
    }

    public static Server getInstance() {
        return new Server();
    }

    @Override
    public void afterPropertiesSet() {
        this.setWchatServerOnHooks(this.onHooks);
        this.setWchatServerQueryHooks(this.queryHooks);
    }
}