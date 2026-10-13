package io.github.Memoires.trbeyond.capability.dungeon;

import io.github.Memoires.trbeyond.block.ShrineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IDungeonCapability extends INBTSerializable<CompoundTag> {
   boolean isRevivalBracelet();

   void setRevivalBracelet(boolean var1);

   boolean isTakenVoucher();

   void setTakenVoucher(boolean var1);

   double getEpLimit();

   void setEpLimit(double var1);

   BlockPos getOverworldCoords();

   void setOverworldCoords(BlockPos var1);

   int getChallengeCompletionCount(ShrineBlock.DungeonShrineTier var1);

   int getTotalChallengeCompletionCount();

   void setChallengeCompletionCount(ShrineBlock.DungeonShrineTier var1, Integer var2);
}
