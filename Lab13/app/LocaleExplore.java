package app; // This file belongs to the 'app' package

import java.util.*;     // Import Scanner (for keyboard input), Locale, ResourceBundle
import java.text.*;     // Import MessageFormat (for inserting values into messages)
import com.*;           // Import ALL classes from the 'com' package (DisplayLocales, SetLocale, Info)

/**
 * LocaleExplore - Main class of the application
 * This is the entry point. It reads commands from keyboard and executes them.
 * Available commands:
 *   locales          - show all available locales
 *   set <language>   - change language (e.g., set ro, set en)
 *   info             - show info about current locale
 *   info <language>  - show info about specific locale (e.g., info ro)
 *   exit             - quit the program
 */
public class LocaleExplore {

    public static void main(String[] args) {
        // --- STEP 1: Create a Scanner to read keyboard input ---
        // System.in = the keyboard
        Scanner scanner = new Scanner(System.in);

        // --- STEP 2: Set the default locale to English ---
        // This determines which .properties file Java loads first
        Locale currentLocale = Locale.ENGLISH;

        // --- STEP 3: Load the resource bundle (translation file) ---
        // "res.Messages" tells Java to look for files in the "res" folder
        // Java automatically picks the right file based on currentLocale:
        //   - If locale is English -> loads Messages.properties
        //   - If locale is Romanian -> loads Messages_ro.properties
        ResourceBundle messages = ResourceBundle.getBundle("res.Messages", currentLocale);

        // --- STEP 4: Create command objects ---
        // Each command is a separate object that knows how to do one thing
        DisplayLocales displayLocales = new DisplayLocales();
        SetLocale setLocale = new SetLocale();
        Info info = new Info();

        // --- STEP 5: Main loop - keep asking for commands until user exits ---
        while (true) {
            // Print the prompt message ("Input command:" or "Comanda ta:")
            System.out.print(messages.getString("prompt") + " ");

            // Read the entire line the user typed
            String input = scanner.nextLine();

            // Split the input into parts (command and arguments)
            // e.g., "set ro" becomes ["set", "ro"]
            // e.g., "locales" becomes ["locales"]
            String[] parts = input.split(" ");
            String command = parts[0]; // First word is the command

            // --- STEP 6: Execute the appropriate command ---

            if (command.equals("locales")) {
                // User wants to see all available locales
                displayLocales.execute(messages);
            }
            else if (command.equals("set")) {
                // User wants to change the locale
                // parts[1] is the language tag (e.g., "ro", "en", "fr")
                if (parts.length > 1) {
                    setLocale.execute(parts[1], messages);
                    // After changing locale, reload messages with new language
                    messages = ResourceBundle.getBundle("res.Messages", SetLocale.getCurrentLocale());
                } else {
                    System.out.println("Usage: set <language>");
                }
            }
            else if (command.equals("info")) {
                // User wants locale information
                Locale targetLocale;
                if (parts.length > 1) {
                    // info ro -> show info about Romanian locale
                    targetLocale = new Locale(parts[1]);
                } else {
                    // info -> show info about current locale
                    targetLocale = SetLocale.getCurrentLocale();
                }
                info.execute(messages, targetLocale);
            }
            else if (command.equals("exit")) {
                // User wants to quit
                System.out.println("Goodbye!");
                break; // Exit the while loop
            }
            else {
                // Unknown command - print error message
                System.out.println(messages.getString("invalid"));
            }
        }

        // Close the scanner to free resources
        scanner.close();
    }
}
