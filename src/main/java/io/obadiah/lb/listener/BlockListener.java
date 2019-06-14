package io.obadiah.lb.listener;

import de.tr7zw.itemnbtapi.NBTItem;
import io.obadiah.lb.LightBlocks;
import io.obadiah.lb.util.Listener;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import ru.beykerykt.lightapi.LightAPI;

public class BlockListener implements Listener {

    public BlockListener() {
        this.startListening();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        Integer lightLevel = new NBTItem(item).getInteger("HeldLightLevel");

        if (lightLevel == null) {
            return;
        }

        if (!LightBlocks.get().getLightConfig().getBlocks().contains(item.getType())) {
            return;
        }

        int fLight = (int) (lightLevel * 1.875);
        if (fLight > 15) {
            fLight = 15;
        }

        LightAPI.createLight(block.getLocation(), fLight, false);
        Bukkit.getScheduler().runTaskLater(LightBlocks.get(), () -> {
            player.getLocation().getWorld().refreshChunk(player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        }, 1L);
    }
}
