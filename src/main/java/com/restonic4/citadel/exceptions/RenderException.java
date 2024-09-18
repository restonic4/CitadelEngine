package com.restonic4.citadel.exceptions;

import com.restonic4.ClientSide;

@ClientSide
public class RenderException extends RuntimeException {
    public RenderException(String string) {
        super(string);
    }

    public RenderException(String string, Throwable throwable) {
        super(string, throwable);
    }
}
