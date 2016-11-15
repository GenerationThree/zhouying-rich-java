package rich;

import rich.place.*;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static rich.GameConstant.*;

public class GameMapImp implements GameMap{
    private List<Place> places;
    private int hospitalLocation;

    public GameMapImp() {
        this.places = new ArrayList<>();
        this.hospitalLocation = 0;
        addStarting();
        addLandOfDistrict(FIRST_DISTRICT_PRICE, FIRST_DISTRICT_LANDS);
        addHospital();
        addLandOfDistrict(SECOND_DISTRICT_PRICE, SECOND_DISTRICT_LANDS);
        addToolsRoom();
        addLandOfDistrict(THIRD_DISTRICT_PRICE, THIRD_DISTRICT_LANDS);
        addGiftsRoom();
        addLandOfDistrict(FOURTH_DISTRICT_PRICE, FOURTH_DISTRICT_LANDS);
        addPrison();
        addLandOfDistrict(FIFTH_DISTRICT_PRICE, FIFTH_DISTRICT_LANDS);
        addMagicRoom();
        addMineralLand();
    }

    public Place starting() {
        return places.get(0);
    }

    private void addMagicRoom() {
        places.add(new MagicRoom());
    }

    private void addMineralLand() {
        List<Integer> points = Arrays.asList(20, 80, 100, 40, 80, 60);
        places.addAll(points.stream().map(MineralLand::new).collect(Collectors.toList()));
    }

    private void addPrison() {
        places.add(new Prison());
    }

    private void addGiftsRoom() {
        places.add(new GiftsRoom());
    }

    private void addToolsRoom() {
        places.add(new ToolsRoom());
    }

    private void addHospital() {
        places.add(new Hospital());
        hospitalLocation = places.size() - 1;
    }

    private void addLandOfDistrict(int districtPrice, int landsAmount) {
        for (int i = 0; i < landsAmount; ++i) {
            places.add(new Land(districtPrice));
        }
    }

    private void addStarting() {
        places.add(new Starting());
    }

    @Override
    public Place move(Place currentPlace, int next) {
        int curPosition = places.indexOf(currentPlace);

        int blockDistance = scanBlock(curPosition, next);
        int bombDistance = scanBomb(curPosition, next);

        Place ret;
        if (blockDistance > next && bombDistance > next) {
            ret = places.get((curPosition + next) % places.size());
        } else if (blockDistance < bombDistance) {
            ret = places.get((curPosition + blockDistance) % places.size());
            ret.clearTool();
        } else {
            ret = hospital();
            ret.clearTool();
        }
        return ret;
    }

    private int scanBlock(int curPosition, int next) {
        for (int step = 1; step <= next; ++step) {
            Place cur = places.get((curPosition + step) % places.size());
            if (cur.isToolAttached() || cur instanceof Prison) {
                return step;
            }
        }
        return next + 1;  // means no block
    }

    private int scanBomb(int curPosition, int next) {
        for (int step = 1; step <= next; ++step) {
            Place cur = places.get((curPosition + step) % places.size());
            if (cur.attachedToolType() == Tool.Bomb) {
                return step;
            }
        }
        return next + 1; // means no bomb
    }

    public Place findByPosition(int position) {
        return places.get(position);
    }

    public int findByPlace(Place place) {
        for (int i = 0; i < places.size(); ++i) {
            if (place == places.get(i)) return i;
        }
        return -1;
    }

    public boolean putBlock(int position) {
        Place cur = places.get(position);
        if (cur.tryToAttachTool(Tool.RoadBlock)) return true;
        return false;
    }

    public int length() {
        return places.size();
    }

    public Place hospital() {
        return places.get(hospitalLocation);
    }

    public boolean putBomb(int position) {
        Place cur = places.get(position);
        if (cur.tryToAttachTool(Tool.Bomb)) return true;
        return false;
    }
}
