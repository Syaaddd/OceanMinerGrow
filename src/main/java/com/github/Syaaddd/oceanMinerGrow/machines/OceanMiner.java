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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class OceanMiner extends SlimefunItem implements EnergyNetComponent {

    private static final int MAX_PLACE_Y = 44;

    /*
     * Satu static map untuk semua tier — menghindari 4 map terpisah.
     * Key: koordinat blok dikodekan jadi long (tanpa alokasi Location).
     * Value: world tick berikutnya ketika mesin boleh produksi.
     * Semua tick berjalan di main thread (isSynchronized=true) → tidak perlu sync.
     */
    private static final Map<Long, Long> NEXT_TICK_MAP = new HashMap<>(512);

    private final int energyConsumption;
    private final int energyCapacity;
    private final int itemsPerTick;
    private final int tier;
    private final int tickDelay;
    private final int[] outputSlots;

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
        this.outputSlots = buildOutputSlots(tier);
    }

    // Encode koordinat blok ke long — menghindari alokasi Location di hot path
    private static long blockKey(Block block) {
        // X: 26 bit, Z: 26 bit, Y: 12 bit — cukup untuk semua koordinat Minecraft
        return (((long)(block.getX() & 0x3FFFFFF)) << 38)
             | (((long)(block.getZ() & 0x3FFFFFF)) << 12)
             | ((long)(block.getY() & 0xFFF));
    }

    private static int[] buildOutputSlots(int tier) {
        // Inventory 54 slot (6 baris). Baris 6 (slot 45-53) selalu dekorasi+info.
        // Output hanya di baris 1-5 (slot 0-44), sesuai tier.
        int count;
        if (tier == 1)      count = 9;   // baris 1
        else if (tier == 2) count = 18;  // baris 1-2
        else if (tier == 3) count = 36;  // baris 1-4
        else                count = 45;  // baris 1-5

        int[] slots = new int[count];
        for (int i = 0; i < count; i++) slots[i] = i;
        return slots;
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
                return outputSlots;
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
                    // Paksa client revert ghost block ke kondisi asli
                    e.getPlayer().sendBlockChange(
                        placed.getLocation(),
                        e.getBlockReplacedState().getBlockData()
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

        Set<Integer> outputSet = new HashSet<>();
        for (int s : outputSlots) outputSet.add(s);

        for (int i = 0; i < 54; i++) {
            if (!outputSet.contains(i)) {
                preset.addItem(i, background, (p, slot, it, action) -> false);
            }
        }

        // Baris terakhir (slot 45-53): label + tampilan item yang bisa dihasilkan
        preset.addItem(45,
            new CustomItemStack(Material.BOOK, ChatColor.YELLOW + "Possible Drops:"),
            (p, slot, it, action) -> false);

        ItemStack[] drops = getPossibleDrops(tier);
        for (int i = 0; i < drops.length && i < 8; i++) {
            preset.addItem(46 + i, drops[i], (p, slot, it, action) -> false);
        }
    }

    private static ItemStack[] getPossibleDrops(int tier) {
        if (tier == 1) return new ItemStack[]{
            OceanMinerItems.CORAL_DUST, OceanMinerItems.PEARLSTONE,
            OceanMinerItems.ABYSSITE, OceanMinerItems.TRIDENTITE_SHARD,
            OceanMinerItems.PRESSURE_GEM
        };
        if (tier == 2) return new ItemStack[]{
            OceanMinerItems.CORAL_DUST, OceanMinerItems.PEARLSTONE,
            OceanMinerItems.ABYSSITE, OceanMinerItems.TRIDENTITE_SHARD,
            OceanMinerItems.TIDESTONE_FRAGMENT, OceanMinerItems.PRESSURE_GEM
        };
        if (tier == 3) return new ItemStack[]{
            OceanMinerItems.CORAL_DUST, OceanMinerItems.PEARLSTONE,
            OceanMinerItems.ABYSSITE, OceanMinerItems.TRIDENTITE_SHARD,
            OceanMinerItems.TIDESTONE_FRAGMENT, OceanMinerItems.PRESSURE_GEM,
            OceanMinerItems.ABYSSAL_CORE
        };
        return new ItemStack[]{
            OceanMinerItems.CORAL_DUST, OceanMinerItems.PEARLSTONE,
            OceanMinerItems.ABYSSITE, OceanMinerItems.TRIDENTITE_SHARD,
            OceanMinerItems.TIDESTONE_FRAGMENT, OceanMinerItems.PRESSURE_GEM,
            OceanMinerItems.ABYSSAL_CORE, OceanMinerItems.VOID_CRYSTAL
        };
    }

    private void tick(Block block) {
        Location loc = block.getLocation();

        BlockMenu menu = BlockStorage.getInventory(loc);
        if (menu == null) return;

        long key = blockKey(block);
        long now = block.getWorld().getFullTime();
        Long scheduled = NEXT_TICK_MAP.get(key);
        if (scheduled != null && now < scheduled) return;

        // Semua kondisi dicek SEBELUM set cooldown — mesin retry tiap Slimefun tick
        // jika energi/slot/air belum tersedia, bukan menunggu satu siklus penuh lagi.
        if (!hasAdjacentWater(block)) return;
        if (!hasOutputSpace(menu)) return;

        int stored = getCharge(loc);
        if (stored < energyConsumption) return;
        removeCharge(loc, energyConsumption);

        // Cooldown dimulai hanya setelah berhasil produksi
        NEXT_TICK_MAP.put(key, now + tickDelay);

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int y = block.getY();
        for (int i = 0; i < itemsPerTick; i++) {
            ItemStack drop = rollDrop(y, rng);
            if (drop != null) {
                menu.pushItem(drop, outputSlots);
            }
        }
    }

    private boolean hasOutputSpace(BlockMenu menu) {
        for (int slot : outputSlots) {
            ItemStack existing = menu.getItemInSlot(slot);
            if (existing == null || existing.getType() == Material.AIR) return true;
            if (existing.getAmount() < existing.getMaxStackSize()) return true;
        }
        return false;
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

    private ItemStack rollDrop(int y, ThreadLocalRandom rng) {
        int roll = rng.nextInt(100);

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
