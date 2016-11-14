package rich;

import rich.place.*;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static rich.GameConstant.*;

public class GameMapImp implements GameMap {
    private List<Place> places;

    public GameMapImp() {
        this.places = new ArrayList<>();
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
        int stopPosition = scanBlock(curPosition, next);

        return places.get((curPosition + stopPosition) % places.size());
    }

    private int scanBlock(int curPosition, int next) {
        for (int step = 1; step <= next; ++step) {
            Place cur = places.get((curPosition + step) % places.size());
            if (cur.isToolAttached() || cur instanceof Prison) {
                // Clear tool when hit player
                cur.clearTool();
                return step;
            }
        }
        return next;
    }

    @Override
    public Place findBy(int position) {
        return places.get(position);
    }

    @Override
    public boolean setBlock(int position) {
        Place cur = places.get(position);
        if (cur.attach(Tool.RoadBlock)) return true;
        return false;
    }

    public int length() {
        return places.size();
    }
}
