package input_output;

public enum DigitalUnit {
    BYTE(1),
    KILOBYTE(BYTE.times(1024)),
    MEGABYTE(KILOBYTE.times(1024));

    private int value;

    DigitalUnit(int value) {
        this.value = value;
    }

    public int times(int times) {
        return value * times;
    }

}
