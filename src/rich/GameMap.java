package rich;

import rich.place.Place;

public interface GameMap {
    Place move(Place currentPlace, int next);

    Place findByPosition(int position);

    int findByPlace(Place place);

    boolean putBlock(int position);
}
