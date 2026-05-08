package com.github.Syaaddd.oceanMinerGrow.listeners;

import com.github.Syaaddd.oceanMinerGrow.machines.OceanMiner;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class OceanMinerPlacementListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        ItemStack hand = e.getItemInHand();
        if (!(SlimefunItem.getByItem(hand) instanceof OceanMiner)) return;

        Block placed = e.getBlockPlaced();
        Material replaced = e.getBlockReplacedState().getType();
        boolean wasWater = replaced == Material.WATER || replaced == Material.BUBBLE_COLUMN;
        boolean validY = placed.getY() <= OceanMiner.MAX_PLACE_Y;

        if (!wasWater || !validY) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(
                ChatColor.RED + "[OceanMinerGrow] " +
                ChatColor.YELLOW + "Ocean Miner harus diletakkan di dalam air pada Y ≤ 44!"
            );
            e.getPlayer().sendBlockChange(placed.getLocation(), e.getBlockReplacedState().getBlockData());
        }
    }
}
