package io.github.math0898.gui;

import suga.engine.input.mouse.BasicMouseListener;
import suga.engine.logger.Level;

import java.awt.event.MouseEvent;

import static suga.engine.GameEngine.getLogger;

public class UltimateMouseListener extends BasicMouseListener {

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked (MouseEvent e) {
        getLogger().log("BasicMouseListener: Received mouse clicked event: " + e, Level.VERBOSE);
        events.add(e);
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed (MouseEvent e) {
        getLogger().log("BasicMouseListener: Received mouse pressed event: " + e, Level.VERBOSE);
        events.add(e);
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased (MouseEvent e) {
        getLogger().log("BasicMouseListener: Received mouse released event: " + e, Level.VERBOSE);
        events.add(e);
    }
}
