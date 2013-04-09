package fr.untitled3.core;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 3/28/13
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebFileResponse {

    private String uri;

    private File file;

    private int responseCode;

    public WebFileResponse(String uri, File file) {
        this.uri = uri;
        this.file = file;
        if (file.exists()) responseCode = 200;
        else responseCode = 404;
    }

    public WebFileResponse(String uri, int responseCode) {
        this.uri = uri;
        this.responseCode = responseCode;
    }

    public String getUri() {
        return uri;
    }

    public File getFile() {
        return file;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
