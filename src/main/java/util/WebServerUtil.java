package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class WebServerUtil {
    private String sitePath;
    public WebServerUtil(String sitepah) {
        this.sitePath=sitepah;
   }

    public File takeRequestedFile(String requestedFilePath) throws IOException {
        String p = this.sitePath;
        p = p.concat(requestedFilePath);
        p = p.trim();
        p = p.replaceAll("%20", " ");
        p = p.trim();
        File fi = new File(p);
        String endpoint = requestedFilePath.replace("/", "").replaceAll("%20", " ");
        if (!fi.exists()) {
            return findRequestedFileInSiteDirectory(endpoint);
        } else {
            return fi;
        }
    }

    public File findRequestedFileInSiteDirectory( String endpoint) throws IOException {
        List<Path> mylist = Files.walk(Paths.get(this.sitePath))
                .filter(Files::isRegularFile)
                .filter(path1 -> path1.endsWith(endpoint))
                .collect(Collectors.toList());
        if (mylist.size() == 0) {
            return new File(this.sitePath +"/error.html");
        } else {
            return new File(String.valueOf(mylist.get(0)));
        }
    }

    public String takePathFromRequestGet(String inputLine) {
        char path[] = new char[100];
        int start_value = 4;
        inputLine.getChars(start_value, inputLine.length() - 9, path, 0);
        String endpoint = String.valueOf(path);
        endpoint = endpoint.trim();
        if (endpoint.length() == 1) {
            endpoint += "index.html";
        }
        return endpoint;
    }
}


