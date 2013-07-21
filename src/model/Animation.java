package model;

public final class Animation {

    private final int id, delay;

    public Animation(int id, int delay) {
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