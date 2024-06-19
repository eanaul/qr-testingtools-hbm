package dev.hbm.qris_testingtools.AppConfiguration.Utility;

public class LoggerHelper {
    public static void LogContainer(StringBuilder logger, String variableName, String value, int L, int t) {
        for (int i = 0; i < t; i++) {
            logger.append("\t");
        }

        logger
                .append(rightPaddingString(variableName, L))
                .append(zeroLeftPaddingString(value.length()))
                .append(": ")
                .append(value)
                .append("\n");
    }

    public static void LogCommon(StringBuilder logger, String message, int t) {
        for (int i = 0; i < t; i++) {
            logger.append("\t");
        }

        logger
                .append(message)
                .append("\n");
    }

    public static String rightPaddingString(String input, int L) {
        return String
                .format("%" + (-L) + "s", input);
    }

    public static String zeroLeftPaddingString(int input) {
        return String
                .format("%" + (5) + "d", input)
                .replace(' ', '0');
    }
}
