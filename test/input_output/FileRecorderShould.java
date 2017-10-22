package input_output;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import packets.DataPacket;

import java.io.File;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class FileRecorderShould {

    private FileRecorder fileRecorder;
    private String storagePath;
    private String filename;

    @Before
    public void setUp() throws Exception {
        storagePath = "out/";
        filename = "filename.txt";
        fileRecorder = new FileRecorder(storagePath, filename);
        File file = new File(storagePath + filename);
        if (file.exists()) file.delete();
    }

    @Test
    public void count_the_amount_of_bytes_received() throws Exception {
        final DataPacket packet1 = packetWithData("Hello World!\n");
        final DataPacket packet2 = packetWithData("Bye!");
        final int expectedLength = packet1.getDataStream().length + packet2.getDataStream().length;
        fileRecorder.receive(packet1);
        fileRecorder.receive(packet2);
        assertThat(fileRecorder.amountOfBytes(), is(expectedLength));
    }

    @Test
    public void storage_all_bytes_into_a_file() throws Exception {
        final DataPacket packet1 = packetWithData("Hello World!\n");
        final DataPacket packet2 = packetWithData("Bye! " + Instant.now());
        fileRecorder.receive(packet1);
        fileRecorder.receive(packet2);
        fileRecorder.store();
        assertThat(new File(storagePath + filename).exists(), is(true));
    }

    private DataPacket packetWithData(String data) {
        DataPacket packet = mock(DataPacket.class);
        doReturn(data.getBytes()).when(packet).getDataStream();
        return packet;
    }

}
