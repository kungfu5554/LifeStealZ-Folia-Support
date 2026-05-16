package com.zetaplugins.lifestealz.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.zetaplugins.lifestealz.LifeStealZ;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * VersionChecker is a utility class that checks for updates of the LifeStealZ plugin
 * by querying the GitHub API for the latest release version.
 */
public final class VersionChecker {
    private final LifeStealZ plugin;
    private final Logger logger;
    private final String modrinthProjectId; // Kept for backwards compatibility with main class
    private boolean newVersionAvailable = false;

    // The GitHub repository to check for updates
    private final String githubRepo = "kungfu5554/LifeStealZ-Folia-Support";

    public VersionChecker(LifeStealZ plugin, String modrinthProjectId) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.modrinthProjectId = modrinthProjectId;
        checkForUpdates();
    }

    private String getGithubApiUrl() {
        return "https://api.github.com/repos/" + githubRepo + "/releases/latest";
    }

    /**
     * Checks for updates of the LifeStealZ plugin by comparing the current version with the latest version available on GitHub.
     * If a new version is available, it logs a message to the console with the details.
     */
    private void checkForUpdates() {
        String latestVersion = fetchLatestVersion();
        if (latestVersion != null) {
            String currentVersion = plugin.getDescription().getVersion();
            if (!latestVersion.trim().equals(currentVersion.trim())) {
                newVersionAvailable = true;

                final String reset = "\u001B[0m";
                final String bold = "\u001B[1m";
                final String darkGray = "\u001B[90m";
                final String lightGray = "\u001B[37m";
                final String red = "\u001B[31m";

                String message = "\n" +
                        darkGray + "==========================================" + reset + "\n" +
                        bold + "A new version of LifeStealZ is available!" + reset + "\n" +
                        bold + "New Version: " + reset + bold + red + latestVersion + reset + lightGray + " (Your version: " + currentVersion + ")" + reset + "\n" +
                        bold + "Download here: " + reset + lightGray + reset + "https://github.com/" + githubRepo + "/releases/latest" + "\n" +
                        darkGray + "==========================================" + reset;

                logger.info(message);
            }
        }
    }

    /**
     * Fetches the latest version of the plugin from GitHub.
     *
     * @return The latest version number as a String, or null if it could not be fetched.
     */
    private String fetchLatestVersion() {
        String versionsUrl = getGithubApiUrl();
        JSONObject latestRelease = fetchJsonFromUrl(versionsUrl);
        
        if (latestRelease == null) return null;

        String tagName = (String) latestRelease.get("tag_name");
        
        // Remove 'v' prefix if it exists (e.g., 'v1.0.0' -> '1.0.0')
        if (tagName != null && tagName.toLowerCase().startsWith("v")) {
            tagName = tagName.substring(1);
        }
        
        return tagName;
    }

    /**
     * Fetches JSON data from a given URL.
     * @param urlString The URL to fetch the JSON data from.
     * @return A JSONObject containing the parsed JSON data, or null if an error occurs.
     */
    private JSONObject fetchJsonFromUrl(String urlString) {
        try {
            HttpURLConnection connection = createHttpConnection(urlString);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String response = readResponse(connection);
                return (JSONObject) new JSONParser().parse(response);
            } else {
                logger.warning("Failed to retrieve data from " + urlString + " Response code: " + connection.getResponseCode());
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            logger.warning("Error fetching data: " + e.getMessage());
        }
        return null;
    }

    /**
     * Fetches a JSON array from a given URL.
     * @param urlString The URL to fetch the JSON array from.
     * @return A JSONArray containing the parsed JSON data, or null if an error occurs.
     */
    private JSONArray fetchJsonArrayFromUrl(String urlString) {
        try {
            HttpURLConnection connection = createHttpConnection(urlString);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String response = readResponse(connection);
                return (JSONArray) new JSONParser().parse(response);
            } else {
                logger.warning("Failed to retrieve data from " + urlString + " Response code: " + connection.getResponseCode());
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            logger.warning("Error fetching data: " + e.getMessage());
        }
        return null;
    }

    /**
     * Creates an HTTP connection to the specified URL.
     * @param urlString The URL to connect to.
     * @return An HttpURLConnection object for the specified URL.
     * @throws IOException If an I/O error occurs while opening the connection.
     */
    private HttpURLConnection createHttpConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // Add User-Agent since GitHub API requires it
        connection.setRequestProperty("User-Agent", "LifeStealZ-Updater");
        return connection;
    }

    /**
     * Reads the response from the given HttpURLConnection.
     * @param connection The HttpURLConnection to read the response from.
     * @return The response as a String.
     * @throws IOException If an I/O error occurs while reading the response.
     */
    private String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    /**
     * Checks if a new version of the plugin is available.
     * @return true if a new version is available, false otherwise.
     */
    public boolean isNewVersionAvailable() {
        return newVersionAvailable;
    }
}