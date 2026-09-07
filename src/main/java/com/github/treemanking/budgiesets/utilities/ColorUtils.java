package com.github.treemanking.budgiesets.utilities;

/**
 * Static helpers for converting colour values declared in the configuration.
 *
 * <p>Import the helpers where they are needed:</p>
 *
 * <pre>{@code
 * import static com.github.treemanking.budgiesets.utilities.ColorUtils.convertHexToRGB;
 * }</pre>
 */
public final class ColorUtils {

    private ColorUtils() {
        throw new AssertionError("ColorUtils is a utility class and must not be instantiated");
    }

    /**
     * Converts a hexadecimal color string to its red, green, and blue components.
     *
     * @param hex the hexadecimal color string (e.g., "#FF5733" or "FF5733")
     * @return an array of three integers representing the red, green, and blue components
     * @throws IllegalArgumentException if the hex string is invalid
     */
    public static int[] convertHexToRGB(String hex) throws IllegalArgumentException {
        if (hex == null) throw new IllegalArgumentException("Hex cannot be null");

        // Remove the leading '#' if it's present
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // Check if the hex string is valid
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex color string. Must be 6 characters long.");
        }

        try {
            int rgb = Integer.parseInt(hex, 16);

            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;

            return new int[]{red, green, blue};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color string. Contains non-hex characters.", e);
        }
    }
}

