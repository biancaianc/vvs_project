import org.junit.Test;
import util.WebServerUtil;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class WebServerUtilTest {
    private String path = "src/main/resources/TestSite/";
    WebServerUtil webServerUtil = new WebServerUtil(path);

    @Test
    public void takeRequestedFileTest() throws IOException {
        File expectedFile = new File(this.path + "/a.html");
        File result = webServerUtil.takeRequestedFile("a.html");
        assertEquals(expectedFile, result);

    }


    @Test
    public void findRequestedFileInSiteDirectoryTest_fileExists() throws IOException {
        File expectedFile = new File(this.path + "\\aaa\\bbb\\c.html");
        File result = webServerUtil.findRequestedFileInSiteDirectory("c.html");
        assertEquals(expectedFile, result);
    }

    @Test
    public void findRequestedFileInSiteDirectoryTest_fileDoesNotExist() throws IOException {
        File expectedFile = new File(this.path + "/error.html");
        File result = webServerUtil.findRequestedFileInSiteDirectory("ca.html");
        assertEquals(expectedFile, result);
    }

    @Test
    public void takePathFromRequestGet_normal() {
        String expected = "/a.html";
        String result = webServerUtil.takePathFromRequestGet("GET /a.html HTTP/1.1");
        assertEquals(expected, result);
    }
    @Test
    public void takePathFromRequestGet_index() {
        String expected = "/index.html";
        String result = webServerUtil.takePathFromRequestGet("GET / HTTP/1.1");
        assertEquals(expected, result);
    }
}
