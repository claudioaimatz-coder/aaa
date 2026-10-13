package io.github.Memoires.trbeyond.config;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class BossConfig {
   public final DoubleValue mezulMovementSpeed;
   public final DoubleValue mezulMaxHealth;
   public final DoubleValue mezulArmor;
   public final DoubleValue mezulAttackDamage;
   public final DoubleValue mezulFollowRange;
   public final DoubleValue mezulKnockbackResistance;
   public final DoubleValue mezulAttackKnockback;
   public final DoubleValue mezulMaxSpiritualHealth;
   public final IntValue mezulGoldDrop;
   public final DoubleValue gozulMovementSpeed;
   public final DoubleValue gozulMaxHealth;
   public final DoubleValue gozulArmor;
   public final DoubleValue gozulAttackDamage;
   public final DoubleValue gozulFollowRange;
   public final DoubleValue gozulKnockbackResistance;
   public final DoubleValue gozulAttackKnockback;
   public final DoubleValue gozulMaxSpiritualHealth;
   public final IntValue gozulGoldDrop;
   public final DoubleValue blackSpiderMovementSpeed;
   public final DoubleValue blackSpiderMaxHealth;
   public final DoubleValue blackSpiderArmor;
   public final DoubleValue blackSpiderAttackDamage;
   public final DoubleValue blackSpiderFollowRange;
   public final DoubleValue blackSpiderKnockbackResistance;
   public final DoubleValue blackSpiderJumpStrength;
   public final DoubleValue blackSpiderSwimSpeed;
   public final IntValue blackSpiderGoldDrop;
   public final DoubleValue evilCentipedeJumpStrength;
   public final DoubleValue evilCentipedeMaxHealth;
   public final DoubleValue evilCentipedeAttackDamage;
   public final DoubleValue evilCentipedeKnockbackResistance;
   public final DoubleValue evilCentipedeArmor;
   public final DoubleValue evilCentipedeArmorToughness;
   public final DoubleValue evilCentipedeAttackSpeed;
   public final DoubleValue evilCentipedeAttackKnockback;
   public final DoubleValue evilCentipedeFollowRange;
   public final DoubleValue evilCentipedeMovementSpeed;
   public final DoubleValue evilCentipedeAttackRange;
   public final DoubleValue evilCentipedeMaxSpiritualHealth;
   public final IntValue evilCentipedeGoldDrop;
   public final DoubleValue tempestSerpentJumpStrength;
   public final DoubleValue tempestSerpentMaxHealth;
   public final DoubleValue tempestSerpentAttackDamage;
   public final DoubleValue tempestSerpentKnockbackResistance;
   public final DoubleValue tempestSerpentArmor;
   public final DoubleValue tempestSerpentArmorToughness;
   public final DoubleValue tempestSerpentAttackSpeed;
   public final DoubleValue tempestSerpentAttackKnockback;
   public final DoubleValue tempestSerpentFollowRange;
   public final DoubleValue tempestSerpentMovementSpeed;
   public final DoubleValue tempestSerpentMaxSpiritualHealth;
   public final IntValue tempestSerpentGoldDrop;
   public final DoubleValue ogreLordJumpStrength;
   public final DoubleValue ogreLordMaxHealth;
   public final DoubleValue ogreLordAttackDamage;
   public final DoubleValue ogreLordKnockbackResistance;
   public final DoubleValue ogreLordArmor;
   public final DoubleValue ogreLordArmorToughness;
   public final DoubleValue ogreLordAttackSpeed;
   public final DoubleValue ogreLordAttackKnockback;
   public final DoubleValue ogreLordFollowRange;
   public final DoubleValue ogreLordMovementSpeed;
   public final DoubleValue ogreLordMaxSpiritualHealth;
   public final IntValue ogreLordGoldDrop;
   public final IntValue bossKillCap;

   public BossConfig(Builder builder) {
      builder.push("mezulAttributes");
      this.mezulMovementSpeed = builder.comment("Mezul's movement speed.").defineInRange("mezulMovementSpeed", 0.3, 0.0, Double.MAX_VALUE);
      this.mezulMaxHealth = builder.comment("Mezul's maximum health.").defineInRange("mezulMaxHealth", 2000.0, 0.0, Double.MAX_VALUE);
      this.mezulArmor = builder.comment("Mezul's armor value.").defineInRange("mezulArmor", 200.0, 0.0, Double.MAX_VALUE);
      this.mezulAttackDamage = builder.comment("Mezul's attack damage.").defineInRange("mezulAttackDamage", 500.0, 0.0, Double.MAX_VALUE);
      this.mezulFollowRange = builder.comment("Mezul's follow range.").defineInRange("mezulFollowRange", 32.0, 0.0, Double.MAX_VALUE);
      this.mezulKnockbackResistance = builder.comment("Mezul's knockback resistance.").defineInRange("mezulKnockbackResistance", 1.0, 0.0, 1.0);
      this.mezulAttackKnockback = builder.comment("Mezul's attack knockback.").defineInRange("mezulAttackKnockback", 1.0, 0.0, Double.MAX_VALUE);
      this.mezulMaxSpiritualHealth = builder.comment("Mezul's maximum spiritual health.")
         .defineInRange("mezulMaxSpiritualHealth", 4000.0, 0.0, Double.MAX_VALUE);
      this.mezulGoldDrop = builder.comment("Number of gold coins Mezul will drop the first time a player kills him. Default: 100, Min: 1, Max: 999")
         .defineInRange("mezulGoldDrop", 100, 1, 999);
      builder.pop();
      builder.push("gozulAttributes");
      this.gozulMovementSpeed = builder.comment("Gozul's movement speed.").defineInRange("gozulMovementSpeed", 0.3, 0.0, Double.MAX_VALUE);
      this.gozulMaxHealth = builder.comment("Gozul's maximum health.").defineInRange("gozulMaxHealth", 2000.0, 0.0, Double.MAX_VALUE);
      this.gozulArmor = builder.comment("Gozul's armor value.").defineInRange("gozulArmor", 200.0, 0.0, Double.MAX_VALUE);
      this.gozulAttackDamage = builder.comment("Gozul's attack damage.").defineInRange("gozulAttackDamage", 500.0, 0.0, Double.MAX_VALUE);
      this.gozulFollowRange = builder.comment("Gozul's follow range.").defineInRange("gozulFollowRange", 32.0, 0.0, Double.MAX_VALUE);
      this.gozulKnockbackResistance = builder.comment("Gozul's knockback resistance.").defineInRange("gozulKnockbackResistance", 1.0, 0.0, 1.0);
      this.gozulAttackKnockback = builder.comment("Gozul's attack knockback.").defineInRange("gozulAttackKnockback", 1.0, 0.0, Double.MAX_VALUE);
      this.gozulMaxSpiritualHealth = builder.comment("Gozul's maximum spiritual health.")
         .defineInRange("gozulMaxSpiritualHealth", 4000.0, 0.0, Double.MAX_VALUE);
      this.gozulGoldDrop = builder.comment("Number of gold coins Gozul will drop the first time a player kills him. Default: 100, Min: 1, Max: 999")
         .defineInRange("gozulGoldDrop", 100, 1, 999);
      builder.pop();
      builder.push("blackSpiderAttributes");
      this.blackSpiderMovementSpeed = builder.comment("Black Spider Boss movement speed.")
         .defineInRange("blackSpiderMovementSpeed", 0.25, 0.0, Double.MAX_VALUE);
      this.blackSpiderMaxHealth = builder.comment("Black Spider Boss maximum health.").defineInRange("blackSpiderMaxHealth", 250.0, 0.0, Double.MAX_VALUE);
      this.blackSpiderArmor = builder.comment("Black Spider Boss armor value.").defineInRange("blackSpiderArmor", 10.0, 0.0, Double.MAX_VALUE);
      this.blackSpiderAttackDamage = builder.comment("Black Spider Boss attack damage.").defineInRange("blackSpiderAttackDamage", 25.0, 0.0, Double.MAX_VALUE);
      this.blackSpiderFollowRange = builder.comment("Black Spider Boss follow range.").defineInRange("blackSpiderFollowRange", 32.0, 0.0, Double.MAX_VALUE);
      this.blackSpiderKnockbackResistance = builder.comment("Black Spider Boss knockback resistance.")
         .defineInRange("blackSpiderKnockbackResistance", 0.8, 0.0, 1.0);
      this.blackSpiderJumpStrength = builder.comment("Black Spider Boss jump strength.").defineInRange("blackSpiderJumpStrength", 1.5, 0.0, Double.MAX_VALUE);
      this.blackSpiderSwimSpeed = builder.comment("Black Spider Boss swim speed.").defineInRange("blackSpiderSwimSpeed", 3.0, 0.0, Double.MAX_VALUE);
      this.blackSpiderGoldDrop = builder.comment(
            "Number of gold coins the Black Spider will drop the first time a player kills it. Default: 3, Min: 1, Max: 999"
         )
         .defineInRange("blackSpiderGoldDrop", 3, 1, 999);
      builder.pop();
      builder.push("evilCentipedeAttributes");
      this.evilCentipedeJumpStrength = builder.comment("Evil Centipede Boss jump strength.")
         .defineInRange("evilCentipedeJumpStrength", 1.5, 0.0, Double.MAX_VALUE);
      this.evilCentipedeMaxHealth = builder.comment("Evil Centipede Boss maximum health.")
         .defineInRange("evilCentipedeMaxHealth", 500.0, 0.0, Double.MAX_VALUE);
      this.evilCentipedeAttackDamage = builder.comment("Evil Centipede Boss attack damage.")
         .defineInRange("evilCentipedeAttackDamage", 50.0, 0.0, Double.MAX_VALUE);
      this.evilCentipedeKnockbackResistance = builder.comment("Evil Centipede Boss knockback resistance.")
         .defineInRange("evilCentipedeKnockbackResistance", 1.0, 0.0, 1.0);
      this.evilCentipedeArmor = builder.comment("Evil Centipede Boss armor value.").defineInRange("evilCentipedeArmor", 15.0, 0.0, Double.MAX_VALUE);
      this.evilCentipedeArmorToughness = builder.comment("Evil Centipede Boss armor toughness.")
         .defineInRange("evilCentipedeArmorToughness", 25.0, 0.0, Double.MAX_VALUE);
      this.evilCentipedeAttackSpeed = builder.comment("Evil Centipede Boss attack speed.")
         .defineInRange("evilCentipedeAttackSpeed", 5.0, 0.0, Double.MAX_VALUE);
      this.evilCentipedeAttackKnockback = builder.comment("Evil Centipede Boss attack knockback.")
         .defineInRange("evilCentipedeAttackKnockback", 5.0, 0.0, Double.MAX_VALUE);
      this.evilCentipedeFollowRange = builder.comment("Evil Centipede Boss follow range.")
         .defineInRange("evilCentipedeFollowRange", 40.0, 0.0, Double.MAX_VALUE);
      this.evilCentipedeMovementSpeed = builder.comment("Evil Centipede Boss movement speed.")
         .defineInRange("evilCentipedeMovementSpeed", 0.4, 0.0, Double.MAX_VALUE);
      this.evilCentipedeAttackRange = builder.comment("Evil Centipede Boss attack range.")
         .defineInRange("evilCentipedeAttackRange", 2.0, 0.0, Double.MAX_VALUE);
      this.evilCentipedeMaxSpiritualHealth = builder.comment("Evil Centipede Boss maximum spiritual health.")
         .defineInRange("evilCentipedeMaxSpiritualHealth", 1000.0, 0.0, Double.MAX_VALUE);
      this.evilCentipedeGoldDrop = builder.comment(
            "Number of gold coins the Evil Centipede will drop the first time a player kills it. Default: 5, Min: 1, Max: 999"
         )
         .defineInRange("evilCentipedeGoldDrop", 5, 1, 999);
      builder.pop();
      builder.push("tempestSerpentAttributes");
      this.tempestSerpentJumpStrength = builder.comment("Tempest Serpent Boss jump strength.")
         .defineInRange("tempestSerpentJumpStrength", 1.5, 0.0, Double.MAX_VALUE);
      this.tempestSerpentMaxHealth = builder.comment("Tempest Serpent Boss maximum health.")
         .defineInRange("tempestSerpentMaxHealth", 1000.0, 0.0, Double.MAX_VALUE);
      this.tempestSerpentAttackDamage = builder.comment("Tempest Serpent Boss attack damage.")
         .defineInRange("tempestSerpentAttackDamage", 200.0, 0.0, Double.MAX_VALUE);
      this.tempestSerpentKnockbackResistance = builder.comment("Tempest Serpent Boss knockback resistance.")
         .defineInRange("tempestSerpentKnockbackResistance", 1.0, 0.0, 1.0);
      this.tempestSerpentArmor = builder.comment("Tempest Serpent Boss armor value.").defineInRange("tempestSerpentArmor", 100.0, 0.0, Double.MAX_VALUE);
      this.tempestSerpentArmorToughness = builder.comment("Tempest Serpent Boss armor toughness.")
         .defineInRange("tempestSerpentArmorToughness", 25.0, 0.0, Double.MAX_VALUE);
      this.tempestSerpentAttackSpeed = builder.comment("Tempest Serpent Boss attack speed.")
         .defineInRange("tempestSerpentAttackSpeed", 5.0, 0.0, Double.MAX_VALUE);
      this.tempestSerpentAttackKnockback = builder.comment("Tempest Serpent Boss attack knockback.")
         .defineInRange("tempestSerpentAttackKnockback", 5.0, 0.0, Double.MAX_VALUE);
      this.tempestSerpentFollowRange = builder.comment("Tempest Serpent Boss follow range.")
         .defineInRange("tempestSerpentFollowRange", 40.0, 0.0, Double.MAX_VALUE);
      this.tempestSerpentMovementSpeed = builder.comment("Tempest Serpent Boss movement speed.")
         .defineInRange("tempestSerpentMovementSpeed", 0.4, 0.0, Double.MAX_VALUE);
      this.tempestSerpentMaxSpiritualHealth = builder.comment("Tempest Serpent Boss maximum spiritual health.")
         .defineInRange("tempestSerpentMaxSpiritualHealth", 2000.0, 0.0, Double.MAX_VALUE);
      this.tempestSerpentGoldDrop = builder.comment(
            "Number of gold coins the Tempest Serpent will drop the first time a player kills it. Default: 20, Min: 1, Max: 999"
         )
         .defineInRange("tempestSerpentGoldDrop", 20, 1, 999);
      builder.pop();
      builder.push("ogreLordAttributes");
      this.ogreLordJumpStrength = builder.comment("Ogre Lord jump strength.").defineInRange("ogreLordJumpStrength", 1.5, 0.0, Double.MAX_VALUE);
      this.ogreLordMaxHealth = builder.comment("Ogre Lord maximum health.").defineInRange("ogreLordMaxHealth", 250.0, 0.0, Double.MAX_VALUE);
      this.ogreLordAttackDamage = builder.comment("Ogre Lord attack damage.").defineInRange("ogreLordAttackDamage", 100.0, 0.0, Double.MAX_VALUE);
      this.ogreLordKnockbackResistance = builder.comment("Ogre Lord knockback resistance.").defineInRange("ogreLordKnockbackResistance", 1.0, 0.0, 1.0);
      this.ogreLordArmor = builder.comment("Ogre Lord armor value.").defineInRange("ogreLordArmor", 30.0, 0.0, Double.MAX_VALUE);
      this.ogreLordArmorToughness = builder.comment("Ogre Lord armor toughness.").defineInRange("ogreLordArmorToughness", 25.0, 0.0, Double.MAX_VALUE);
      this.ogreLordAttackSpeed = builder.comment("Ogre Lord attack speed.").defineInRange("ogreLordAttackSpeed", 5.0, 0.0, Double.MAX_VALUE);
      this.ogreLordAttackKnockback = builder.comment("Ogre Lord attack knockback.").defineInRange("ogreLordAttackKnockback", 5.0, 0.0, Double.MAX_VALUE);
      this.ogreLordFollowRange = builder.comment("Ogre Lord follow range.").defineInRange("ogreLordFollowRange", 40.0, 0.0, Double.MAX_VALUE);
      this.ogreLordMovementSpeed = builder.comment("Ogre Lord movement speed.").defineInRange("ogreLordMovementSpeed", 0.4, 0.0, Double.MAX_VALUE);
      this.ogreLordMaxSpiritualHealth = builder.comment("Ogre Lord maximum spiritual health.")
         .defineInRange("ogreLordMaxSpiritualHealth", 500.0, 0.0, Double.MAX_VALUE);
      this.ogreLordGoldDrop = builder.comment("Number of gold coins the Ogre Lord will drop the first time a player kills it. Default: 10, Min: 1, Max: 999")
         .defineInRange("ogreLordGoldDrop", 10, 1, 999);
      builder.pop();
      builder.push("bossKillCap");
      this.bossKillCap = builder.comment(
            "Number of times a single boss type can be killed within an hour without breaking dungeon rules and getting attacked by the dryads. Default: 4, Min: 1, Max: 999"
         )
         .defineInRange("bossKillCap", 4, 1, 999);
      builder.pop();
   }
}
