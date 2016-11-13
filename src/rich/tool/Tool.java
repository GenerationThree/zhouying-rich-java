package rich.tool;

public enum Tool {
    RoadBlock {
        @Override
        public int getPoints() {
            return 50;
        }
    }, Bomb {
        @Override
        public int getPoints() {
            return 50;
        }
    }, Robot {
        @Override
        public int getPoints() {
            return 30;
        }
    };

    abstract public int getPoints();
}
