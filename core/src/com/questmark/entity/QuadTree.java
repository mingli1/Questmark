package com.questmark.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Represents a tree data structure with 4 children from each node. Can be represented
 * as a grid on a 2D plane. Nodes store {@link Rectangle} objects that represent collision
 * boxes of all entities on a plane. Offers O(n log n) collision detection.
 *
 * @author Ming Li
 */
public class QuadTree {

    private static final int MAX_BOXES_PER_LEVEL = 8;
    private static final int MAX_LEVELS = 4;

    // Region quadrants
    private static final int QUADRANT_II = 0;
    private static final int QUADRANT_I = 1;
    private static final int QUADRANT_III = 2;
    private static final int QUADRANT_IV = 3;

    private static final int[] QUADRANTS =
            new int[] { QUADRANT_I, QUADRANT_II, QUADRANT_III, QUADRANT_IV };

    // the current level of the tree
    private int currLevel;

    // the bounding boxes on this region
    private Array<Rectangle> boxes;
    private Rectangle regionBounds;
    // the 4 children of this node
    private QuadTree[] subtrees;

    public QuadTree(int currLevel, Rectangle regionBounds) {
        this.currLevel = currLevel;
        this.regionBounds = regionBounds;
        boxes = new Array<Rectangle>();
        subtrees = new QuadTree[4];
    }

    /**
     * Clears the bounding boxes on this quadtree and recursively on its children
     */
    public void clear() {
        boxes.clear();

        for (int i = 0; i < subtrees.length; i++) {
            if (subtrees[i] != null) {
                subtrees[i].clear();
                subtrees[i] = null;
            }
        }
    }

    /**
     * Splits this quadtree into 4 subtrees
     */
    private void split() {
        float x = regionBounds.x;
        float y = regionBounds.y;
        float newWidth = regionBounds.width / 2;
        float newHeight = regionBounds.height / 2;
        int nextLevel = currLevel + 1;

        subtrees[QUADRANT_I] = new QuadTree(nextLevel,
                new Rectangle(x + newWidth, y + newHeight, newWidth, newHeight));
        subtrees[QUADRANT_II] = new QuadTree(nextLevel,
                new Rectangle(x, y + newHeight, newWidth, newHeight));
        subtrees[QUADRANT_III] = new QuadTree(nextLevel,
                new Rectangle(x, y, newWidth, newHeight));
        subtrees[QUADRANT_IV] = new QuadTree(nextLevel,
                new Rectangle(x + newWidth, y, newWidth, newHeight));
    }

    /**
     * Returns which quadrant number in this quadtree a given bounding box is in
     *
     * @param bounds
     * @return
     */
    private int getQuandrant(Rectangle bounds) {
        int ret = -1;

        // if tree hasn't been subdivided yet
        if (subtrees[0] == null) split();

        for (int i = 0; i < 4; i++) {
            if (subtrees[QUADRANTS[i]].getRegionBounds().contains(bounds)) {
                return QUADRANTS[i];
            }
        }

        return ret;
    }

    /**
     * Inserts a given bounding box into the quadtree. If the number of boxes per level
     * exceeds the capacity then the tree will split and boxes will be placed into subtrees.
     *
     * @param bounds
     */
    public void insert(Rectangle bounds) {
        if (subtrees[0] != null) {
            int quadrant = getQuandrant(bounds);
            if (quadrant != -1) {
                subtrees[quadrant].insert(bounds);
                return;
            }
        }

        boxes.add(bounds);

        if (boxes.size > MAX_BOXES_PER_LEVEL && currLevel < MAX_LEVELS) {
            if (subtrees[0] == null) split();

            for (int i = 0; i < boxes.size; i++) {
                int quadrant = getQuandrant(boxes.get(i));
                if (quadrant != -1) {
                    subtrees[quadrant].insert(boxes.removeIndex(i));
                }
            }
        }
    }

    /**
     * Returns a list of bounding boxes that is within the region of a given bounding box.
     *
     * @param subboxes
     * @param bounds
     * @return
     */
    public Array<Rectangle> retrieve(Array<Rectangle> subboxes, Rectangle bounds) {
        int quadrant = getQuandrant(bounds);
        if (quadrant != -1 && subtrees[0] != null) {
            subtrees[quadrant].retrieve(subboxes, bounds);
        }
        subboxes.addAll(boxes);
        return subboxes;
    }

    /**
     * Returns the bounds of the current quadtree
     *
     * @return
     */
    public Rectangle getRegionBounds() {
        return regionBounds;
    }

}
