package input_output;

import packets.DataPacket;

import java.io.*;

public class FileRecorder {
    private final String filename;
    private final String storagePath;
    private final ByteArrayOutputStream stream;

    public FileRecorder(String storagePath, String filename) {
        this.storagePath = storagePath;
        this.filename = filename;
        stream = new ByteArrayOutputStream();
    }

    public void receive(DataPacket packet) throws IOException {
        stream.write(packet.getDataStream());
    }

    public int amountOfBytes() {
        return stream.size();
    }

    public void store() throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(storagePath + filename);
            fileOutputStream.write(stream.toByteArray());
        } finally {
            fileOutputStream.close();
        }
    }
}
