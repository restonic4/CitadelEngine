package me.restonic4.citadel.registries.built_in.types;

import me.restonic4.citadel.input.KeyListener;
import me.restonic4.citadel.registries.RegistryObject;

public class KeyBind extends RegistryObject {
    private int[] defaultKeyCombination;

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
        int pressed = 0;

        for (int i : defaultKeyCombination) {
            if (KeyListener.isKeyPressedOnce(i)) {
                pressed++;
            }
        }

        return pressed == defaultKeyCombination.length;
    }
}
