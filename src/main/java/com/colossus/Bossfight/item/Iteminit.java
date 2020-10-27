package com.colossus.Bossfight.item;

import com.colossus.Bossfight.Entities;
import com.colossus.Bossfight.item.items.ModSpawnEggItem;
import com.colossus.Bossfight.modinfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Iteminit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            modinfo.ModID);

    public static final RegistryObject<ModSpawnEggItem> ASTAROTH_EGG = ITEMS.register("astaroth_spawn_egg",
            () -> new ModSpawnEggItem(Entities.ASTAROTH, 0x17254F, 0x9FE8DC,
                    new Item.Properties().group(ItemGroup.MISC).maxStackSize(64)));


}
