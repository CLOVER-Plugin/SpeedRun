package yd.kingdom.speedRun.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Random;

public class BlockBreakHandler implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Material blockType = block.getType();

        // 1. 일정 확률로 블럭 파괴 취소 10%
        if (random.nextDouble() < 0.10) {
            event.setCancelled(true);
            return;
        }

        // 2. 맨손으로 나무 파괴 시 → 판자 4개 드랍
        if (tool.getType() == Material.AIR && isLog(blockType)) {
            event.setDropItems(false);
            block.setType(Material.AIR);

            Material planks = switch (blockType) {
                case OAK_LOG -> Material.OAK_PLANKS;
                case BIRCH_LOG -> Material.BIRCH_PLANKS;
                case SPRUCE_LOG -> Material.SPRUCE_PLANKS;
                case JUNGLE_LOG -> Material.JUNGLE_PLANKS;
                case ACACIA_LOG -> Material.ACACIA_PLANKS;
                case DARK_OAK_LOG -> Material.DARK_OAK_PLANKS;
                case MANGROVE_LOG -> Material.MANGROVE_PLANKS;
                case CHERRY_LOG -> Material.CHERRY_PLANKS;
                case CRIMSON_STEM -> Material.CRIMSON_PLANKS;
                case WARPED_STEM -> Material.WARPED_PLANKS;
                default -> null;
            };

            if (planks != null) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(planks, 4));
            } else {
                // 매핑 안 된 경우 원목 자체라도 드랍
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(blockType, 1));
            }
            return;
        }

        // 3. 광물 맨손 파괴 시 → 정상 드랍 유지
        if (tool.getType() == Material.AIR && isOre(blockType)) {
            event.setDropItems(false);

            ItemStack netheritePickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
            Collection<ItemStack> drops = block.getDrops(netheritePickaxe);

            block.setType(Material.AIR);

            for (ItemStack item : drops) {
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }

            return;
        }

        // 4. 기타 블럭 → 일정 확률로 5배 드랍
        if (random.nextDouble() < 0.10) { // 10%
            event.setDropItems(false);
            block.setType(Material.AIR);
            ItemStack drop = new ItemStack(blockType, 5);
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
        }
    }

    private boolean isLog(Material type) {
        return type.name().endsWith("_LOG") || type.name().endsWith("_STEM");
    }

    private boolean isOre(Material type) {
        return type.name().endsWith("_ORE") ||
                type == Material.RAW_IRON_BLOCK || type == Material.RAW_COPPER_BLOCK ||
                type == Material.RAW_GOLD_BLOCK;
    }

}