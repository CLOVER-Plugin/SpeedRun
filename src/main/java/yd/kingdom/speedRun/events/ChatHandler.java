package yd.kingdom.speedRun.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import yd.kingdom.speedRun.SpeedRun;


import java.util.Random;

public class ChatHandler implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        //Bukkit.getLogger().info("[ChatHandler] onChat 호출: msg=\"" + e.getMessage() + "\"");

        if (random.nextDouble() < 0.5) {
            Location loc = e.getPlayer().getLocation();
            //Bukkit.getLogger().info("[ChatHandler] sync 스폰 스케줄링");

            // SpeedRun 플러그인의 메인 스레드로 작업 넘기기
            Bukkit.getScheduler().runTask(
                    SpeedRun.getPlugin(SpeedRun.class),
                    () -> {
                        World w = loc.getWorld();
                        if (w != null) {
                            TNTPrimed tnt = w.spawn(loc, TNTPrimed.class);
                            tnt.setFuseTicks(20);
                            //Bukkit.getLogger().info("[ChatHandler] TNT 스폰 완료 at " + loc);
                        }
                    }
            );
        } else {
            //Bukkit.getLogger().info("[ChatHandler] 확률 미충족, TNT 미생성");
        }
    }
}
