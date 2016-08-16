package com.weitaomi.systemconfig.exception;

/**
 * Created by Administrator on 2016/8/14.
 */
public class InfoException extends BaseException {
    private static final long serialVersionUID = -123456789L;
    public InfoException(String message) {
        super(message);
    }

    public InfoException(Throwable cause) {
        super(cause);
    }

    public InfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
