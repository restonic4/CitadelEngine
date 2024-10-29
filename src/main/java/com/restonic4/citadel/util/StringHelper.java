package com.restonic4.citadel.util;

import java.nio.charset.Charset;

public class StringHelper {
    public static byte[] getCharByteSize(char character) {
        String charAsString = Character.toString(character);

        byte[] utf8Bytes = charAsString.getBytes(Charset.forName("UTF-8"));

        System.out.println("Tamaño del carácter '" + character + "' en UTF-8: " + utf8Bytes.length + " bytes");

        return utf8Bytes;
    }

    public static long getMemorySize(String string) {
        if (string == null) return 0;

        long stringSize = 8;
        stringSize += 8;
        stringSize += 4;
        stringSize += 4;


        stringSize += 16;
        stringSize += string.length() * 2L;

        return stringSize;
    }

    public static String formatJson(String json) {
        StringBuilder formatted = new StringBuilder();
        int indent = 0;
        boolean inQuotes = false;

        for (char c : json.toCharArray()) {
            switch (c) {
                case '"':
                    inQuotes = !inQuotes;
                    formatted.append(c);
                    break;
                case '{':
                case '[':
                    formatted.append(c);
                    if (!inQuotes) {
                        formatted.append("\n");
                        indent++;
                        addIndent(formatted, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!inQuotes) {
                        formatted.append("\n");
                        indent--;
                        addIndent(formatted, indent);
                    }
                    formatted.append(c);
                    break;
                case ',':
                    formatted.append(c);
                    if (!inQuotes) {
                        formatted.append("\n");
                        addIndent(formatted, indent);
                    }
                    break;
                default:
                    formatted.append(c);
            }
        }

        return formatted.toString();
    }

    private static void addIndent(StringBuilder formatted, int indent) {
        for (int i = 0; i < indent; i++) {
            formatted.append("    ");
        }
    }
}
