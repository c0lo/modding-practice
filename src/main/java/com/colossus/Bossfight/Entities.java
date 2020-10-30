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

    public static final RegistryObject<EntityType<AstarothEntity>>ASTAROTH = ENTITY_TYPE.register("wardenofsorrow", () -> createLivingNetherEntity(AstarothEntity::new, EntityClassification.MONSTER, "wardenofsorrow", 1F, 2.5F));
    public static final RegistryObject<EntityType<StatueEntity>>STATUE = ENTITY_TYPE.register("statue", () -> createLivingNetherEntity(StatueEntity::new, EntityClassification.MONSTER, "statue", 1F, 1F));
    private static <E extends Entity> RegistryObject<EntityType<E>> registerEntityType(String name, EntityType.Builder<E> builder) {
        return ENTITY_TYPE.register(name, () -> builder.build(modinfo.ModID + ":" + name));
    }









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

}