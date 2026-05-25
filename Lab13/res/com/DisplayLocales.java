package com; // This file belongs to the 'com' package

import java.util.Locale; // Import the Locale class from Java's standard library

/**
 * DisplayLocales command
 * This class displays all available locales installed in the Java Development Kit (JDK).
 * A locale represents a specific geographical, political, or cultural region.
 */
public class DisplayLocales {

    /**
     * execute method - runs when user types "locales" command
     * @param messages - the resource bundle containing translated text messages
     */
    public void execute(java.util.ResourceBundle messages) {
        // Print the translated message for "locales" key (e.g., "The available locales are:")
        System.out.println(messages.getString("locales"));

        // Locale.getAvailableLocales() returns ALL locales that Java knows about
        // This includes every language/country combination Java supports
        Locale[] availableLocales = Locale.getAvailableLocales();

        // Loop through each locale and print its language tag
        // A language tag looks like: en, ro, en_US, fr_FR, de_DE, etc.
        for (Locale locale : availableLocales) {
            // toLanguageTag() returns the short code like "en", "ro", "en-US"
            System.out.println(locale.toLanguageTag());
        }
    }
}
