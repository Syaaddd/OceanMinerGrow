package com.github.Syaaddd.oceanMinerGrow.items;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public final class OceanMinerItems {

    public static final SlimefunItemStack OCEAN_MINER = new SlimefunItemStack(
        "OCEAN_MINER",
        Material.PRISMARINE,
        ChatColor.AQUA + "Ocean Miner",
        "",
        ChatColor.GRAY + "Place underwater (Y < 45)",
        ChatColor.GRAY + "Automatically mines ocean materials",
        "",
        ChatColor.YELLOW + "Energy: " + ChatColor.WHITE + "500 J/tick"
    );

    public static final SlimefunItemStack OCEAN_MINER_MK2 = new SlimefunItemStack(
        "OCEAN_MINER_MK2",
        Material.PRISMARINE_BRICKS,
        ChatColor.AQUA + "Ocean Miner " + ChatColor.GREEN + "MkII",
        "",
        ChatColor.GRAY + "Place underwater (Y < 45)",
        ChatColor.GRAY + "Mines 2 materials per tick",
        "",
        ChatColor.YELLOW + "Energy: " + ChatColor.WHITE + "2.000 J/tick"
    );

    public static final SlimefunItemStack OCEAN_MINER_MK3 = new SlimefunItemStack(
        "OCEAN_MINER_MK3",
        Material.DARK_PRISMARINE,
        ChatColor.AQUA + "Ocean Miner " + ChatColor.GOLD + "MkIII",
        "",
        ChatColor.GRAY + "Place underwater (Y < 45)",
        ChatColor.GRAY + "Mines 3 materials per tick",
        "",
        ChatColor.YELLOW + "Energy: " + ChatColor.WHITE + "8.000 J/tick"
    );

    public static final SlimefunItemStack OCEAN_MINER_MK4 = new SlimefunItemStack(
        "OCEAN_MINER_MK4",
        Material.CRYING_OBSIDIAN,
        ChatColor.AQUA + "Ocean Miner " + ChatColor.LIGHT_PURPLE + "MkIV",
        "",
        ChatColor.GRAY + "Place underwater (Y < 45)",
        ChatColor.GRAY + "Mines 5 materials per tick",
        "",
        ChatColor.YELLOW + "Energy: " + ChatColor.WHITE + "32.000 J/tick"
    );

    public static final SlimefunItemStack CORAL_DUST = new SlimefunItemStack(
        "OCEAN_MINER_CORAL_DUST",
        Material.SUGAR,
        ChatColor.WHITE + "Coral Dust",
        "",
        ChatColor.GRAY + "A fine powder from ancient coral.",
        ChatColor.GREEN + "Common"
    );

    public static final SlimefunItemStack PEARLSTONE = new SlimefunItemStack(
        "OCEAN_MINER_PEARLSTONE",
        Material.QUARTZ,
        ChatColor.WHITE + "Pearlstone",
        "",
        ChatColor.GRAY + "A smooth stone with a pearlescent sheen.",
        ChatColor.GREEN + "Common"
    );

    public static final SlimefunItemStack ABYSSITE = new SlimefunItemStack(
        "OCEAN_MINER_ABYSSITE",
        Material.AMETHYST_SHARD,
        ChatColor.DARK_PURPLE + "Abyssite",
        "",
        ChatColor.GRAY + "A crystal formed in the deep abyss.",
        ChatColor.YELLOW + "Uncommon"
    );

    public static final SlimefunItemStack TRIDENTITE_SHARD = new SlimefunItemStack(
        "OCEAN_MINER_TRIDENTITE_SHARD",
        Material.PRISMARINE_SHARD,
        ChatColor.DARK_AQUA + "Tridentite Shard",
        "",
        ChatColor.GRAY + "Fragments of an ancient sea weapon.",
        ChatColor.YELLOW + "Uncommon"
    );

    public static final SlimefunItemStack PRESSURE_GEM = new SlimefunItemStack(
        "OCEAN_MINER_PRESSURE_GEM",
        Material.EMERALD,
        ChatColor.DARK_GREEN + "Pressure Gem",
        "",
        ChatColor.GRAY + "Crystallized by immense ocean pressure.",
        ChatColor.RED + "Rare"
    );

    public static final SlimefunItemStack TIDESTONE_FRAGMENT = new SlimefunItemStack(
        "OCEAN_MINER_TIDESTONE_FRAG",
        Material.PRISMARINE_CRYSTALS,
        ChatColor.AQUA + "Tidestone Fragment",
        "",
        ChatColor.GRAY + "A fragment resonating with tidal energy.",
        ChatColor.RED + "Rare"
    );

    public static final SlimefunItemStack ABYSSAL_CORE = new SlimefunItemStack(
        "OCEAN_MINER_ABYSSAL_CORE",
        Material.ECHO_SHARD,
        ChatColor.DARK_PURPLE + "Abyssal Core",
        "",
        ChatColor.GRAY + "The pulsing heart of the deep abyss.",
        ChatColor.DARK_RED + "Epic"
    );

    public static final SlimefunItemStack VOID_CRYSTAL = new SlimefunItemStack(
        "OCEAN_MINER_VOID_CRYSTAL",
        Material.AMETHYST_SHARD,
        ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Void Crystal",
        "",
        ChatColor.GRAY + "A crystal born from the void beneath the ocean.",
        ChatColor.GOLD + "Legendary"
    );

    private OceanMinerItems() {}
}
