import processing.core.PImage;

import java.util.List;

public class Obstacle extends AnimationEntity{
    /**
     * Creates a new Obstacle.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param images   The image list associated with this entity.
     */
    public Obstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        super(id, position, images, animationPeriod);
    }
}
