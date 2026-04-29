package com.github.Syaaddd.oceanMinerGrow.setup;

import com.github.Syaaddd.oceanMinerGrow.OceanMinerGrow;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import org.bukkit.NamespacedKey;

public class ResearchSetup {

    // IDs in 9001-9099 range to avoid conflicts with base Slimefun and other addons
    private static final int BASE_ID = 9001;

    public static void register(OceanMinerGrow plugin) {
        // MK1 + Common drops: Coral Dust, Pearlstone — 100 XP
        research(plugin, "ocean_miner_mk1", BASE_ID, "Ocean Miner MK1", 100,
            "OCEAN_MINER",
            "OCEAN_MINER_CORAL_DUST",
            "OCEAN_MINER_PEARLSTONE");

        // MK2 + Uncommon drops: Abyssite, Tridentite Shard — 150 XP
        research(plugin, "ocean_miner_mk2", BASE_ID + 1, "Ocean Miner MkII", 150,
            "OCEAN_MINER_MK2",
            "OCEAN_MINER_ABYSSITE",
            "OCEAN_MINER_TRIDENTITE_SHARD");

        // MK3 + Rare drops: Pressure Gem, Tidestone Fragment — 200 XP
        research(plugin, "ocean_miner_mk3", BASE_ID + 2, "Ocean Miner MkIII", 200,
            "OCEAN_MINER_MK3",
            "OCEAN_MINER_PRESSURE_GEM",
            "OCEAN_MINER_TIDESTONE_FRAG");

        // MK4 + Epic/Legendary drops: Abyssal Core, Void Crystal — 250 XP
        research(plugin, "ocean_miner_mk4", BASE_ID + 3, "Ocean Miner MkIV", 250,
            "OCEAN_MINER_MK4",
            "OCEAN_MINER_ABYSSAL_CORE",
            "OCEAN_MINER_VOID_CRYSTAL");
    }

    private static void research(OceanMinerGrow plugin, String key, int id,
                                  String name, int xpCost, String... itemIds) {
        Research research = new Research(new NamespacedKey(plugin, key), id, name, xpCost);
        for (String itemId : itemIds) {
            SlimefunItem item = SlimefunItem.getById(itemId);
            if (item != null) {
                research.addItems(item);
            }
        }
        research.register();
    }
}
