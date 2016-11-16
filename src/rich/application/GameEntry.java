package rich.application;

import rich.command.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class GameEntry {
    public static void main(String[] args) throws IOException {

        GameMapImp map = new GameMapImp();

        final int START_BALANCE = initializeStartBalance();
        List<Player> players = initializePlayers(map, START_BALANCE);

        // Game start
//        Player cur = players.get(0);
//        while (cur.getState() == Player.State.WAITING_FOR_COMMAND) {
//            remindPlayerInput(cur);
//            Command command = commandFromIo(map);
//            System.out.println(command);
//
//            cur.execute(command);
//            Player.State tmp = cur.getState();
//            System.out.println(cur.getState());
//            System.out.println(map.findByPlace(cur.getCurrentPlace()));
//        }

        while(true) {
            for (Player currentPlayer : players) {
                while (currentPlayer.getState() == Player.State.WAITING_FOR_COMMAND) {
                    remindPlayerInput(currentPlayer);
                    Command command = commandFromIo(map);
                    Player.State curState = currentPlayer.execute(command);
                    while (curState == Player.State.WAITING_FOR_RESPONSE) {
                        curState = currentPlayer.respond(RollCommand.YesToBuy);
                        System.out.println(currentPlayer.query());
                    }
                }
            }
        }

//        for (Player p : players) {
//            System.out.print(p.getName());
//            System.out.println(p.getBalance());
//        }
    }

    private static void remindPlayerInput(Player currentPlayer) {
        System.out.print(currentPlayer.getName() + "> ");
    }

    private static Command commandFromIo(GameMapImp map) {
        Scanner scanner = new Scanner(System.in);
        Command command;
        while (true) {
            String input = scanner.nextLine();
            command = parseInputToCommand(input, map);
            if (command == null) {
                System.out.print("输入命令不合法, 请重新输入, 输入help获取帮助信息: \n> ");
                continue;
            }
            return command;
        }
    }

    private static Command parseInputToCommand(String input, GameMapImp map) {
        String[] args = input.split(" ");
        Command ret = null;

        if (args.length > 2) ret = null;

        if (args.length == 1) {
            String command = args[0].toLowerCase();
            if (command.equals("roll")) {
                ret = new RollCommand(map, () -> 1);
            } else if (command.toLowerCase().equals("robot")) {
                ret = new RobotCommand();
            } else if (command.equals("query")) {
                ret = new QueryCommand();
            } else if (command.equals("help")) {
                ret = new HelpCommand();
            } else {
                ret = null;
            }
        }

        if (args.length == 2) {
            String command = args[0].toLowerCase();
            int number;
            try {
                number = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return null;
            }

            if (command.equals("block")) {
                if (number <= GameConstant.BLOCK_LIMIT_STEP && number >= -GameConstant.BLOCK_LIMIT_STEP)
                    ret = new BlockCommand(number);
                else
                    ret = null;
            } else if (command.equals("bomb")) {
                if (number <= GameConstant.BLOCK_LIMIT_STEP && number >= -GameConstant.BLOCK_LIMIT_STEP)
                    ret = new BombCommand(number);
                else
                    ret = null;
            } else if (command.equals("sell")) {
                ret = new SellLandCommand(number);
            } else if (command.equals("selltool")) {
                ret = new SellToolCommand(number);
            } else {
                ret = null;
            }
        }

        return ret;
    }

    private static List<Player> initializePlayers(GameMapImp map, int startBalance) {
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

    private static int initializeStartBalance() {
        System.out.print("请设置初始玩家资金(1000~50000), 默认10000, 按Enter确认:\n> ");
        Scanner scanner = new Scanner(System.in);
        int ret = GameConstant.DEFAULT_STARTING_BALANCE;
        while (true) {
            String read = scanner.nextLine();
            if (read.isEmpty()) break;

            try {
                ret = Integer.parseInt(read);
                if (ret < 1000 || ret > 50000) {
                    System.out.print("输入不合法, 初始金钱范围1000~50000, 请重新输入:\n> ");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.print("输入不合法, 请输入数字(1000~50000)\n> ");
            }
        }
        return ret;
    }

}

