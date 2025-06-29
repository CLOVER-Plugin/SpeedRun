package yd.kingdom.speedRun.events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


import java.util.Random;

public class ChatHandler implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        // 50% 확률로 채팅 시 TNT 점화생성
        if (e.getMessage().toLowerCase().contains("") && random.nextDouble() < 0.5) {

            World world = e.getPlayer().getWorld();
            Location loc = e.getPlayer().getLocation();

            TNTPrimed tnt = world.spawn(loc, TNTPrimed.class);
            tnt.setFuseTicks(10);   // 생성 후 0.5초 안에 점화

        }

    }

}
