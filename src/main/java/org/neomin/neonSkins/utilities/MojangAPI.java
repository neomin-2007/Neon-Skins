package org.neomin.neonSkins.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.neomin.neonSkins.NeonSkins;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

@RequiredArgsConstructor
public class MojangAPI {

    private final NeonSkins plugin;

    public boolean updateSkin(Player player, String uuidOrName) {

        UUID uuid = null;
        try {
            uuid = UUID.fromString(uuidOrName);
        } catch (IllegalArgumentException e) {
            uuid = getUUIDFromName(uuidOrName);
        }
        if (uuid == null) return false;

        final Pair<String, String> skin_pair = plugin.getCached_skin().getOrDefault(uuid.toString(), getSkin(uuid.toString()));
        if (skin_pair == null) return false;

        final EntityPlayer eP = ((CraftPlayer) player).getHandle();
        final GameProfile gP = eP.getProfile();

        final PropertyMap pm = gP.getProperties();

        pm.removeAll("textures");
        pm.put("textures", new Property("textures", skin_pair.getKey(), skin_pair.getValue()));

        if (!plugin.getCached_skin().containsKey(uuid.toString())) {
            plugin.getCached_skin().put(uuid.toString(), skin_pair);
        }

        Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(player));
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player));
        return true;
    }

    private Pair<String, String> getSkin(String uuid) {
        try {

            final HttpURLConnection connection = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid+ "?unsigned=false").openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                System.err.println("Failed to fetch skin: HTTP " + connection.getResponseCode());
                return null;
            }

            final InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            final JsonObject response = new JsonParser().parse(reader).getAsJsonObject();

            if (response.has("error")) {
                System.err.println("API Error: " + response.get("error").getAsString());
                return null;
            }

            JsonArray properties = response.getAsJsonArray("properties");
            JsonObject settings = properties.get(0).getAsJsonObject();

            String value = settings.get("value").getAsString();
            String signature = settings.get("signature").getAsString();

            return new Pair<>(value, signature);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UUID getUUIDFromName(String playerName) {
        try {
            String apiUrl = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                String response = new Scanner(connection.getInputStream()).useDelimiter("\\A").next();
                JSONObject json = (JSONObject) new JSONParser().parse(response);
                String uuidStr = (String) json.get("id");

                // Formata o UUID (adiciona hífens)
                return UUID.fromString(
                        uuidStr.replaceFirst(
                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                                "$1-$2-$3-$4-$5"
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
