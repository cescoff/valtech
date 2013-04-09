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

    public void loadPathFromFileSystem() {
        File documentRoot = new File(configuration.getDocumentRoot());
        Collection<File> documentRootFiles = FileUtils.listFiles(documentRoot, null, true);
        Set<String> newUriPaths = Sets.newHashSet();
        for (File documentRootFile : documentRootFiles) {
            String uriPath = StringUtils.substring(documentRootFile.getPath(), StringUtils.indexOf(documentRootFile.getPath(), configuration.getDocumentRoot()) + configuration.getDocumentRoot().length());
            if (!path2Files.containsKey(uriPath)) path2Files.put(uriPath, documentRootFile);
            newUriPaths.add(uriPath);
        }
        Set<String> knownUris = Sets.newHashSet(path2Files.keySet());
        for (String uri : knownUris) {
            if (!newUriPaths.contains(uri)) {
                synchronized (path2Files) {
                    path2Files.remove(uri);
                }
            }
        }

    }

    public WebFileResponse resolve(String uri) {
        try {
            loadPathFromFileSystem();
            synchronized (path2Files) {
                if (path2Files.containsKey(uri)) {
                    return new WebFileResponse(uri, path2Files.get(uri));
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
