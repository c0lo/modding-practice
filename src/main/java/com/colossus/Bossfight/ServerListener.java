package com.colossus.Bossfight;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
@Mod.EventBusSubscriber(modid = modinfo.ModID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerListener
{

    public static void onSetup(FMLCommonSetupEvent event)
    {
        for(RegistryObject<EntityType<?>> entity : Entities.ENTITY_TYPE.getEntries())
        {
        }
    }

    }