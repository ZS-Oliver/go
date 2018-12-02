package cn.idea.modules.common.exception;

import com.google.common.base.Supplier;

public class ServiceException extends Exception {
    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Object... args) {
        super(String.format(msg, args));
    }

    public static void when(boolean condition, Supplier<String> msgSupplier) throws ServiceException {
        if (condition) throw new ServiceException(msgSupplier.get());
    }

    public static void when(boolean condition, String msg) throws ServiceException {
        if (condition) throw new ServiceException(msg);
    }
}
