package io.obadiah.lb.config;

import com.google.common.collect.Sets;
import io.obadiah.lb.LightBlocks;
import io.obadiah.lb.util.LoadableConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.nio.file.Path;
import java.util.Set;

public class LightConfig extends LoadableConfig<LightConfig> {

    private static final LightConfig DEFAULT_CONFIG = new LightConfig(ChatColor.GRAY + "Light %block_name%", Sets.newHashSet(
      Material.DIRT,
      Material.STONE,
      Material.DIAMOND_BLOCK
    ));

    private String lore;
    private Set<Material> blocks;

    /**
     * Represents a configuration file.
     */
    public LightConfig() {
        super(LightConfig.class);

        this.lore = "";
        this.blocks = Sets.newHashSet();
    }

    /**
     * Represents a configuration file.
     */
    private LightConfig(String lore, Set<Material> blocks) {
        super(LightConfig.class);

        this.lore = lore;
        this.blocks = blocks;
    }

    public String getLore(Material material) {
        return ChatColor.translateAlternateColorCodes('&', this.lore.replace("%block_name&",
          material.name().substring(0, 1).toUpperCase() +
          material.name().substring(1).replace("_", " ").toLowerCase()));
    }

    public Set<Material> getBlocks() {
        return this.blocks;
    }

    @Override
    public Path getPath() {
        return LightBlocks.get().getPath().resolve("blocks.json");
    }

    @Override
    public LightConfig getDefaultConfig() {
        return DEFAULT_CONFIG;
    }
}
