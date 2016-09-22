package com.weitaomi.systemconfig.exception;

/**
 * Created by yuguodong on 2016/7/6.
 */
public class BaseException  extends RuntimeException {

    public BaseException(String message) {
        super(message,new Throwable(message));
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
