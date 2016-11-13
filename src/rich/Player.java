package rich;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private State state;
    private Command lastExecuted;
    private Land currentLand;
    private List<Land> lands;
    private int balance;

    public Player() {
        this.state = State.WAITING_FOR_COMMAND;
    }

    public State getState() {
        return state;
    }

    public State execute(Command command) {
        state = command.execute(this);
        lastExecuted = command;
        return state;
    }

    public State respond(Response response) {
        state = response.execute(this, response);
        return state;
    }

    public Land getCurrentLand() {
        return currentLand;
    }

    public void moveTo(Land target) {
        currentLand = target;
    }

    public static Player createPlayerWithStarting(Land starting) {
        Player player = new Player();
        player.currentLand = starting;
        return player;
    }

    public static Player createPlayerWithBalance(Land starting, int money) {
        Player player = new Player();
        player.currentLand = starting;
        player.lands = new ArrayList<>();
        player.balance = money;
        return player;
    }

    public int getBalance() {
        return balance;
    }

    public List<Land> getLands() {
        return lands;
    }

    public void buy() {
        if (currentLand.getPrice() <= balance) {
            balance -= currentLand.getPrice();
            lands.add(currentLand);
            currentLand.boughtBy(this);
        }
    }

    public void upgrade() {
        if (currentLand.getPrice() <= balance && currentLand.canUpgrade()) {
            balance -= currentLand.getPrice();
            currentLand.upgrade();
        }
    }

    public enum State {WAITING_FOR_RESPONSE, END_TURN, WAITING_FOR_COMMAND}
}
