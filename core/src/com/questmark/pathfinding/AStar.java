package com.questmark.pathfinding;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BinaryHeap;
import com.questmark.entity.QuadTree;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that represents the A* search algorithm for pathfinding.
 * Each node on the graph is represented by a {@link Node}.
 * Uses a {@link BinaryHeap} for the open list.
 *
 * @author Ming Li
 */
public final class AStar {

    // collidables to avoid
    private QuadTree quadTree;
    private Array<Rectangle> collisions;
    private int tileSize;
    private int mapWidth;
    private int mapHeight;

    // the bounding box of the current node
    private Rectangle currBounds;

    private BinaryHeap<Node> openHeap;
    private Map<Vector2, Node> openSet;
    private Map<Vector2, Node> closedSet;
    private Array<Node> path;
    private Vector2 tempTarget;

    public AStar(int mapWidth, int mapHeight, int tileSize) {
        this.tileSize = tileSize;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        quadTree = new QuadTree(0, new Rectangle(0, 0, mapWidth * tileSize, mapHeight * tileSize));
        openHeap = new BinaryHeap<Node>();
        openSet = new HashMap<Vector2, Node>();
        closedSet = new HashMap<Vector2, Node>();
        path = new Array<Node>();
        tempTarget = new Vector2();

        collisions = new Array<Rectangle>();
        currBounds = new Rectangle(0, 0, tileSize, tileSize);
    }

    /**
     * Updates the list of collidables for the algorithm to account for.
     * Uses a {@link QuadTree} for O(log n) bounding box retrieval
     *
     * @param boundingBoxes the bounding boxes of all entities and walls
     */
    public void setCollisionData(Array<Rectangle> boundingBoxes) {
        quadTree.clear();
        for (Rectangle bounds : boundingBoxes) quadTree.insert(bounds);
    }

    /**
     * Returns a list of {@link Node} representing the A* path from a given start position to
     * a given target position, considering a list of bounding boxes to avoid.
     * Uses Euclidean heuristic function
     *
     * @param start the start position as the center of the tile the player's position is currently on
     * @param target the target position
     * @param epsilon margin of error between current position and target
     * @return an A* path
     */
    public Array<Node> findPath(Vector2 start, Vector2 target, float epsilon) {
        return findPath(start, target, 0, epsilon);
    }

    /**
     * Returns a list of {@link Node} representing the A* path from a given start position to a
     * given target position, considering a list of bounding boxes to avoid. This function allows a
     * choice for the heuristic function where (0 - euclidean, 1 - manhattan, 2 - diagonal)
     * Returns null is there is no possible path from start to target.
     *
     * @param start the start position of the tile the player's position is currently on
     * @param target the target position
     * @param heuristic an int representing a heuristic function as documented above
     * @param epsilon margin of error between current position and target
     * @return an A* path and null if there is no path
     */
    public Array<Node> findPath(Vector2 start, Vector2 target, int heuristic, float epsilon) {
        openHeap.clear();
        openSet.clear();
        closedSet.clear();
        path.clear();

        // initialize source
        Node source = new Node(start, null, 0, getHeuristic(start, target, heuristic));
        openHeap.add(source);
        openSet.put(source.position, source);

        while (openHeap.size > 0) {
            Node curr = openHeap.pop();
            openSet.remove(curr.position);
            if (curr.position.epsilonEquals(target, epsilon)) {
                while (curr.parent != null) {
                    path.add(curr);
                    curr = curr.parent;
                }
                return path;
            }
            closedSet.put(curr.position, curr);
            // process current node's 8 successors
            for (int i = 0; i < 9; i++) {
                if (i == 4) continue;
                float x = curr.position.x + ((i % 3) - 1) * tileSize;
                float y = curr.position.y + ((i / 3) - 1) * tileSize;
                currBounds.setPosition(x, y);
                tempTarget.set(x, y);

                if (x < 0 || x > mapWidth * tileSize || y < 0 || y > mapHeight * tileSize) continue;

                collisions.clear();
                quadTree.retrieve(collisions, currBounds);
                boolean invalid = false;
                for (Rectangle bounds : collisions) {
                    if (currBounds.overlaps(bounds)) {
                        invalid = true;
                        break;
                    }
                }
                if (invalid) continue;

                float gScore = curr.gScore + getHeuristic(curr.position, tempTarget, heuristic);
                float hScore = getHeuristic(tempTarget, target, heuristic);
                Node successor = new Node(new Vector2(x, y), curr, gScore, hScore);

                if (openSet.containsKey(successor.position) &&
                        openSet.get(successor.position).fScore < successor.fScore) continue;

                if (closedSet.containsKey(successor.position) &&
                        closedSet.get(successor.position).fScore < successor.fScore) continue;
                if (!closedSet.containsKey(successor.position) ||
                        closedSet.get(successor.position).fScore >= successor.fScore) {
                    openHeap.add(successor);
                    openSet.put(successor.position, successor);
                }
            }
        }

        return null;
    }

    /**
     * Returns the heuristic calculation based on heuristic function type.
     * (0 - euclidean, 1 - manhattan, 2 - diagonal)
     *
     * @param start
     * @param target
     * @param heuristic
     * @return
     */
    private float getHeuristic(Vector2 start, Vector2 target, int heuristic) {
        if (heuristic == 0) return euclidean(start, target);
        if (heuristic == 1) return manhattan(start, target);
        if (heuristic == 2) return diagonal(start, target);
        return -1;
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
