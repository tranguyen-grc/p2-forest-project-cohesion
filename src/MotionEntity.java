import processing.core.PImage;

import java.util.List;

public abstract class MotionEntity extends ActivityEntity{

    /**
     * Creates a new MotionEntity.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param images   The image list associated with this entity.
     * @param actionPeriod The time (seconds) taken for each activity.
     * @param animationPeriod The time (seconds) taken for each animation.
     */
    public MotionEntity(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    //implemented in the Fairy, DudeFull, and DudeNotFull classes
    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    //implemented in the Fairy and Dude classes
    public abstract Point nextPosition(WorldModel world, Point destPos);


}
