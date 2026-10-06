package htcpcp;

public class CoffeePot {

    public enum State { IDLE, BREWING }

    private State state = State.IDLE;

    public State state () {
        return state;
    }

    public boolean start() {
        if (state == State.BREWING) {
            return false;
        }
        state = State.BREWING;
        return true;
    }

    public boolean stop() {
        if (state == State.IDLE) {
            return false;
        }
        state = State.IDLE;
        return true;
    }
}