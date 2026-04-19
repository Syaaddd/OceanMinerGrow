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

        // Register output materials (obtainable only from the machine)
        new SlimefunItem(group, OceanMinerItems.CORAL_DUST,
            RecipeType.NULL, new ItemStack[9]).register(plugin);

        new SlimefunItem(group, OceanMinerItems.PEARLSTONE,
            RecipeType.NULL, new ItemStack[9]).register(plugin);

        new SlimefunItem(group, OceanMinerItems.ABYSSITE,
            RecipeType.NULL, new ItemStack[9]).register(plugin);

        new SlimefunItem(group, OceanMinerItems.TRIDENTITE_SHARD,
            RecipeType.NULL, new ItemStack[9]).register(plugin);

        new SlimefunItem(group, OceanMinerItems.PRESSURE_GEM,
            RecipeType.NULL, new ItemStack[9]).register(plugin);

        new SlimefunItem(group, OceanMinerItems.TIDESTONE_FRAGMENT,
            RecipeType.NULL, new ItemStack[9]).register(plugin);

        new SlimefunItem(group, OceanMinerItems.ABYSSAL_CORE,
            RecipeType.NULL, new ItemStack[9]).register(plugin);

        new SlimefunItem(group, OceanMinerItems.VOID_CRYSTAL,
            RecipeType.NULL, new ItemStack[9]).register(plugin);

        // Ocean Miner MK1
        // [Nautilus Shell] [Nautilus Shell]    [Nautilus Shell]
        // [Prism Shard]    [Heart of the Sea]  [Prism Shard]
        // [Reinf Alloy]    [Basic Circuit]     [Reinf Alloy]
        ItemStack[] recipeMk1 = new ItemStack[]{
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

        // Ocean Miner MK2 (1000 J/tick, 2 items/tick)
        // [Prismarine Brick] [Prismarine Brick]  [Prismarine Brick]
        // [Tridentite Shard] [Ocean Miner MK1]   [Tridentite Shard]
        // [Reinf Alloy]      [Advanced Circuit]  [Reinf Alloy]
        ItemStack[] recipeMk2 = new ItemStack[]{
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

        // Ocean Miner MK3 (2500 J/tick, 3 items/tick)
        // [Tidestone Frag]  [Tidestone Frag]    [Tidestone Frag]
        // [Pressure Gem]    [Ocean Miner MK2]   [Pressure Gem]
        // [Reinf Alloy]     [Electric Motor]    [Reinf Alloy]
        ItemStack[] recipeMk3 = new ItemStack[]{
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

        // Ocean Miner MK4 (5000 J/tick, 5 items/tick)
        // [Abyssal Core]    [Abyssal Core]      [Abyssal Core]
        // [Void Crystal]    [Ocean Miner MK3]   [Void Crystal]
        // [Reinf Alloy]     [Cargo Motor]       [Reinf Alloy]
        ItemStack[] recipeMk4 = new ItemStack[]{
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
    }
}
