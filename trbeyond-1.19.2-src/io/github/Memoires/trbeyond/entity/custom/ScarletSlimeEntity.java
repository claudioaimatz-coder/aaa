package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.api.entity.ai.WanderingFollowOwnerGoal;
import com.github.manasmods.tensura.api.entity.controller.JumpingEntityMoveControl;
import com.github.manasmods.tensura.api.entity.subclass.IJumpingEntity;
import com.github.manasmods.tensura.entity.SlimeEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtByTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtTargetGoal;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.WanderAroundPosGoal;
import com.github.manasmods.tensura.entity.variant.SlimeType;
import com.github.manasmods.tensura.registry.attribute.TensuraAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ScarletSlimeEntity extends SlimeEntity {
   protected int regenAmount;
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public ScarletSlimeEntity(EntityType<? extends ScarletSlimeEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.moveControl = new ScarletSlimeEntity.ScarletSlimeMoveControl(this);
      this.regenAmount = 4;
      this.xpReward = 400;
      this.setSize(4, false, false, false);
      this.setType(SlimeType.MERGE);
   }

   @Override
   public AnimationFactory getFactory() {
      return this.factory;
   }

   public boolean playerInRadius() {
      return !this.level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10.0, 10.0, 10.0), player -> !player.isCreative() && !player.isSpectator()).isEmpty();
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new WanderAroundPosGoal(this, 60, 1.0, 10, 7));
      this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
      this.targetSelector
         .addGoal(
            1,
            new NearestAttackableTargetGoal(
               this,
               LivingEntity.class,
               false,
               target -> target.getClass() != LivingArmorEntity.class
                     && target.getClass() != MageEntity.class
                     && target.getClass() != SkullNightEntity.class
                     && target.getClass() != MilimSlimeScarletEntity.class
            )
         );
      this.targetSelector.addGoal(2, new TensuraHurtByTargetGoal(this).setAlertOthers(new Class[0]));
      this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2, false) {
         public boolean canUse() {
            return !ScarletSlimeEntity.this.isTame() ? false : super.canUse();
         }
      });
      this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
      this.goalSelector.addGoal(1, new WanderingFollowOwnerGoal(this, 0.2, 20.0F, 5.0F, false));
      this.targetSelector.addGoal(1, new TensuraOwnerHurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new TensuraOwnerHurtTargetGoal(this));
   }

   public static AttributeSupplier setAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 250.0)
         .add(Attributes.ATTACK_DAMAGE, 100.0)
         .add(Attributes.FOLLOW_RANGE, 64.0)
         .add(Attributes.JUMP_STRENGTH, 1.2)
         .add(Attributes.MOVEMENT_SPEED, 1.5)
         .add((Attribute)TensuraAttributeRegistry.MAX_SPIRITUAL_HEALTH.get(), 1000.0)
         .build();
   }

   @Override
   protected void selfRegen() {
      this.heal((float)this.regenAmount);
      this.selfRegen = 20;
   }

   @Override
   public void setSize(int pSize, boolean pResetHealth) {
      this.setSize(pSize, true, pResetHealth, false);
   }

   @Override
   public int getJumpDelay() {
      return this.random.nextInt(10);
   }

   protected float getWaterSlowDown() {
      return 0.9F;
   }

   @Override
   protected void jumpFromGround() {
      Vec3 vec3 = this.getDeltaMovement();
      this.setDeltaMovement(vec3.x, (double)this.getJumpPower() * 2.0, vec3.z);
      this.hasImpulse = true;
   }

   @Override
   public boolean isTamingFood(ItemStack stack) {
      return false;
   }

   @Override
   public boolean isDyeable() {
      return false;
   }

   protected static class ScarletSlimeMoveControl extends JumpingEntityMoveControl {
      public ScarletSlimeMoveControl(PathfinderMob slimeIn) {
         super(slimeIn, 0.05F);
      }

      @Override
      public void setJumpDelay(IJumpingEntity jumpingEntity) {
         this.jumpDelay = jumpingEntity.getJumpDelay();
         if (this.mob.getTarget() != null && this.mob instanceof ScarletSlimeEntity slime && slime.playerInRadius()) {
            this.jumpDelay /= 2;
         }
      }
   }
}
