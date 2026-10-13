package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.api.entity.subclass.ITeleportation;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import io.github.Memoires.trbeyond.handlers.DungeonRulesHandler;
import io.github.Memoires.trbeyond.util.TimerUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class KillerDryadEntity extends TensuraTamableEntity implements IAnimatable, ITeleportation {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public KillerDryadEntity(EntityType<? extends TensuraTamableEntity> entityType, Level level) {
      super(entityType, level);
      this.setNoGravity(true);
   }

   public boolean canBeAffected(MobEffectInstance pEffectInstance) {
      return false;
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true) {
         protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double)(this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + pAttackTarget.getBbWidth() + 100.0F);
         }
      });
   }

   public static AttributeSupplier setAttributes() {
      return Villager.createLivingAttributes()
         .add(Attributes.MAX_HEALTH, 420.0)
         .add(Attributes.ARMOR, 0.0)
         .add(Attributes.ATTACK_DAMAGE, 69.0)
         .add(Attributes.FOLLOW_RANGE, 64.0)
         .add(Attributes.MOVEMENT_SPEED, 3.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .build();
   }

   public boolean isInvulnerableTo(DamageSource damageSource) {
      return !damageSource.equals(DamageSource.OUT_OF_WORLD);
   }

   public boolean isPushable() {
      return false;
   }

   @Override
   public void checkDespawn() {
   }

   public boolean doHurtTarget(Entity pEntity) {
      if (pEntity.hurt(DamageSource.MAGIC.bypassInvul().bypassMagic().bypassEnchantments().bypassArmor(), 100000.0F)) {
         this.level.playSound(null, pEntity.blockPosition(), SoundEvents.ENDERMAN_DEATH, this.getSoundSource(), 1.0F, 1.0F);
         pEntity.sendSystemMessage(Component.translatable("trbeyond.message.rule_violation.eliminated").withStyle(ChatFormatting.RED));
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void tick() {
      super.tick();
      LivingEntity owner = this.getOwner();
      if (owner instanceof Player player) {
         double dist = (double)owner.distanceTo(this);
         if (dist > 5.0) {
            this.teleportTowards(this, owner, 5.0);
         }

         this.getLookControl().setLookAt(owner);
         this.lookAt(owner, 180.0F, 180.0F);
         TimerUtils.Timer timerBoss = TimerUtils.getOrCreateTimer(owner.getDisplayName().getString() + "_boss");
         if (timerBoss.getTicks() >= 100) {
            this.resetOwner(null);
            timerBoss.reset();
            this.setTarget(owner);
            this.swing(InteractionHand.MAIN_HAND, true);
            this.doHurtTarget(owner);
            return;
         }

         if (timerBoss.active) {
            return;
         }

         if (DungeonRulesHandler.playerPositions.get(player.getUUID()) == null) {
            this.discard();
            return;
         }

         if (!owner.isAlive() || !DungeonRulesHandler.isWithinArea(owner.blockPosition(), DungeonRulesHandler.playerPositions.get(player.getUUID()))) {
            this.discard();
            return;
         }

         TimerUtils.Timer timer = TimerUtils.getOrCreateTimer(owner.getDisplayName().getString() + "_idle");
         if (timer.getTicks() >= 11999) {
            this.resetOwner(null);
            timer.reset();
            this.setTarget(owner);
            this.swing(InteractionHand.MAIN_HAND, true);
            this.doHurtTarget(owner);
         }
      } else if (this.tickCount % 40 == 0) {
         this.discard();
      }
   }

   @Override
   public void teleport(LivingEntity entity, double pX, double pY, double pZ, double minY) {
      MutableBlockPos pos = new MutableBlockPos(pX, pY, pZ);
      if (this.shouldCountMotionBlock()) {
         while (pos.getY() > entity.level.getMinBuildHeight() && !entity.level.getBlockState(pos).getMaterial().blocksMotion()) {
            pos.move(Direction.DOWN);
         }
      } else if ((double)pos.getY() > minY) {
         while ((double)pos.getY() > minY && !entity.level.getBlockState(pos).getMaterial().blocksMotion()) {
            pos.move(Direction.DOWN);
         }
      }

      BlockState state = entity.level.getBlockState(pos);
      if ((!this.shouldCountMotionBlock() || state.getMaterial().blocksMotion()) && state.getFluidState().isEmpty()) {
         Vec3 vec3 = entity.position();
         if (this.randomTeleport(entity, pX, pY, pZ, true)) {
            entity.level.gameEvent(GameEvent.TELEPORT, vec3, Context.of(entity));
            if (!entity.isSilent() && this.getTeleportSound() != null) {
               entity.level.playSound(null, entity.xo, entity.yo, entity.zo, this.getTeleportSound(), entity.getSoundSource(), 1.0F, 1.0F);
               entity.playSound(this.getTeleportSound(), 1.0F, 1.0F);
            }
         }
      }
   }

   @Override
   public void commanding(Player player) {
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      return PlayState.CONTINUE;
   }

   private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
      if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
         event.getController().markNeedsReload();
         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.killerdryad.swipe", EDefaultLoopTypes.PLAY_ONCE));
         this.swinging = false;
      }

      return PlayState.CONTINUE;
   }

   public void registerControllers(AnimationData animationData) {
      animationData.addAnimationController(new AnimationController(this, "controller", 0.0F, this::predicate));
      animationData.addAnimationController(new AnimationController(this, "attackController", 0.0F, this::attackPredicate));
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
