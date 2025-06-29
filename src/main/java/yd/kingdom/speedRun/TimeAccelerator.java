package yd.kingdom.speedRun;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

// 하루 주기 2배 가속
public class TimeAccelerator {

    private final SpeedRun plugin;

    public TimeAccelerator(SpeedRun plugin) {
        this.plugin = plugin;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    long time = world.getTime();
                    world.setTime((time + 2) % 24000);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}