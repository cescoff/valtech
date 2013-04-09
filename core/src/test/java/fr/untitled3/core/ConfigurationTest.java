package fr.untitled3.core;

import fr.untitled3.utils.JAXBUtils;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 4/2/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationTest {
    @Test
    public void testGetPort() throws Exception {
        Configuration configuration = new Configuration();
        System.out.println(JAXBUtils.marshal(configuration, true));
    }
}
