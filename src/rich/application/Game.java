package rich.application;

import rich.command.Command;
import rich.command.Response;
import rich.place.Land;
import rich.place.Place;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Game {

    static GameMapImp map;
    static List<Player> players;
    static Controller controller;

    public static void main(String[] args) throws IOException {

        controller = new Controller();
        map = new GameMapImp();
        final int START_BALANCE = controller.startBalanceFromIo();
        players = initializePlayers(START_BALANCE);

        while (true) {
            run();
        }
    }

    private static void run() {
        for (Player player : players) {
            if (player.getPauseTimes() > 0) {
                System.out.println(player.getName() + "当前回合等待, 下一个玩家");
                player.setPauseTime(player.getPauseTimes() - 1);
                continue;
            }
            player.setActive();
            if (player.getState() == Player.State.WAITING_FOR_COMMAND) {
                Command command = controller.commandFromIo(player);
                player.execute(command);
                if (player.getState() == Player.State.WAITING_FOR_RESPONSE) {
                    Response response = controller.responseFromIo(player, map);
                    player.respond(response);
                }
                else {
                    Place curPlace = player.getCurrentPlace();
                    if (curPlace instanceof Land) {
                        Land land = (Land) curPlace;
                        System.out.print("来到" + land.getOwner().getName() + "的地产, 缴纳过路费" + land.roadToll() + "元\n");
                    }
                }
            }
            System.out.println(player.query());
        }
    }

    private static List<Player> initializePlayers(int startBalance) {
        System.out.print("请选择2~4位不重复玩家，输入编号即可(1.钱夫人; 2.阿土伯; 3.孙小美; 4.金贝贝), 如输入12: \n> ");
        Scanner scanner = new Scanner(System.in);
        Map<Integer, String> names = new HashMap<Integer, String>() {{
            put(1, "钱夫人");
            put(2, "阿土伯");
            put(3, "孙小美");
            put(4, "金贝贝");
        }};

        List<Integer> numbers;
        List<Player> players = new ArrayList<>();
        while (true) {
            String read = scanner.nextLine();
            try {
                List<Integer> tmp = Arrays.stream(read.split("")).map(Integer::parseInt).collect(Collectors.toList());
                if (tmp.stream().distinct().count() < tmp.size()) {
                    System.out.print("玩家编号重复, 请重新输入:\n> ");
                    continue;
                }

                tmp = tmp.stream().distinct().collect(Collectors.toList());
                if (tmp.stream().anyMatch(i -> i < 1 || i > 4)) {
                    System.out.print("玩家编号不在范围内, 请重新输入:\n> ");
                    continue;
                }

                if (tmp.size() < 2) {
                    System.out.print("玩家数量不足, 请重新输入:\n> ");
                    continue;
                }
                numbers = tmp;
                break;
            } catch (NumberFormatException e) {
                System.out.print("输入不合法, 请输入数字): \n> ");
            }
        }
        players.addAll(numbers.stream().map(i -> new Player(map, names.get(i), startBalance)).collect(Collectors.toList()));
        return players;
    }

}

