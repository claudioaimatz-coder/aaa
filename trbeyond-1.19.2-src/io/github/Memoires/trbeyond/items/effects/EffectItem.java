package io.github.Memoires.trbeyond.items.effects;

import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public abstract class EffectItem extends Item implements IAnimatable {
   public AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public EffectItem() {
      super(new Properties());
   }

   public void registerControllers(AnimationData data) {
      AnimationController<EffectItem> controller = new AnimationController(this, "controller", 0.0F, this::predicate);
      if (FMLEnvironment.dist == Dist.CLIENT) {
         controller.registerCustomInstructionListener(EffectHandler::handleInstructionKeyframe);
      }

      data.addAnimationController(controller);
   }

   private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
      event.getController().setAnimation(new AnimationBuilder().loop("attack"));
      return PlayState.CONTINUE;
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }

   public static void offsetPlayer(ServerPlayer player) {
      if (player != null) {
         Level level = player.level;
         Vec3 eyePos = player.getEyePosition();
         Vec3 lookVec = player.getLookAngle().multiply(1.0, 0.0, 1.0).normalize();
         double maxDistance = 10.0;
         Vec3 targetVec = eyePos.add(lookVec.scale(maxDistance));
         BlockHitResult hitResult = level.clip(new ClipContext(eyePos, targetVec, Block.COLLIDER, Fluid.NONE, player));
         Vec3 safeTarget = hitResult.getType() == Type.BLOCK ? hitResult.getLocation().subtract(lookVec.scale(1.0)) : targetVec;
         player.teleportTo(safeTarget.x, player.getY(), safeTarget.z);
         player.hurtMarked = true;
      }
   }

   public static class Gabiru extends EffectItem {
      public AnimationFactory factory = GeckoLibUtil.createFactory(this);

      public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
         super.initializeClient(consumer);
         consumer.accept(new IClientItemExtensions() {
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
               return new EffectRenderer(new EffectModel(AnimatedEffectWeapon.Effect.GABIRU));
            }
         });
      }

      @Override
      public AnimationFactory getFactory() {
         return this.factory;
      }
   }

   public static class Gelds extends EffectItem {
      public AnimationFactory factory = GeckoLibUtil.createFactory(this);

      public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
         super.initializeClient(consumer);
         consumer.accept(new IClientItemExtensions() {
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
               return new EffectRenderer(new EffectModel(AnimatedEffectWeapon.Effect.GELDS));
            }
         });
      }

      @Override
      public AnimationFactory getFactory() {
         return this.factory;
      }
   }

   public static class Gorikimaru extends EffectItem {
      public AnimationFactory factory = GeckoLibUtil.createFactory(this);

      public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
         super.initializeClient(consumer);
         consumer.accept(new IClientItemExtensions() {
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
               return new EffectRenderer(new EffectModel(AnimatedEffectWeapon.Effect.GORIKIMARU));
            }
         });
      }

      @Override
      public AnimationFactory getFactory() {
         return this.factory;
      }
   }

   public static class Guren extends EffectItem {
      public AnimationFactory factory = GeckoLibUtil.createFactory(this);

      public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
         super.initializeClient(consumer);
         consumer.accept(new IClientItemExtensions() {
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
               return new EffectRenderer(new EffectModel(AnimatedEffectWeapon.Effect.GUREN));
            }
         });
      }

      @Override
      public AnimationFactory getFactory() {
         return this.factory;
      }
   }

   public static class Haruko extends EffectItem {
      public AnimationFactory factory = GeckoLibUtil.createFactory(this);

      public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
         super.initializeClient(consumer);
         consumer.accept(new IClientItemExtensions() {
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
               return new EffectRenderer(new EffectModel(AnimatedEffectWeapon.Effect.HAKURO_SWORD));
            }
         });
      }

      @Override
      public AnimationFactory getFactory() {
         return this.factory;
      }
   }

   public static class MinosTrident extends EffectItem {
      public AnimationFactory factory = GeckoLibUtil.createFactory(this);

      public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
         super.initializeClient(consumer);
         consumer.accept(new IClientItemExtensions() {
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
               return new EffectRenderer(new EffectModel(AnimatedEffectWeapon.Effect.MINOS_TRIDENT_UNIQUE));
            }
         });
      }

      @Override
      public AnimationFactory getFactory() {
         return this.factory;
      }
   }
}
