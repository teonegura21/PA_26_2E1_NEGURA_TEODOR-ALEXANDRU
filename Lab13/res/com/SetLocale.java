package com; // This file belongs to the 'com' package

import java.util.Locale; // Import the Locale class
import java.util.ResourceBundle; // Import ResourceBundle for loading translations

/**
 * SetLocale command
 * This class changes the application's current locale (language/region setting).
 * When locale changes, all messages switch to that language.
 */
public class SetLocale {

    // Static variable to store the CURRENT locale of the application
    // "static" means all parts of the program share this same value
    // Default is English (Locale.ENGLISH)
    private static Locale currentLocale = Locale.ENGLISH;

    /**
     * execute method - runs when user types "set <language>" command
     * @param languageTag - the language code like "en", "ro", "fr", etc.
     * @param messages - the resource bundle (we reload it with new locale)
     */
    public void execute(String languageTag, ResourceBundle messages) {
        // Create a new Locale object from the language tag
        // For example: "ro" becomes Romanian locale, "en" becomes English
        currentLocale = new Locale(languageTag);

        // Reload the resource bundle with the NEW locale
        // This makes all messages appear in the selected language
        // "res.Messages" tells Java to look for files: Messages.properties, Messages_ro.properties, etc.
        ResourceBundle newMessages = ResourceBundle.getBundle("res.Messages", currentLocale);

        // Get the "locale.set" message template and insert the locale name
        // MessageFormat.format replaces {0} with the actual locale string
        String message = java.text.MessageFormat.format(
            newMessages.getString("locale.set"),
            currentLocale.toLanguageTag()
        );

        // Print the confirmation message
        System.out.println(message);
    }

    /**
     * Getter method - returns the current locale
     * Other classes use this to know which locale is active
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
