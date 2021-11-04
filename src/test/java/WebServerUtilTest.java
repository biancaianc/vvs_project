import org.junit.Test;
import org.mockito.Mockito;
import util.WebServerUtil;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class WebServerUtilTest {
    private String path = "src/main/resources/TestSite/";
    WebServerUtil webServerUtil=new WebServerUtil(path);

    @Test
    public void takeRequestedFileTest_simpleRelative() throws IOException {
        File expectedFile = new File(this.path + "/a.html");
        File result = webServerUtil.takeRequestedFile("a.html");
        assertEquals(expectedFile, result);

    }
    @Test
    public void takeRequestedFileTest_generalRelative() throws IOException {
        File expectedFile = new File(this.path + "/aaa/b.html");
        File result = webServerUtil.takeRequestedFile("/aaa/b.html");
        assertEquals(expectedFile, result);
    }

    @Test
    public void takeRequestedFileTest_simpleAbsolute() throws IOException {
        File expectedFile = new File(this.path + "/aaa/bbb/c.html");
        File result = webServerUtil.takeRequestedFile("/c.html");
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


}
