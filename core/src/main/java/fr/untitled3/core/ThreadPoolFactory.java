package fr.untitled3.core;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 4/2/13
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPoolFactory implements FactoryBean<ThreadPoolTaskExecutor> {

    private Configuration configuration;

    @Override
    public ThreadPoolTaskExecutor getObject() throws Exception {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        threadPoolTaskExecutor.setMaxPoolSize(configuration.getThreadPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(configuration.getThreadPoolSize() * 3);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public Class<?> getObjectType() {
        return ThreadPoolTaskExecutor.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
