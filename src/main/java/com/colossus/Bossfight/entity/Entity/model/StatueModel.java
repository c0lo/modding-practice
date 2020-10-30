package com.colossus.Bossfight.entity.Entity.model;

import com.colossus.Bossfight.modinfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.model.AnimatedGeoModel;

public class StatueModel<T extends LivingEntity> extends AnimatedGeoModel
{
    @Override
    public ResourceLocation getAnimationFileLocation(Object entity)
    {
        return new ResourceLocation(modinfo.ModID, "animations/statue.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Object entity)
    {
        return new ResourceLocation(modinfo.ModID, "geo/statue.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object entity)
    {
        return new ResourceLocation(modinfo.ModID, "textures/model/entity/statue.png");
    }


}
