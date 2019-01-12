package com.questmark.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor

/**
 * Key input handler for the game that processes key input for entity movement, skills, etc.
 *
 * @author Ming Li
 */
class KeyInput(private val keyInputHandler: KeyInputHandler) : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> keyInputHandler.move(Direction.Up)
            Input.Keys.S -> keyInputHandler.move(Direction.Down)
            Input.Keys.A -> keyInputHandler.move(Direction.Left)
            Input.Keys.D -> keyInputHandler.move(Direction.Right)
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> keyInputHandler.stop(Direction.Up)
            Input.Keys.S -> keyInputHandler.stop(Direction.Down)
            Input.Keys.A -> keyInputHandler.stop(Direction.Left)
            Input.Keys.D -> keyInputHandler.stop(Direction.Right)
        }
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        return true
    }

    /** The key event listener methods after this don't matter as much  */

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

}
