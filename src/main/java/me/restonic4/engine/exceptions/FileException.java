package me.restonic4.engine.exceptions;


public class FileException extends RuntimeException {
    public FileException(String string) {
        super(string);
    }

    public FileException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
