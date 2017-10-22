package input_output;

public enum DigitalUnit {
    ONE_BYTE(1),
    ONE_KILOBYTE(ONE_BYTE.times(1024)),
    ONE_MEGABYTE(ONE_KILOBYTE.times(1024));

    private int value;

    DigitalUnit(int value) {
        this.value = value;
    }

    public int times(int times) {
        return value * times;
    }

    public int value() {
        return value;
    }
}
