package com.restonic4.citadel.files;

public interface Serializable {
    String serialize();

    Object deserialize(String data);
}
