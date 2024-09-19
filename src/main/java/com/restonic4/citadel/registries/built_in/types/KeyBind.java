package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.input.KeyListener;
import com.restonic4.citadel.registries.RegistryObject;

public class KeyBind extends RegistryObject {
    private int[] defaultKeyCombination;
    private boolean wasPressedOnce = false;

    public KeyBind(int... defaultKeyCombination) {
        this.defaultKeyCombination = defaultKeyCombination;
    }

    public boolean isPressed() {
        int pressed = 0;

        for (int i : defaultKeyCombination) {
            if (KeyListener.isKeyPressed(i)) {
                pressed++;
            }
        }

        return pressed == defaultKeyCombination.length;
    }

    public boolean isPressedOnce() {
        if (isPressed() && !wasPressedOnce) {
            wasPressedOnce = true;
            return true;
        }

        if (!isPressed()) {
            wasPressedOnce = false;
        }

        return false;
    }
}