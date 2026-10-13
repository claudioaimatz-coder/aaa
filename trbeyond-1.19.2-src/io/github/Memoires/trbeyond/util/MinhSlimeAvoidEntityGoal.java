package io.github.Memoires.trbeyond.util;

import com.github.manasmods.tensura.api.entity.ai.PanicAroundEntityGoal;
import io.github.Memoires.trbeyond.entity.custom.MinhSlimeEntity;
import net.minecraft.world.entity.PathfinderMob;

public class MinhSlimeAvoidEntityGoal extends PanicAroundEntityGoal {
   private final PathfinderMob mob;

   public MinhSlimeAvoidEntityGoal(PathfinderMob pMob, double pWalkSpeedModifier, Class pEntityClassToAvoid, float pMaxDistance) {
      super(pMob, pWalkSpeedModifier, pEntityClassToAvoid, pMaxDistance);
      this.mob = pMob;
   }

   public void start() {
      super.start();
      this.mob.setSprinting(true);
      if (this.mob instanceof MinhSlimeEntity minhSlime) {
         minhSlime.setSpookedByPlayer(true);
      }
   }

   public void stop() {
      super.stop();
      this.mob.setSprinting(false);
   }
}
