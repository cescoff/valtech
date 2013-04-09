package fr.untitled3.core;

import fr.untitled3.utils.JAXBUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 3/28/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationFactory implements FactoryBean<Configuration> {

    private Resource configurationFile;

    @Override
    public Configuration getObject() throws Exception {
        return JAXBUtils.unmarshal(Configuration.class, configurationFile.getInputStream());
    }

    @Override
    public Class<?> getObjectType() {
        return Configuration.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Resource getConfigurationFile() {
        return configurationFile;
    }

    public void setConfigurationFile(Resource configurationFile) {
        this.configurationFile = configurationFile;
    }
}
