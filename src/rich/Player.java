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

import static rich.tool.Tool.RoadBlock;
import static rich.tool.Tool.Robot;

public class Player {
    private State state;
    private Command lastExecuted;
    private List<Land> lands;
    private Map<Integer, Integer> tools;
    private int balance;
    private Place currentPlace;
    private int points;
    private int toolsAmount;
    private int noPunishTimes;

    public int getPoints() {
        return points;
    }

    public Player() {
        this.state = State.WAITING_FOR_COMMAND;
        this.lastExecuted = null;
        this.lands = new ArrayList<>();
        this.tools = new HashMap<>();
        this.balance = 0;
        this.points = 0;
        this.toolsAmount = 0;
        this.noPunishTimes = 0;
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

    public static Player createPlayerWithStartingAndPoints(Place starting, int startPoints) {
        Player player = new Player();
        player.currentPlace = starting;
        player.points = startPoints;
        player.tools = new HashMap<>();
        player.toolsAmount = 0;
        return player;
    }

    public boolean buyTool(Tool tool) {
        if (canBuy(tool)) {
            points -= tool.getPoints();
            toolsAmount += 1;
            if (tool.ordinal() == RoadBlock.ordinal()) {
                int curNum = tools.getOrDefault(tool.ordinal(), 0);
                tools.put(RoadBlock.ordinal(), curNum + 1);
            } else if (tool.ordinal() == Robot.ordinal()) {
                int curNum = tools.getOrDefault(tool.ordinal(), 0);
                tools.put(RoadBlock.ordinal(), curNum + 1);
            } else {
                int curNum = tools.getOrDefault(tool.ordinal(), 0);
                tools.put(RoadBlock.ordinal(), curNum + 1);
            }
            return true;
        }
        return false;
    }

    public boolean canBuy(Tool tool) {
        return tool.getPoints() < points && toolsAmount < 10;
    }

    public int getToolsAmount() {
        return toolsAmount;
    }

    public void gainBonus() {
        balance += GameConstant.MONEY_BONUS;
    }

    public void gainPoints() {
        points += GameConstant.POINT_BONUS;
    }

    public void gainNoPunishTimes() {
        noPunishTimes += GameConstant.MASCOT_BONUS;
    }

    public int getNoPunishTimes() {
        return noPunishTimes;
    }

    public enum State {WAITING_FOR_RESPONSE, END_TURN, WAITING_FOR_COMMAND}
}
