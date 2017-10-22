package packets;

import input_output.DigitalUnit;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Packet {
    public static final int MAXIMUM_LENGTH = DigitalUnit.ONE_BYTE.times(516);
    public static final byte NULL_BYTE = 0;
    protected final int length;
    protected DatagramPacket datagramPacket;

    public static Packet createFromDatagram(DatagramPacket datagram) throws InstantiationException {
        byte operationCode = datagram.getData()[1];
        if (operationCode == DataPacket.OPERATION_CODE) return new DataPacket(datagram);
        if (operationCode == Acknowledge.OPERATION_CODE) return new Acknowledge(datagram);
        if (operationCode == ErrorPacket.OPERATION_CODE) return new ErrorPacket(datagram);
        if (operationCode == RequestPacket.READ_OPERATION_CODE) return new RequestPacket(datagram);
        if (operationCode == RequestPacket.WRITE_OPERATION_CODE) return new RequestPacket(datagram);
        throw new IllegalArgumentException("Invalid datagram for RFC 1350.");
    }

    protected Packet(DatagramPacket datagram) {
        length = datagram.getLength();
        this.datagramPacket = datagram;
    }

    public Packet(int length) throws InstantiationException {
        this.length = length;
        if (length > MAXIMUM_LENGTH)
            throw new InstantiationException("Too much data, can't create the datagramPacket. " +
                    "The datagramPacket is " + length + " bytes long and the maximum length is 516 bytes.");
    }

    public DatagramPacket getDatagram() {
        return datagramPacket;
    }

    public byte[] getByteStream() {
        return datagramPacket.getData();
    }

    public InetAddress getAddress() {
        return datagramPacket.getAddress();
    }

    public int getPort() {
        return datagramPacket.getPort();
    }

    public int length() {
        return length;
    }

}
