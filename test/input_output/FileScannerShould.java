package input_output;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static input_output.DigitalUnit.BYTE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class FileScannerShould {

    private FileScanner bigFileScanner;
    private FileScanner smallFileScanner;
    private int smallFileSize;

    public FileScannerShould() throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream("out/smallFile.txt")) {
            String phrase = "Hello, this is a text inside the file =D \n";
            fileOutputStream.write(phrase.getBytes());
            smallFileSize = phrase.length();
        }
    }

    @Before
    public void setUp() throws Exception {
        bigFileScanner = new FileScanner("out/bigFile.txt", BYTE.times(512));
        smallFileScanner = new FileScanner("out/smallFile.txt", BYTE.times(512));
    }

    @Test
    public void raise_an_exception_when_file_not_exists() throws Exception {
        assertThatExceptionOfType(FileNotFoundException.class)
                .isThrownBy(() -> new FileScanner("/not/existing/file.txt", BYTE.times(512)));
    }

    @Test
    public void count_the_amount_of_bytes_readed() throws Exception {
        final long expectedLength = BYTE.times(512) * 3;
        bigFileScanner.readBlock();
        bigFileScanner.readBlock();
        bigFileScanner.readBlock();
        assertThat(bigFileScanner.countOfReadedBytes(), is(expectedLength));
    }

    @Test
    public void get_blocks_of_512_bytes_from_buffer_when_is_full() throws Exception {
        final int expectedLength = BYTE.times(512);
        final byte[] data1 = bigFileScanner.readBlock();
        final byte[] data2 = bigFileScanner.readBlock();
        assertThat(data1.length, is(expectedLength));
        assertThat(data2.length, is(expectedLength));
        assertThat(data1, is(not(equalTo(data2))));
    }

    @Test
    public void get_blocks_of_bytes_from_buffer_when_is_not_empty() throws Exception {
        final int expectedLength = smallFileSize;
        final byte[] data = smallFileScanner.readBlock();
        assertThat(data.length, is(expectedLength));
    }

    @Test
    public void get_a_block_of_length_0_when_buffer_is_empty() throws Exception {
        final int firstReadExpectedLength = smallFileSize;
        final int secondReadExpectedLength = 0;
        final byte[] data1 = smallFileScanner.readBlock();
        final byte[] data2 = smallFileScanner.readBlock();
        assertThat(data1.length, is(firstReadExpectedLength));
        assertThat(data2.length, is(secondReadExpectedLength));
    }
}
