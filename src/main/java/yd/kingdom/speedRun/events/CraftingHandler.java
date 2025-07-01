package yd.kingdom.speedRun.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import yd.kingdom.speedRun.SpeedRun;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CraftingHandler implements Listener {
    private final Random random = new Random();
    private static final Map<Material, Material> upgradeMap = new HashMap<>();
    private final SpeedRun plugin;

    public CraftingHandler(SpeedRun plugin) {
        this.plugin = plugin;
    }

    static {
        String[] toolTypes = {"PICKAXE", "AXE", "SHOVEL", "HOE", "SWORD"};
        String[] armorTypes = {"HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS"};
        String[] tiers = {"WOODEN", "STONE", "GOLDEN", "IRON", "DIAMOND", "NETHERITE"};

        // 도구 업그레이드: WOODEN → ... → NETHERITE
        for (int i = 0; i < tiers.length - 1; i++) {
            for (String type : toolTypes) {
                Material from = Material.getMaterial(tiers[i] + "_" + type);
                Material to = Material.getMaterial(tiers[i + 1] + "_" + type);
                if (from != null && to != null) {
                    upgradeMap.put(from, to);
                }
            }
        }

        // 방어구 업그레이드: GOLDEN → ... → NETHERITE
        String[] armorTiers = {"GOLDEN", "IRON", "DIAMOND", "NETHERITE"};
        for (int i = 0; i < armorTiers.length - 1; i++) {
            for (String type : armorTypes) {
                Material from = Material.getMaterial(armorTiers[i] + "_" + type);
                Material to = Material.getMaterial(armorTiers[i + 1] + "_" + type);
                if (from != null && to != null) {
                    upgradeMap.put(from, to);
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.isCancelled()) return;

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory() instanceof CraftingInventory inventory)) return;

        ItemStack result = event.getRecipe().getResult();
        Material originalType = result.getType();

        boolean isShiftClick = (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT);

        // Shift 클릭 → 수동 처리 (복수 확률 적용)
        if (isShiftClick) {
            int craftableTimes = getCraftAmount(inventory);
            if (craftableTimes <= 0) return;

            event.setCancelled(true);
            inventory.setResult(null);

            int failed = 0, greatSuccess = 0, upgraded = 0, normal = 0;

            for (int i = 0; i < craftableTimes; i++) {
                double rand = Math.random();

                if (rand < 0.10) {
                    failed++;
                    continue;
                }

                if (rand < 0.15) {
                    ItemStack success = result.clone();
                    success.setAmount(result.getAmount() * 3);
                    giveOrDrop(player, success);
                    greatSuccess++;
                    continue;
                }

                if (rand < 0.25 && upgradeMap.containsKey(originalType)) {
                    Material upgradedMat = upgradeMap.get(originalType);
                    ItemStack upgradedItem = new ItemStack(upgradedMat, result.getAmount());
                    giveOrDrop(player, upgradedItem);
                    upgraded++;
                    continue;
                }

                ItemStack normalItem = result.clone();
                giveOrDrop(player, normalItem);
                normal++;
            }

            consumeIngredients(inventory, craftableTimes);

//            player.sendMessage("§7[제작 결과] §c실패: " + failed + " §a대성공: " + greatSuccess + " §6업그레이드: " + upgraded + " §f일반: " + normal);
            return;
        }

        // 단일 제작 → 후처리 방식으로 확률 적용
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ItemStack cursor = player.getItemOnCursor();
            if (cursor == null || cursor.getType() == Material.AIR) return;
            if (!cursor.isSimilar(result)) return;

            int resultAmount = result.getAmount();
            int cursorAmount = cursor.getAmount();
            double rand = Math.random();

            // 1. 실패 (10%)
            if (rand < 0.10) {
                if (cursorAmount <= resultAmount) {
                    player.setItemOnCursor(null);
                } else {
                    cursor.setAmount(cursorAmount - resultAmount);
                    player.setItemOnCursor(cursor);
                }
//                player.sendMessage("§c제작 실패! 결과 아이템이 사라졌습니다.");
                return;
            }

            // 2. 대성공 (5%)
            if (rand < 0.15) {
                int newAmount = Math.min(cursorAmount - resultAmount + resultAmount * 3, 64);
                cursor.setAmount(newAmount);
                player.setItemOnCursor(cursor);
//                player.sendMessage("§a제작 대성공! x5");
                return;
            }

            // 3. 업그레이드 (5%)
            if (rand < 0.20 && upgradeMap.containsKey(originalType)) {
                Material upgraded = upgradeMap.get(originalType);
                ItemStack upgradedItem = new ItemStack(upgraded, resultAmount);
                if (cursorAmount <= resultAmount) {
                    player.setItemOnCursor(upgradedItem);
                } else {
                    cursor.setAmount(cursorAmount - resultAmount);
                    player.setItemOnCursor(cursor);
                    player.getInventory().addItem(upgradedItem);
                }
//                player.sendMessage("§6업그레이드 성공! → " + upgraded.name());
            }
            // 일반 성공은 그대로 유지
        }, 1L);
    }


    private void giveOrDrop(Player player, ItemStack item) {
        int amount = item.getAmount();
        item.setAmount(1);
        while (amount > 0) {
            int give = Math.min(64, amount);
            ItemStack stack = item.clone();
            stack.setAmount(give);
            Map<Integer, ItemStack> leftover = player.getInventory().addItem(stack);
            for (ItemStack drop : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
            amount -= give;
        }
    }

    private int getCraftAmount(CraftingInventory inventory) {
        int amount = Integer.MAX_VALUE;
        for (ItemStack item : inventory.getMatrix()) {
            if (item != null) {
                amount = Math.min(amount, item.getAmount());
            }
        }
        return amount == Integer.MAX_VALUE ? 0 : amount;
    }

    private void consumeIngredients(CraftingInventory inventory, int times) {
        ItemStack[] contents = inventory.getMatrix();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null) {
                int newAmount = item.getAmount() - times;
                contents[i] = newAmount > 0 ? new ItemStack(item.getType(), newAmount) : null;
            }
        }
        inventory.setMatrix(contents);
    }
}
