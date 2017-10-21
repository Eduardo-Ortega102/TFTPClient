package packets;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Acknowledge extends Packet {
    public static final byte OPERATION_CODE = 4;
    private final byte[] blockNumber;

    public Acknowledge(DatagramPacket datagram) throws InstantiationException {
        super(datagram);
        blockNumber = new byte[]{datagram.getData()[2], datagram.getData()[3]};
    }

    public Acknowledge(InetAddress serverAddress, int port, byte[] blockNumber) throws InstantiationException {
        super(4);
        this.blockNumber = blockNumber;
        datagramPacket = new DatagramPacket(createBuffer(), length, serverAddress, port);
    }

    private byte[] createBuffer() {
        byte[] buffer = new byte[length];
        int position = 0;
        buffer[position++] = NULL_BYTE;
        buffer[position++] = OPERATION_CODE;
        for (byte b : blockNumber) buffer[position++] = b;
        return buffer;
    }


    public byte[] getBlockNumber() {
        return blockNumber;
    }
}
