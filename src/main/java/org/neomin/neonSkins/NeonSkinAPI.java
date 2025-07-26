package org.neomin.neonSkins;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.neomin.neonSkins.configuration.SkinPlayer;
import org.neomin.neonSkins.utilities.MineSkinAPI;
import org.neomin.neonSkins.utilities.MojangAPI;
import org.neomin.neonSkins.utilities.SkinUpdate;

@RequiredArgsConstructor
public class NeonSkinAPI {

    private final NeonSkins plugin;

    public boolean applySkin(Player player, String skinId, String texture, String signature) {
        if (player == null || texture.isEmpty() || signature.isEmpty()) return false;
        return new SkinUpdate(plugin).apply(player, skinId, texture, signature);
    }

    public boolean applySkin(Player player, String nickOrURL) {
        if (player == null || nickOrURL.isEmpty()) return false;

        MineSkinAPI mineSkinAPI = new MineSkinAPI(plugin);
        MojangAPI mojangAPI = new MojangAPI(plugin);

        return mojangAPI.updateSkin(player, nickOrURL) || mineSkinAPI.updateSkin(player, nickOrURL);
    }

    public Pair<String, String> getSkinFromID(String skinId) {
        return plugin.getCached_skin().get(skinId);
    }

    public Pair<String, String> getPlayerSkin(String key) {
        final SkinPlayer skinPlayer = getSkinPlayer(key);
        return new ImmutablePair<>(skinPlayer.getTexture(), skinPlayer.getSignature());
    }

    public SkinPlayer getSkinPlayer(String key) {
        return plugin.getCached_players().get(key);
    }

    public boolean updateToCache(String key, SkinPlayer skinPlayer) {
        return plugin.getCached_players().put(key, skinPlayer) != null;
    }

    public boolean updateToDatabase(String key, SkinPlayer skinPlayer) {
        if (!updateToCache(key, skinPlayer)) return false;
        plugin.getSkinsDatabase().getInstructions().savePlayerData(key);
        return true;
    }
}
