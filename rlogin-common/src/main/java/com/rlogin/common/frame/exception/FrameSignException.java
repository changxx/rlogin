package com.rlogin.common.frame.exception;

public class FrameSignException extends FrameRuntimeException {

    private static final long serialVersionUID = -2397792907800264316L;

    public FrameSignException(String message) {
        super(message);
    }

    public FrameSignException(Throwable cause) {
        super(cause);
    }
}
