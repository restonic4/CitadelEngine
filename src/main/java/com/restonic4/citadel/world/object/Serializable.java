package com.restonic4.citadel.world.object;

public abstract class Serializable {
    public String serialize() {
        return "null";
    }

    public Object deserialize(String data) {
        return this;
    }
}
