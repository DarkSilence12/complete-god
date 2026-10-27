package com.bai.god.feature;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GodStatePersistence extends SavedData {

    private static final String STATE_KEY = "complete_god_state";

    private final Set<UUID> godPlayers = new HashSet<>();

    public static GodStatePersistence get(MinecraftServer server) {
        DimensionDataStorage storage = server.overworld().getDataStorage();
        return storage.computeIfAbsent(GodStatePersistence::fromNbt, GodStatePersistence::new, STATE_KEY);
    }

    public boolean isGod(UUID uuid) {
        return uuid != null && godPlayers.contains(uuid);
    }

    public void setGod(UUID uuid, boolean isGod) {
        if (uuid == null) {
            return;
        }
        boolean changed = isGod ? godPlayers.add(uuid) : godPlayers.remove(uuid);
        if (changed) {
            setDirty();
        }
    }

    public Set<UUID> getGodPlayers() {
        return new HashSet<>(godPlayers);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag list = new ListTag();
        for (UUID uuid : godPlayers) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("uuid", uuid);
            list.add(entry);
        }
        nbt.put("godPlayers", list);
        return nbt;
    }

    public static GodStatePersistence fromNbt(CompoundTag nbt) {
        GodStatePersistence state = new GodStatePersistence();
        if (nbt != null && nbt.contains("godPlayers")) {
            ListTag list = nbt.getList("godPlayers", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                try {
                    state.godPlayers.add(list.getCompound(i).getUUID("uuid"));
                } catch (Exception ignored) {
                }
            }
        }
        return state;
    }
}
