package com.rlogin.common.frame.exception;

public class FrameIllegalPrivilegeException extends FrameRuntimeException {

    private static final long serialVersionUID = -1700437931256930467L;

    public FrameIllegalPrivilegeException(String message) {
        super(message);
    }

    public FrameIllegalPrivilegeException(Throwable cause) {
        super(cause);
    }
}