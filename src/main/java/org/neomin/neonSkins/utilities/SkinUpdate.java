package org.neomin.neonSkins.utilities;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.neomin.neonSkins.NeonSkins;
import org.neomin.neonSkins.configuration.SkinPlayer;

@RequiredArgsConstructor
public class SkinUpdate {

    private final NeonSkins plugin;

    public void apply(Player player, String skin_id, String texture, String signature) {
        final EntityPlayer eP = ((CraftPlayer) player).getHandle();
        final GameProfile gP = eP.getProfile();

        final PropertyMap pm = gP.getProperties();

        pm.removeAll("textures");
        pm.put("textures", new Property("textures", texture, signature));

        if (plugin.getFileManager().getBoolean("database.cached_skin")) {
            if (!plugin.getCached_skin().containsKey(skin_id.toString())) {
                plugin.getCached_skin().put(skin_id.toString(), Pair.of(texture, signature));
            }
        }

        final SkinPlayer skinPlayer = plugin.getCached_players().get(player.getName());
        if (skinPlayer != null) {
            skinPlayer.setSkinId(skin_id);
            skinPlayer.setTexture(texture);
            skinPlayer.setSignature(signature);
        }

        Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(player));
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player));
    }
}
