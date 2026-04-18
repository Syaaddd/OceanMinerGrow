package com.github.Syaaddd.oceanMinerGrow.machines;

import com.github.Syaaddd.oceanMinerGrow.items.OceanMinerItems;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OceanMiner extends SlimefunItem implements EnergyNetComponent {

    private static final int MAX_PLACE_Y = 44;
    private static final int[] OUTPUT_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8};

    private final int energyConsumption;
    private final int energyCapacity;
    private final int itemsPerTick;
    private final int tier;
    private final int tickDelay;
    private final Map<Location, Integer> tickCounters = new HashMap<>();
    private final Random random = new Random();

    public OceanMiner(ItemGroup itemGroup, SlimefunItemStack item,
                      RecipeType recipeType, ItemStack[] recipe,
                      int energyConsumption, int energyCapacity,
                      int itemsPerTick, int tier, int tickDelay) {
        super(itemGroup, item, recipeType, recipe);
        this.energyConsumption = energyConsumption;
        this.energyCapacity = energyCapacity;
        this.itemsPerTick = itemsPerTick;
        this.tier = tier;
        this.tickDelay = tickDelay;
    }

    @Override
    public void preRegister() {
        new BlockMenuPreset(getId(), getItemName()) {
            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(Block block, org.bukkit.entity.Player player) {
                return true;
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) return new int[]{};
                return OUTPUT_SLOTS;
            }
        };

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block placed = e.getBlockPlaced();
                Material replacedType = e.getBlockReplacedState().getType();
                boolean wasWater = replacedType == Material.WATER
                    || replacedType == Material.BUBBLE_COLUMN;
                boolean validY = placed.getY() <= MAX_PLACE_Y;

                if (!wasWater || !validY) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(
                        ChatColor.RED + "[OceanMinerGrow] " +
                        ChatColor.YELLOW + "Ocean Miner harus diletakkan di dalam air pada Y \u2264 44!"
                    );
                }
            }
        });

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem item, Config data) {
                OceanMiner.this.tick(block);
            }
        });
    }

    private void constructMenu(BlockMenuPreset preset) {
        ItemStack background = new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 9; i < 27; i++) {
            preset.addItem(i, background, (p, slot, item, action) -> false);
        }

        String tierLabel;
        ChatColor tierColor;
        switch (tier) {
            case 2: tierLabel = "MkII";  tierColor = ChatColor.GREEN;        break;
            case 3: tierLabel = "MkIII"; tierColor = ChatColor.GOLD;         break;
            case 4: tierLabel = "MkIV";  tierColor = ChatColor.LIGHT_PURPLE; break;
            default: tierLabel = null;   tierColor = ChatColor.AQUA;         break;
        }

        String displayName = tierLabel == null
            ? ChatColor.AQUA + "Ocean Miner"
            : ChatColor.AQUA + "Ocean Miner " + tierColor + tierLabel;

        double cyclesPerSec = 20.0 / tickDelay;
        String rateStr = String.format("%.1f", itemsPerTick * cyclesPerSec) + " item/detik";

        preset.addItem(22, new CustomItemStack(Material.PRISMARINE,
            displayName,
            ChatColor.GRAY + "Mengumpulkan material laut...",
            "",
            ChatColor.YELLOW + "Energy: " + ChatColor.WHITE + energyConsumption + " J/tick",
            ChatColor.YELLOW + "Output: " + ChatColor.WHITE + itemsPerTick + " item / " + tickDelay + " tick (" + rateStr + ")"
        ), (p, slot, item, action) -> false);
    }

    private void tick(Block block) {
        BlockMenu menu = BlockStorage.getInventory(block.getLocation());
        if (menu == null) return;

        // Cooldown: produce only every tickDelay ticks
        Location loc = block.getLocation();
        int count = tickCounters.merge(loc, 1, Integer::sum);
        if (count < tickDelay) return;
        tickCounters.remove(loc);

        if (!hasAdjacentWater(block)) return;

        int stored = getCharge(loc);
        if (stored < energyConsumption) return;
        removeCharge(loc, energyConsumption);

        int y = block.getY();
        for (int i = 0; i < itemsPerTick; i++) {
            ItemStack drop = rollDrop(y, tier);
            if (drop != null) {
                menu.pushItem(drop.clone(), OUTPUT_SLOTS);
            }
        }
    }

    private boolean hasAdjacentWater(Block block) {
        for (BlockFace face : new BlockFace[]{
            BlockFace.NORTH, BlockFace.SOUTH,
            BlockFace.EAST, BlockFace.WEST,
            BlockFace.UP, BlockFace.DOWN
        }) {
            Material m = block.getRelative(face).getType();
            if (m == Material.WATER || m == Material.BUBBLE_COLUMN) return true;
        }
        return false;
    }

    private ItemStack rollDrop(int y, int tier) {
        int roll = random.nextInt(100);

        if (tier == 1) {
            if (y >= 25) {
                if (roll < 45) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 75) return OceanMinerItems.PEARLSTONE.clone();
                return null;
            } else if (y >= 0) {
                if (roll < 45) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 75) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 90) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 98) return OceanMinerItems.TRIDENTITE_SHARD.clone();
                return null;
            } else {
                if (roll < 45) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 75) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 90) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 98) return OceanMinerItems.TRIDENTITE_SHARD.clone();
                return OceanMinerItems.PRESSURE_GEM.clone();
            }
        } else if (tier == 2) {
            if (y >= 25) {
                if (roll < 40) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 70) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 90) return OceanMinerItems.TIDESTONE_FRAGMENT.clone();
                return null;
            } else if (y >= 0) {
                if (roll < 35) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 60) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 78) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 90) return OceanMinerItems.TRIDENTITE_SHARD.clone();
                if (roll < 98) return OceanMinerItems.TIDESTONE_FRAGMENT.clone();
                return null;
            } else {
                if (roll < 35) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 60) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 75) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 85) return OceanMinerItems.TRIDENTITE_SHARD.clone();
                if (roll < 95) return OceanMinerItems.TIDESTONE_FRAGMENT.clone();
                return OceanMinerItems.PRESSURE_GEM.clone();
            }
        } else if (tier == 3) {
            if (y >= 25) {
                if (roll < 35) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 60) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 80) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 95) return OceanMinerItems.TIDESTONE_FRAGMENT.clone();
                return null;
            } else if (y >= 0) {
                if (roll < 30) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 50) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 70) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 85) return OceanMinerItems.TRIDENTITE_SHARD.clone();
                if (roll < 95) return OceanMinerItems.TIDESTONE_FRAGMENT.clone();
                return OceanMinerItems.ABYSSAL_CORE.clone();
            } else {
                if (roll < 25) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 40) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 58) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 70) return OceanMinerItems.TRIDENTITE_SHARD.clone();
                if (roll < 82) return OceanMinerItems.TIDESTONE_FRAGMENT.clone();
                if (roll < 92) return OceanMinerItems.PRESSURE_GEM.clone();
                return OceanMinerItems.ABYSSAL_CORE.clone();
            }
        } else {
            // tier 4
            if (y >= 25) {
                if (roll < 30) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 50) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 70) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 90) return OceanMinerItems.TIDESTONE_FRAGMENT.clone();
                if (roll < 98) return OceanMinerItems.ABYSSAL_CORE.clone();
                return OceanMinerItems.VOID_CRYSTAL.clone();
            } else if (y >= 0) {
                if (roll < 25) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 40) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 58) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 70) return OceanMinerItems.TRIDENTITE_SHARD.clone();
                if (roll < 82) return OceanMinerItems.TIDESTONE_FRAGMENT.clone();
                if (roll < 94) return OceanMinerItems.ABYSSAL_CORE.clone();
                if (roll < 98) return OceanMinerItems.PRESSURE_GEM.clone();
                return OceanMinerItems.VOID_CRYSTAL.clone();
            } else {
                if (roll < 20) return OceanMinerItems.CORAL_DUST.clone();
                if (roll < 30) return OceanMinerItems.PEARLSTONE.clone();
                if (roll < 45) return OceanMinerItems.ABYSSITE.clone();
                if (roll < 55) return OceanMinerItems.TRIDENTITE_SHARD.clone();
                if (roll < 67) return OceanMinerItems.TIDESTONE_FRAGMENT.clone();
                if (roll < 82) return OceanMinerItems.ABYSSAL_CORE.clone();
                if (roll < 90) return OceanMinerItems.PRESSURE_GEM.clone();
                return OceanMinerItems.VOID_CRYSTAL.clone();
            }
        }
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return energyCapacity;
    }
}
