package yd.kingdom.speedRun;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumSet;
import java.util.Set;

public enum FoodCategory {
    FRUIT(PotionEffectType.SPEED, EnumSet.of(
            Material.APPLE,
            Material.GOLDEN_APPLE,
            Material.MELON_SLICE,
            Material.SWEET_BERRIES,
            Material.GLOW_BERRIES,
            Material.CHORUS_FRUIT
    )),

    MEAT(PotionEffectType.REGENERATION, EnumSet.of(
            Material.COOKED_BEEF,
            Material.COOKED_PORKCHOP,
            Material.COOKED_CHICKEN,
            Material.COOKED_MUTTON,
            Material.COOKED_RABBIT,
            Material.ROTTEN_FLESH // 좀비 고기도 육류임 ㅋㅋ
    )),

    CROP(PotionEffectType.SATURATION, EnumSet.of(
            Material.CARROT,
            Material.POTATO,
            Material.BAKED_POTATO,
            Material.BEETROOT,
            Material.DRIED_KELP
    )),

    FISH(PotionEffectType.WATER_BREATHING, EnumSet.of(
            Material.COOKED_COD,
            Material.COOKED_SALMON,
            Material.TROPICAL_FISH,
            Material.PUFFERFISH
    )),

    BAKERY(PotionEffectType.ABSORPTION, EnumSet.of(
            Material.CAKE,
            Material.COOKIE,
            Material.BREAD,
            Material.PUMPKIN_PIE
    )),

    STEW(PotionEffectType.FIRE_RESISTANCE, EnumSet.of(
            Material.MUSHROOM_STEW,
            Material.BEETROOT_SOUP,
            Material.RABBIT_STEW,
            Material.SUSPICIOUS_STEW
    )),

    DRINK(PotionEffectType.NIGHT_VISION, EnumSet.of(
            Material.HONEY_BOTTLE,
            Material.OMINOUS_BOTTLE
    ));

    private final PotionEffectType effect;
    private final Set<Material> items;

    FoodCategory(PotionEffectType effect, Set<Material> items) {
        this.effect = effect;
        this.items = items;
    }

    public PotionEffectType getEffect() {
        return effect;
    }

    public static FoodCategory of(Material mat) {
        for (FoodCategory cat : values()) {
            if (cat.items.contains(mat)) return cat;
        }
        return null;
    }
}
