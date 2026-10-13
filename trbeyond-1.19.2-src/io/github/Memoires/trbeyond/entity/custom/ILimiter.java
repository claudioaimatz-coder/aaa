package io.github.Memoires.trbeyond.entity.custom;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import com.github.manasmods.manascore.api.skills.capability.SkillStorage;
import com.github.manasmods.tensura.ability.skill.resist.ResistSkill;
import com.github.manasmods.tensura.capability.ep.TensuraEPCapability;
import com.github.manasmods.tensura.registry.effects.TensuraMobEffects;
import io.github.Memoires.trbeyond.capability.dungeon.DungeonCapability;
import io.github.Memoires.trbeyond.network.worldgen.DungeonSaveData;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import io.github.Memoires.trbeyond.registry.effects.BeyondEffects;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface ILimiter {
   default boolean shouldAttack(Mob mob, LivingEntity entity) {
      if (entity == mob || mob.isAlliedTo(entity)) {
         return false;
      } else if (entity == mob.getTarget()) {
         return true;
      } else {
         double EP = TensuraEPCapability.getEP(entity);
         double selfEP = TensuraEPCapability.getEP(mob);
         return EP < selfEP;
      }
   }

   default void applyLimitation(Mob mob) {
      if (mob.isAlive()) {
         LivingEntity target = mob.getTarget();
         int limit = this.getLimitation();
         if (target == null) {
            if (limit != 0) {
               this.setLimitation(0);
            }
         } else {
            this.limitHighEP(mob, target, limit);
            if (mob.getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
               List<LivingEntity> list = mob.getLevel()
                  .getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(10.0), entityx -> entityx != mob && entityx != target && !mob.isAlliedTo(entityx));
               if (!list.isEmpty()) {
                  DungeonSaveData saveData = DungeonSaveData.getOverworld(mob.level);
                  if (saveData != null) {
                     Vec3 pos = saveData.getStartGenPos();

                     for (LivingEntity entity : list) {
                        if (!(entity instanceof Player player) || !player.isCreative() && !player.isSpectator()) {
                           entity.unRide();
                           entity.teleportToWithTicket(7.0, 167.0, 349.0);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   default void limitHighEP(Mob mob, LivingEntity target, int limit) {
      if (limit == ILimiter.Limitation.NO_TOGGLE.getId()) {
         SkillStorage storage = SkillAPI.getSkillsFrom(target);

         for (ManasSkillInstance instance : storage.getLearnedSkills()) {
            if (!(instance.getSkill() instanceof ResistSkill) && instance.isToggled()) {
               instance.setToggled(false);
               instance.onToggleOff(target);
               instance.markDirty();
            }
         }

         storage.syncChanges();
         if (target instanceof Player player && !player.isCreative() && !player.isSpectator()) {
            player.stopFallFlying();
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
         }
      } else if (limit == ILimiter.Limitation.NO_RESISTANCE.getId()) {
         SkillStorage storage = SkillAPI.getSkillsFrom(target);

         for (ManasSkillInstance instancex : storage.getLearnedSkills()) {
            if (instancex.getSkill() instanceof ResistSkill && instancex.isToggled()) {
               instancex.setToggled(false);
               instancex.onToggleOff(target);
               instancex.markDirty();
            }
         }

         storage.syncChanges();
      } else if (limit == ILimiter.Limitation.NO_SKILL.getId()) {
         target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.ANTI_SKILL.get(), 100, 0, false, false, false));
         target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.MAGIC_INTERFERENCE.get(), 100, 0, false, false, false));
         target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.SILENCE.get(), 100, 0, false, false, false));
         if (target instanceof Player player && !player.isCreative() && !player.isSpectator()) {
            player.stopFallFlying();
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
         }
      } else if (limit == ILimiter.Limitation.LOWER_SELF.getId()) {
         DungeonCapability.setEpLimit(target, TensuraEPCapability.getEP(mob));
         target.addEffect(new MobEffectInstance((MobEffect)BeyondEffects.LIMITED.get(), 100, 0, false, false, false));
         SkillStorage storage = SkillAPI.getSkillsFrom(target);

         for (ManasSkillInstance instancexx : storage.getLearnedSkills()) {
            if (instancexx.getSkill() instanceof ResistSkill && instancexx.isToggled()) {
               instancexx.setToggled(false);
               instancexx.onToggleOff(target);
               instancexx.markDirty();
            }
         }

         storage.syncChanges();
         if (target instanceof Player player && !player.isCreative() && !player.isSpectator()) {
            player.stopFallFlying();
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
         }
      } else {
         this.limitLowEP(mob, target);
      }
   }

   default void limitLowEP(Mob mob, LivingEntity target) {
      double EP = TensuraEPCapability.getEP(target);
      double selfEP = TensuraEPCapability.getEP(mob);
      if (EP < selfEP * 0.1F) {
         target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.ANTI_SKILL.get(), 100, 0, false, false, false));
         target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.MAGIC_INTERFERENCE.get(), 100, 0, false, false, false));
         target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.SILENCE.get(), 100, 0, false, false, false));
         if (target instanceof Player player && !player.isCreative() && !player.isSpectator()) {
            player.stopFallFlying();
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
         }

         SkillStorage storage = SkillAPI.getSkillsFrom(target);

         for (ManasSkillInstance instance : storage.getLearnedSkills()) {
            if (instance.isToggled()) {
               instance.setToggled(false);
               instance.onToggleOff(target);
               instance.markDirty();
            }
         }

         storage.syncChanges();
      } else if (EP < selfEP * 0.3F) {
         target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.ANTI_SKILL.get(), 100, 0, false, false, false));
         target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.MAGIC_INTERFERENCE.get(), 100, 0, false, false, false));
         if (target instanceof Player player && !player.isCreative() && !player.isSpectator()) {
            player.stopFallFlying();
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
         }
      } else if (EP < selfEP * 0.5) {
         target.addEffect(new MobEffectInstance((MobEffect)TensuraMobEffects.ANTI_SKILL.get(), 100, 0, false, false, false));
      }
   }

   int getLimitation();

   void setLimitation(int var1);

   public static enum Limitation {
      NO_TOGGLE(1),
      NO_RESISTANCE(2),
      NO_SKILL(3),
      LOWER_SELF(4);

      private final int id;

      public int getId() {
         return this.id;
      }

      private Limitation(int id) {
         this.id = id;
      }
   }
}
