package fr.untitled3.core;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 3/28/13
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathResolver {

    private static Logger logger = LoggerFactory.getLogger(PathResolver.class);

    private Configuration configuration;

    private Map<String, File> path2Files = Maps.newHashMap();

    public void loadPathFromFileSystem(String uri) {
        if (path2Files.containsKey(uri)) return;
        File documentRoot = new File(configuration.getDocumentRoot());
        File requestedFile = new File(documentRoot, uri);

        synchronized (path2Files) {
            if (requestedFile.exists()) path2Files.put(uri, requestedFile);
        }
    }

    public WebFileResponse resolve(String uri) {
        try {
            loadPathFromFileSystem(uri);
            synchronized (path2Files) {
                if (path2Files.containsKey(uri)) {
                    File resultFile = path2Files.get(uri);
                    if (!resultFile.exists()) {
                        path2Files.remove(uri);
                        return new WebFileResponse(uri, 404);
                    } else return new WebFileResponse(uri, resultFile);

                } else {
                    StringBuilder uriWithWelcomeFile = new StringBuilder(uri);
                    if (StringUtils.endsWith(uri, "/")) {
                        uriWithWelcomeFile.append(configuration.getWelcomeFile());
                    } else {
                        uriWithWelcomeFile.append("/").append(configuration.getWelcomeFile());
                    }
                    if (path2Files.containsKey(uriWithWelcomeFile.toString())) return new WebFileResponse(uri, path2Files.get(uriWithWelcomeFile.toString()));
                    else return new WebFileResponse(uri, 404);
                }
            }
        } catch (Throwable t) {
            logger.error("An error has occured while resolving uri '" + uri + "'", t);
            return new WebFileResponse(uri, 500);
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
