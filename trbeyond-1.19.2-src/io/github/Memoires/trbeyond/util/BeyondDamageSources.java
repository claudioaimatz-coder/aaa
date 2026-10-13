package io.github.Memoires.trbeyond.util;

import com.github.manasmods.tensura.util.damage.TensuraDamageSource;
import io.github.Memoires.trbeyond.entity.projectile.ThrownMinosBardicheProjectile;
import javax.annotation.Nullable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

public class BeyondDamageSources {
   public static final DamageSource PARALYSIS = new TensuraDamageSource("tensura.paralysis").setNotTensuraMagic().bypassArmor().bypassEnchantments().setMagic();

   public static DamageSource minosBardiche(ThrownMinosBardicheProjectile spear, @Nullable Entity pIndirectEntity) {
      return new IndirectEntityDamageSource("tensura.minos_bardiche", spear, pIndirectEntity).setProjectile();
   }
}
