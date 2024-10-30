import processing.core.PImage;

import java.util.List;

public class Sapling extends PlantEntity{
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000;

    private int healthLimit;

    /**
     * Creates a new Sapling.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param images   The image list associated with this entity.
     * @param health   The entity's starting health.
     */
    public Sapling(String id, Point position, int health, List<PImage> images) {
        super(id, position, images, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, health);
        this.healthLimit = SAPLING_HEALTH_LIMIT;
    }

    /**
     * Adds an activity to the scheduler.
     * @param world      The game world.
     * @param imageStore Stores all the images.
     * @param scheduler  Stores all the scheduled events.
     */
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.health++;
        if (!this.transform(world, scheduler, imageStore)) {
            ActivityAction newAction = new ActivityAction(this, world, imageStore);
            scheduler.scheduleEvent(this, newAction, this.actionPeriod);
        }
    }

    /**
     * Transforms a sapling into a stump or tree based on its health.
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
        } else if (this.health >= this.healthLimit) {
            Tree tree = new Tree(TREE_KEY + "_" + this.id, this.position, Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN), Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN), Functions.getIntFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN), imageStore.getImageList(TREE_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

}
