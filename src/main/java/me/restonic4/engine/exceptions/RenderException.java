package me.restonic4.engine.exceptions;


public class RenderException extends RuntimeException {
    public RenderException(String string) {
        super(string);
    }

    public RenderException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
