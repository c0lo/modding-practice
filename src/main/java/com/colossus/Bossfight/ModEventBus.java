

package com.colossus.Bossfight;




import com.colossus.Bossfight.entity.Client.*;

import com.colossus.Bossfight.item.items.ModSpawnEggItem;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = modinfo.ModID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBus
{




	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerEntityRenderers(final FMLClientSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(Entities.ASTAROTH.get(), AstarothRenderer::new);
	}
	@SubscribeEvent
	public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event){
		ModSpawnEggItem.initSpawnEggs();
	}

}