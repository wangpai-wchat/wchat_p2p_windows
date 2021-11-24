package org.wangpai.wchat.ui.model.universal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class WchatUtil {
    /**
     * @since 2021-11-24
     */
    public static ImageView byteArray2ImageView(byte[] bytes, double size) {
        return new ImageView(new Image(WchatUtil.byteArray2InputStream(bytes), size, size, true, true));
    }

    /**
     * @since 2021-11-24
     */
    public static InputStream byteArray2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 此方法不会重置流，因此经过转化后，原来的流将不能再使用。
     * 因此，如果想要反复读取流，请转化前对流进行备份
     *
     * @since 2021-11-24
     */
    public static byte[] inputStream2byteArray(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }

    /**
     * @since 2021-11-24
     */
    public static byte[] file2byteArray(File file) throws IOException {
        return WchatUtil.inputStream2byteArray(new FileInputStream(file));
    }

    /**
     * 此方法在调用完之后，会将传入 inputStream 重置。
     * 此方法假定用户需要的图片，其长宽都是相等的
     *
     * @since 2021-11-16
     */
    public static ImageView inputStream2ImageView(InputStream inputStream, double size) throws IOException {
        // 如果此 InputStream 支持方法 mark
        if (!inputStream.markSupported()) {
            throw new IOException("错误：不支持方法 mark");
        }

        inputStream.mark(inputStream.available()); // 在当前位置作标记
        var result = new ImageView(new Image(inputStream, size, size, true, true));
        inputStream.reset(); // 将 inputStream 重置
        return result;
    }

    /**
     * 返回的结果不会包含文件夹流
     *
     * path 不需要以斜杠开头。path 是以资源目录 resources 为基准的
     *
     * @since 2022-1-16
     */
    public static InputStream[] readAllResFiles(String path, ReadMode mode) throws IOException {
        var resolver = new PathMatchingResourcePatternResolver();

        var suffix = switch (mode) {
            case RECURSIVE -> "**";
            case ONE_LEVEL_SUFFIX_FILE_ONLY -> "*.*";
            case ONE_LEVEL -> "*";
        };

        var resources = resolver.getResources("classpath:" + path + "/" + suffix);
        var inputStreams = new ArrayList<InputStream>(resources.length);
        for (var resource : resources) {
            // 如果 resource 不是目录
            if (resource.getFile().isFile()) {
                inputStreams.add(resource.getInputStream());
            }
        }
        return inputStreams.toArray(InputStream[]::new); // List 转数组
    }

    /**
     * 计算文字所占像素的宽度
     *
     * @since 2021-11-23
     */
    public static double calculateTextPixelWidth(String text, Font font) {
        Text theText = new Text(text);
        theText.setFont(font);

        return theText.getBoundsInLocal().getWidth();
    }

    /**
     * @param originText 内文本的内容
     * @param font 内文本的字体
     * @param originMaxWidth 内文本最大的行宽
     * @param rowExtension 对话框横向两端与内文本的边距
     * @param originSingleHeight 内文本一行的高度
     * @param columnExtension 对话框纵向向两端与内文本的边距
     * @return 计算出的对话框的宽度。其中，[0] 代表宽度，[1] 代表高度
     */
    public static double[] calculateDialogSize(String originText, Font font,
                                               double originMaxWidth, double rowExtension,
                                               double originSingleHeight, double columnExtension) {
        String lineSeparator = "\n"; // TextArea 中的换行符为 '\n'

        return WchatUtil.calculateDialogSize(originText, font, lineSeparator,
                originMaxWidth, rowExtension, originSingleHeight, columnExtension);
    }

    /**
     * @param originText 内文本的内容
     * @param font 内文本的字体
     * @param lineSeparator 换行符的定义
     * @param originMaxWidth 内文本最大的行宽
     * @param rowExtension 对话框横向两端与内文本的边距
     * @param originSingleHeight 内文本一行的高度
     * @param columnExtension 对话框纵向向两端与内文本的边距
     * @return 计算出的对话框的宽度。其中，[0] 代表宽度，[1] 代表高度
     */
    public static double[] calculateDialogSize(String originText, Font font, String lineSeparator,
                                               double originMaxWidth, double rowExtension,
                                               double originSingleHeight, double columnExtension) {
        double maxRowLength = 0;
        int formattedColumnNum = 0;

        if (originText != null && !"".equals(originText)) {
            var texts = originText.split(lineSeparator);

            if (texts.length == 0) { // 如果文本中只有换行符
                maxRowLength = 0;
                formattedColumnNum = originText.length() + 1; // 注意要加 1
            } else {
                double singleRowLength = 0;
                for (var text : texts) {
                    var singleOriginWidth = WchatUtil.calculateTextPixelWidth(text, font);
                    singleRowLength = Math.min(singleOriginWidth, originMaxWidth); // 注意：这是求最小值
                    maxRowLength = Math.max(maxRowLength, singleRowLength); // 注意：这里求最大值
                    formattedColumnNum += (int) (singleOriginWidth / originMaxWidth) + 1; // 注意要加 1
                }
            }
        }

        double[] result = new double[2];
        result[0] = maxRowLength + rowExtension * 2;
        result[1] = formattedColumnNum * originSingleHeight + columnExtension * 2;

        return result;
    }

    public static double min(double... args) {
        return Arrays.stream(args).min().getAsDouble();
    }

    public static double max(double... args) {
        return Arrays.stream(args).max().getAsDouble();
    }
}
