package rich;

import rich.command.Command;
import rich.command.Response;
import rich.place.Land;
import rich.place.Place;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.tools.doclint.Entity.sum;

public class Player {
    private State state;
    private Command lastExecuted;
    private List<Land> lands;
    private Map<Tool, Integer> tools;
    //    private List<Tool> tools;
    private int balance;
    private Place currentPlace;
    private int points;

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

    public static Player createPlayerWithPoints(int startPoints) {
        Player player = new Player();
        player.points = startPoints;
        return player;
    }

    public static Player createPlayerWithStartingAndPoints(Place starting, int startPoints) {
        Player player = new Player();
        player.currentPlace = starting;
        player.points = startPoints;
        player.tools = new HashMap<>();
        return player;
    }

    public boolean canHaveMoreTools() {
        int toolQuantityAmount = 0;
        for (Integer quantity : tools.values()) {
            toolQuantityAmount += quantity;
        }
        if (toolQuantityAmount >= 10)
            return false;
        else
            return true;
    }


    public boolean pointsEnoughToBuyRoadBlock() {
        return points > Tool.RoadBlock.getPoints();
    }

    public void buyTool(Tool tool) {
        points -= tool.getPoints();
        if (tools.containsKey(tool)) {
            tools.put(tool, tools.get(tool) + 1);
        } else {
            tools.put(tool, 1);
        }
    }

    public boolean hasEnoughPoints() {
        return points >= Tool.Robot.getPoints();
    }

    public int getToolsAmount() {
        int toolQuantityAmount = 0;
        for (Integer quantity : tools.values()) {
            toolQuantityAmount += quantity;
        }
        return toolQuantityAmount;
    }

    public enum State {WAITING_FOR_RESPONSE, END_TURN, WAITING_FOR_COMMAND}
}
