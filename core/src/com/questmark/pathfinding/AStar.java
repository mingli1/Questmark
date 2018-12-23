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
    // bounding boxes of diagonal nodes
    private Rectangle diag1;
    private Rectangle diag2;

    private BinaryHeap<Node> openHeap;
    private Map<Vector2, Node> openSet;
    private Map<Vector2, Node> closedSet;
    private Vector2 tempTarget;

    public AStar(int mapWidth, int mapHeight, int tileSize) {
        this.tileSize = tileSize;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        quadTree = new QuadTree(0, new Rectangle(0, 0, mapWidth * tileSize, mapHeight * tileSize));
        openHeap = new BinaryHeap<Node>();
        openSet = new HashMap<Vector2, Node>();
        closedSet = new HashMap<Vector2, Node>();
        tempTarget = new Vector2();

        collisions = new Array<Rectangle>();
        currBounds = new Rectangle(0, 0, tileSize, tileSize);
        diag1 = new Rectangle(0, 0, tileSize, tileSize);
        diag2 = new Rectangle(0, 0, tileSize, tileSize);
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
     * Returns a list of {@link Node} representing the A* path from a given start position to a
     * given target position, considering a list of bounding boxes to avoid.
     * Returns null is there is no possible path from start to target.
     *
     * @param start the start position of the tile the player's position is currently on
     * @param target the target position
     * @return an A* path and null if there is no path
     */
    public Array<Node> findPath(Vector2 start, Vector2 target) {
        openHeap.clear();
        openSet.clear();
        closedSet.clear();
        Array<Node> path = new Array<Node>();

        // initialize source
        Node source = new Node(start, null, 0, diagonal(start, target));
        openHeap.add(source);
        openSet.put(source.position, source);

        while (openHeap.size > 0) {
            Node curr = openHeap.pop();
            openSet.remove(curr.position);
            if (curr.position.equals(target)) {
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

                if (x < 0 || x > (mapWidth - 1) * tileSize || y < 0 || y > (mapHeight - 1) * tileSize) continue;

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

                // check for diagonal path but blocked by perpendicular nodes
                if (i == 0) {
                    diag1.setPosition(curr.position.x - tileSize, curr.position.y);
                    diag2.setPosition(curr.position.x, curr.position.y - tileSize);
                }
                else if (i == 6) {
                    diag1.setPosition(curr.position.x, curr.position.y + tileSize);
                    diag2.setPosition(curr.position.x - tileSize, curr.position.y);
                }
                else if (i == 8) {
                    diag1.setPosition(curr.position.x, curr.position.y + tileSize);
                    diag2.setPosition(curr.position.x + tileSize, curr.position.y);
                }
                else if (i == 2) {
                    diag1.setPosition(curr.position.x + tileSize, curr.position.y);
                    diag2.setPosition(curr.position.x, curr.position.y - tileSize);
                }
                if (i == 0 || i == 2 || i == 6 || i == 8) {
                    boolean b1 = false, b2 = false;
                    for (Rectangle bounds : collisions) {
                        if (diag1.overlaps(bounds)) {
                            b1 = true;
                            break;
                        }
                    }
                    for (Rectangle bounds : collisions) {
                        if (diag2.overlaps(bounds)) {
                            b2 = true;
                            break;
                        }
                    }
                    if (b1 && b2) continue;
                }

                float gScore = curr.gScore + diagonal(curr.position, tempTarget);
                float hScore = diagonal(tempTarget, target);
                Node successor = new Node(new Vector2(x, y), curr, gScore, hScore);

                if (openSet.containsKey(successor.position) &&
                        openSet.get(successor.position).fScore < successor.fScore) continue;

                if (closedSet.containsKey(successor.position) &&
                        closedSet.get(successor.position).fScore <= successor.fScore) continue;
                if (!closedSet.containsKey(successor.position) ||
                        closedSet.get(successor.position).fScore > successor.fScore) {
                    openHeap.add(successor);
                    openSet.put(successor.position, successor);
                }
            }
        }
        return null;
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
        float dx = Math.abs(start.x - target.x);
        float dy = Math.abs(start.y - target.y);
        float d = 1.f;
        float d2 = 1.41421f;
        return d * (dx + dy) + (d2 - 2 * d) * Math.min(dx, dy);
    }

}
