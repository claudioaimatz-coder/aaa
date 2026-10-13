package io.github.Memoires.trbeyond;

import io.github.Memoires.trbeyond.network.worldgen.portal.DungeonStructureSet;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Database {
   public static final String MOD_ID = "trbeyond";
   public static final DungeonStructureSet F1_10 = DungeonStructureSet.newSet();
   public static final DungeonStructureSet F11_20 = DungeonStructureSet.newSet();
   public static final DungeonStructureSet F21_30 = DungeonStructureSet.newSet();
   public static final DungeonStructureSet F31_40 = DungeonStructureSet.newSet();
   public static final DungeonStructureSet F41_50 = DungeonStructureSet.newSet();

   @Nullable
   public static DungeonStructureSet getByFloor(int floor) {
      if (floor < 11) {
         return F1_10;
      } else if (floor < 21) {
         return F11_20;
      } else if (floor < 31) {
         return F21_30;
      } else if (floor < 41) {
         return F31_40;
      } else {
         return floor < 51 ? F41_50 : null;
      }
   }

   @NotNull
   public static ResourceLocation rl(String s) {
      return new ResourceLocation("trbeyond", s);
   }
}
