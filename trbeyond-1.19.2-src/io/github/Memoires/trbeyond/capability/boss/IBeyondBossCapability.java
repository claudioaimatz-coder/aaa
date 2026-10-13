package io.github.Memoires.trbeyond.capability.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBeyondBossCapability extends INBTSerializable<CompoundTag> {
   int killedSpider();

   int killedCentipede();

   int killedSerpent();

   int killedOgre();

   int killedMezul();

   int killedGozul();

   void setKilledSpider(int var1);

   void setKilledCentipede(int var1);

   void setKilledSerpent(int var1);

   void setKilledOgre(int var1);

   void setKilledGozul(int var1);

   void setKilledMezul(int var1);

   long getSpiderLastKillTime();

   void setSpiderLastKillTime(long var1);

   long getCentipedeLastKillTime();

   void setCentipedeLastKillTime(long var1);

   long getSerpentLastKillTime();

   void setSerpentLastKillTime(long var1);

   long getOgreLastKillTime();

   void setOgreLastKillTime(long var1);

   long getGozulLastKillTime();

   void setGozulLastKillTime(long var1);

   long getMezulLastKillTime();

   void setMezulLastKillTime(long var1);
}
