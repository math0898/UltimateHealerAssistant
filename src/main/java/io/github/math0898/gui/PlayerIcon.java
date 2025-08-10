package io.github.math0898.gui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import suga.engine.game.objects.BasicGameObject;
import suga.engine.graphics.DrawListener;
import suga.engine.graphics.GraphicsPanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

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

    private static BufferedImage icon;

    /**
     * Temporary testing method.
     */
    public static void main (String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        File file = new File("/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/api-secret");
        Scanner scanner = new Scanner(file);
        String apiSecret = scanner.nextLine().replace("\n", "");

        String body = "grant_type=client_credentials";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://oauth.battle.net/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + apiSecret)
                .method("POST", HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
        System.out.println(response.body());

        request = HttpRequest.newBuilder()
                .uri(URI.create("https://us.api.blizzard.com/profile/wow/character/stormrage/nillath/character-media?namespace=profile-us&locale=en_US"))
                .header("Authorization", "Bearer " + response.body().replaceAll("\\{\"access_token\":\"(.+)\",\"token_type\":.+", "$1"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
        System.out.println(response.body());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.body());
        String avatarUrl = node.get("assets").get(0).get("value").asText();
        icon = ImageIO.read(URI.create(avatarUrl).toURL());
    }

    /**
     * Creates a new PlayerIcon with the given realm slug and character name.
     *
     * @param realmSlug This is the server name without US and a dash. Full lowercase.
     * @param characterName This is the name of the character. Full lowercase.
     */
    public PlayerIcon (String realmSlug, String characterName) {
        this.realm = realmSlug;
        this.character = characterName;
        try {
            PlayerIcon.main(new String[]{});
        } catch (Exception ignored) { }
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
        panel.addImage(width / 2, height / 2, 50, 50, icon);
    }
}
