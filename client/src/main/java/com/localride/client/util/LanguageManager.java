package com.localride.client.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * LanguageManager is a utility class for managing language resources in the application.
 * It allows for dynamic loading of language-specific resource bundles based on the user's preference.
 */
public class LanguageManager {
    private static ResourceBundle bundle;

    /**
     * Initializes the LanguageManager by loading the appropriate resource bundle based on user input.
     * @param scanner a Scanner instance to read user input
     * @return the initialized ResourceBundle
     */
    public static ResourceBundle init(Scanner scanner) {
        System.out.println("Please select a language (en/de):");
        String language = scanner.nextLine().trim().toLowerCase();

        Locale locale = switch (language.toLowerCase()){
            case "en" -> Locale.ENGLISH;//new Locale("en", "US");
            case "de" -> Locale.GERMAN;//new Locale("de", "DE");
            default -> Locale.ENGLISH;//new Locale("en", "US"); // Default to English if unsupported language
        };
        bundle = ResourceBundle.getBundle("messages", locale);
        return bundle;
    }

    public static String get(String key, Object... args) {
        if (bundle == null) {
            //throw new IllegalStateException("LanguageManager not initialized. Call init() first.");
            bundle = getBundle(); // Default to English if not initialized
        }

        String pattern = bundle.getString(key);
        return MessageFormat.format(pattern, args);
    }

    public static ResourceBundle getBundle() {
        if (bundle == null) {
            //throw new IllegalStateException("LanguageManager not initialized. Call init() first.");
            bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH); // Default to English if not initialized
        }
        return bundle;
    }
}
