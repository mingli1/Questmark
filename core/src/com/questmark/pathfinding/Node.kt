package com.questmark.pathfinding

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.BinaryHeap

import java.util.Objects

/**
 * Represents a node on a graph for A* search. Stores f, g, and h scores such that
 * f = g + h represents the estimated cost for a particular path to the target.
 * Also stores a parent node pointer and the position of the node relative to the map.
 *
 * @author Ming Li
 */
class Node(var position: Vector2, var parent: Node?, var gScore: Float, var hScore: Float) : BinaryHeap.Node(gScore + hScore) {

    // f score is the estimated cost of the path from the start node to the target through the current node
    var fScore: Float = 0f

    init {
        this.fScore = gScore + hScore
    }

    override fun equals(node: Any?): Boolean {
        if (node !is Node) return false
        val n = node as Node?
        return this.position == n!!.position && this.parent == n.parent &&
                this.fScore == n.fScore && this.gScore == n.gScore && this.hScore == n.hScore
    }

    override fun hashCode(): Int {
        return Objects.hash(fScore, gScore, hScore, position, parent)
    }

}
