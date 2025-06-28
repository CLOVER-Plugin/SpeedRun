package yd.kingdom.speedRun.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import yd.kingdom.speedRun.SpeedRun;

public class ReloadCommand implements CommandExecutor {

    private final SpeedRun plugin;
    public ReloadCommand(SpeedRun plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("speedrun.reload")) {
            sender.sendMessage("§c권한이 없습니다.");
            return true;
        }
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "SpeedRun config reloaded.");
        return true;
    }
}
