package com.localride.util;

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
            throw new IllegalStateException("LanguageManager not initialized. Call init() first.");
        }

        String pattern = bundle.getString(key);
        return MessageFormat.format(pattern, args);
    }
//
//    /**
//     * Retrieves a localized string for the given key.
//     * @param key the key for the desired string
//     * @return the localized string
//     * @throws IllegalStateException if LanguageManager has not been initialized
//     */
//    public static String get(String key) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        return bundle.getString(key);
//    }
//
//    public static boolean get(String passengerAdded, String name, int id) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(passengerAdded);
//        System.out.println(message.replace("{name}", name).replace("{id}", String.valueOf(id)));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String passengerRideRequested, int id, String name, Integer rating) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(passengerRideRequested);
//        System.out.println(message.replace("{id}", String.valueOf(id))
//                                   .replace("{name}", name)
//                                   .replace("{rating}", String.valueOf(rating)));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String rideCreated, int id, String name, String name1) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(rideCreated);
//        System.out.println(message.replace("{id}", String.valueOf(id))
//                                   .replace("{name}", name)
//                                   .replace("{name1}", name1));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String rideBuilt, String name, String name1, int id) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(rideBuilt);
//        System.out.println(message.replace("{name}", name)
//                                   .replace("{name1}", name1)
//                                   .replace("{id}", String.valueOf(id)));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String cannotAcceptRideStatus, RideStatus status) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(cannotAcceptRideStatus);
//        System.out.println(message.replace("{status}", status.toString()));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String rideAcceptedBy, String name, String name1) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(rideAcceptedBy);
//        System.out.println(message.replace("{name}", name).replace("{name1}", name1));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String rideStarting, String name, String name1, String carModel, String string) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(rideStarting);
//        System.out.println(message.replace("{name}", name)
//                                   .replace("{name1}", name1)
//                                   .replace("{carModel}", carModel)
//                                   .replace("{status}", string));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String rideNotActive, String string) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(rideNotActive);
//        System.out.println(message.replace("{status}", string));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String rideEnded, String name, double fare) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(rideEnded);
//        System.out.println(message.replace("{name}", name).replace("{fare}", String.valueOf(fare)));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String rideCreatedSuccess, int id, String name, String name1, RideStatus status) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(rideCreatedSuccess);
//        System.out.println(message.replace("{id}", String.valueOf(id))
//                                   .replace("{name}", name)
//                                   .replace("{name1}", name1)
//                                   .replace("{status}", status.toString()));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String rideCompleted, int id, String name, RideStatus status) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(rideCompleted);
//        System.out.println(message.replace("{id}", String.valueOf(id))
//                                   .replace("{name}", name)
//                                   .replace("{status}", status.toString()));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String rideCancelledBy, int id, String cancelledBy) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(rideCancelledBy);
//        System.out.println(message.replace("{id}", String.valueOf(id))
//                                   .replace("{cancelledBy}", cancelledBy));
//        return true; // Assuming this method is intended to return a boolean
//    }
//
//    public static boolean get(String cannotEndRide, int id, RideStatus status) {
//        if (bundle == null) {
//            throw new IllegalStateException(get("languageManagerNotInitialized"));
//        }
//        String message = bundle.getString(cannotEndRide);
//        System.out.println(message.replace("{id}", String.valueOf(id))
//                                   .replace("{status}", status.toString()));
//        return true; // Assuming this method is intended to return a boolean
//    }
}
