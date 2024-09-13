package me.restonic4.citadel.exceptions;


public class NetworkException extends RuntimeException {
    public NetworkException(String string) {
        super(string);
    }

    public NetworkException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
