public class AnimationAction extends Action{

    /**
     * Creates a new AnimationAction.
     * @param entity The entity of the AnimationAction
     * @param repeatCount The number of times the Animation repeats
     */
    public AnimationAction(Entity entity, int repeatCount) {
        super(entity, null, null, repeatCount);
    }

    /**
     * Ask the EventScheduler to execute an activity action for this action's Entity.
     * This entails telling the Entity to execute its activity.
     *
     * @param scheduler The scheduler that queues up events.
     */
    @Override
    public void executeAction(EventScheduler scheduler) {
        ((AnimationEntity) this.entity).nextImage();

        if (this.repeatCount != 1) {
            AnimationAction newAction = new AnimationAction(this.entity, Math.max(this.repeatCount - 1, 0));
            scheduler.scheduleEvent(
                    this.entity,
                    newAction,
                    ((AnimationEntity) this.entity).getAnimationPeriod()
            );
        }
    }
}
