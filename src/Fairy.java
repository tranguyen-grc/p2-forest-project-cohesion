import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fairy extends MotionEntity{

    /**
     * Creates a new Fairy.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param actionPeriod The time (seconds) taken for each activity.
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param images   The image list associated with this entity.
     */
    public Fairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    /**
     * Adds an activity to the scheduler.
     * @param world      The game world.
     * @param imageStore Stores all the images.
     * @param scheduler  Stores all the scheduled events.
     */
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.position, new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Sapling sapling = new Sapling(SAPLING_KEY + "_" + fairyTarget.get().id, tgtPos, 0,imageStore.getImageList(SAPLING_KEY));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }
        ActivityAction newAction = new ActivityAction(this, world, imageStore);
        scheduler.scheduleEvent(this, newAction, this.actionPeriod);
    }

    /**
     * updates the Fairy's position depending on the target
     * @param world      The game world.
     * @param target  The entity the Fiary should move toward.
     * @param scheduler  Stores all the scheduled events.
     */
    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    /**
     * updates the Fairy's position depending on the target
     * @param world      The game world.
     * @param destPos     The location of the entity the Fairy should move toward.
     */
    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }
}
