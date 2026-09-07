package com.github.treemanking.budgiesets.utilities;

import com.github.treemanking.budgiesets.BudgieSets;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public final class ChatUtils {
    public static final String PREFIX = "&7[&bBudgieSets&7] &r";

    // Send Messages
    public static void sendMsg(Object player, String message) {
        if (player instanceof Player) sendMsg((Player) player, message);
        else if (player instanceof ConsoleCommandSender) sendMsg((ConsoleCommandSender) player, message);
    }
    public static void sendMsg(CommandSender sender, String message) { sendMsg(sender, message, true); }
    public static void sendMsg(Object player, String message, Boolean showPrefix) { if (player instanceof CommandSender sender) sendMsg(sender, message, showPrefix); }
    public static void sendMsg(CommandSender sender, String message, boolean showPrefix) { sendMsg(sender, toTextComp(message), showPrefix); }
    public static void sendMsg(CommandSender sender, TextComponent message, boolean showPrefix) {
        if (sender == null || sender instanceof ConsoleCommandSender) {
            log(Level.INFO, message);
        } else if (sender instanceof Player) {
            if (showPrefix) message = toTextComp(PREFIX).append(message);
            sendMsg(sender, message);
        }
    }
    public static void sendMsg(CommandSender sender, TextComponent message) {
        if (sender == null || sender instanceof ConsoleCommandSender) {
            log(Level.INFO, message);
        } else if (sender instanceof Player) {
            sender.sendMessage(message);
        }
    }

    public static void error(String message) { ChatUtils.log(Level.SEVERE, message); }
    public static void warn(String message) { ChatUtils.log(Level.WARNING, message); }
    // Server Logger
    public static void log(TextComponent message) { ChatUtils.log(Level.INFO, message); }
    public static void log(String message) { ChatUtils.log(Level.INFO, message); }
    public static void log(Level level, TextComponent message) { log(level, PlainTextComponentSerializer.plainText().serializeOrNull(message)); }
    public static void log(Level level, String message) {
        if (message == null || message.isBlank()) return;
        BudgieSets.getBudgieSets().getLogger().log(level, message);
    }

    /**
     * Converts a string to TextComponent and parses & for colour, # for hex.
     * @param string The string to parse.
     * @return The formated TextComponent.
     */
    public static TextComponent toTextComp(String string) {
        return LegacyComponentSerializer.builder().character('&').hexCharacter('#').build().deserialize(string);
    }
}
