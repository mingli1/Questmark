package com.questmark.entity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.questmark.entity.entities.NormalAnim
import com.questmark.entity.entities.Player
import com.questmark.entity.systems.*
import com.questmark.entity.systems.enemy.*
import com.questmark.util.Resources

/**
 * The manager for the Ashley entity component system (ECS).
 * Contains the universal [Engine] for all Ashley [Entity] components and systems.
 *
 * @author Ming Li
 */
class ECS(batch: Batch, res: Resources) : Disposable {

    private val engine: Engine = Engine()

    // default systems that are inherent with entities
    private lateinit var movementSystem: MovementSystem
    private lateinit var renderSystem: RenderSystem
    private lateinit var tileMapCollisionSystem: TileMapCollisionSystem
    private lateinit var randomMovementSystem: RandomMovementSystem
    private lateinit var circularMovementSystem: CircularMovementSystem
    private lateinit var horizontalMovementSystem: HorizontalMovementSystem
    private lateinit var verticalMovementSystem: VerticalMovementSystem
    private lateinit var aStarMovementSystem: AStarMovementSystem

    // entities
    var player: Player? = null
        private set

    init {
        this.addEntities(res)
        this.addSystems(batch, res)
    }

    /**
     * Initializes and adds all entities to the engine.
     */
    private fun addEntities(res: Resources) {
        player = Player(Vector2(168f, 180f), res)

        engine.addEntity(player)
        engine.addEntity(NormalAnim(Vector2(180f, 192f), res))
    }

    /**
     * Initializes and adds all systems to the engine in priority order.
     *
     * @param batch
     * @param res
     */
    private fun addSystems(batch: Batch, res: Resources) {
        movementSystem = MovementSystem()
        renderSystem = RenderSystem(batch)
        tileMapCollisionSystem = TileMapCollisionSystem()
        randomMovementSystem = RandomMovementSystem()
        circularMovementSystem = CircularMovementSystem()
        horizontalMovementSystem = HorizontalMovementSystem()
        verticalMovementSystem = VerticalMovementSystem()
        aStarMovementSystem = AStarMovementSystem()

        addSystem(movementSystem)
        addSystem(tileMapCollisionSystem)
        addSystem(randomMovementSystem)
        addSystem(circularMovementSystem)
        addSystem(horizontalMovementSystem)
        addSystem(verticalMovementSystem)
        addSystem(aStarMovementSystem)
        addSystem(renderSystem)
    }

    fun update(dt: Float) {
        engine.update(dt)
    }

    override fun dispose() {

    }

    /**
     * Adds an [EntitySystem] to the engine.
     *
     * @param system
     */
    fun addSystem(system: EntitySystem) {
        engine.addSystem(system)
    }

    /**
     * Returns an [EntitySystem] from a given [com.badlogic.ashley.core.Component] class type.
     *
     * @param system
     */
    fun <T : EntitySystem> getSystem(system: Class<T>): T {
        return engine.getSystem(system)
    }

    /**
     * Toggles a given [EntitySystem] on or off.
     *
     * @param system the given entity system
     * @param toggle toggle on or off
     */
    fun <T : EntitySystem> toggleProcessing(system: Class<T>, toggle: Boolean) {
        engine.getSystem(system).setProcessing(toggle)
    }

    /**
     * Updates [com.questmark.entity.systems.collision.CollisionSystem] systems with tile map data.
     *
     * @param mapWidth
     * @param mapHeight
     * @param tileSize
     * @param boundingBoxes
     */
    fun updateCollisionSystems(mapWidth: Int, mapHeight: Int, tileSize: Int, boundingBoxes: Array<Rectangle>) {
        tileMapCollisionSystem.setMapData(mapWidth, mapHeight, tileSize, boundingBoxes)
        aStarMovementSystem.setMapData(mapWidth, mapHeight, tileSize, boundingBoxes)
    }

}
