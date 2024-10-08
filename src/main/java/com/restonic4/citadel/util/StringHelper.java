package com.restonic4.citadel.util;

import java.nio.charset.Charset;

public class StringHelper {
    public static byte[] getCharByteSize(char character) {
        String charAsString = Character.toString(character);

        byte[] utf8Bytes = charAsString.getBytes(Charset.forName("UTF-8"));

        System.out.println("Tamaño del carácter '" + character + "' en UTF-8: " + utf8Bytes.length + " bytes");

        return utf8Bytes;
    }
}
