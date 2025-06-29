package yd.kingdom.speedRun.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import yd.kingdom.speedRun.FoodCategory;

import java.util.EnumMap;
import java.util.Map;

public class ConsumeHandler implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Material food = e.getItem().getType();
        FoodCategory cat = FoodCategory.of(food);
        if (cat != null)
            e.getPlayer().addPotionEffect(new PotionEffect(cat.getEffect(), 100, 0));
    }

}
