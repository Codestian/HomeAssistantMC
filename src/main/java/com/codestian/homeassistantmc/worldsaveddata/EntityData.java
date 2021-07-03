package com.codestian.homeassistantmc.worldsaveddata;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Map;
import java.util.logging.Logger;

public class EntityData extends WorldSavedData {

    private static final String NAME = "HassEntityData";
    private final Logger logger = Logger.getLogger("HomeAssistant");
    private final Gson gson = new Gson();
    private JsonArray entities = new JsonArray();

    public EntityData() {
        super(NAME);
    }

    public EntityData(String name) {
        super(name);
    }

    public static EntityData get(World worldIn) {
        if (!(worldIn instanceof ServerWorld)) {
            throw new RuntimeException("Attempted to get data from client world. A server world is required for this argument.");
        }
        ServerWorld world = worldIn.getServer().getLevel(World.OVERWORLD);
        DimensionSavedDataManager storage = world.getDataStorage();
        return storage.computeIfAbsent(EntityData::new, NAME);
    }

    public JsonArray getEntities() {
        return entities;
    }

    public JsonObject getOneEntity(String entity_id) {
        for (JsonElement entity : entities) {
            try {
                if (entity.getAsJsonObject().get("entity_id").getAsString().equals(entity_id)) {
                    return entity.getAsJsonObject();
                }
            } catch (IllegalStateException ignored) {
            }
        }
        return null;
    }

    public void putEntities(JsonArray result) {
        entities = result;
        setDirty();
    }

    public void putOneEntity(JsonObject result) {
        int i = 0;
        for (JsonElement entity : entities) {
            if (entity.getAsJsonObject().get("entity_id").getAsString().equals(result.get("entity_id").getAsString())) {
                Map firstObject = gson.fromJson(gson.toJson(entity), Map.class);
                Map secondObject = gson.fromJson(gson.toJson(result), Map.class);
                firstObject.putAll(secondObject);
                entities.set(i, gson.fromJson(gson.toJson(firstObject), JsonObject.class));
                setDirty();
                break;
            }
            i++;
        }
    }

    @Override
    public void load(CompoundNBT nbt) {
//        for (String key : nbt.getAllKeys()) {
//            if (key.equals("entities")) {
//                CompoundNBT compoundNBT = (CompoundNBT) nbt.get(key);
//                if (compoundNBT != null) {
//                    JsonArray jsonArray = new JsonArray();
//                    for (String entity : compoundNBT.getAllKeys()) {
//                        jsonArray.add(compoundNBT.get(entity).toString());
//                    }
//                    entities = jsonArray;
//                }
//            }
//        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        logger.info("SAVING DATA");
        if (entities.size() > 0) {
            CompoundNBT compoundNBT = new CompoundNBT();

            for (JsonElement entity : entities) {
                JsonObject entityObject = entity.getAsJsonObject();
                try {
                    compoundNBT.put(entityObject.get("entity_id").getAsString(), JsonToNBT.parseTag(entityObject.toString()));
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }
            compound.put("entities", compoundNBT);
        }
        return compound;
    }
}
