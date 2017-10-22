package input_output;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static input_output.DigitalUnit.MEGABYTE;

public class FileRecorder {
    public static final int BUFFER_LENGTH = MEGABYTE.value();
    private final String filePath;
    private final ByteArrayOutputStream buffer;
    private long amountOfBytes;

    public FileRecorder(String storagePath, String filename) {
        this.filePath = storagePath + filename;
        buffer = new ByteArrayOutputStream(BUFFER_LENGTH);
        amountOfBytes = 0;
    }

    public void receive(byte[] data) throws IOException {
        buffer.write(data);
        amountOfBytes += data.length;
        if (buffer.size() > BUFFER_LENGTH) store();
    }

    public long countOfBytes() {
        return amountOfBytes;
    }

    public void store() throws IOException {
        if (buffer.size() == 0) return;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(buffer.toByteArray());
            buffer.reset();
        } finally {
            fileOutputStream.close();
        }
    }
}
