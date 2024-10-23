import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    public static final int KEYED_IMAGE_MIN = 5;
    public static final int PROPERTY_KEY = 0;
    public static final int PROPERTY_ID = 1;
    public static final int PROPERTY_COL = 2;
    public static final int PROPERTY_ROW = 3;
    public static final int ENTITY_NUM_PROPERTIES = 4;
    public static final int COLOR_MASK = 0xffffff;
    public static final int KEYED_RED_IDX = 2;
    public static final int KEYED_GREEN_IDX = 3;
    public static final int KEYED_BLUE_IDX = 4;
    private int numRows;
    private int numCols;
    private Background[][] background;
    private Entity[][] occupancy;
    private Set<Entity> entities;

    public WorldModel() {

    }

    public int getRows() {
        return this.numRows;
    }

    public int getCols() {
        return this.numCols;
    }

    public Set<Entity> getEntities() {
        return this.entities;
    }

    private static void processImageLine(Map<String, List<PImage>> images, String line, PApplet screen) {
        String[] attrs = line.split("\\s");
        if (attrs.length >= 2) {
            String key = attrs[0];
            PImage img = screen.loadImage(attrs[1]);
            if (img != null && img.width != -1) {
                List<PImage> imgs = getImages(images, key);
                imgs.add(img);

                if (attrs.length >= KEYED_IMAGE_MIN) {
                    int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
                    int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
                    int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
                    setAlpha(img, screen.color(r, g, b), 0);
                }
            }
        }
    }

    public static List<PImage> getImages(Map<String, List<PImage>> images, String key) {
        return images.computeIfAbsent(key, k -> new LinkedList<>());
    }

    /* Called with color for which alpha should be set and alpha value.
        setAlpha(img, color(255, 255, 255), 0));
    */
    private static void setAlpha(PImage img, int maskColor, int alpha) {
        int alphaValue = alpha << 24;
        int nonAlpha = maskColor & COLOR_MASK;
        img.format = PApplet.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
                img.pixels[i] = alphaValue | nonAlpha;
            }
        }
        img.updatePixels();
    }

    public static void loadImages(Scanner in, ImageStore imageStore, PApplet screen) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                processImageLine(imageStore.getImages(), in.nextLine(), screen);
            } catch (NumberFormatException e) {
                System.out.printf("Image format error on line %d\n", lineNumber);
            }
            lineNumber++;
        }
    }

    private void parseSapling(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Entity.SAPLING_NUM_PROPERTIES) {
            int health = Integer.parseInt(properties[Entity.SAPLING_HEALTH_IDX]);
            Entity entity = Entity.createSapling(id, pt, imageStore.getImageList(Entity.SAPLING_KEY), health);
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Entity.SAPLING_KEY, Entity.SAPLING_NUM_PROPERTIES));
        }
    }

    private void parseDude(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Entity.DUDE_NUM_PROPERTIES) {
            Entity entity = Entity.createDudeNotFull(id, pt, Double.parseDouble(properties[Entity.DUDE_ACTION_PERIOD_IDX]), Double.parseDouble(properties[Entity.DUDE_ANIMATION_PERIOD_IDX]), Integer.parseInt(properties[Entity.DUDE_RESOURCE_LIMIT_IDX]), imageStore.getImageList(Entity.DUDE_KEY));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Entity.DUDE_KEY, Entity.DUDE_NUM_PROPERTIES));
        }
    }

    private void parseFairy(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Entity.FAIRY_NUM_PROPERTIES) {
            Entity entity = Entity.createFairy(id, pt, Double.parseDouble(properties[Entity.FAIRY_ACTION_PERIOD_IDX]), Double.parseDouble(properties[Entity.FAIRY_ANIMATION_PERIOD_IDX]), imageStore.getImageList(Entity.FAIRY_KEY));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Entity.FAIRY_KEY, Entity.FAIRY_NUM_PROPERTIES));
        }
    }

    private void parseTree(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Entity.TREE_NUM_PROPERTIES) {
            Entity entity = Entity.createTree(id, pt, Double.parseDouble(properties[Entity.TREE_ACTION_PERIOD_IDX]), Double.parseDouble(properties[Entity.TREE_ANIMATION_PERIOD_IDX]), Integer.parseInt(properties[Entity.TREE_HEALTH_IDX]), imageStore.getImageList(Entity.TREE_KEY));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Entity.TREE_KEY, Entity.TREE_NUM_PROPERTIES));
        }
    }

    private void parseObstacle(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Entity.OBSTACLE_NUM_PROPERTIES) {
            Entity entity = Entity.createObstacle(id, pt, Double.parseDouble(properties[Entity.OBSTACLE_ANIMATION_PERIOD_IDX]), imageStore.getImageList(Entity.OBSTACLE_KEY));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Entity.OBSTACLE_KEY, Entity.OBSTACLE_NUM_PROPERTIES));
        }
    }

    private void parseHouse(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Entity.HOUSE_NUM_PROPERTIES) {
            Entity entity = Entity.createHouse(id, pt, imageStore.getImageList(Entity.HOUSE_KEY));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Entity.HOUSE_KEY, Entity.HOUSE_NUM_PROPERTIES));
        }
    }

    private void parseStump(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Entity.STUMP_NUM_PROPERTIES) {
            Entity entity = Entity.createStump(id, pt, imageStore.getImageList(Entity.STUMP_KEY));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Entity.STUMP_KEY, Entity.STUMP_NUM_PROPERTIES));
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos) {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }

    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.y][pos.x] = background;
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }

    /**
     * This method reads a line and creates a new Entity depending on the line's instructions,
     * then adds the new Entity to the World.
     *
     * @param line The line. An example line is: tree 35 37 0.256 1.168 2
     * @param imageStore Images to use for the Entity that will be created.
     */
    public void parseEntity(String line, ImageStore imageStore) {
        String[] properties = line.split(" ", ENTITY_NUM_PROPERTIES + 1);
        if (properties.length >= ENTITY_NUM_PROPERTIES) {
            String key = properties[PROPERTY_KEY];
            String id = properties[PROPERTY_ID];
            Point pt = new Point(Integer.parseInt(properties[PROPERTY_COL]), Integer.parseInt(properties[PROPERTY_ROW]));

            properties = properties.length == ENTITY_NUM_PROPERTIES ?
                    new String[0] : properties[ENTITY_NUM_PROPERTIES].split(" ");

            switch (key) {
                case Entity.OBSTACLE_KEY -> this.parseObstacle(properties, pt, id, imageStore);
                case Entity.DUDE_KEY -> this.parseDude(properties, pt, id, imageStore);
                case Entity.FAIRY_KEY -> this.parseFairy(properties, pt, id, imageStore);
                case Entity.HOUSE_KEY -> this.parseHouse(properties, pt, id, imageStore);
                case Entity.TREE_KEY -> this.parseTree(properties, pt, id, imageStore);
                case Entity.SAPLING_KEY -> this.parseSapling(properties, pt, id, imageStore);
                case Entity.STUMP_KEY -> this.parseStump(properties, pt, id, imageStore);
                default -> throw new IllegalArgumentException("Entity key is unknown");
            }
        }else{
            throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
        }
    }

    /**
     * Like {@code parseEntity}, parses a line to determine what kind of Background to add to the world.
     * @param line A line of text (e.g., "grass grass grass flowers glowers dirt_vert_left") that
     *             determines what background images are drawn on a per-cell basis.
     * @param row The index of the row for which Backgrounds are being selected.
     * @param imageStore Images to use for the Backgrounds.
     */
    public void parseBackgroundRow(String line, int row, ImageStore imageStore) {
        String[] cells = line.split(" ");
        if(row < this.numRows){
            int rows = Math.min(cells.length, this.numCols);
            for (int col = 0; col < rows; col++){
                this.background[row][col] = new Background(cells[col], imageStore.getImageList(cells[col]));
            }
        }
    }

    /**
     * Parses a save file that describes the start state of the World. It describes the entities,
     * their starting positions and properties, and backgrounds for each location in the World.
     *
     * @param saveFile A Scanner that is set up to read from a specified save file.
     * @param imageStore Images to use for Entities and Backgrounds.
     */
    public void parseSaveFile(Scanner saveFile, ImageStore imageStore){
        String lastHeader = "";
        int headerLine = 0;
        int lineCounter = 0;
        while(saveFile.hasNextLine()){
            lineCounter++;
            String line = saveFile.nextLine().strip();
            if(line.endsWith(":")){
                headerLine = lineCounter;
                lastHeader = line;
                switch (line){
                    case "Backgrounds:" -> this.background = new Background[this.numRows][this.numCols];
                    case "Entities:" -> {
                        this.occupancy = new Entity[this.numRows][this.numCols];
                        this.entities = new HashSet<>();
                    }
                }
            }else{
                switch (lastHeader){
                    case "Rows:" -> this.numRows = Integer.parseInt(line);
                    case "Cols:" -> this.numCols = Integer.parseInt(line);
                    case "Backgrounds:" -> this.parseBackgroundRow(line, lineCounter-headerLine-1, imageStore);
                    case "Entities:" -> this.parseEntity(line, imageStore);
                }
            }
        }
    }

    public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        this.parseSaveFile(saveFile, imageStore);
        if(this.background == null){
            this.background = new Background[this.numRows][this.numCols];
            for (Background[] row : this.background)
                Arrays.fill(row, defaultBackground);
        }
        if(this.occupancy == null){
            this.occupancy = new Entity[this.numRows][this.numCols];
            this.entities = new HashSet<>();
        }
    }

    public void setOccupancyCell(Point pos, Entity entity) {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    public void removeEntity(EventScheduler scheduler, Entity entity) {
        scheduler.unscheduleAllEvents(entity);
        this.removeEntityAt(entity.getPosition());
    }

    public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            Optional<Entity> occupant = this.getOccupant(pos);
            occupant.ifPresent(target -> this.removeEntity(scheduler, target));
            this.setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    /*
           Assumes that there is no entity currently occupying the
           intended destination cell.
        */
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            this.setOccupancyCell(entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public Optional<Entity> findNearest(Point pos, List<EntityKind> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (EntityKind kind : kinds) {
            for (Entity entity : this.entities) {
                if (entity.getKind() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return Entity.nearestEntity(ofType, pos);
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && this.getOccupancyCell(pos) != null;
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0 && pos.x < this.numCols;
    }

    public void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : entities) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }
}
