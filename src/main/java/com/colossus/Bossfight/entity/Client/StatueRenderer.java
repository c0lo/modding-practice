/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.colossus.Bossfight.entity.Client;


import com.colossus.Bossfight.entity.Entity.entity.AstarothEntity;
import com.colossus.Bossfight.entity.Entity.entity.StatueEntity;
import com.colossus.Bossfight.entity.Entity.model.AstarothModel;
import com.colossus.Bossfight.entity.Entity.model.StatueModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.renderers.geo.GeoEntityRenderer;

public class StatueRenderer extends GeoEntityRenderer<StatueEntity>
{
	public StatueRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new StatueModel<LivingEntity>());
	}

	@Override
	public RenderType getRenderType(StatueEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation)
	{
		stack.scale(2F, 2F, 2F);
		return RenderType.getEntityTranslucent(getTextureLocation(animatable));
	}

	}