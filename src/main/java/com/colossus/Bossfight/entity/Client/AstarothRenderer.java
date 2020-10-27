/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.colossus.Bossfight.entity.Client;


import com.colossus.Bossfight.entity.Entity.entity.AstarothEntity;
import com.colossus.Bossfight.entity.Entity.model.AstarothModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderers.geo.GeoEntityRenderer;

public class AstarothRenderer extends GeoEntityRenderer<AstarothEntity>
{
	public AstarothRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new AstarothModel());
	}

	@Override
	public RenderType getRenderType(AstarothEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation)
	{
		stack.scale(0.17F, 0.17F, 0.17F);
		return RenderType.getEntityTranslucent(getTextureLocation(animatable));
	}


	}