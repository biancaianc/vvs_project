package factory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputFactory {

    public BufferedReader getInputBufferReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));

    }
}
