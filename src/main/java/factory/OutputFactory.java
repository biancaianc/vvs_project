package factory;

import java.io.OutputStream;
import java.io.PrintWriter;

public class OutputFactory {
    public PrintWriter getOutputPrintWriter(OutputStream outputStream) {
        return new PrintWriter(outputStream, true);
    }
}
