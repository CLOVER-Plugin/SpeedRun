package yd.kingdom.speedRun;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class WaterLavaEffectManager {

    private final SpeedRun plugin;

    public WaterLavaEffectManager(SpeedRun plugin) {
        this.plugin = plugin;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Material feetBlock = player.getLocation().getBlock().getType();

                    double hp = player.getHealth();
                    double maxHP = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();

                    if (feetBlock == Material.WATER || feetBlock == Material.KELP || feetBlock == Material.SEAGRASS) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 0, true, false));

                        if (hp <= 4.0) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 300, 0, true, false));
                        }
                    }

                    if (feetBlock == Material.LAVA || feetBlock == Material.MAGMA_BLOCK) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300, 0, true, false));

                        if (hp <= 4.0) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 0, true, false));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}
