package io.github.Memoires.trbeyond.data;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class BeyondTags {
   public static final TagKey<EntityType<?>> LOOT_T1 = forgeTag("loot_t1");
   public static final TagKey<EntityType<?>> LOOT_T2 = forgeTag("loot_t2");
   public static final TagKey<EntityType<?>> LOOT_T3 = forgeTag("loot_t3");
   public static final TagKey<EntityType<?>> LOOT_T4 = forgeTag("loot_t4");
   public static final TagKey<EntityType<?>> LOOT_T5 = forgeTag("loot_t5");
   public static final TagKey<EntityType<?>> LOOT_T6 = forgeTag("loot_t6");
   public static final TagKey<EntityType<?>> LOOT_T7 = forgeTag("loot_t7");
   public static final TagKey<EntityType<?>> SLIME_DOLLS = forgeTag("slime_dolls");
   public static final TagKey<EntityType<?>> DUNGEON_BOSS = forgeTag("dungeon_boss");
   public static final TagKey<EntityType<?>> NOT_ELITE = forgeTag("not_elite");
   public static final TagKey<Block> DUNGEON_PLACEABLE = forgeBlockTag("dungeon_placeable");

   static TagKey<EntityType<?>> forgeTag(String name) {
      return create(new ResourceLocation("forge", name));
   }

   static TagKey<EntityType<?>> create(ResourceLocation name) {
      return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, name);
   }

   static TagKey<Block> forgeBlockTag(String name) {
      return BlockTags.create(new ResourceLocation("forge", name));
   }
}
