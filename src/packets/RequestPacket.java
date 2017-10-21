package packets;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class RequestPacket extends Packet{

    public static final byte READ_OPERATION_CODE = 1;
    public static final byte WRITE_OPERATION_CODE = 2;
    private final byte operationCode;

    public static RequestPacket createReadRequest(InetAddress serverAddress,
                                                  int port, String fileName, String transferMode) throws InstantiationException {
        return new RequestPacket(READ_OPERATION_CODE, serverAddress, port, fileName, transferMode);
    }

    public static RequestPacket createWriteRequest(InetAddress serverAddress,
                                                   int port, String fileName, String transferMode) throws InstantiationException {
        return new RequestPacket(WRITE_OPERATION_CODE, serverAddress, port, fileName, transferMode);
    }

    public RequestPacket(DatagramPacket datagram) throws InstantiationException {
        super(datagram);
        operationCode = datagram.getData()[1];
        if (isNotValidOperationCode())
            throw new InstantiationException("Invalid operation code for Read and Write Request");
    }

    private boolean isNotValidOperationCode() {
        return operationCode != READ_OPERATION_CODE && operationCode != WRITE_OPERATION_CODE;
    }

    private RequestPacket(byte operationCode, InetAddress serverAddress,
                          int port, String fileName, String transferMode) throws InstantiationException {
        super(4 + fileName.length() + transferMode.length());
        this.operationCode = operationCode;
        byte[] buffer = createBuffer(length, fileName, transferMode);
        datagramPacket = new DatagramPacket(buffer, length, serverAddress, port);
    }

    private byte[] createBuffer(int capacity, String fileName, String transferMode) {
        byte[] buffer = new byte[capacity];
        int position = 0;
        buffer[position++] = NULL_BYTE;
        buffer[position++] = operationCode;
        for (byte b : fileName.getBytes()) buffer[position++] = b;
        buffer[position++] = NULL_BYTE;
        for (byte b : transferMode.getBytes()) buffer[position++] = b;
        buffer[position++] = NULL_BYTE;
        return buffer;
    }

    public boolean isReadRequest(){
        return operationCode == READ_OPERATION_CODE;
    }

}
