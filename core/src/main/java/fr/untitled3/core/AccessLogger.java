package fr.untitled3.core;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 4/2/13
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessLogger {

    private Logger logger = LoggerFactory.getLogger(AccessLogger.class);

    private Configuration configuration;

    private DateTimeFormatter dateTimeFormatter;

    private boolean init = false;

    private void init() {
        dateTimeFormatter = DateTimeFormat.forPattern(configuration.getDateTimeFormat());
    }

    public void logAccess(String method, String host, String userAgent) {
        logger.info("{} : Access : {} / {} / {}", dateTimeFormatter.print(DateTime.now()), method, host, userAgent);
    }

}
