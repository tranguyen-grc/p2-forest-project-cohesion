import processing.core.PImage;

import java.util.List;


public abstract class PlantEntity extends ActivityEntity implements HealthEntity{
    protected int health;

    /**
     * Creates a new PlantEntity.
     * @param id       The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param images   The image list associated with this entity.
     * @param actionPeriod The time (seconds) taken for each activity (checking its health).
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param health   The entity's starting health.
     */
    public PlantEntity(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int health) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
    }


    /**
     * getter for health
     * @return health
     */
    @Override
    public int getHealth() {
        return health;
    }

    /**
     * setter for health
     * @param health
     */
    @Override
    public void setHealth(int health) {
        this.health = health;
    }
}
