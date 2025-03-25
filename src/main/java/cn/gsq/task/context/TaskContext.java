package cn.gsq.task.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Project : galaxy
 * Class : cn.gsq.task.context.TaskContext
 *
 * @author : xyy
 * @since : 2025-03-25 10:35
 **/
public class TaskContext {
    private static final ThreadLocal<Map<String, String>> stringThreadLocal = ThreadLocal.withInitial(HashMap::new);

    public static void set(String taskId, String stepName) {
        stringThreadLocal.get().put(taskId, stepName);
    }

    public static String get(String taskId) {
        return stringThreadLocal.get().get(taskId);
    }

    public static Map<String, String> get() {
        return stringThreadLocal.get();
    }

    public static void clear() {
        stringThreadLocal.remove();
    }
}
