package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.entity.MetalSlimeEntity;
import com.github.manasmods.tensura.registry.items.TensuraMaterialItems;
import io.github.Memoires.trbeyond.util.BeyondParticleHelper;
import io.github.Memoires.trbeyond.util.MinhSlimeAvoidEntityGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MinhSlimeEntity extends MetalSlimeEntity {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
   private boolean spookedByPlayer = false;
   private final int minhSlimeDespawnTimer = 200;
   private int tickCounter = 0;

   public MinhSlimeEntity(EntityType<? extends MetalSlimeEntity> pEntityType, Level pLevel) {
      super(pEntityType, pLevel);
      this.moveControl = new MoveControl(this);
   }

   public static AttributeSupplier setAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 100.0)
         .add(Attributes.ATTACK_DAMAGE, 1.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .add(Attributes.ARMOR, 10.0)
         .add(Attributes.JUMP_STRENGTH, 1.2)
         .add(Attributes.MOVEMENT_SPEED, 1.5)
         .build();
   }

   public void setSpookedByPlayer(boolean value) {
      this.spookedByPlayer = value;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.spookedByPlayer) {
         this.tickCounter++;
      }

      if (this.tickCounter >= 200) {
         this.spawnPoofParticles(this.getX(), this.getY(), this.getZ());
         this.discard();
      }
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(2, new MinhSlimeAvoidEntityGoal(this, 2.5, Player.class, 8.0F));
      this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (!event.isMoving()) {
         if (this.isSprinting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime.walk", EDefaultLoopTypes.LOOP));
         } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime.walk", EDefaultLoopTypes.LOOP));
         }
      } else {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.slime.idle", EDefaultLoopTypes.LOOP));
      }

      return PlayState.CONTINUE;
   }

   @Override
   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
   }

   @Override
   public AnimationFactory getFactory() {
      return this.factory;
   }

   @Override
   public void die(DamageSource cause) {
      super.die(cause);
      if (!this.level.isClientSide && (cause.getEntity() instanceof Player || cause.getDirectEntity() instanceof Player)) {
         int chance = this.random.nextInt(100);
         int extraDrops = chance < 1 ? 2 : (chance < 5 ? 1 : 0);
         int totalDrops = 1 + extraDrops;

         for (int i = 0; i < totalDrops; i++) {
            this.spawnAtLocation(new ItemStack((ItemLike)TensuraMaterialItems.GOLD_COIN.get()));
         }
      }
   }

   private void spawnPoofParticles(double x, double y, double z) {
      double radius = 0.5;
      int amount = 300;

      for (int i = 0; i < amount; i++) {
         double offsetX = this.random.nextGaussian() * radius;
         double offsetY = this.random.nextGaussian() * radius;
         double offsetZ = this.random.nextGaussian() * radius;
         BeyondParticleHelper.spawnParticle(this.level, ParticleTypes.WHITE_ASH, x + offsetX, y + offsetY, z + offsetZ, 0.0, 0.0, 0.0);
      }
   }

   @Override
   public void checkDespawn() {
   }
}
