package me.restonic4.citadel.util;

// This class was made to avoid memory leaks doing string concatenations
public class StringBuilderHelper {
    private static final StringBuilder sb = new StringBuilder();

    /**
     * Concatenate the given strings using the shared StringBuilder.
     *
     * @param parts Strings to be concatenated.
     * @return Concatenated result.
     */
    public static String concatenate(String... parts) {
        sb.setLength(0);

        for (String part : parts) {
            sb.append(part);
        }

        return sb.toString();
    }

    /**
     * Concatenate the given objects' string representations using the shared StringBuilder.
     *
     * @param parts Objects to be concatenated.
     * @return Concatenated result.
     */
    public static String concatenate(Object... parts) {
        sb.setLength(0);

        for (Object part : parts) {
            sb.append(part);
        }

        return sb.toString();
    }
}
