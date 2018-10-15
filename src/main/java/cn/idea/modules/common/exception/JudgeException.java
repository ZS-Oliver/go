package cn.idea.modules.common.exception;

import com.google.common.base.Supplier;

public class JudgeException extends Exception {
    public JudgeException(String message) {
        super(message);
    }

    public JudgeException(String message, Object... objects) {
        super(String.format(message, objects));
    }

    public static void when(boolean condition, Supplier<String> msgSupplier) throws JudgeException {
        if (condition) throw new JudgeException(msgSupplier.get());
    }

    public static void when(boolean condition, String msg) throws JudgeException {
        if (condition) throw new JudgeException(msg);
    }
}
