package rich.application;

import rich.command.Command;
import rich.command.Response;
import rich.place.Land;
import rich.place.Place;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private GameMapImp map;
    private String name;
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

    public Player(GameMapImp map, String name, int balance) {
        this.map = map;
        this.name = name;
        this.state = State.WAITING_FOR_COMMAND;
        this.lastExecuted = null;
        this.lands = new ArrayList<>();
        this.tools = new HashMap<>();
        this.balance = balance;
        this.currentPlace = map.starting();
        this.points = 0;
        this.toolsAmount = 0;
        this.noPunishTimes = 0;
        this.pauseTimes = 0;
    }

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
        target.setPlayerOn(this);
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

    public void setPauseTime(int pauseTimes) {
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
        Place place = map.findByPosition(position);
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

    public static Player createPlayerWithBalanceAndAMap(GameMapImp map, int startBalance) {
        Player player = new Player();
        player.map = map;
        player.currentPlace = map.starting();
        player.balance = startBalance;
        return player;
    }

    public String query() {
        int zeroLevelAmount = 0;
        int oneLevelAmount = 0;
        int twoLevelAmount = 0;
        int threeLevelAmount = 0;
        for (Land land : lands) {
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
        String ret = "资金: $" + balance + "\n" +
                "点数: " + points + "\n" +
                "地产: " + "空地: " + zeroLevelAmount + "处; " +
                "茅屋: " + oneLevelAmount + "处; " +
                "洋房: " + twoLevelAmount + "处; " +
                "摩天楼: " + threeLevelAmount + "处\n" +
                "道具: " +
                "路障: " + tools.getOrDefault(Tool.RoadBlock.ordinal(), 0) + "个; " +
                "炸弹: " + tools.getOrDefault(Tool.Bomb.ordinal(), 0) + "个; " +
                "机器娃娃: " + tools.getOrDefault(Tool.Robot.ordinal(), 0) + "个\n ";
        return ret;
    }

    public static Player createWithBalanceAndPoints(int balance, int points) {
        Player player = new Player();
        player.balance = balance;
        player.points = points;
        return player;
    }

    public boolean putBomb(int steps) {
        if (tools.getOrDefault(Tool.Bomb.ordinal(), 0) == 0)
            return false;

        int curPosition = map.findByPlace(currentPlace);
        int targetPosition = curPosition + steps;
        if (targetPosition < 0) targetPosition += map.length();
        Place targetPlace = map.findByPosition(targetPosition);

        if (targetPlace.tryToAttachTool(Tool.Bomb)) {
            tools.put(Tool.Bomb.ordinal(), tools.get(Tool.Bomb.ordinal()) - 1);
            return true;
        }
        return false;
    }

    public boolean putBlock(int steps) {
        if (tools.getOrDefault(Tool.RoadBlock.ordinal(), 0) == 0)
            return false;

        int curPosition = map.findByPlace(currentPlace);
        int targetPosition = curPosition + steps;
        if (targetPosition < 0) targetPosition += map.length();
        Place targetPlace = map.findByPosition(targetPosition);

        if (targetPlace.tryToAttachTool(Tool.RoadBlock)) {
            tools.put(Tool.RoadBlock.ordinal(), tools.get(Tool.RoadBlock.ordinal()) - 1);
            return true;
        }
        return false;
    }

    public void sendToHospital() {
        this.currentPlace = map.hospital();
    }

    public void gainPoints_(int points) {
        this.points += points;
    }

    public boolean cleanRoad(int step) {
        if (tools.getOrDefault(Tool.Robot.ordinal(), 0) == 0) {
            return false;
        }
        map.cleanRoad(this.currentPlace, step);
        return true;
    }

    public String help() {
        String ret = "roll: 掷骰子命令，行走1~6步\n" +
                "block n: 拥有路障后，可将路障放置到离当前位置前后n步的距离，任一玩家经过路障，都将被拦截。该道具一次有效。n表示前后的相对距离，负数表示后方\n" +
                "bomb n: 拥有炸弹后，可将炸弹放置到离当前位置前后n步的距离，任一玩家经过炸弹会被炸弹炸伤，送往医院。该道具一次有效。n表示前后的相对距离，负数表示后方\n" +
                "robot: 使用该道具，可清扫前方路面上10步以内的其它道具，如炸弹、路障\n" +
                "sell x: 出售自己的房产，x表示地图上的绝对位置，即地产的编号\n" +
                "sellTool x: 出售道具，x表示道具编号\n" +
                "query: 显示自家资产信息\n" +
                "help: 查看命令帮助\n" +
                "quit: 强制退出\n";
        return ret;
    }

    public String getName() {
        return name;
    }

    public enum State {WAITING_FOR_RESPONSE, END_TURN, GAME_OVER, WAITING_FOR_COMMAND}
}
