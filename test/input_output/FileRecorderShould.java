package input_output;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.Instant;

import static input_output.DigitalUnit.MEGABYTE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class FileRecorderShould {

    private FileRecorder fileRecorder;
    private String storagePath;
    private String filename;

    @Before
    public void setUp() throws Exception {
        storagePath = "out/";
        filename = "filename.txt";
        fileRecorder = new FileRecorder(storagePath, filename);
    }

    @After
    public void tearDown() throws Exception {
        File file = new File(storagePath + filename);
        if (file.exists()) file.delete();
    }

    @Test
    public void count_the_amount_of_bytes_received() throws Exception {
        final byte[] data1 = data("Hello World!\n");
        final byte[] data2 = data("Bye!");
        final long expectedLength = data1.length + data2.length;
        fileRecorder.receive(data1);
        fileRecorder.receive(data2);
        assertThat(fileRecorder.countOfBytes(), is(expectedLength));
    }

    @Test
    public void store_when_is_empty() throws Exception {
        final long expectedCount = 0;
        fileRecorder.store();
        assertThat(fileRecorder.countOfBytes(), is(expectedCount));
        assertThat(new File(storagePath + filename).exists(), is(true));
    }

    @Test
    public void store_all_bytes_into_a_file() throws Exception {
        final byte[] data1 = data("Hello World!\n");
        final byte[] data2 = data("Bye! " + Instant.now());
        fileRecorder.receive(data1);
        fileRecorder.receive(data2);
        fileRecorder.store();
        assertThat(new File(storagePath + filename).exists(), is(true));
    }

    @Test
    public void store_all_bytes_when_is_full() throws Exception {
        final byte[] data = data("Hello, this text is going to be repeated a lot of times inside the file =D \n");
        for (int i = 0; i <= MEGABYTE.times(1) / data.length; i++) fileRecorder.receive(data);
        File file = new File(storagePath + filename);
        assertThat(file.exists(), is(true));
        assertThat(file.length(), is(fileRecorder.countOfBytes()));
    }

    private byte[] data(String data) {
        return data.getBytes();
    }

}
