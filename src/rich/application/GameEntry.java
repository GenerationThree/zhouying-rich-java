package rich.application;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class GameEntry {
    public static void main(String[] args) throws IOException {
        final int START_BALANCE = initializeStartBalance();
        List<Player> players = initializePlayers(START_BALANCE);
        for (Player p : players) {
            System.out.print(p.getName());
            System.out.println(p.getBalance());
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

                if (tmp.size() < 2)
                {
                    System.out.print("玩家数量不足, 请重新输入:\n> ");
                    continue;
                }
                numbers = tmp;
                break;
            } catch (NumberFormatException e) {
                System.out.print("输入不合法, 请输入数字): \n> ");
            }
        }
        players.addAll(numbers.stream().map(i -> new Player(names.get(i), startBalance)).collect(Collectors.toList()));
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

