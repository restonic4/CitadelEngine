package me.restonic4.engine.exceptions;


public class RegistryItemException extends RuntimeException {
    public RegistryItemException(String string) {
        super(string);
    }

    public RegistryItemException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
