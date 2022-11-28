package com.sk.signet.onm.common.exception;

public class JsonException extends RuntimeException {
    private static final long serialVersionUID = 815893495835856532L;

    public JsonException() {
        super();
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JsonException(final Throwable cause) {
        super(cause);
    }
}
