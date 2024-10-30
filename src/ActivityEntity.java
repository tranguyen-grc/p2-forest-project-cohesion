import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends AnimationEntity{
    protected double actionPeriod;

    /**
     * Creates a new ActivityEntity.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param images   The image list associated with this entity.
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param actionPeriod The time (seconds) taken for each activity.
     */
    public ActivityEntity(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    //implemented in the Tree, Sapling, Fairy, DudeNotFull, and DudeFull classes
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    /**
     * adds the entity's action to the scheduler
     * @param scheduler Stores all the scheduled events.
     * @param world The game world.
     * @param imageStore Stores all the images.
     */
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        ActivityAction newAction = new ActivityAction(this, world, imageStore);
        AnimationAction newAnimation = new AnimationAction(this, 0);
        scheduler.scheduleEvent(this, newAction, this.actionPeriod);
        scheduler.scheduleEvent(this, newAnimation, getAnimationPeriod());
    }

    /**
     * actionPeriod getter
     * @returns actionPeriod
     */
    public double getActionPeriod() {
        return actionPeriod;
    }
}
