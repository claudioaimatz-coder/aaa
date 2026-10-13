package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.tensura.entity.template.TensuraTamableEntity;
import com.github.manasmods.tensura.entity.template.TensuraTamableEntity.TensuraOwnerHurtByTargetGoal;
import io.github.Memoires.trbeyond.block.ShrineBlock;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ShrineEntity extends TensuraTamableEntity implements IAnimatable {
   ShrineBlock.DungeonShrineTier tier;
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public ShrineEntity(EntityType<? extends ShrineEntity> entityType, Level level, ShrineBlock.DungeonShrineTier tier) {
      super(entityType, level);
      this.tier = tier;
   }

   public static AttributeSupplier setAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.ATTACK_DAMAGE, 1.0)
         .add(Attributes.MAX_HEALTH, 1000.0)
         .add(Attributes.MOVEMENT_SPEED, 0.0)
         .add(Attributes.FOLLOW_RANGE, 32.0)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .build();
   }

   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.targetSelector.addGoal(1, new TensuraOwnerHurtByTargetGoal(this));
   }

   public void registerControllers(AnimationData data) {
   }

   public boolean isPushable() {
      return false;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.getPersistentData().getString("challenge").equals("Protect the Shrine")) {
         this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(25.0)).forEach(mob -> {
            if (mob.getPersistentData().contains("challengeMob") && mob.getPersistentData().getString("challengeMob").equals("Protect the Shrine")) {
               mob.setTarget(this);
            }
         });
      }
   }

   public ShrineBlock.DungeonShrineTier getTier() {
      return this.tier;
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
