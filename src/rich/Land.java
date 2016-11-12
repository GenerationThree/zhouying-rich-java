package rich;

public class Land {
    private Player owner;
    private int currentLevel;
    private int price;

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
}
