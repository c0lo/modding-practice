package com.colossus.Bossfight;



import com.colossus.Bossfight.entity.Entity.entity.*;


import com.google.common.collect.Lists;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.*;
import java.util.function.BiFunction;


public class Entities {


    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES,
            modinfo.ModID);

    public static final RegistryObject<EntityType<AstarothEntity>>ASTAROTH = ENTITY_TYPE.register("ghastqueen", () -> createLivingNetherEntity(AstarothEntity::new, EntityClassification.MONSTER, "astaroth", 1F, 2.5F));

    private static <E extends Entity> RegistryObject<EntityType<E>> registerEntityType(String name, EntityType.Builder<E> builder) {
        return ENTITY_TYPE.register(name, () -> builder.build(modinfo.ModID + ":" + name));
    }






    public static final List<Biome> DesertBiomes = Lists.newArrayList(Biomes.DESERT, Biomes.DESERT_HILLS);
    public static final List<Biome> NetherBiomes = Lists.newArrayList(Biomes.NETHER);
    public static final List<Biome> OceanBiomes = Lists.newArrayList(Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.WARM_OCEAN);


    //=========================================================================================================================================================================================================================================
    //=========================================================================================================================================================================================================================================

    //LivingEntity Creation Method
    private static <T extends LivingEntity> EntityType<T> createLivingEntity(EntityType.IFactory<T> factory, EntityClassification entityClassification, String name, float width, float height)
    {
        ResourceLocation location = new ResourceLocation(modinfo.ModID, name);
        EntityType<T> entity = EntityType.Builder.create(factory, entityClassification)
                .size(width, height)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(3)
                .build(location.toString()
                );
        return entity;
    }

    //Entity Creation Method
    private static <T extends Entity> EntityType<T> createEntity(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification entityClassification, String name, float width, float height)
    {
        ResourceLocation location = new ResourceLocation(modinfo.ModID, name);
        EntityType<T> entity = EntityType.Builder.create(factory, entityClassification)
                .size(width, height)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(3)
                .setCustomClientFactory(clientFactory)
                .build(location.toString()
                );
        return entity;
    }

    //Nether Entity Creation Method
    private static <T extends LivingEntity> EntityType<T> createLivingNetherEntity(EntityType.IFactory<T> factory, EntityClassification entityClassification, String name, float width, float height)
    {
        ResourceLocation location = new ResourceLocation(modinfo.ModID, name);
        EntityType<T> entity = EntityType.Builder.create(factory, entityClassification)
                .size(width, height)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(3)
                .immuneToFire()
                .build(location.toString()
                );
        return entity;
    }

    //=========================================================================================================================================================================================================================================
    //=========================================================================================================================================================================================================================================

    @SuppressWarnings("unchecked")
    private static <T extends MobEntity> void registerCustomSpawnEntry(EntityType<T> entity, List<Biome> biomes, int frequency, int minAmount, int maxAmount, EntitySpawnPlacementRegistry.PlacementType placementType, Heightmap.Type heightMapType, @SuppressWarnings("rawtypes") EntitySpawnPlacementRegistry.IPlacementPredicate canSpawnHere)
    {
        registerBiomeSpawnEntry(entity, frequency, minAmount, maxAmount, biomes);
        EntitySpawnPlacementRegistry.register(entity, placementType, heightMapType, canSpawnHere);
    }



    private static void registerBiomeSpawnEntry(EntityType<?> entity, int frequency, int minAmount, int maxAmount, List<Biome> biomes)
    {
        biomes.stream().filter(Objects::nonNull).forEach(biome -> biome.getSpawns(entity.getClassification()).add(new Biome.SpawnListEntry(entity, frequency, minAmount, maxAmount)));
    }


 

}