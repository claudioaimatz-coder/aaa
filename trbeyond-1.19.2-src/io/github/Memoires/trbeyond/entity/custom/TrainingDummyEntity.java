package io.github.Memoires.trbeyond.entity.custom;

import io.github.Memoires.trbeyond.client.renderer.FontRenderer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class TrainingDummyEntity extends PathfinderMob implements IAnimatable {
   public int animationTicks;
   private final AnimationFactory factory = new AnimationFactory(this);

   public boolean hurt(@NotNull DamageSource p_21016_, float p_21017_) {
      if (this.animationTicks == 0) {
         this.animationTicks = 30;
      }

      FontRenderer.renderText(Float.toString(p_21017_), this.position(), 30);
      return super.hurt(p_21016_, p_21017_);
   }

   public void knockback(double p_147241_, double p_147242_, double p_147243_) {
   }

   public TrainingDummyEntity(EntityType<? extends PathfinderMob> type, Level level) {
      super(type, level);
   }

   public void tick() {
      super.tick();
      if (this.animationTicks >= 0) {
         this.animationTicks--;
      }
   }

   public static AttributeSupplier setAttributes() {
      return PathfinderMob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 2.0).build();
   }

   public void registerControllers(AnimationData data) {
      data.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      if (this.animationTicks > 0) {
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.new", false));
         return PlayState.CONTINUE;
      } else {
         return PlayState.STOP;
      }
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
