package com.restonic4.citadel.exceptions;

public class UnknownPlatformException extends RuntimeException {
    public UnknownPlatformException() {
        super("Unknown platform received: Unknown");
    }

    public UnknownPlatformException(String string) {
        super("Unknown platform received: " + string);
    }

    public UnknownPlatformException(String string, Throwable throwable) {
        super("Unknown platform received: " + string, throwable);
    }
}
