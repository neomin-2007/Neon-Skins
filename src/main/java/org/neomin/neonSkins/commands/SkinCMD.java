package org.neomin.neonSkins.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.neomin.neonSkins.NeonSkins;
import org.neomin.neonSkins.utilities.MineSkinAPI;
import org.neomin.neonSkins.utilities.MojangAPI;

@RequiredArgsConstructor
public class SkinCMD implements CommandExecutor {

    private final NeonSkins plugin;
    private final String MESSAGE_PREFIX = "messages.";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!hasPermission(sender, "help")) {
            sendMessage(sender, "no-permission");
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "alterar":
                handleChangeCommand(sender, args);
                break;
            case "copiar":
                handleCopyCommand(sender, args);
                break;
            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    private void handleChangeCommand(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "change")) {
            sendMessage(sender, "change-no-permission");
            return;
        }

        if (args.length <= 1) {
            sender.sendMessage("§ePor favor, alterar <uuid/ajuda/nome da skin>");
            sender.sendMessage("§ePor favor, alterar <jogador> <uuid/nome da skin>");
            return;
        }

        if (args[1].equalsIgnoreCase("ajuda")) {
            showChangeHelp(sender);
            return;
        }

        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cEste comando só pode ser usado por jogadores.");
                return;
            }

            Player player = (Player) sender;
            String skinInput = args[1];

            if (tryUpdateSkin(player, skinInput)) {
                sendMessage(player, "change");
            } else {
                sendMessage(player, "not-changed");
            }
            return;
        }

        if (!hasPermission(sender, "change-other")) {
            sendMessage(sender, "change-other-no-permission");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sendMessage(sender, "offline-player");
            return;
        }

        String skinInput = args[1];
        if (tryUpdateSkin(target, skinInput)) {
            sendMessage(target, "change", target.getName());
        } else {
            sendMessage(sender, "not-changed");
        }
    }

    private boolean tryUpdateSkin(Player player, String skinInput) {
        MineSkinAPI mineSkinAPI = new MineSkinAPI(plugin);
        MojangAPI mojangAPI = new MojangAPI(plugin);

        return mojangAPI.updateSkin(player, skinInput) || mineSkinAPI.updateSkin(player, skinInput);
    }

    private void handleCopyCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem executar este comando!");
            return;
        }

        final Player player = (Player) sender;

        if (!hasPermission(player, "copy")) {
            sendMessage(player, "copy-no-permission");
            return;
        }

        if (args.length <= 1) {
            player.sendMessage("§ePor favor, utilize /skin copiar <jogador>");
            return;
        }

        final Player to_copy = Bukkit.getPlayer(args[1]);
        if (to_copy == null) {
            sendMessage(player, "offline-player");
            return;
        }

        String skinInput = args[1];
        if (tryUpdateSkin(player, skinInput)) {
            sendMessage(player, "change", to_copy.getName());
        } else {
            sendMessage(player, "not-changed");
        }
    }

    private void showChangeHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("§e§lMINESKIN.ORG (UUID ÚNICO)");
        sender.sendMessage("    §eVocê pode direcionar o uuid único");
        sender.sendMessage("    §7obtido na URL de skins do MineSkin.");
        sender.sendMessage("");
        sender.sendMessage("§eExemplo: (SKIN BMO)");
        sender.sendMessage("    §e/skin alterar 3110d0a38eb64a3aaf33225719c2f855");
        sender.sendMessage("");
        sender.sendMessage("§e§lMOJANG API (NOME)");
        sender.sendMessage("    §eVocê pode digitar o nome do");
        sender.sendMessage("    §7jogador original para obter sua skin.");
        sender.sendMessage("");
        sender.sendMessage("§eExemplo: (SKIN BMO)");
        sender.sendMessage("    §e/skin alterar BMO");
        sender.sendMessage("");
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("§eComandos de Skin:");
        sender.sendMessage("");

        int availableCommands = 0;

        if (hasPermission(sender, "change")) {
            sender.sendMessage("§e/skin alterar <uuid/ajuda/nome da skin>");
            availableCommands++;
        }

        if (hasPermission(sender, "change-other")) {
            sender.sendMessage("§e/skin alterar <jogador> <uuid/nome da skin>");
            availableCommands++;
        }

        if (hasPermission(sender, "copy")) {
            sender.sendMessage("§e/skin copiar <jogador>");
            availableCommands++;
        }

        if (availableCommands == 0) {
            sender.sendMessage("§eVocê não tem as permissões para");
            sender.sendMessage("§eos comandos deste sistema!");
        }

        sender.sendMessage("");
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(plugin.getFileManager().getString("permissions.skin-" + permission));
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getString(MESSAGE_PREFIX + message)));
    }

    private void sendMessage(CommandSender sender, String message, String player) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getString(MESSAGE_PREFIX + message))
                .replace("%player%", player));
    }
}