package xyz.spoonmap.server.aop.log;

import java.util.UUID;

public class LogTraceThreadLocal {

    private record TraceId(String id) {
    }

    private static final ThreadLocal<TraceId> threadLocal = new ThreadLocal<>();

    public static void set() {
        String[] uuid = UUID.randomUUID().toString().split("-");
        threadLocal.set(new TraceId(uuid[0]));
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static String getId() {
        return threadLocal.get().id();
    }

}
