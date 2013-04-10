package fr.untitled3.core;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.text.DateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 3/28/13
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Configuration {

    @XmlElement
    private int port = 8080;

    @XmlElement
    private int threadPoolSize = 25;

    @XmlElement
    private String dateTimeFormat = "dd/MM/yyyy HH:mm";

    @XmlElement
    private String page404HtmlPath = "errors/404.html";

    @XmlElement
    private String page500HtmlPath = "errors/500.html";

    @XmlElement
    private String documentRoot = "www-data";

    @XmlElement
    private String welcomeFile = "index.html";

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public String getPage404HtmlPath() {
        return page404HtmlPath;
    }

    public void setPage404HtmlPath(String page404HtmlPath) {
        this.page404HtmlPath = page404HtmlPath;
    }

    public String getPage500HtmlPath() {
        return page500HtmlPath;
    }

    public void setPage500HtmlPath(String page500HtmlPath) {
        this.page500HtmlPath = page500HtmlPath;
    }

    public String getDocumentRoot() {
        return documentRoot;
    }

    public void setDocumentRoot(String documentRoot) {
        this.documentRoot = documentRoot;
    }

    public String getWelcomeFile() {
        return welcomeFile;
    }

    public void setWelcomeFile(String welcomeFile) {
        this.welcomeFile = welcomeFile;
    }

    public void validate() {
        if (threadPoolSize < 1) throw new IllegalStateException("'threadPoolSize' must be a value > 1");
        try {
            DateTimeFormat.forPattern(dateTimeFormat);
        } catch (Throwable t) {
            throw new IllegalStateException("Invalid value for 'dateTimeFormat'", t);
        }

        File page404File = new File(page404HtmlPath);
        if (!page404File.exists()) throw new IllegalStateException("'page404HtmlPath' does not exist");

        File page500File = new File(page500HtmlPath);
        if (!page500File.exists()) throw new IllegalStateException("'page500HtmlPath' does not exist");

        File documentRootDir = new File(documentRoot);
        if (!documentRootDir.exists()) throw new IllegalStateException("'documentRoot' does not exist");

    }


}
