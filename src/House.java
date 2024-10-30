import processing.core.PImage;

import java.util.List;

public class House extends Entity {
    /**
     * Creates a new House.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param images   The image list associated with this entity.
     */
    public House(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
