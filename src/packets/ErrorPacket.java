package packets;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public class ErrorPacket extends Packet {
    public static final byte OPERATION_CODE = 5;
    private final byte errorCode;
    private final String errorMessage;

    public static ErrorPacket create(InetAddress serverAddress, int port, byte errorCode, String message) throws InstantiationException {
        return errorCode == 0 ?
                new ErrorPacket(serverAddress, port, errorCode, message) :
                new ErrorPacket(serverAddress, port, errorCode, getMessageFromCode(errorCode));
    }

    private static String getMessageFromCode(byte code) {
        if (code == 1) return "File not found.";
        if (code == 2) return "Access violation.";
        if (code == 3) return "Disk full or allocation exceeded.";
        if (code == 4) return "Illegal TFTP operation.";
        if (code == 5) return "Unknown transfer ID.";
        if (code == 6) return "File already exists.";
        if (code == 7) return "No such user.";
        throw new IllegalArgumentException("Invalid error code for RFC 1350. Must be between 0 and 7 but found: " + code + ".");
    }

    public ErrorPacket(DatagramPacket datagram) throws InstantiationException {
        super(datagram);
        this.errorCode = datagram.getData()[3];
        this.errorMessage = new String(Arrays.copyOfRange(datagram.getData(), 4, length - 1));
    }

    private ErrorPacket(InetAddress serverAddress, int port, byte errorCode, String errorMessage) throws InstantiationException {
        super(5 + errorMessage.length());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        datagramPacket = new DatagramPacket(createBuffer(), length, serverAddress, port);
    }

    private byte[] createBuffer() {
        byte[] buffer = new byte[length];
        int position = 0;
        buffer[position++] = NULL_BYTE;
        buffer[position++] = OPERATION_CODE;
        buffer[position++] = NULL_BYTE;
        buffer[position++] = errorCode;
        for (byte b : errorMessage.getBytes()) buffer[position++] = b;
        buffer[position++] = NULL_BYTE;
        return buffer;
    }

    public byte getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
