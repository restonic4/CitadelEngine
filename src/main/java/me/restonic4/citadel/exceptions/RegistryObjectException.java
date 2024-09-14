package me.restonic4.citadel.exceptions;


public class RegistryObjectException extends RuntimeException {
    public RegistryObjectException(String string) {
        super(string);
    }

    public RegistryObjectException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
