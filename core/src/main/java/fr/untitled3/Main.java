package fr.untitled3;

import fr.untitled3.core.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 4/2/13
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Server server = (Server) applicationContext.getBean("server");
        try {
            server.listen();
        } catch (IOException e) {
            logger.error("A fatal error has occured while starting server", e);
        }
        // Used to prevent maven plugin to stop
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

}
