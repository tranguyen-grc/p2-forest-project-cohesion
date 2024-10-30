import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends Entity {
    protected double animationPeriod;

    /**
     * Creates a new DudeNotFull.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param images   The image list associated with this entity.
     * @param animationPeriod The time (seconds) taken for each animation.
     */
    public AnimationEntity(String id, Point position, List<PImage> images, double animationPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    /**
     * animationPeriod getter
     * @returns animationPeriod
     */
    public double getAnimationPeriod() {
        return animationPeriod;
    }

    /**
     * adds the entity's action to the scheduler
     * @param scheduler Stores all the scheduled events.
     * @param world The game world.
     * @param imageStore Stores all the images.
     */
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        AnimationAction newAnimation = new AnimationAction(this, 0);
        scheduler.scheduleEvent(this, newAnimation, getAnimationPeriod());
    }

    /**
     * increments imageIndex
     * @returns imageIndex
     */
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }
}
