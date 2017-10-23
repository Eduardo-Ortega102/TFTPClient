package input_output;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static input_output.DigitalUnit.MEGABYTE;

public class FileScanner {

    private final InputStream fileInputStream;
    private InputStream buffer;
    private final int block_size;
    private long readedBytes;
    private boolean streamIsClosed;

    public FileScanner(String filePath, int block_size) throws IOException {
        fileInputStream = new FileInputStream(filePath);
        this.block_size = block_size;
        readedBytes = 0;
        streamIsClosed = false;
        fillBuffer();
    }

    private void fillBuffer() throws IOException {
        if (streamIsClosed) return;
        byte[] bytes = getBytes(MEGABYTE.times(1), fileInputStream);
        if (bytes.length == 0) handleClose();
        else buffer = new ByteArrayInputStream(bytes);
    }

    private void handleClose() throws IOException {
        fileInputStream.close();
        streamIsClosed = true;
    }

    private byte[] getBytes(int amountOfData, InputStream dataSource) throws IOException {
        byte[] block = new byte[amountOfData];
        int readedBytes = dataSource.read(block);
        if (readedBytes == -1) return new byte[0];
        if (readedBytes < amountOfData) block = shrink(block, readedBytes);
        return block;
    }

    private byte[] shrink(byte[] block, int length) {
        return Arrays.copyOfRange(block, 0, length);
    }

    public byte[] readBlock() throws IOException {
        if (buffer.available() < block_size) fillBuffer();
        byte[] block = getBytes(block_size, buffer);
        this.readedBytes += block.length;
        return block;
    }

    public long countOfReadedBytes() {
        return readedBytes;
    }
}
