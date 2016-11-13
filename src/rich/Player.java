package rich;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private State state;
    private Command lastExecuted;
    private List<Land> lands;
    private int balance;
    private Place currentPlace;

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
        state = lastExecuted.respondWith(this, response);
        return state;
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }


    public void moveTo(Place target) {
        currentPlace = target;
    }

    public static Player createPlayerWithStarting(Place starting) {
        Player player = new Player();
        player.currentPlace = starting;
        return player;
    }

    public static Player createPlayerWithBalance(Place starting, int startBalance) {
        Player player = new Player();
        player.currentPlace = starting;
        player.lands = new ArrayList<>();
        player.balance = startBalance;
        return player;
    }

    public int getBalance() {
        return balance;
    }

    public List<Land> getLands() {
        return lands;
    }

    public void buy(Land land) {
        if (land.getPrice() <= balance) {
            balance -= land.getPrice();
            land.setOwner(this);
            lands.add(land);
        }
    }

    public void upgrade(Land land) {
        if (land.getPrice() <= balance && land.canUpgrade()) {
            balance -= land.getPrice();
            land.upgrade();
        }
    }

    public static Player createPlayerWithBalanceStartingAndOwingLand(int startBalance, Place starting, Land owingLand) {
        Player player = new Player();
        player.currentPlace = starting;
        player.balance = startBalance;
        player.lands = new ArrayList<>();
        player.lands.add(owingLand);
        return player;
    }

    public enum State {WAITING_FOR_RESPONSE, END_TURN, WAITING_FOR_COMMAND}
}
