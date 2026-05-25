package com; // This file belongs to the 'com' package

import java.util.*; // Import all utility classes (Locale, Currency, DateFormatSymbols, etc.)
import java.text.*; // Import text formatting classes (MessageFormat, SimpleDateFormat)

/**
 * Info command
 * This class displays detailed information about a locale:
 * - Country name (in English and local language)
 * - Language name (in English and local language)
 * - Currency (code and name)
 * - Week days (in local language)
 * - Months (in local language)
 * - Today's date (formatted in local style)
 */
public class Info {

    /**
     * execute method - runs when user types "info" or "info <language>" command
     * @param messages - the resource bundle with translated text
     * @param locale - the locale to display info about (current or specified)
     */
    public void execute(ResourceBundle messages, Locale locale) {
        // Print the header message, inserting the locale's language tag
        // e.g., "Information about ro:"
        System.out.println(MessageFormat.format(
            messages.getString("info"),
            locale.toLanguageTag()
        ));

        // --- COUNTRY ---
        // getDisplayCountry() returns country name in DEFAULT locale's language
        // getDisplayCountry(locale) returns country name in THAT locale's language
        System.out.println("Country: " + locale.getDisplayCountry()
            + " (" + locale.getDisplayCountry(locale) + ")");

        // --- LANGUAGE ---
        // getDisplayLanguage() returns language name in DEFAULT locale's language
        // getDisplayLanguage(locale) returns language name in THAT locale's language
        System.out.println("Language: " + locale.getDisplayLanguage()
            + " (" + locale.getDisplayLanguage(locale) + ")");

        // --- CURRENCY ---
        // Currency.getInstance() gets the currency for this locale
        // getCurrencyCode() returns the 3-letter code (RON, USD, EUR)
        // getDisplayName() returns the full name (Romanian Leu, US Dollar)
        try {
            Currency currency = Currency.getInstance(locale);
            System.out.println("Currency: " + currency.getCurrencyCode()
                + " (" + currency.getDisplayName(locale) + ")");
        } catch (IllegalArgumentException e) {
            // Some locales don't have a specific currency
            System.out.println("Currency: Not available");
        }

        // --- WEEK DAYS ---
        // DateFormatSymbols gets names of days/months for a locale
        // getWeekdays() returns array: [empty, Sunday, Monday, Tuesday, ...]
        // We skip index 0 (it's empty) and index 1 (Sunday), start from Monday
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] weekdays = symbols.getWeekdays();
        System.out.print("Week Days: ");
        // Print days from Monday (2) to Sunday (1) in the order: Mon, Tue, Wed, Thu, Fri, Sat, Sun
        String[] dayOrder = {weekdays[2], weekdays[3], weekdays[4], weekdays[5], weekdays[6], weekdays[7], weekdays[1]};
        System.out.println(String.join(", ", dayOrder));

        // --- MONTHS ---
        // getMonths() returns array of month names in the locale's language
        String[] months = symbols.getMonths();
        System.out.print("Months: ");
        // Months array has 13 elements (index 0-11 are Jan-Dec, index 12 is often empty)
        // We only take the first 12 months
        String[] realMonths = Arrays.copyOf(months, 12);
        System.out.println(String.join(", ", realMonths));

        // --- TODAY ---
        // new Date() creates the current date and time
        // SimpleDateFormat formats the date according to locale conventions
        Date today = new Date();
        // DateFormat.getDateInstance() creates a date formatter for the locale
        // FULL format = long format like "May 8, 2016" or "8 mai 2016"
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        System.out.println("Today: " + dateFormat.format(today));
    }
}
