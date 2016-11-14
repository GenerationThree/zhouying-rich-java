package rich;

import rich.place.Place;

public interface GameMap {
    Place move(Place currentPlace, int next);

    Place findBy(int position);

    boolean putBlock(int position);
}
