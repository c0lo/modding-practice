package com.colossus.Bossfight.entity.Entity.entity;


import com.colossus.Bossfight.Entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
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


public class AstarothEntity extends SummonerSpell implements IAnimatable {
	private AnimationFactory controller = new AnimationFactory(this);

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController(this, "controller", 20, this::predicate));
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
		if (isSpellcasting()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("dd"));
			return PlayState.CONTINUE;
		}
	else	if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F))
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("fly", true));
		}
		else
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
		}
		return PlayState.CONTINUE;

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
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
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
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new AstarothEntity.CastingASpellGoal());
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1D, 1D));
		this.goalSelector.addGoal(2, new AvoidEntityGoal(this, IronGolemEntity.class, 8.0F, 1D, 1D));
		this.goalSelector.addGoal(5, new AstarothEntity.AttackSpellGoal());
		this.goalSelector.addGoal(5, new AstarothEntity.SummonSpellGoal());
		this.goalSelector.addGoal(5, new AstarothEntity.BlindnessSpellGoal());
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1D));
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));

	}


	protected boolean isDespawnPeaceful() {
		return true;
	}








	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 2.2F;
	}




	@Override
	public AnimationFactory getFactory() {
		return this.controller;
	}


class AttackSpellGoal extends AstarothEntity.UseSpellGoal {
	private AttackSpellGoal() {
	}

	protected int getCastingTime() {
		return 40;
	}

	protected int getCastingInterval() {
		return 100;
	}

	protected void castSpell() {
		LivingEntity livingentity = AstarothEntity.this.getAttackTarget();
		double d0 = Math.min(livingentity.getPosY(), AstarothEntity.this.getPosY());
		double d1 = Math.max(livingentity.getPosY(), AstarothEntity.this.getPosY()) + 1.0D;
		float f = (float)MathHelper.atan2(livingentity.getPosZ() - AstarothEntity.this.getPosZ(), livingentity.getPosX() - AstarothEntity.this.getPosX());
		if (AstarothEntity.this.getDistanceSq(livingentity) < 9.0D) {
			for(int i = 0; i < 5; ++i) {
				float f1 = f + (float)i * (float)Math.PI * 0.4F;
			}

			for(int k = 0; k < 8; ++k) {
				float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
				this.spawnFangs(AstarothEntity.this.getPosX() + (double)MathHelper.cos(f2) * 2.5D, AstarothEntity.this.getPosZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
			}
		} else {
			for(int l = 0; l < 30; ++l) {
				//spawn fang amount
				double d2 = 1.25D * (double)(l + 1);
				int j = 1 * l;
				this.spawnFangs(AstarothEntity.this.getPosX() + (double)MathHelper.cos(f) * d2, AstarothEntity.this.getPosZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, j);

			}

			for(int k = 0; k < 8; ++k) {
				float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
				this.spawnFangs(AstarothEntity.this.getPosX() + (double)MathHelper.cos(f2) * 2.5D, AstarothEntity.this.getPosZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 6);


			}
			for(int k = 0; k < 16; ++k) {
				float f2 = f + (float)k * (float)Math.PI * 2.0F / 16.0F + 1.2566371F;
				this.spawnFangs(AstarothEntity.this.getPosX() + (double)MathHelper.cos(f2) * 5D, AstarothEntity.this.getPosZ() + (double)MathHelper.sin(f2) * 5D, d0, d1, f2, 9);


			}




			}


			for(int k = 0; k < 48; ++k) {
				float f2 = f + (float)k * (float)Math.PI * 2.0F / 44.0F + 1.2566371F;
				this.spawnFangs(AstarothEntity.this.getPosX() + (double)MathHelper.cos(f2) * 15D, AstarothEntity.this.getPosZ() + (double)MathHelper.sin(f2) * 15D, d0, d1, f2, 27);


			}




			}


		private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
		BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
		boolean flag = false;
		double d0 = 0.0D;

		while(true) {
			BlockPos blockpos1 = blockpos.down();
			BlockState blockstate = AstarothEntity.this.world.getBlockState(blockpos1);
			if (blockstate.isSolidSide(AstarothEntity.this.world, blockpos1, Direction.UP)) {
				if (!AstarothEntity.this.world.isAirBlock(blockpos)) {
					BlockState blockstate1 = AstarothEntity.this.world.getBlockState(blockpos);
					VoxelShape voxelshape = blockstate1.getCollisionShape(AstarothEntity.this.world, blockpos);
					if (!voxelshape.isEmpty()) {
						d0 = voxelshape.getEnd(Direction.Axis.Y);
					}
				}

				flag = true;
				break;
			}

			blockpos = blockpos.down();
			if (blockpos.getY() < MathHelper.floor(p_190876_5_) - 1) {
				break;
			}
		}

		if (flag) {
		AstarothEntity.this.world.addEntity(new EvokerFangsEntity(AstarothEntity.this.world, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, AstarothEntity.this));
		}

	}

	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
	}

	protected SummonerSpell.SpellType getSpellType() {
		return SummonerSpell.SpellType.FANGS;
	}
}
class SummonSpellGoal extends SummonerSpell.UseSpellGoal {
	private final EntityPredicate field_220843_e = (new EntityPredicate()).setDistance(16.0D).setLineOfSiteRequired().setUseInvisibilityCheck().allowInvulnerable().allowFriendlyFire();

	private SummonSpellGoal() {
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean shouldExecute() {
		if (!super.shouldExecute()) {
			return false;
		} else {
			int i = AstarothEntity.this.world.getTargettableEntitiesWithinAABB(VexEntity.class, this.field_220843_e, AstarothEntity.this, AstarothEntity.this.getBoundingBox().grow(16.0D)).size();
			return AstarothEntity.this.rand.nextInt(8) + 1 > i;
		}
	}

	protected int getCastingTime() {
		return 100;
	}

	protected int getCastingInterval() {
		return 340;
	}

	protected void castSpell() {
		for(int i = 0; i < 3; ++i) {
			BlockPos blockpos = (new BlockPos(AstarothEntity.this)).add(-2 + AstarothEntity.this.rand.nextInt(5), 1, -2 + AstarothEntity.this.rand.nextInt(5));
			StatueEntity vexentity = Entities.STATUE.get().create(AstarothEntity.this.world);
			vexentity.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
			vexentity.onInitialSpawn(AstarothEntity.this.world, AstarothEntity.this.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
			vexentity.setOwner(AstarothEntity.this);
			vexentity.setBoundOrigin(blockpos);
			vexentity.setLimitedLife(20 * (30 + AstarothEntity.this.rand.nextInt(90)));
			AstarothEntity.this.world.addEntity(vexentity);
		}

	}

	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON;
	}

	protected SummonerSpell.SpellType getSpellType() {
		return SummonerSpell.SpellType.SUMMON_VEX;
	}
}


class BlindnessSpellGoal extends SummonerSpell.UseSpellGoal {
	private int lastTargetId;

	private BlindnessSpellGoal() {
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean shouldExecute() {
		if (!super.shouldExecute()) {
			return false;
		} else if (AstarothEntity.this.getAttackTarget() == null) {
			return false;
		} else {
			return AstarothEntity.this.world.getDifficultyForLocation(new BlockPos(AstarothEntity.this)).isHarderThan((float)Difficulty.EASY.ordinal());
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
		this.lastTargetId = AstarothEntity.this.getAttackTarget().getEntityId();
	}

	protected int getCastingTime() {
		return 20;
	}

	protected int getCastingInterval() {
		return 180;
	}

	protected void castSpell() {
		AstarothEntity.this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100));
		AstarothEntity.this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.WITHER, 100));
		AstarothEntity.this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200));
		AstarothEntity.this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.NAUSEA, 100));
	}

	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT;
	}

	protected SummonerSpell.SpellType getSpellType() {
		return SummonerSpell.SpellType.BLINDNESS;
	}
}}
