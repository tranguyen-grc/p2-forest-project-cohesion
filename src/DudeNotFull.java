import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude{
    private int resourceLimit;

    /**
     * Creates a new DudeNotFull.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param actionPeriod The time (seconds) taken for each activity.
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param resourceLimit The resourceLimit for this entity.
     * @param images   The image list associated with this entity.
     */
    public DudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, images,0);
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
        Optional<Entity> target = world.findNearest(this.position, new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (target.isEmpty() || !this.moveTo(world, target.get(), scheduler) || !this.transform(world, scheduler, imageStore)) {
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
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            DudeFull dude = new DudeFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceCount, this.resourceLimit, this.images);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    /**
     * updates the DudeNotFull's position depending on the target
     * @param world      The game world.
     * @param target  The entity the DudeNotFull should move toward.
     * @param scheduler  Stores all the scheduled events.
     */
    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            this.resourceCount += 1;
            target = (PlantEntity) target;
            ((PlantEntity) target).setHealth(((PlantEntity) target).getHealth() - 1);
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
