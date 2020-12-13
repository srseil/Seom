package seom.utils;

import java.util.logging.*;

public class Log {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void finest(String message) {
        logger.finest(message);
    }

    public static void finer(String message) {
        logger.finer(message);
    }

    public static void fine(String message) {
        logger.fine(message);
    }

    public static void config(String message) {
        logger.config(message);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void severe(String message) {
        logger.severe(message);
    }

    public static void setLevel(Level level) {
        logger.setLevel(level);
    }

    public static void enableSimpleLogging() {
        Logger rootLogger = logger.getParent();
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(Level.ALL);
        }
    }

    private static class SimpleFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return record.getLevel() + ": " + formatMessage(record) + System.lineSeparator();
        }
    }
}
