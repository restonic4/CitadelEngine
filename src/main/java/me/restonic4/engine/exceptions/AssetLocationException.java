package me.restonic4.engine.exceptions;


public class AssetLocationException extends RuntimeException {
    public AssetLocationException(String string) {
        super(string);
    }

    public AssetLocationException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
