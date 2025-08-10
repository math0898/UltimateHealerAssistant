package io.github.math0898.gui;

import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

/**
 * The Player icon is an icon generated with a player name and server which allows this to grab the image from Blizzard's
 * WoW armory api.
 * @see <a href="https://develop.battle.net/documentation/world-of-warcraft/guides/character-renders">Blizzard API Character Renders.</a>
 *
 * @author Sugaku
 */
public class PlayerIcon extends BasicGameObject implements DrawListener {

    /**
     * The name of the realm the Player in this icon is on.
     */
    private final String realm;

    /**
     * The name of the character this PLayer icon represents.
     */
    private final String character;

    /**
     * Creates a new PlayerIcon with the given realm slug and character name.
     *
     * @param realmSlug This is the server name without US and a dash. Full lowercase.
     * @param characterName This is the name of the character. Full lowercase.
     */
    public PlayerIcon (String realmSlug, String characterName) {
        this.realm = realmSlug;
        this.character = characterName;
    }

    /**
     * If present, returns the DrawListener associated with this GameObject. May be null.
     *
     * @return Either the DrawListener attached to this GameObject or null.
     */
    @Override
    public DrawListener getDrawListener () {
        return this;
    }

    /**
     * Called every drawing frame so programs have a chance to make their voices heard on what gets drawn.
     *
     * @param width  The width of the pixel map.
     * @param height The height of the pixel map.
     * @param panel  The panel to apply changes to.
     */
    @Override
    public void applyChanges (int width, int height, GraphicsPanel panel) {

    }
}
