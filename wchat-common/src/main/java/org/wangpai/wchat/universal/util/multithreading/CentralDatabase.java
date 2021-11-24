package org.wangpai.wchat.universal.util.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @since 2021-9-27
 */
public class CentralDatabase {
    /**
     * 这里凡是使用 volatile 的变量都使用了单例模式中的“双重检查锁定”
     */

    /**
     * 注意：记得在程序结束时，如果 executor 不为 null， 使用 “executor.shutdown();” 来回收资源
     *
     * @since 2021-9-28
     */
    private static volatile ExecutorService executor;
    private static final Object EXECUTOR_LOCK = new Object();

    /**
     * 注意：记得在程序结束时，如果 tasks 不为 null，对于每一个 task，使用 “task.cancel();” 来回收资源
     *
     * @since 2021-9-28
     */
    private static volatile List<FutureTask<Object>> tasks;
    private static final Object TASKS_LOCK = new Object();

    /**
     * 提供此方法是为了进行“懒加载”
     *
     * 这里使用了单例模式中的“双重检查锁定”
     *
     * @since 2021-9-28
     */
    public static ExecutorService getExecutor() {
        // 第一重判断
        if (CentralDatabase.executor == null) {
            // 上锁
            synchronized (EXECUTOR_LOCK) {
                // 第二重判断
                if (CentralDatabase.executor == null) {
                    // 设置初始线程个数，大致为 6
                    CentralDatabase.executor = Executors.newFixedThreadPool(6);
                }
            }
        }
        return CentralDatabase.executor;
    }

    /**
     * 提供此方法是为了进行“懒加载”
     *
     * @since 2021-9-28
     */
    public static List<FutureTask<Object>> getTasks() {
        // 第一重判断
        if (CentralDatabase.tasks == null) {
            // 上锁
            synchronized (TASKS_LOCK) {
                // 第二重判断
                if (CentralDatabase.tasks == null) {
                    CentralDatabase.tasks = new ArrayList<>();
                }
            }
        }

        return CentralDatabase.tasks;
    }

    /**
     * 记得在打算关闭应用时调用此方法。
     * 不能在应用正在运行时调用此方法
     *
     * 作用：回收本类所有关于多线程的资源
     *
     * @since 2021-9-28
     */
    public static void multithreadingClosed() {
        if (CentralDatabase.tasks != null) {
            for (var task : CentralDatabase.tasks) {
                if (!task.isDone()) {
                    task.cancel(true);
                }
            }
        }

        if (CentralDatabase.executor != null) {
            CentralDatabase.executor.shutdown();
        }
    }
}

