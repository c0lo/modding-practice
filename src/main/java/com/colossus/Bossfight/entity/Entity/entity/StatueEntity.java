package com.colossus.Bossfight.entity.Entity.entity;


import com.colossus.Bossfight.Entities;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import software.bernie.geckolib.core.IAnimatable;
import software.bernie.geckolib.core.PlayState;
import software.bernie.geckolib.core.builder.AnimationBuilder;
import software.bernie.geckolib.core.controller.AnimationController;
import software.bernie.geckolib.core.event.predicate.AnimationEvent;
import software.bernie.geckolib.core.manager.AnimationData;
import software.bernie.geckolib.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Random;


public class StatueEntity extends CreatureEntity implements IAnimatable {
	private AnimationFactory controller = new AnimationFactory(this);
	private BlockPos boundOrigin;
	private boolean limitedLifespan;
	private int limitedLifeTicks;
	private MobEntity owner;
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){

		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F))
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.statue.move", true));
		}
		else
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.statue.idle", true));
		}
		return PlayState.CONTINUE;

	}

	public MobEntity getOwner() {
		return this.owner;
	}
	public void setOwner(MobEntity ownerIn) {
		this.owner = ownerIn;
	}

	public StatueEntity(EntityType<StatueEntity> type, World worldIn) {
		super(type, worldIn);

	}

	public static boolean canSpawn(EntityType<StatueEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random)
	{
		Block block = world.getBlockState(pos.down()).getBlock();
		return world.getDifficulty() != Difficulty.PEACEFUL && block != Blocks.MAGMA_BLOCK;

	}

	class CopyOwnerTargetGoal extends TargetGoal {
		private final EntityPredicate field_220803_b = (new EntityPredicate()).setLineOfSiteRequired().setUseInvisibilityCheck();

		public CopyOwnerTargetGoal(CreatureEntity creature) {
			super(creature, false);
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			return StatueEntity.this.owner != null && StatueEntity.this.owner.getAttackTarget() != null && this.isSuitableTarget(StatueEntity.this.owner.getAttackTarget(), this.field_220803_b);
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			StatueEntity.this.setAttackTarget(StatueEntity.this.owner.getAttackTarget());
			super.startExecuting();
		}
	}






	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
		this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(20D);
	}


	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains("BoundX")) {
			this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
		}

		if (compound.contains("LifeTicks")) {
			this.setLimitedLife(compound.getInt("LifeTicks"));
		}

	}
	public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
		this.boundOrigin = boundOriginIn;
	}

	public void setLimitedLife(int limitedLifeTicksIn) {
		this.limitedLifespan = true;
		this.limitedLifeTicks = limitedLifeTicksIn;
	}
	@Override
	public void onDeathUpdate() {

		++this.deathTime;

		if (this.deathTime == 50) {
			if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))) {
				int i = this.getExperiencePoints(this.attackingPlayer);
				i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
				while (i > 0) {
					int j = ExperienceOrbEntity.getXPSplit(i);
					i -= j;
					this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), j));
				}
			}

			this.remove();


		}

	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		spawnDataIn =  super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		return spawnDataIn;
	}




	protected void registerGoals() {
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1D));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new StatueEntity.CopyOwnerTargetGoal(this));
		this.targetSelector.addGoal(2, new MeleeAttackGoal(this, 2D, true));
	}


	protected boolean isDespawnPeaceful() {
		return true;
	}













	@Override
	public AnimationFactory getFactory() {
		return this.controller;
	}


}
