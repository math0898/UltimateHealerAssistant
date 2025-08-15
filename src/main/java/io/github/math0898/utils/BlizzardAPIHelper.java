package io.github.math0898.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import suga.engine.GameEngine;
import suga.engine.logger.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

/**
 * The BlizzardAPIHelper is a singleton class to help manage API tokens and requests made to the Blizzard API.
 *
 * @author Sugaku
 */
public class BlizzardAPIHelper {

    /**
     * The currently active bearer token.
     */
    private String bearerToken;

    /**
     * The active BlizzardAPIHelper instance.
     */
    private static BlizzardAPIHelper instance;

    /**
     * Creates a new BlizzardAPIHelper.
     */
    private BlizzardAPIHelper () {

    }

    /**
     * Accessor to the singleton instance of the BlizzardAPIHelper.
     *
     * @return The active BlizzardAPIHelper.
     */
    public static BlizzardAPIHelper getInstance () {
        if (instance == null) instance = new BlizzardAPIHelper();
        return instance;
    }

    /**
     * Generates a Bearer token using basic authentication with Blizzard's servers.
     *
     * @return The generated bearer token, null in case of failure.
     */
    protected String generateBearerToken () {
        File file = new File("/home/sugaku/Development/Standalone/Java/UltimateHealerAssistant/api-secret");
        String apiSecret;
        try (Scanner scanner = new Scanner(file)) {
            apiSecret = scanner.nextLine().replace("\n", "");
        } catch (IOException exception) {
            GameEngine.getLogger().log(exception);
            return null;
        }

        try (HttpClient client = HttpClient.newHttpClient()) {
            String body = "grant_type=client_credentials";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth.battle.net/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + apiSecret)
                    .method("POST", HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            GameEngine.getLogger().log("Generate Bearer Token: " + response.statusCode(), Level.DEBUG);
            GameEngine.getLogger().log("Generate Bearer Token: " + response.body(), Level.DEBUG); // todo: JSON Parse and include expiration time.
            return response.body().replaceAll("\\{\"access_token\":\"(.+)\",\"token_type\":.+", "$1");
        } catch (IOException | InterruptedException exception) {
            GameEngine.getLogger().log(exception);
            return null;
        }
    }

    /**
     * Requests a PlayerIcon from Blizzard's API.
     *
     * @param realm The realm of the character to grab. {@link Utils#parseRealm(String)}
     * @param character The character's name. {@link Utils#parseCharName(String)}
     * @return The buffered image version of the PlayerIcon.
     */
    public BufferedImage requestPlayerIcon (String realm, String character) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            String bearerToken = BlizzardAPIHelper.getInstance().getBearerToken();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://us.api.blizzard.com/profile/wow/character/" + realm + "/" + character + "/character-media?namespace=profile-us&locale=en_US"))
                    .header("Authorization", "Bearer " + bearerToken)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            GameEngine.getLogger().log("Icon Request: " + realm + "-" + character, Level.DEBUG);
            GameEngine.getLogger().log("Icon Request: " + response.statusCode(), Level.DEBUG);
            GameEngine.getLogger().log("Icon Request: " + response.body(), Level.DEBUG);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body());
            String avatarUrl = node.get("assets").get(0).get("value").asText();
            return ImageIO.read(URI.create(avatarUrl).toURL());
        } catch (InterruptedException | IOException exception) {
            GameEngine.getLogger().log(exception);
            return null;
        }
    }

    /**
     * Requests a spell icon from Blizzard's API.
     *
     * @param id The id of the spell to request.
     * @return The buffered image version of the SpellIcon.
     */
    public BufferedImage requestSpellIcon (long id) {
        return null; // todo: Implement.
    }

    /**
     * Requests the details on a spell from Blizzard.
     *
     * @param id The id of the spell to request.
     * @return The details of the spell.
     */
    public SpellDetails requestSpellDetails (long id) {
        return null; // todo: Implement.
    }

    /**
     * Requests a Bearer token to be used in API calls.
     */
    public String getBearerToken () {
        if (bearerToken == null) bearerToken = generateBearerToken();
        return bearerToken;
    }
}
