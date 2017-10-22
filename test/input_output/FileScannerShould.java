package input_output;

import org.junit.Before;
import org.junit.Test;

public class FileScannerShould {

    private FileScanner fileScanner;

    @Before
    public void setUp() throws Exception {
        fileScanner = new FileScanner("out/filename2.txt");
    }

    @Test
    public void raise_an_exception_when_file_not_exists() throws Exception {

    }

    @Test
    public void read_blocks_of_512_bytes_from_a_file_when_is_full() throws Exception {

    }

    @Test
    public void read_blocks_of_bytes_from_a_file_when_is_not_empty() throws Exception {

    }

    @Test
    public void raise_an_exception_if_try_to_read_when_is_empty() throws Exception {
        // TODO esto significaría que ha terminado la lectura
    }
}
