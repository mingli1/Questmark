package com.questmark.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BinaryHeap;

import java.util.Objects;

/**
 * Represents a node on a graph for A* search. Stores f, g, and h scores such that
 * f = g + h represents the estimated cost for a particular path to the target.
 * Also stores a parent node pointer and the position of the node relative to the map.
 *
 * @author Ming Li
 */
public class Node extends BinaryHeap.Node {

    // f score is the estimated cost of the path from the start node to the target through the current node
    public float fScore;
    // g score is the cost of the path to get to the current node
    public float gScore;
    // h score is the estimated cost of the path from the current node to the target (heuristic)
    public float hScore;

    public Vector2 position;
    public Node parent;

    /**
     * Constructs a node given its position, parent pointer, g and h scores.
     * f score is automatically calculated as the sum of g and h scores.
     *
     * @param position position of the node relative to the map
     * @param parent parent of this node or the previous node on the path to this node
     * @param gScore the calculated g score
     * @param hScore the estimated h score
     */
    public Node(Vector2 position, Node parent, float gScore, float hScore) {
        super(gScore + hScore);
        this.position = position;
        this.parent = parent;
        this.gScore = gScore;
        this.hScore = hScore;
        this.fScore = gScore + hScore;
    }

    @Override
    public boolean equals(Object node) {
        if (!(node instanceof Node)) return false;
        Node n = (Node) node;
        return this.position.equals(n.position) && this.parent.equals(n.parent) &&
                this.fScore == n.fScore && this.gScore == n.gScore && this.hScore == n.hScore;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fScore, gScore, hScore, position, parent);
    }

}
