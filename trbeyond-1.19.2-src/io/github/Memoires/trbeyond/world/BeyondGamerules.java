package io.github.Memoires.trbeyond.world;

import java.lang.reflect.Method;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Category;
import net.minecraft.world.level.GameRules.IntegerValue;
import net.minecraft.world.level.GameRules.Key;
import net.minecraft.world.level.GameRules.Type;
import net.minecraft.world.level.GameRules.Value;

public class BeyondGamerules {
   public static Key<BooleanValue> NIGHTMARE_MODE;
   public static Key<IntegerValue> AMBIENT_MOB_SPAWN_RATE;
   public static Key<BooleanValue> DISABLE_CHALLENGES;
   public static Key<BooleanValue> DISABLE_SHRINES;
   public static Key<BooleanValue> UNIQUE_OVERWORLD;
   public static Key<BooleanValue> UNIQUE_PARTICLES;
   public static Key<IntegerValue> UNIQUE_MONSTER_CHANCE;
   public static Key<IntegerValue> LOOT_EXPLOSION_CHANCE;
   public static Key<BooleanValue> MOBS_SPAWN_WITH_DUNGEON;
   public static Key<IntegerValue> DUNGEON_REENTRY_COOLDOWN;
   public static Key<IntegerValue> DUNGEON_FLOOR_SIZE;
   public static Key<BooleanValue> BOSS_EP_ONE_TIME_DROP;

   public static void registryGameRules() {
      NIGHTMARE_MODE = GameRules.register("nightmareMode", Category.MISC, createBooleanType(false));
      UNIQUE_MONSTER_CHANCE = GameRules.register("uniqueMonsterChance", Category.SPAWNING, createIntegerType(1));
      UNIQUE_OVERWORLD = GameRules.register("UniqueMonsterOverworld", Category.SPAWNING, createBooleanType(false));
      UNIQUE_PARTICLES = GameRules.register("UniqueMonsterParticles", Category.MOBS, createBooleanType(true));
      LOOT_EXPLOSION_CHANCE = GameRules.register("lootExplosionChance", Category.DROPS, createIntegerType(1));
      AMBIENT_MOB_SPAWN_RATE = GameRules.register("ambientMobSpawnRate", Category.SPAWNING, createIntegerType(100));
      DISABLE_CHALLENGES = GameRules.register("disablechallenges", Category.PLAYER, createBooleanType(false));
      DISABLE_SHRINES = GameRules.register("disableShrines", Category.PLAYER, createBooleanType(false));
      MOBS_SPAWN_WITH_DUNGEON = GameRules.register("mobsSpawnWithDungeon", Category.SPAWNING, BooleanValue.create(true));
      DUNGEON_FLOOR_SIZE = GameRules.register("dungeonFloorSize", Category.MISC, IntegerValue.create(100));
      BOSS_EP_ONE_TIME_DROP = GameRules.register("bossEPOneTimeDrop", Category.DROPS, createBooleanType(false));
   }

   public static boolean getBoolean(Level level, Key<BooleanValue> ruleKey) {
      if (level == null) {
         return false;
      } else {
         if (level.getGameRules().getRule(ruleKey) instanceof BooleanValue boolVal && boolVal.get()) {
            return true;
         }

         return false;
      }
   }

   public static int getInt(Level level, Key<IntegerValue> ruleKey) {
      if (level == null) {
         return 0;
      } else {
         Value<IntegerValue> value = level.getGameRules().getRule(ruleKey);
         return ((IntegerValue)value).get();
      }
   }

   private static Type<BooleanValue> createBooleanType(boolean defaultValue) {
      String[] methodNames = new String[]{"create", "create"};
      String[] var2 = methodNames;
      int var3 = methodNames.length;
      int var4 = 0;

      while (var4 < var3) {
         String name = var2[var4];

         try {
            Method createMethod = BooleanValue.class.getDeclaredMethod(name, boolean.class);
            createMethod.setAccessible(true);
            return (Type<BooleanValue>)createMethod.invoke(null, defaultValue);
         } catch (NoSuchMethodException var7) {
            var4++;
         } catch (Exception var8) {
            throw new RuntimeException("Failed to create boolean GameRule type via method '" + name + "'", var8);
         }
      }

      throw new RuntimeException("Failed to create boolean GameRule type: no suitable create method found");
   }

   private static Type<IntegerValue> createIntegerType(int defaultValue) {
      String[] methodNames = new String[]{"create", "m_46251_", "create"};
      String[] var2 = methodNames;
      int var3 = methodNames.length;
      int var4 = 0;

      while (var4 < var3) {
         String name = var2[var4];

         try {
            Method createMethod = IntegerValue.class.getDeclaredMethod(name, int.class);
            createMethod.setAccessible(true);
            return (Type<IntegerValue>)createMethod.invoke(null, defaultValue);
         } catch (NoSuchMethodException var7) {
            var4++;
         } catch (Exception var8) {
            throw new RuntimeException("Failed to create integer GameRule type via method '" + name + "'", var8);
         }
      }

      throw new RuntimeException("Failed to create integer GameRule type: no suitable create method found");
   }
}
