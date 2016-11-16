package rich.application;

import rich.command.*;
import rich.place.Land;
import rich.place.Place;

import java.util.Scanner;

import static rich.application.Game.map;

public class Controller {

    public int startBalanceFromIo() {
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

    public Command commandFromIo(Player player) {
        System.out.print(player.getName() + "> ");
        Scanner scanner = new Scanner(System.in);
        Command command;
        while (true) {
            String input = scanner.nextLine();
            command = parseInputToCommand(input);
            if (command == null) {
                System.out.print("输入命令不合法, 请重新输入, 输入help获取帮助信息: \n" + player.getName() + "> ");
                continue;
            }
            return command;
        }
    }

    public Response responseFromIo(Player player, GameMapImp map) {
        Place currentPlace = player.getCurrentPlace();
        Scanner scanner = new Scanner(System.in);
        String responseString;

        if (currentPlace instanceof Land) {
            Land land = (Land) currentPlace;
            if (land.getOwner() == null) {
                System.out.print("是否购买该处空地, " + land.getPrice() + "元 (Y/N)?\n" + player.getName() + "> ");
                while (true) {
                    responseString = scanner.nextLine().toLowerCase();
                    if (responseString.equals("y"))
                        return RollCommand.YesToBuy;
                    else if (responseString.equals("n"))
                        return RollCommand.NoToBuy;
                }
            } else if (land.getOwner() == player) {
                System.out.print("是否升级该处地产, " + land.getPrice() + "元 (Y/N)?\n" + player.getName() + "> ");
                while (true) {
                    responseString = scanner.nextLine().toLowerCase();
                    if (responseString.equals("y"))
                        return RollCommand.YesToUpgrade;
                    else if (responseString.equals("n"))
                        return RollCommand.NoToUpgrade;
                }
            }

        }
        return null;
    }

    private Command parseInputToCommand(String input) {
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

}
