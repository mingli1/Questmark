package com.questmark.pathfinding

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.BinaryHeap

import java.util.HashMap

/**
 * Utility class that represents the A* search algorithm for pathfinding.
 * Each node on the graph is represented by a [Node].
 * Uses a [BinaryHeap] for the open list.
 *
 * @author Ming Li
 */
class AStar(private val mapWidth: Int, private val mapHeight: Int, private val tileSize: Int) {

    // collidables to avoid
    private var collisions: Array<Rectangle>

    // the bounding box of the current node
    private val currBounds: Rectangle
    // bounding boxes of diagonal nodes
    private val diag1: Rectangle
    private val diag2: Rectangle

    private val openHeap: BinaryHeap<Node> = BinaryHeap()
    private val openSet: MutableMap<Vector2, Node> = HashMap()
    private val closedSet: MutableMap<Vector2, Node> = HashMap()
    private val tempTarget: Vector2 = Vector2()

    init {
        collisions = Array()
        currBounds = Rectangle(0f, 0f, tileSize.toFloat(), tileSize.toFloat())
        diag1 = Rectangle(0f, 0f, tileSize.toFloat(), tileSize.toFloat())
        diag2 = Rectangle(0f, 0f, tileSize.toFloat(), tileSize.toFloat())
    }

    /**
     * Updates the list of collidables for the algorithm to account for.
     *
     * @param boundingBoxes the bounding boxes of all entities and walls
     */
    fun setCollisionData(boundingBoxes: Array<Rectangle>) {
        this.collisions = boundingBoxes
    }

    /**
     * Returns a list of [Node] representing the A* path from a given start position to a
     * given target position, considering a list of bounding boxes to avoid.
     * Returns null is there is no possible path from start to target.
     *
     * @param start the start position of the tile the player's position is currently on
     * @param target the target position
     * @return an A* path and null if there is no path
     */
    fun findPath(start: Vector2, target: Vector2): Array<Node>? {
        openHeap.clear()
        openSet.clear()
        closedSet.clear()
        val path = Array<Node>()

        // initialize source
        val source = Node(start, null, 0f, diagonal(start, target))
        openHeap.add(source)
        openSet[source.position] = source

        while (openHeap.size > 0) {
            var curr = openHeap.pop()
            openSet.remove(curr.position)
            if (curr.position == target) {
                while (curr.parent != null) {
                    path.add(curr)
                    curr = curr.parent
                }
                return path
            }
            closedSet[curr.position] = curr
            // process current node's 8 successors
            for (i in 0..8) {
                if (i == 4) continue
                val x = curr.position.x + (i % 3 - 1) * tileSize
                val y = curr.position.y + (i / 3 - 1) * tileSize
                currBounds.setPosition(x, y)
                tempTarget.set(x, y)

                if (x < 0 || x > (mapWidth - 1) * tileSize || y < 0 || y > (mapHeight - 1) * tileSize) continue

                var invalid = false
                for (bounds in collisions) {
                    if (currBounds.overlaps(bounds)) {
                        invalid = true
                        break
                    }
                }
                if (invalid) continue

                // check for diagonal path but blocked by perpendicular nodes
                when (i) {
                    0 -> {
                        diag1.setPosition(curr.position.x - tileSize, curr.position.y)
                        diag2.setPosition(curr.position.x, curr.position.y - tileSize)
                    }
                    6 -> {
                        diag1.setPosition(curr.position.x, curr.position.y + tileSize)
                        diag2.setPosition(curr.position.x - tileSize, curr.position.y)
                    }
                    8 -> {
                        diag1.setPosition(curr.position.x, curr.position.y + tileSize)
                        diag2.setPosition(curr.position.x + tileSize, curr.position.y)
                    }
                    2 -> {
                        diag1.setPosition(curr.position.x + tileSize, curr.position.y)
                        diag2.setPosition(curr.position.x, curr.position.y - tileSize)
                    }
                }
                if (i == 0 || i == 2 || i == 6 || i == 8) {
                    var b1 = false
                    var b2 = false
                    for (bounds in collisions) {
                        if (diag1.overlaps(bounds)) {
                            b1 = true
                            break
                        }
                    }
                    for (bounds in collisions) {
                        if (diag2.overlaps(bounds)) {
                            b2 = true
                            break
                        }
                    }
                    if (b1 && b2) continue
                }

                val gScore = curr.gScore + diagonal(curr.position, tempTarget)
                val hScore = diagonal(tempTarget, target)
                val successor = Node(Vector2(x, y), curr, gScore, hScore)

                if (openSet.containsKey(successor.position) && openSet[successor.position]!!.fScore < successor.fScore)
                    continue

                if (closedSet.containsKey(successor.position) && closedSet[successor.position]!!.fScore <= successor.fScore)
                    continue
                if (!closedSet.containsKey(successor.position) || closedSet[successor.position]!!.fScore > successor.fScore) {
                    openHeap.add(successor)
                    openSet[successor.position] = successor
                }
            }
        }
        return null
    }

    /**
     * A heuristic function that returns the diagonal distance between two given positions.
     * Used for restricting A* search to eight directions: N, E, S, W, NE, NW, SE, SW
     *
     * @param start the start position
     * @param target the target position
     * @return the diagonal distance
     */
    private fun diagonal(start: Vector2, target: Vector2): Float {
        val dx = Math.abs(start.x - target.x)
        val dy = Math.abs(start.y - target.y)
        val d = 1f
        val d2 = 1.41421f
        return d * (dx + dy) + (d2 - 2 * d) * Math.min(dx, dy)
    }

}
