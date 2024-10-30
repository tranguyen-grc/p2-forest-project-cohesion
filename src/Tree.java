import processing.core.PImage;

import java.util.List;

public class Tree extends PlantEntity{

    /**
     * Creates a new Tree.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param images   The image list associated with this entity.
     * @param actionPeriod The time (seconds) taken for each activity (checking its health).
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param health   The entity's starting health.
     */
    public Tree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        super(id, position, images, animationPeriod, actionPeriod, health);
    }

    /**
     * Adds an activity to the scheduler.
     * @param world      The game world.
     * @param imageStore Stores all the images.
     * @param scheduler  Stores all the scheduled events.
     */
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!this.transform(world, scheduler, imageStore)) {
            ActivityAction newAction = new ActivityAction(this, world, imageStore);
            scheduler.scheduleEvent(this, newAction, this.actionPeriod);
        }
    }

    /**
     * Transforms a tree into a stump.
     * @param world      The game world.
     * @param imageStore Stores all the images.
     * @param scheduler  Stores all the scheduled events.
     */
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Stump stump = new Stump(STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
}
