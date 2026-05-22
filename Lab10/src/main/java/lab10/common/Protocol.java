package lab10.common;

public class Protocol {
    public static final String JOIN = "JOIN";
    public static final String START = "START";
    public static final String QUESTION = "QUESTION";
    public static final String ANSWER = "ANSWER";
    public static final String RESULT = "RESULT";
    public static final String SCORE = "SCORE";
    public static final String END = "END";
    public static final String STOP = "STOP";
    public static final String PING = "PING";
    public static final String ERROR = "ERROR";
    public static final String WAIT = "WAIT";
    public static final String BOT = "BOT";

    public static final String DELIMITER = "|";
    public static final int DEFAULT_PORT = 12345;
    public static final long ANSWER_TIMEOUT_MS = 15000; // 15 seconds per question

    public static String encode(String command, String payload) {
        return command + DELIMITER + payload;
    }

    public static String[] decode(String message) {
        int idx = message.indexOf(DELIMITER);
        if (idx == -1) {
            return new String[]{message, ""};
        }
        return new String[]{message.substring(0, idx), message.substring(idx + 1)};
    }
}
