
import input_output.TFTPSocket;
import org.junit.Before;
import org.junit.Test;
import packets.Acknowledge;
import packets.Packet;
import packets.builder.PacketBuilder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TFTPSocketShould {

    private TFTPSocket socket;
    private DatagramSocket datagramSocket;
    private PacketBuilder packetBuilder;

    @Before
    public void setUp() throws Exception {
        datagramSocket = mock(DatagramSocket.class);
        socket = new TFTPSocket(datagramSocket);
        packetBuilder = new PacketBuilder();
    }

    @Test
    public void receive_a_packet() throws Exception {
        final byte[] expectedBuffer = {0, 4, 110, 32};
        doAnswer(invocationOnMock -> {
            DatagramPacket datagramPacket = (DatagramPacket) invocationOnMock.getArguments()[0];
            datagramPacket.setData(new byte[]{0, 4, 110, 32});
            return null;
        }).when(datagramSocket).receive(any(DatagramPacket.class));
        Packet packet = socket.receive();
        assertThat(packet, instanceOf(Acknowledge.class));
        assertThat(packet.getByteStream(), is(expectedBuffer));
    }

    @Test
    public void send_a_packet() throws Exception {
        Packet packet = packetBuilder
                .withServer(InetAddress.getByName("192.168.1.11"))
                .withFileName("AnyFileName")
                .withTransferMode("AnyMode")
                .createReadRequestPacket();
        socket.send(packet);
        verify(datagramSocket, times(1)).send(packet.getDatagram());
    }

}
