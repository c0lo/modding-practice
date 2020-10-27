package com.colossus.Bossfight;

import com.colossus.Bossfight.item.Iteminit;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.eventbus.api.IEventBus;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(modinfo.ModID)
public class modinfo
{

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String ModID = "modinfo";
    public modinfo() {
        GeckoLib.initialize();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Entities.ENTITY_TYPE.register(bus);
        bus.addListener(ServerListener::onSetup);
        MinecraftForge.EVENT_BUS.register(this);
        Iteminit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    }
