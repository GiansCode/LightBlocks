package io.obadiah.lb.config;

import com.google.common.collect.Sets;
import io.obadiah.lb.LightBlocks;
import io.obadiah.lb.util.LoadableConfig;
import org.bukkit.Material;

import java.nio.file.Path;
import java.util.Set;

public class LightConfig extends LoadableConfig<LightConfig> {

    private static final LightConfig DEFAULT_CONFIG = new LightConfig(Sets.newHashSet(
      Material.DIRT,
      Material.STONE,
      Material.DIAMOND_BLOCK
    ));

    private Set<Material> blocks;

    /**
     * Represents a configuration file.
     */
    public LightConfig() {
        super(LightConfig.class);

        this.blocks = Sets.newHashSet();
    }

    /**
     * Represents a configuration file.
     */
    private LightConfig(Set<Material> blocks) {
        super(LightConfig.class);

        this.blocks = blocks;
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
