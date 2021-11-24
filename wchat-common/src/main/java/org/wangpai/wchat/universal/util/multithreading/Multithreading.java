package org.wangpai.wchat.universal.util.multithreading;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


/**
 * @since 2021-10-3
 */
public class Multithreading {
    /**
     * 无结果反馈的版本
     *
     * @since 2021-10-3
     * @lastModified 2022-1-5
     */
    public static void execute(Function function) {
        /**
         * 开新线程来完成下面的操作
         */
        FutureTask<Object> task = new FutureTask<>(() -> {
            function.run();
            return null; // 因为此处不需要结果反馈，所以返回 null
        });

        CentralDatabase.getTasks().add(task);
        CentralDatabase.getExecutor().execute(task);
    }

    /**
     * 有结果反馈的版本
     *
     * @since 2021-10-10
     */
    public static Future<?> submit(Function function) {
        /**
         * 开新线程来完成下面的操作
         */
        FutureTask<Object> task = new FutureTask<>(() -> {
            function.run();
            return null; // 因为此处不需要结果反馈，所以返回 null
        });

        CentralDatabase.getTasks().add(task);
        return CentralDatabase.getExecutor().submit(task);
    }
}
