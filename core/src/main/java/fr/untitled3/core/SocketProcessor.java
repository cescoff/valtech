package fr.untitled3.core;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: corentinescoffier
 * Date: 4/2/13
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class SocketProcessor implements Runnable {

    private Logger logger = LoggerFactory.getLogger(SocketProcessor.class);

    private Socket socket;

    private PathResolver pathResolver;

    private Configuration configuration;

    public SocketProcessor(Socket socket, PathResolver pathResolver, Configuration configuration) {
        this.socket = socket;
        this.pathResolver = pathResolver;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        HttpHeader httpHeader = null;
        try {
            httpHeader = parseHttpHeader();
        } catch (Throwable t) {
            logger.error("An error has occured while parsing http headers");
        }
        WebFileResponse webFileResponse = pathResolver.resolve(httpHeader.getPath());
        if (webFileResponse.getResponseCode() == 200) {
            try {
                appendHttpHeaders(httpHeader, webFileResponse);
            } catch (Throwable t) {
                try {
                    writeHttpError(httpHeader, 500);
                } catch (IOException e) {
                    logger.error("A fatal error has occured while writing response", e);
                }
            }
        } else {
            try {
                writeHttpError(httpHeader, webFileResponse.getResponseCode());
            } catch (IOException e) {
                logger.error("A fatal error has occured while writing response", e);
            }
        }
    }

    private HttpHeader parseHttpHeader() throws IOException {
        InputStream inputStream = socket.getInputStream();
        LineIterator lineIterator = new LineIterator(new InputStreamReader(inputStream));
        int lineNumber = 0;

        String method = null;
        String path = null;
        String protocol = null;
        String host = null;
        String userAgent = null;

        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();

            if (lineNumber == 0) {
                String[] elements = StringUtils.split(line, " ");
                method = elements[0];
                path = elements[1];
                protocol = elements[2];
            }

            if (lineNumber > 0) {
                if (StringUtils.indexOf(line, "User-Agent: ") >= 0) {
                    userAgent = StringUtils.remove(line, "User-Agent: ");
                } else if (StringUtils.indexOf(line, "Host: ") >= 0) {
                    host = StringUtils.remove(line, "Host: ");
                }
            }

            if (StringUtils.isNotEmpty(method) && StringUtils.isNotEmpty(path) && StringUtils.isNotEmpty(protocol) && StringUtils.isNotEmpty(host) && StringUtils.isNotEmpty(userAgent)) {
                return new HttpHeader(method, path, protocol, host, userAgent);
            }

            lineNumber++;
        }

        return null;
    }

    private void appendHttpHeaders(HttpHeader httpHeader, WebFileResponse webFileResponse) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(new StringBuilder().append(httpHeader.getProtocol()).append(" ").append(200).append(" ").append("OK").append("\n\r").toString().getBytes());
        outputStream.write(new StringBuilder().append("Date: ").append(DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss z").print(DateTime.now())).append("\n\r").toString().getBytes());
        outputStream.write(new StringBuilder().append("Content-Type: text/html").append("\n\r").toString().getBytes());

        File webFile = webFileResponse.getFile();
        outputStream.write(new StringBuilder().append("Content-Length: ").append(FileUtils.sizeOf(webFile)).append("\n\r\n\r").toString().getBytes());
        FileInputStream fileInputStream = new FileInputStream(webFile);
        outputStream.write(IOUtils.toString(fileInputStream).getBytes());
        fileInputStream.close();
        outputStream.close();
    }

    private void writeHttpError(HttpHeader httpHeader, int code) throws IOException {
        OutputStream outputStream = socket.getOutputStream();

        String message = "OK";
        String errorHtml = null;
        if (code == 404) {
            message = "Not found";
            File error404File = new File(configuration.getPage404HtmlPath());
            errorHtml = IOUtils.toString(new FileInputStream(error404File));
        } else if (code == 500) {
            message = "Internal Server Error";
            File error500File = new File(configuration.getPage404HtmlPath());
            errorHtml = IOUtils.toString(new FileInputStream(error500File));
        }
        outputStream.write(new StringBuilder().append(httpHeader.getProtocol()).append(" ").append(code).append(" ").append(message).append("\n\r").toString().getBytes());
        outputStream.write(new StringBuilder().append("Date: ").append(DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss z").print(DateTime.now())).append("\n\r").toString().getBytes());
        outputStream.write(new StringBuilder().append("Content-Type: text/html").append("\n\r").toString().getBytes());

        if (StringUtils.isNotEmpty(errorHtml)) {
            outputStream.write(new StringBuilder().append("Content-Length: ").append(errorHtml.getBytes().length).append("\n\r\n\r").toString().getBytes());
            outputStream.write(errorHtml.getBytes());
        }
        outputStream.close();
    }

    public static class HttpHeader {

        private String method;

        private String path;

        private String protocol;

        private String host;

        private String userAgent;

        public HttpHeader(String method, String path, String protocol, String host, String userAgent) {
            this.method = method;
            this.path = path;
            this.protocol = protocol;
            this.host = host;
            this.userAgent = userAgent;
        }

        public String getMethod() {
            return method;
        }

        public String getPath() {
            return path;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getHost() {
            return host;
        }

        public String getUserAgent() {
            return userAgent;
        }
    }

}
