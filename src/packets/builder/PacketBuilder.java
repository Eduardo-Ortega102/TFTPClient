package packets.builder;

import packets.*;

import java.net.InetAddress;

public class PacketBuilder {
    private int port;
    private String fileName;
    private String transferMode;
    private InetAddress serverAddress;
    private byte[] blockNumber;
    private byte errorCode;
    private byte[] data;
    private String errorMessage;

    public PacketBuilder() {
        reset();
    }

    public void reset() {
        this.port = 69;
        this.fileName = "";
        this.transferMode = "";
        this.serverAddress = null;
        this.blockNumber = null;
        this.errorCode = -1;
        this.data = null;
        this.errorMessage = null;
    }

    public PacketBuilder withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public PacketBuilder withTransferMode(String transferMode) {
        this.transferMode = transferMode;
        return this;
    }

    public PacketBuilder withServer(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
        return this;
    }

    public PacketBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public PacketBuilder withBlockNumber(byte[] blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    public PacketBuilder withData(byte[] data) {
        this.data = data;
        return this;
    }

    public PacketBuilder withErrorCode(byte errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public PacketBuilder withErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public RequestPacket createReadRequestPacket() throws InstantiationException {
        RequestPacket readRequest = RequestPacket.createReadRequest(serverAddress, port, fileName, transferMode);
        reset();
        return readRequest;
    }

    public RequestPacket createWriteRequestPacket() throws InstantiationException {
        RequestPacket writeRequest = RequestPacket.createWriteRequest(serverAddress, port, fileName, transferMode);
        reset();
        return writeRequest;
    }

    public ErrorPacket createErrorPacket() throws InstantiationException {
        ErrorPacket errorPacket = ErrorPacket.create(serverAddress, port, errorCode, errorMessage);
        reset();
        return errorPacket;
    }

    public Acknowledge createAcknowledgePacket() throws InstantiationException {
        Acknowledge acknowledge = new Acknowledge(serverAddress, port, blockNumber);
        reset();
        return acknowledge;
    }

    public DataPacket createDataPacket() throws InstantiationException {
        DataPacket dataPacket = new DataPacket(serverAddress, port, blockNumber, data);
        reset();
        return dataPacket;
    }

}
