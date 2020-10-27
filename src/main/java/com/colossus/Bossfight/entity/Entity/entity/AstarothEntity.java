package com.colossus.Bossfight.entity.Entity.entity;


import com.colossus.Bossfight.Entities;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.IAnimatable;
import software.bernie.geckolib.core.PlayState;
import software.bernie.geckolib.core.builder.AnimationBuilder;
import software.bernie.geckolib.core.controller.AnimationController;
import software.bernie.geckolib.core.event.predicate.AnimationEvent;
import software.bernie.geckolib.core.manager.AnimationData;
import software.bernie.geckolib.core.manager.AnimationFactory;


import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;


public class AstarothEntity extends CreatureEntity implements IAnimatable {
	private AnimationFactory controller = new AnimationFactory(this);

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){

		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
			return PlayState.CONTINUE;
		}

	}



	public AstarothEntity(EntityType<AstarothEntity> type, World worldIn) {
		super(type, worldIn);
		this.ignoreFrustumCheck = true;

	}
	public AstarothEntity(World worldIn, double posX, double posY, double posZ)
	{
		this(Entities.ASTAROTH.get(), worldIn);
		this.setPosition(posX, posY, posZ);

	}

	public static boolean canSpawn(EntityType<AstarothEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random)
	{
		Block block = world.getBlockState(pos.down()).getBlock();
		return world.getDifficulty() != Difficulty.PEACEFUL && block != Blocks.MAGMA_BLOCK;

	}






	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
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
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
	}


	protected boolean isDespawnPeaceful() {
		return true;
	}








	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 2.6F;
	}


	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.controller;
	}

}
