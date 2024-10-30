public class ActivityAction extends Action{

    /**
     * Creates a new ActivityAction.
     * @param entity The entity of the AnimationAction
     * @param world The game world.
     * @param imageStore Stores all the images.
     */
    public ActivityAction(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity, world, imageStore, 0);
    }

    /**
     * Ask the EventScheduler to execute an activity action for this action's Entity.
     * This entails telling the Entity to execute its activity.
     *
     * @param scheduler The scheduler that queues up events.
     */
    @Override
    public void executeAction(EventScheduler scheduler) {
        ((ActivityEntity) this.entity).executeActivity(this.world, this.imageStore, scheduler);
    }
}
