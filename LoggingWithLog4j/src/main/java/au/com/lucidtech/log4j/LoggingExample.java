package au.com.lucidtech.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExample {
    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);

    public static void main(String[] args) {
        final String parameter = "logging";
        if (logger.isTraceEnabled()) {
            logger.trace("This is trace " + parameter);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("This is debug " + parameter);
        }

        if (logger.isInfoEnabled()) {
            logger.info("This is info " + parameter);
        }

        logger.warn("This is warn " + parameter);
        logger.error("This is error " + parameter);
    }
}