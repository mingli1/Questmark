package com.questmark.pathfinding;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BinaryHeap;

/**
 * Utility class that represents the A* search algorithm for pathfinding.
 * Each node on the graph is represented by a {@link Node}.
 * Uses a {@link BinaryHeap} for the open list.
 *
 * @author Ming Li
 */
public final class AStar {

    private Array<Rectangle> boundingBoxes;
    private BinaryHeap<Node> openList;

    public AStar() {
        openList = new BinaryHeap<Node>();
        boundingBoxes = null;
    }

    /**
     * Updates the list of collidables for the algorithm to account for.
     *
     * @param boundingBoxes
     */
    public void setBoundingBoxes(Array<Rectangle> boundingBoxes) {
        this.boundingBoxes = boundingBoxes;
    }

    /**
     * Returns a list of {@link Node} representing the A* path from a given start position to
     * a given target position, considering a list of bounding boxes to avoid.
     *
     * @param start the start position
     * @param target the target position
     * @return an A* path
     */
    public Array<Node> findPath(Vector2 start, Vector2 target) {
        return null;
    }

    /**
     * Returns a list of {@link Node} representing the A* path from a given start position to a
     * given target position, considering a list of bounding boxes to avoid. This function allows a
     * choice for the heuristic function where (0 - euclidean, 1 - manhattan, 2 - diagonal)
     *
     * @param start the start position
     * @param target the target position
     * @param heuristic an int representing a heuristic function as documented above
     * @return an A* path
     */
    public Array<Node> findPath(Vector2 start, Vector2 target, int heuristic) {
        return null;
    }

    /** HEURISTIC FUNCTIONS **/

    /**
     * A heuristic function that returns the Manhattan distance between two given positions.
     * Used for restricting A* search to four cardinal directions.
     *
     * @param start the start position
     * @param target the target position
     * @return the Manhattan distance
     */
    private float manhattan(Vector2 start, Vector2 target) {
        return Math.abs(start.x - target.x) + Math.abs(start.y - target.y);
    }

    /**
     * A heuristic function that returns the diagonal distance between two given positions.
     * Used for restricting A* search to eight directions: N, E, S, W, NE, NW, SE, SW
     *
     * @param start the start position
     * @param target the target position
     * @return the diagonal distance
     */
    private float diagonal(Vector2 start, Vector2 target) {
        return Math.max(Math.abs(start.x - target.x), Math.abs(start.y - target.y));
    }

    /**
     * A heuristic function that returns the Euclidean distance between two given positions.
     * Allows for unrestricted directions in A* search.
     *
     * @param start the start position
     * @param target the end position
     * @return the Euclidean distance
     */
    private float euclidean(Vector2 start, Vector2 target) {
        float x = start.x - target.x;
        float y = start.y - target.y;
        return (float) Math.sqrt((x * x) + (y * y));
    }

}
