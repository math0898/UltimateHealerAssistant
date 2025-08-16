package io.github.math0898.utils;

import suga.engine.GameEngine;
import suga.engine.logger.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The WoWHeadScrapper is the last resort for locating spell icons and spell details. It works by scrapping data from
 * the website.
 *
 * @author Sugaku
 */
public class WoWHeadScrapper {

    /**
     * The active BlizzardAPIHelper instance.
     */
    private static WoWHeadScrapper instance;

    /**
     * Creates a new BlizzardAPIHelper.
     */
    private WoWHeadScrapper () {

    }

    /**
     * Accessor to the singleton instance of the WoWHeadScrapper.
     *
     * @return The active WoWHeadScrapper.
     */
    public static WoWHeadScrapper getInstance () {
        if (instance == null) instance = new WoWHeadScrapper();
        return instance;
    }

    /**
     * Requests a spell icon from WoWHead.
     *
     * @param id The id of the spell to request.
     * @return The buffered image version of the SpellIcon.
     */
    public BufferedImage requestSpellIcon (long id) {
        try {
            return ImageIO.read(new File("./icons/placeholder.png"));
        } catch (Exception exception) {
            GameEngine.getLogger().log(exception);
            return null;
        }
    }

    /**
     * Requests the details on a spell from WoWHead.
     *
     * @param id The id of the spell to request.
     * @return The details of the spell.
     */
    public SpellDetails requestSpellDetails (long id) {
        try (HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()) {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.wowhead.com/spell=" + id)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            GameEngine.getLogger().log("WoWHead Scrape Request: " + id, Level.DEBUG);
            GameEngine.getLogger().log("WoWHead Scrape Request: " + response.statusCode(), Level.DEBUG);
            GameEngine.getLogger().log("WoWHead Scrape Request: " + response.body().substring(0, 100), Level.DEBUG);
            String toParse = response.body();
            String descriptionExp = " {4}<meta property=\"og:description\" content=\"(.+?) \\[]\">";
            Pattern pattern1 = Pattern.compile(descriptionExp, Pattern.DOTALL);
            Matcher matcher1 = pattern1.matcher(toParse);
            matcher1.find();
            String nameExp = "<h1 class=\"heading-size-1\">(.+?)</h1>"; // todo: Description matcher doesn't seem to be working.
            Pattern pattern2 = Pattern.compile(nameExp, Pattern.DOTALL);
            Matcher matcher2 = pattern2.matcher(toParse);
            matcher2.find();
            return new SpellDetails(id, matcher2.group(1), matcher1.group(1), System.currentTimeMillis());
        } catch (Exception exception) {
            GameEngine.getLogger().log(exception);
            return null;
        }
    }
}
