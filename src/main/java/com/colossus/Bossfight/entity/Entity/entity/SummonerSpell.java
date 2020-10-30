package com.colossus.Bossfight.entity.Entity.entity;


import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SummonerSpell extends MonsterEntity {
    private static final DataParameter<Byte> SPELL = EntityDataManager.createKey(SummonerSpell.class, DataSerializers.BYTE);
    protected int spellTicks;
    private SpellType activeSpell = SpellType.NONE;

    protected SummonerSpell(EntityType<? extends SummonerSpell> type, World worldin) {
        super(type, worldin);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(SPELL, (byte) 0);
    }


    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.spellTicks = compound.getInt("SpellTicks");
    }


    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("SpellTicks", this.spellTicks);
    }


    public boolean isSpellcasting() {
        if (this.world.isRemote) {
            return this.dataManager.get(SPELL) > 0;
        } else {
            return this.spellTicks > 0;
        }
    }

    public void setSpellType(SpellType spellType) {
        this.activeSpell = spellType;
        this.dataManager.set(SPELL, (byte) spellType.id);
    }

    protected SpellType getSpellType() {
        return !this.world.isRemote ? this.activeSpell : SpellType.getFromId(this.dataManager.get(SPELL));
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }

    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();


    }

    protected int getSpellTicks() {
        return this.spellTicks;
    }


    public class CastingASpellGoal extends Goal {
        public CastingASpellGoal() {
            this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return SummonerSpell.this.getSpellTicks() > 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            super.startExecuting();
            SummonerSpell.this.navigator.clearPath();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            super.resetTask();
            SummonerSpell.this.setSpellType(SpellType.NONE);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (SummonerSpell.this.getAttackTarget() != null) {
                SummonerSpell.this.getLookController().setLookPositionWithEntity(SummonerSpell.this.getAttackTarget(), (float) SummonerSpell.this.getHorizontalFaceSpeed(), (float) SummonerSpell.this.getVerticalFaceSpeed());
            }

        }
    }

    public static enum SpellType {
        NONE(0, 0.0D, 0.0D, 0.0D),
        SUMMON_VEX(1, 0.7D, 0.7D, 0.8D),
        FANGS(2, 0.4D, 0.3D, 0.35D),
        WOLOLO(3, 0.7D, 0.5D, 0.2D),
        DISAPPEAR(4, 0.3D, 0.3D, 0.8D),
        BLINDNESS(5, 0.1D, 0.1D, 0.2D);

        private final int id;
        private final double[] particleSpeed;

        private SpellType(int idIn, double xParticleSpeed, double yParticleSpeed, double zParticleSpeed) {
            this.id = idIn;
            this.particleSpeed = new double[]{xParticleSpeed, yParticleSpeed, zParticleSpeed};
        }

        public static SpellType getFromId(int idIn) {
            for (SpellType spellcastingillagerentity$spelltype : values()) {
                if (idIn == spellcastingillagerentity$spelltype.id) {
                    return spellcastingillagerentity$spelltype;
                }
            }

            return NONE;
        }
    }

    public abstract class UseSpellGoal extends Goal {
        protected int spellWarmup;
        protected int spellCooldown;

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = SummonerSpell.this.getAttackTarget();
            if (livingentity != null && livingentity.isAlive()) {
                if (SummonerSpell.this.isSpellcasting()) {
                    return false;
                } else {
                    return SummonerSpell.this.ticksExisted >= this.spellCooldown;
                }
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = SummonerSpell.this.getAttackTarget();
            return livingentity != null && livingentity.isAlive() && this.spellWarmup > 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.spellWarmup = this.getCastWarmupTime();
            SummonerSpell.this.spellTicks = this.getCastingTime();
            this.spellCooldown = SummonerSpell.this.ticksExisted + this.getCastingInterval();
            SoundEvent soundevent = this.getSpellPrepareSound();
            if (soundevent != null) {
                SummonerSpell.this.playSound(soundevent, 1.0F, 1.0F);
            }

            SummonerSpell.this.setSpellType(this.getSpellType());
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            --this.spellWarmup;
            if (this.spellWarmup == 0) {
                this.castSpell();
            }

        }

        protected abstract void castSpell();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract SpellType getSpellType();
    }

    public boolean shouldAttackPlayer(PlayerEntity player) {
        {
            return true;
        }

    }

    public boolean shouldAttackEntity(MobEntity player) {
        {
            return true;
        }

    }


}
