package com.github.Syaaddd.oceanMinerGrow.setup;

import com.github.Syaaddd.oceanMinerGrow.OceanMinerGrow;
import com.github.Syaaddd.oceanMinerGrow.items.OceanMinerItems;
import com.github.Syaaddd.oceanMinerGrow.machines.OceanMiner;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class ItemSetup {

    private final OceanMinerGrow plugin;

    public ItemSetup(OceanMinerGrow plugin) {
        this.plugin = plugin;
    }

    public void register() {
        NamespacedKey groupKey = new NamespacedKey(plugin, "ocean_miner_group");
        ItemGroup group = new ItemGroup(groupKey,
            new CustomItemStack(Material.PRISMARINE, ChatColor.AQUA + "Ocean Miner Grow")
        );

        // Custom RecipeType: shows each drop "di bawah" (below the machine's crafting recipe)
        // The display item is OceanMiner so the guide links drops back to the machine
        RecipeType oceanMinerDrop = new RecipeType(
            new NamespacedKey(plugin, "ocean_miner_drop"),
            OceanMinerItems.OCEAN_MINER
        );

        // ── Machines – crafting recipe tetap Enhanced Crafting Table ──────────

        // MK1: [Nautilus x3] / [Prism Shard, Heart of Sea, Prism Shard] / [Reinf Alloy, Basic Circuit, Reinf Alloy]
        ItemStack[] recipeMk1 = {
            new ItemStack(Material.NAUTILUS_SHELL),
            new ItemStack(Material.NAUTILUS_SHELL),
            new ItemStack(Material.NAUTILUS_SHELL),
            new ItemStack(Material.PRISMARINE_SHARD),
            new ItemStack(Material.HEART_OF_THE_SEA),
            new ItemStack(Material.PRISMARINE_SHARD),
            SlimefunItems.REINFORCED_ALLOY_INGOT,
            SlimefunItems.BASIC_CIRCUIT_BOARD,
            SlimefunItems.REINFORCED_ALLOY_INGOT
        };
        new OceanMiner(group, OceanMinerItems.OCEAN_MINER,
            RecipeType.ENHANCED_CRAFTING_TABLE, recipeMk1,
            500, 5000, 1, 1, 40).register(plugin);

        // MK2: [Prismarine Brick x3] / [Tridentite, MK1, Tridentite] / [Reinf Alloy, Advanced Circuit, Reinf Alloy]
        ItemStack[] recipeMk2 = {
            new ItemStack(Material.PRISMARINE_BRICKS),
            new ItemStack(Material.PRISMARINE_BRICKS),
            new ItemStack(Material.PRISMARINE_BRICKS),
            OceanMinerItems.TRIDENTITE_SHARD,
            OceanMinerItems.OCEAN_MINER,
            OceanMinerItems.TRIDENTITE_SHARD,
            SlimefunItems.REINFORCED_ALLOY_INGOT,
            SlimefunItems.ADVANCED_CIRCUIT_BOARD,
            SlimefunItems.REINFORCED_ALLOY_INGOT
        };
        new OceanMiner(group, OceanMinerItems.OCEAN_MINER_MK2,
            RecipeType.ENHANCED_CRAFTING_TABLE, recipeMk2,
            2000, 20000, 2, 2, 30).register(plugin);

        // MK3: [Tidestone x3] / [Pressure Gem, MK2, Pressure Gem] / [Reinf Alloy, Electric Motor, Reinf Alloy]
        ItemStack[] recipeMk3 = {
            OceanMinerItems.TIDESTONE_FRAGMENT,
            OceanMinerItems.TIDESTONE_FRAGMENT,
            OceanMinerItems.TIDESTONE_FRAGMENT,
            OceanMinerItems.PRESSURE_GEM,
            OceanMinerItems.OCEAN_MINER_MK2,
            OceanMinerItems.PRESSURE_GEM,
            SlimefunItems.REINFORCED_ALLOY_INGOT,
            SlimefunItems.ELECTRIC_MOTOR,
            SlimefunItems.REINFORCED_ALLOY_INGOT
        };
        new OceanMiner(group, OceanMinerItems.OCEAN_MINER_MK3,
            RecipeType.ENHANCED_CRAFTING_TABLE, recipeMk3,
            8000, 80000, 3, 3, 20).register(plugin);

        // MK4: [Abyssal Core x3] / [Void Crystal, MK3, Void Crystal] / [Reinf Alloy, Cargo Motor, Reinf Alloy]
        ItemStack[] recipeMk4 = {
            OceanMinerItems.ABYSSAL_CORE,
            OceanMinerItems.ABYSSAL_CORE,
            OceanMinerItems.ABYSSAL_CORE,
            OceanMinerItems.VOID_CRYSTAL,
            OceanMinerItems.OCEAN_MINER_MK3,
            OceanMinerItems.VOID_CRYSTAL,
            SlimefunItems.REINFORCED_ALLOY_INGOT,
            SlimefunItems.CARGO_MOTOR,
            SlimefunItems.REINFORCED_ALLOY_INGOT
        };
        new OceanMiner(group, OceanMinerItems.OCEAN_MINER_MK4,
            RecipeType.ENHANCED_CRAFTING_TABLE, recipeMk4,
            32000, 320000, 5, 4, 15).register(plugin);

        // ── Output materials – recipe menampilkan mesin yang menghasilkannya ───
        // Layout 4-mesin: [MK1][ ][MK2] / [MK3][ ][MK4]   → slots 0,2,3,5
        // Layout 3-mesin: [MK2][ ][MK3] / [MK4][ ][ ]     → slots 0,2,3
        // Layout 2-mesin: [MK3][ ][MK4]                    → slots 0,2
        // Layout 1-mesin: [ ][ ][ ] / [ ][MK4][ ]          → slot 4 (center)

        ItemStack[] fromAllTiers = {
            OceanMinerItems.OCEAN_MINER,     null, OceanMinerItems.OCEAN_MINER_MK2,
            OceanMinerItems.OCEAN_MINER_MK3, null, OceanMinerItems.OCEAN_MINER_MK4,
            null, null, null
        };

        new SlimefunItem(group, OceanMinerItems.CORAL_DUST,
            oceanMinerDrop, fromAllTiers.clone()).register(plugin);

        new SlimefunItem(group, OceanMinerItems.PEARLSTONE,
            oceanMinerDrop, fromAllTiers.clone()).register(plugin);

        new SlimefunItem(group, OceanMinerItems.ABYSSITE,
            oceanMinerDrop, fromAllTiers.clone()).register(plugin);

        new SlimefunItem(group, OceanMinerItems.TRIDENTITE_SHARD,
            oceanMinerDrop, fromAllTiers.clone()).register(plugin);

        new SlimefunItem(group, OceanMinerItems.PRESSURE_GEM,
            oceanMinerDrop, fromAllTiers.clone()).register(plugin);

        // TidestoneFragment: MK2, MK3, MK4
        new SlimefunItem(group, OceanMinerItems.TIDESTONE_FRAGMENT,
            oceanMinerDrop, new ItemStack[]{
                OceanMinerItems.OCEAN_MINER_MK2, null, OceanMinerItems.OCEAN_MINER_MK3,
                OceanMinerItems.OCEAN_MINER_MK4, null, null,
                null, null, null
            }).register(plugin);

        // AbyssalCore: MK3, MK4
        new SlimefunItem(group, OceanMinerItems.ABYSSAL_CORE,
            oceanMinerDrop, new ItemStack[]{
                OceanMinerItems.OCEAN_MINER_MK3, null, OceanMinerItems.OCEAN_MINER_MK4,
                null, null, null,
                null, null, null
            }).register(plugin);

        // VoidCrystal: MK4 saja
        new SlimefunItem(group, OceanMinerItems.VOID_CRYSTAL,
            oceanMinerDrop, new ItemStack[]{
                null, null, null,
                null, OceanMinerItems.OCEAN_MINER_MK4, null,
                null, null, null
            }).register(plugin);
    }
}
