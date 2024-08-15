package me.restonic4.engine.exceptions;


public class ModelException extends RuntimeException {
    public ModelException(String string) {
        super(string);
    }

    public ModelException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
