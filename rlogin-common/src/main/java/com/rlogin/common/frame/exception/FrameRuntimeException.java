package com.rlogin.common.frame.exception;

/**
 * 基础运行时异常类
 */
public class FrameRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1608048841142902592L;

    public FrameRuntimeException() {
        super();
    }

    public FrameRuntimeException(String message) {
        super(message);
    }

    public FrameRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameRuntimeException(Throwable cause) {
        super(cause);
    }

}
