package util;

import common.Common;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WebServerUtil {

    public static File takeRequestedFile(String requestedFilePath) throws IOException {
        String p = Common.rootDirectory;
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

    public static File findRequestedFileInSiteDirectory(String endpoint) throws IOException {
        Optional<Path> myPath = Files.walk(Paths.get(Common.rootDirectory))
                .filter(Files::isRegularFile)
                .filter(path1 -> path1.endsWith(endpoint))
                .findFirst();

        if (myPath.isPresent() ) {

            return new File(String.valueOf(myPath.get()));

        }
        return new File(Common.rootDirectory + "/error.html");
    }

}



