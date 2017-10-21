package input_output;

import packets.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class TFTPSocket {

    private final DatagramSocket datagramSocket;

    public TFTPSocket(DatagramSocket datagramSocket) throws SocketException {
        this.datagramSocket = datagramSocket;
    }

    public void send(Packet packet) throws IOException {
        datagramSocket.send(packet.getDatagram());
    }

    public synchronized Packet receive() throws IOException, InstantiationException {
        DatagramPacket inputDatagram = new DatagramPacket(new byte[Packet.MAXIMUM_LENGTH], Packet.MAXIMUM_LENGTH);
        datagramSocket.receive(inputDatagram);
        return Packet.createFromDatagram(inputDatagram);
    }

}
