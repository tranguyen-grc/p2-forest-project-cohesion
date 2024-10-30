import processing.core.PImage;

import java.util.List;

public class Stump extends Entity {

    /**
     * Creates a new Stump.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param images   The image list associated with this entity.
     */
    public Stump(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
