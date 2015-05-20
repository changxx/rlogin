package com.rlogin.common.frame.exception;

public class FrameBSHandledException extends FrameRuntimeException {

    private static final long serialVersionUID = 3167274779171418896L;

    public FrameBSHandledException(String message) {
        super(message);
    }

    public FrameBSHandledException(Throwable cause) {
        super(cause);
    }
}
