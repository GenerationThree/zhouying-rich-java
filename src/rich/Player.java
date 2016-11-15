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
    private int pauseTimes;
    private GameMap map;

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
        this.pauseTimes = 0;
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

    public static Player createPlayerWithStartingAndBalance(Place starting, int startBalance) {
        Player player = new Player();
        player.currentPlace = starting;
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

    public boolean buyTool(Tool tool) {
        if (canBuy(tool)) {
            points -= tool.getPoints();
            toolsAmount += 1;
            tools.put(tool.ordinal(), tools.getOrDefault(tool.ordinal(), 0) + 1);
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

    public boolean canAfford(int price) {
        if (noPunishTimes > 0) return true;
        else return balance >= price;
    }

    public void pay(int price) {
        balance -= price;
    }

    public int getPauseTimes() {
        return pauseTimes;
    }

    public void pausedBy(int pauseTimes) {
        this.pauseTimes = pauseTimes;
    }

    public static Player createPlayerWithPoints(int startPoints) {
        Player player = new Player();
        player.points = startPoints;
        return player;
    }

    public boolean sellTool(int index) {
        if (tools.getOrDefault(index, 0) > 0) {
            tools.put(index, tools.get(index) - 1);
            toolsAmount -= 1;
            points += Tool.values()[index].getPoints();
            return true;
        }
        return false;
    }

    public boolean sellLand(int position) {
        Place place = map.findBy(position);
        if (place instanceof Land) {
            Land cur = (Land) place;
            if (this == cur.getOwner()) {
                balance += cur.getPrice();
                lands.remove(cur);
                cur.setOwner(null);
            }
        }
        return false;
    }

    public static Player createPlayerWithBalanceAndAMap(GameMap map, int startBalance) {
        Player player = new Player();
        player.map = map;
        player.balance = startBalance;
        return player;
    }

    public String query() {
        int zeroLevelAmount = 0;
        int oneLevelAmount = 0;
        int twoLevelAmount = 0;
        int threeLevelAmount = 0;
        for (Land land: lands) {
            switch (land.getCurrentLevel()) {
                case 0:
                    zeroLevelAmount += 1;
                    break;
                case 1:
                    oneLevelAmount += 1;
                    break;
                case 2:
                    twoLevelAmount += 1;
                    break;
                case 3:
                    threeLevelAmount += 1;
                    break;
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("资金: $").append(balance).append("\n")
                .append("点数: ").append(points).append("\n")
                .append("地产: ").append("空地 :").append(zeroLevelAmount).append("处; ")
                .append("茅屋: ").append(oneLevelAmount).append("处; ")
                .append("洋房: ").append(twoLevelAmount).append("处; ")
                .append("摩天楼: ").append(threeLevelAmount).append("处\n")
                .append("道具: ")
                .append("路障: ").append(tools.getOrDefault(Tool.RoadBlock.ordinal(), 0)).append("个; ")
                .append("炸弹: ").append(tools.getOrDefault(Tool.Bomb.ordinal(), 0)).append("个; ")
                .append("机器娃娃: ").append(tools.getOrDefault(Tool.Robot.ordinal(), 0)).append("个\n ");
        return builder.toString();
    }

    public static Player createWithBalanceAndPoints(int balance, int points) {
        Player player = new Player();
        player.balance = balance;
        player.points = points;
        return player;
    }

    public enum State {WAITING_FOR_RESPONSE, END_TURN, GAME_OVER, WAITING_FOR_COMMAND}
}
