package model;

public final class Graphic {

    private final int id, delay;

    public Graphic(int id, int delay) {
        this.id = id;
        this.delay = delay;
    }

    public int getId() {
        return id;
    }

    public int getDelay() {
        return delay;
    }
}