package packets;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Random;

import static input_output.DigitalUnit.BYTE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PacketBuilderShould {

    private int port;
    private InetAddress serverAddress;
    private PacketBuilder packetBuilder;

    @Before
    public void setUp() throws Exception {
        port = 123;
        serverAddress = InetAddress.getByName("192.168.1.11");
        packetBuilder = new PacketBuilder();
    }

    @Test
    public void create_a_packet_on_default_port() throws Exception {
        final int expectedPort = 69;
        RequestPacket packet = packetBuilder
                .withFileName("AnyFileName")
                .withTransferMode("AnyMode")
                .withServer(serverAddress)
                .createReadRequestPacket();
        final byte[] expectedBuffer = {0, 1, 65, 110, 121, 70, 105, 108, 101, 78, 97, 109, 101, 0, 65, 110, 121, 77, 111, 100, 101, 0};
        assertThat(packet.getPort(), is(expectedPort));
        assertThat(packet.getAddress(), is(serverAddress));
        assertThat(packet.getByteStream(), is(expectedBuffer));
    }

    @Test
    public void create_a_read_request_packet() throws Exception {
        RequestPacket packet = packetBuilder
                .withFileName("AnyFileName")
                .withTransferMode("AnyMode")
                .withServer(serverAddress)
                .withPort(port)
                .createReadRequestPacket();
        final byte[] expectedBuffer = {0, 1, 65, 110, 121, 70, 105, 108, 101, 78, 97, 109, 101, 0, 65, 110, 121, 77, 111, 100, 101, 0};
        assertThat(packet.getPort(), is(port));
        assertThat(packet.getAddress(), is(serverAddress));
        assertThat(packet.getByteStream(), is(expectedBuffer));
    }

    @Test
    public void create_a_write_request_packet() throws Exception {
        RequestPacket packet = packetBuilder
                .withFileName("AnyFileName")
                .withTransferMode("AnyMode")
                .withServer(serverAddress)
                .withPort(port)
                .createWriteRequestPacket();
        final byte[] expectedBuffer = {0, 2, 65, 110, 121, 70, 105, 108, 101, 78, 97, 109, 101, 0, 65, 110, 121, 77, 111, 100, 101, 0};
        assertThat(packet.getPort(), is(port));
        assertThat(packet.getAddress(), is(serverAddress));
        assertThat(packet.getByteStream(), is(expectedBuffer));
    }

    @Test
    public void create_an_acknowledge_packet() throws Exception {
        final byte[] blockNumber = new byte[]{110, 32};
        Acknowledge packet = packetBuilder
                .withServer(serverAddress)
                .withPort(port)
                .withBlockNumber(blockNumber)
                .createAcknowledgePacket();
        final byte[] expectedBuffer = {0, 4, 110, 32};
        assertThat(packet.getPort(), is(port));
        assertThat(packet.getAddress(), is(serverAddress));
        assertThat(packet.getBlockNumber(), is(blockNumber));
        assertThat(packet.getByteStream(), is(expectedBuffer));
    }

    @Test
    public void create_an_error_packet() throws Exception {
        final byte errorCode = 1;
        final String expectedErrorMessage = "File not found.";
        ErrorPacket packet = packetBuilder
                .withServer(serverAddress)
                .withPort(port)
                .withErrorCode(errorCode)
                .createErrorPacket();
        final byte[] expectedBuffer = {0, 5, 0, 1, 70, 105, 108, 101, 32, 110, 111, 116, 32, 102, 111, 117, 110, 100, 46, 0};
        assertThat(packet.getPort(), is(port));
        assertThat(packet.getAddress(), is(serverAddress));
        assertThat(packet.getErrorCode(), is(errorCode));
        assertThat(packet.getErrorMessage(), is(expectedErrorMessage));
        assertThat(packet.getByteStream(), is(expectedBuffer));
    }

    @Test
    public void create_an_error_packet_with_custom_message() throws Exception {
        final byte errorCode = 0;
        final String errorMessage = "A custom message.";
        ErrorPacket packet = packetBuilder
                .withServer(serverAddress)
                .withPort(port)
                .withErrorCode(errorCode)
                .withErrorMessage(errorMessage)
                .createErrorPacket();
        final byte[] expectedBuffer = {0, 5, 0, 0, 65, 32, 99, 117, 115, 116, 111, 109, 32, 109, 101, 115, 115, 97, 103, 101, 46, 0};
        assertThat(packet.getPort(), is(port));
        assertThat(packet.getAddress(), is(serverAddress));
        assertThat(packet.getErrorCode(), is(errorCode));
        assertThat(packet.getErrorMessage(), is(errorMessage));
        assertThat(packet.getByteStream(), is(expectedBuffer));
    }

    @Test
    public void not_create_an_error_packet_with_invalid_error_code() throws Exception {
        final byte errorCode = -1;
        final String errorMessage = "A custom message.";
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> packetBuilder
                        .withServer(serverAddress)
                        .withPort(port)
                        .withErrorCode(errorCode)
                        .withErrorMessage(errorMessage)
                        .createErrorPacket())
                .withMessage("Invalid error code for RFC 1350. Must be between 0 and 7 but found: -1.");
    }

    @Test
    public void create_a_data_packet() throws Exception {
        final int dataStreamLength = BYTE.times(512);
        final int packetLength = dataStreamLength + 4;
        final byte[] dataStream = getBytes(dataStreamLength);
        final byte[] blockNumber = new byte[]{110, 32};
        DataPacket packet = packetBuilder
                .withServer(serverAddress)
                .withPort(port)
                .withBlockNumber(blockNumber)
                .withData(dataStream)
                .createDataPacket();
        final byte[] expectedBuffer = new byte[packetLength];
        expectedBuffer[0] = 0;
        expectedBuffer[1] = 3;
        expectedBuffer[2] = 110;
        expectedBuffer[3] = 32;
        System.arraycopy(dataStream, 0, expectedBuffer, 4, dataStream.length);
        assertThat(packet.length(), is(packetLength));
        assertThat(packet.getPort(), is(port));
        assertThat(packet.getAddress(), is(serverAddress));
        assertThat(packet.getBlockNumber(), is(blockNumber));
        assertThat(packet.getDataStream(), is(dataStream));
        assertThat(packet.getByteStream(), is(expectedBuffer));
    }

    @Test
    public void not_create_a_packet_longer_than_516_elements() throws Exception {
        final int dataStreamLength = BYTE.times(513);
        assertThatExceptionOfType(InstantiationException.class)
                .isThrownBy(() -> packetBuilder
                        .withServer(serverAddress)
                        .withPort(port)
                        .withBlockNumber(new byte[]{110, 32})
                        .withData(getBytes(dataStreamLength))
                        .createDataPacket())
                .withMessage("Too much data, can't create the datagramPacket. " +
                        "The datagramPacket is " + (dataStreamLength + 4) + " bytes long and the maximum length is 516 bytes.");
    }


    @Test
    public void reset_after_use() throws Exception {
        RequestPacket packet1 = packetBuilder
                .withFileName("AnyFileName")
                .withTransferMode("AnyMode")
                .createReadRequestPacket();
        RequestPacket packet2 = packetBuilder
                .withFileName("AnyFileName")
                .createReadRequestPacket();
        assertThat(packet1.getByteStream(), is(not(equalTo(packet2.getByteStream()))));
    }

    private byte[] getBytes(int size) {
        final byte[] data = new byte[size];
        new Random().nextBytes(data);
        return data;
    }

}
