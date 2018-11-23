package com.questmark.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Key input handler for the game that processes key input for entity movement, skills, etc.
 *
 * @author Ming Li
 */
public class KeyInput implements InputProcessor {

    private KeyInputHandler keyInputHandler;

    public KeyInput(KeyInputHandler keyInputHandler) {
        this.keyInputHandler = keyInputHandler;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                keyInputHandler.move(Direction.Up);
                break;
            case Input.Keys.S:
                keyInputHandler.move(Direction.Down);
                break;
            case Input.Keys.A:
                keyInputHandler.move(Direction.Left);
                break;
            case Input.Keys.D:
                keyInputHandler.move(Direction.Right);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                keyInputHandler.stop(Direction.Up);
                break;
            case Input.Keys.S:
                keyInputHandler.stop(Direction.Down);
                break;
            case Input.Keys.A:
                keyInputHandler.stop(Direction.Left);
                break;
            case Input.Keys.D:
                keyInputHandler.stop(Direction.Right);
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    /** The key event listener methods after this don't matter as much **/

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
