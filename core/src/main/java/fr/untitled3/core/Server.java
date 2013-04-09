package fr.untitled3.core;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 4/2/13
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    private Configuration configuration;

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private PathResolver pathResolver;

    public void listen() throws IOException {
        ServerSocket serverSocket = new ServerSocket(configuration.getPort());
        while (true) {
            Socket clientSocket = serverSocket.accept();
            SocketProcessor socketProcessor = new SocketProcessor(clientSocket, pathResolver, configuration);
            threadPoolTaskExecutor.execute(socketProcessor);
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return threadPoolTaskExecutor;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public PathResolver getPathResolver() {
        return pathResolver;
    }

    public void setPathResolver(PathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }
}
