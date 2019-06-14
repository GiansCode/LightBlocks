package io.obadiah.lb;

import de.tr7zw.itemnbtapi.NBTItem;
import io.obadiah.lb.config.LightConfig;
import io.obadiah.lb.listener.BlockListener;
import io.obadiah.lb.util.ItemBuilder;
import io.obadiah.lb.util.LoadableConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LightBlocks extends JavaPlugin {

    private static Path path;
    private static LightConfig config;

    @Override
    public void onEnable() {
        path = Paths.get(this.getDataFolder().toURI());
        config = (LightConfig) LoadableConfig.getByClass(LightConfig.class).load();

        new BlockListener();
        this.registerRecipe();
    }

    private void registerRecipe() {
        config.getBlocks().forEach(material -> {
            for (int i = 1; i < 9; i++) {
                ItemStack stack = new ItemBuilder(material)
                  .setLore(config.getLore(material) + " " + (int) (i * 1.875))
                  .setAmount(1)
                  .build();

                NBTItem item = new NBTItem(stack);
                item.setInteger("HeldLightLevel", i);

                ShapelessRecipe recipe = new ShapelessRecipe(item.getItem());
                recipe.addIngredient(material);
                recipe.addIngredient(i, Material.GLOWSTONE_DUST);

                Bukkit.addRecipe(recipe);
            }
        });
    }

    public Path getPath() {
        return path;
    }

    public LightConfig getLightConfig() {
        return config;
    }

    public static LightBlocks get() {
        return JavaPlugin.getPlugin(LightBlocks.class);
    }
}
