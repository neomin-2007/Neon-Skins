package org.neomin.neonSkins.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.neomin.neonSkins.NeonSkins;
import org.neomin.neonSkins.configuration.SkinPlayer;
import org.neomin.neonSkins.utilities.SkinUpdate;

@RequiredArgsConstructor
public class PlayerEvents
implements Listener {

    private final NeonSkins plugin;

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        final SkinPlayer skinPlayer = plugin.getSkinsDatabase().getInstructions().fetchPlayerData(player.getName());
        if (skinPlayer == null) return;

        if (skinPlayer.getTexture() == null || skinPlayer.getSignature() == null) return;

        new SkinUpdate(plugin).apply(player, skinPlayer.getSkinId(), skinPlayer.getTexture(), skinPlayer.getSignature());
    }

    @EventHandler
    void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        plugin.getSkinsDatabase().getInstructions().savePlayerData(player.getName());

        if (plugin.getFileManager().getBoolean("database.cache_clear_on_leave")) {
            plugin.getCached_players().remove(player.getName());
        }
    }
}
