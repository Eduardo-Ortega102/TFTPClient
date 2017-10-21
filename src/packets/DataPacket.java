package packets;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public class DataPacket extends Packet {
    public static final byte OPERATION_CODE = 3;
    private final byte[] blockNumber;

    public DataPacket(DatagramPacket datagram) throws InstantiationException {
        super(datagram);
        blockNumber = new byte[]{datagram.getData()[2], datagram.getData()[3]};
    }

    public DataPacket(InetAddress serverAddress, int port, byte[] blockNumber, byte[] data) throws InstantiationException {
        super(4 + data.length);
        this.blockNumber = blockNumber;
        datagramPacket = new DatagramPacket(createBuffer(blockNumber, data), length, serverAddress, port);
    }

    private byte[] createBuffer(byte[] blockNumber, byte[] data) {
        byte[] buffer = new byte[length];
        int position = 0;
        buffer[position++] = NULL_BYTE;
        buffer[position++] = OPERATION_CODE;
        for (byte b : blockNumber) buffer[position++] = b;
        for (byte b : data) buffer[position++] = b;
        return buffer;
    }

    public byte[] getBlockNumber() {
        return blockNumber;
    }

    public byte[] getDataStream() {
        return Arrays.copyOfRange(getByteStream(), 4, length);
    }

}
