package yd.kingdom.speedRun;

import org.bukkit.plugin.java.JavaPlugin;
import yd.kingdom.speedRun.commands.ReloadCommand;
import yd.kingdom.speedRun.events.*;

public final class SpeedRun extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("SpeedRun plugin enabled.");

        new TimeAccelerator(this).start();

        getServer().getPluginManager().registerEvents(new BlockBreakHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveHandler(), this);
        getServer().getPluginManager().registerEvents(new CraftingHandler(), this);
        getServer().getPluginManager().registerEvents(new ConsumeHandler(), this);
        getServer().getPluginManager().registerEvents(new ChatHandler(), this);
        getServer().getPluginManager().registerEvents(new DamageHandler(), this);
        getServer().getPluginManager().registerEvents(new ExplosionHandler(), this);
        getServer().getPluginManager().registerEvents(new EntityTargetHandler(), this);
        getServer().getPluginManager().registerEvents(new ToolDurabilityHandler(), this);

        getCommand("srreload").setExecutor(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("SpeedRun plugin disabled.");
    }

    private void registerEvents(org.bukkit.event.Listener... listeners) {
        for (org.bukkit.event.Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
