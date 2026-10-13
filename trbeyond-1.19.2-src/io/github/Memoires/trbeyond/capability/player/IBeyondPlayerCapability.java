package io.github.Memoires.trbeyond.capability.player;

import io.github.Memoires.trbeyond.util.ChallengeType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBeyondPlayerCapability extends INBTSerializable<CompoundTag> {
   boolean isAction();

   void setAction(boolean var1);

   boolean isUsedTicket();

   void setUsedTicket(boolean var1);

   boolean isReturned();

   void setReturned(boolean var1);

   boolean getAction();

   void setChallengeType(ChallengeType var1);

   ChallengeType getChallengeType();

   void setOverworldCoords(BlockPos var1);

   BlockPos getOverworldCoords();

   boolean isUsedCrystal();

   void setUsedCrystal(boolean var1);
}
