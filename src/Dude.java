import processing.core.PImage;

import java.util.List;

public abstract class Dude extends MotionEntity{
    protected int resourceCount;

    /**
     * Creates a new DudeNotFull.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param actionPeriod The time (seconds) taken for each activity.
     * @param images   The image list associated with this entity.
     * @param animationPeriod The time (seconds) taken for each animation.
     */
    public Dude(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
    }


    /**
     * resourceCount getter
     * @returns resourceCount
     */
    public int getResourceCount() {
        return resourceCount;
    }

    /**
     * updates the Dude's position depending on the target
     * @param world      The game world.
     * @param destPos     The location of the entity the Fairy should move toward.
     */
    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
                newPos = this.position;
            }
        }

        return newPos;
    }
}
