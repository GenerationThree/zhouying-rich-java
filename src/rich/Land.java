package rich;

public class Land {
    private static final int TOP_LEVEL = 3;
    private Player owner;
    private int currentLevel;
    private int price;
    private int roadToll;

    public Land() {
        currentLevel = 0;
        owner = null;
    }

    public Player getOwner() {
        return owner;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getPrice() {
        return price;
    }

    public void boughtBy(Player player) {
        owner = player;
    }

    public static Land createLandWithPrice(int price) {
        Land land = new Land();
        land.price = price;
        return land;
    }

    public int roadToll() {
        int ret = 0;
        if (owner != null) {
            switch (currentLevel) {
                case 0:
                    ret = price / 2;
                    break;
                case 1:
                    ret = price;
                    break;
                case 2:
                    ret = price * 2;
                    break;
                case 3:
                    ret = price * 4;
                    break;
            }
        }
        return ret;
    }

    public void upgrade() {
        currentLevel += 1;
    }

    public boolean canUpgrade() {
        return currentLevel < TOP_LEVEL;
    }

    // For test, clean it!!!
    public void setPrice(int price) {
        this.price = price;
    }

    public void setLevel(int level) {
        this.currentLevel = level;
    }
}
