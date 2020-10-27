package com.colossus.Bossfight.entity.Entity.model;

import com.colossus.Bossfight.modinfo;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.model.AnimatedGeoModel;

public class AstarothModel extends AnimatedGeoModel
{
    @Override
    public ResourceLocation getAnimationFileLocation(Object entity)
    {
        return new ResourceLocation(modinfo.ModID, "animations/astaroth.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Object entity)
    {
        return new ResourceLocation(modinfo.ModID, "geo/astaroth.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Object entity)
    {
        return new ResourceLocation(modinfo.ModID, "textures/model/entity/astaroth.png");
    }
}
