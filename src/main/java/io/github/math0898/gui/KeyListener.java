package io.github.math0898.gui;

import suga.engine.input.keyboard.GameKeyListener;
import suga.engine.input.keyboard.KeyMapper;
import suga.engine.input.keyboard.KeyValue;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Stack;

public class KeyListener implements GameKeyListener {

    /**
     * Overrides the currently used KeyMapper with a new KeyMapper.
     *
     * @param mapping The new mapping to apply to this key listener.
     */
    @Override
    public void setKeyMapping (KeyMapper mapping) {

    }

    /**
     * Accessor method for the currently used KeyMapper. Helpful for modifying mappings.
     *
     * @return The KeyMapper instance being used by this KeyMapper.
     */
    @Override
    public KeyMapper getKeyMapping () {
        return null;
    }

    /**
     * Checks whether the given key is currently being held or not.
     *
     * @param key The key to check if it's being held or not.
     * @return True if the key is currently being held, otherwise false.
     */
    @Override
    public boolean isHeld (KeyValue key) {
        return false;
    }

    /**
     * Returns a stack of key presses that have not yet been handled.
     *
     * @return The 'to handle' stack of key presses.
     */
    @Override
    public Stack<KeyValue> getKeyPresses () {
        return null;
    }

    /**
     * Returns a stack of key releases that have not yet been handled.
     *
     * @return The 'to handle' stack of key releases.
     */
    @Override
    public Stack<KeyValue> getKeyReleases () {
        return null;
    }

    /**
     * Sets the frame that this key listener is listening to. Does not deregister with the old frame.
     *
     * @param frame The new frame to listen to.
     */
    @Override
    public void setFrame (JFrame frame) {

    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped (KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed (KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased (KeyEvent e) {

    }
}
