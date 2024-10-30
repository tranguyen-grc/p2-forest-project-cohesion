import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude {
    private int resourceLimit;

    /**
     * Creates a new DudeFull.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param actionPeriod The time (seconds) taken for each activity.
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param resourceLimit The resourceLimit for this entity.
     * @param images   The image list associated with this entity.
     */
    public DudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceCount, int resourceLimit, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, images, resourceCount);
        this.resourceLimit = resourceLimit;
    }

    /**
     * Adds an activity to the scheduler.
     * @param world      The game world.
     * @param imageStore Stores all the images
     * @param scheduler  Stores all the scheduled events.
     */
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler)) {
            this.transform(world, scheduler, imageStore);
        } else {
            ActivityAction newAction = new ActivityAction(this, world, imageStore);
            scheduler.scheduleEvent(this, newAction, this.actionPeriod);
        }
    }

    /**
     * Transforms a DudeNotFull into a DudeFull based on its resourceCount.
     * @param world      The game world.
     * @param imageStore Stores all the images.
     * @param scheduler  Stores all the scheduled events.
     */
    public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        DudeNotFull dude = new DudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
    }

    /**
     * updates the DudeFull's position depending on the target
     * @param world      The game world.
     * @param target  The entity the DudeFull should move toward.
     * @param scheduler  Stores all the scheduled events.
     */
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
}
