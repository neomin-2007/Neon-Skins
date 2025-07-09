package org.neomin.neonSkins.utilities;

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
import org.neomin.neonSkins.NeonSkins;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiredArgsConstructor
public class MineSkinAPI {

    private final NeonSkins plugin;

    public boolean updateSkin(Player player, String uuid) {
        final Pair<String, String> skin_pair = plugin.getCached_skin().getOrDefault(uuid, getSkin(uuid));
        if (skin_pair == null) return false;
        new SkinUpdate(plugin).apply(player, uuid, skin_pair.getKey(), skin_pair.getValue());
        return true;
    }

    private Pair<String, String> getSkin(String uuid) {
        try {

            final HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mineskin.org/v2/skins/" + uuid).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "MyMineSkinApp/v1.0");

            if (connection.getResponseCode() != 200) {
                System.err.println("A API do MineSkin.org não pode ser utilizada neste caso! HTTP (" + connection.getResponseCode() + ")");
                System.err.println("Redirecionando para a MojangAPI...");
                return null;
            }

            final InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            final JsonObject response = new JsonParser().parse(reader).getAsJsonObject();

            if (response.has("error")) {
                System.err.println("Erro na API do MineSkin.org: " + response.get("error").getAsString());
                return null;
            }

            JsonObject skin = response.getAsJsonObject("skin");
            JsonObject texture = skin.getAsJsonObject("texture");
            JsonObject data = texture.getAsJsonObject("data");

            String value = data.get("value").getAsString();
            String signature = data.get("signature").getAsString();

            return new Pair<>(value, signature);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
