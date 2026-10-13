package io.github.Memoires.trbeyond.data;

import com.github.manasmods.tensura.data.TensuraTags.EntityTypes;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

public class BeyondEntityTypeTagProvider extends EntityTypeTagsProvider {
   public BeyondEntityTypeTagProvider(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
      super(generator, modId, existingFileHelper);
   }

   public BeyondEntityTypeTagProvider(GatherDataEvent gatherDataEvent) {
      this(gatherDataEvent.getGenerator(), "trbeyond", gatherDataEvent.getExistingFileHelper());
   }

   protected void addTags() {
      this.tag(EntityTypes.SLIME_WALKABLE_MOBS)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.MINH_SLIME.get(),
               (EntityType)BeyondEntities.SILVER_MIMIC_CHEST.get(),
               (EntityType)BeyondEntities.GOLD_MIMIC_CHEST.get(),
               (EntityType)BeyondEntities.SLIME_WALL.get(),
               (EntityType)BeyondEntities.PRIMED_CHEST.get(),
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KILLER_DRYAD.get(),
               (EntityType)BeyondEntities.BLUE_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.GREEN_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.GREEN_SLIME_DOLL_MINION.get(),
               (EntityType)BeyondEntities.RED_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.YELLOW_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.GOZUL.get(),
               (EntityType)BeyondEntities.MEZUL.get()
            }
         );
      this.tag(EntityTypes.DROP_CRYSTAL)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.MINH_SLIME.get(),
               (EntityType)BeyondEntities.SILVER_MIMIC_CHEST.get(),
               (EntityType)BeyondEntities.GOLD_MIMIC_CHEST.get(),
               (EntityType)BeyondEntities.SLIME_WALL.get(),
               (EntityType)BeyondEntities.PRIMED_CHEST.get(),
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KILLER_DRYAD.get(),
               (EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(),
               (EntityType)BeyondEntities.OGRE_LORD.get(),
               (EntityType)BeyondEntities.BLUE_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.GREEN_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.GREEN_SLIME_DOLL_MINION.get(),
               (EntityType)BeyondEntities.RED_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.YELLOW_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.GOZUL.get(),
               (EntityType)BeyondEntities.MEZUL.get()
            }
         );
      this.tag(EntityTypes.HERO_BOSS)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(),
               (EntityType)BeyondEntities.GOZUL.get(),
               (EntityType)BeyondEntities.MEZUL.get(),
               (EntityType)BeyondEntities.EVIL_CENTIPEDE_BOSS.get(),
               (EntityType)BeyondEntities.OGRE_LORD.get(),
               (EntityType)BeyondEntities.TEMPEST_SERPENT_BOSS.get()
            }
         );
      this.tag(EntityTypes.NO_FEAR)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(),
               (EntityType)BeyondEntities.GOZUL.get(),
               (EntityType)BeyondEntities.MEZUL.get(),
               (EntityType)BeyondEntities.EVIL_CENTIPEDE_BOSS.get(),
               (EntityType)BeyondEntities.OGRE_LORD.get(),
               (EntityType)BeyondEntities.TEMPEST_SERPENT_BOSS.get(),
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KUROBE.get(),
               (EntityType)BeyondEntities.GUILD_TESTER.get(),
               (EntityType)BeyondEntities.GUILD_LADY.get(),
               (EntityType)BeyondEntities.TRAINING_DUMMY.get(),
               (EntityType)BeyondEntities.KAIJIN.get(),
               (EntityType)BeyondEntities.MJOLLMILE.get()
            }
         );
      this.tag(EntityTypes.NO_POSSESSION)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(),
               (EntityType)BeyondEntities.GOZUL.get(),
               (EntityType)BeyondEntities.MEZUL.get(),
               (EntityType)BeyondEntities.EVIL_CENTIPEDE_BOSS.get(),
               (EntityType)BeyondEntities.OGRE_LORD.get(),
               (EntityType)BeyondEntities.TEMPEST_SERPENT_BOSS.get(),
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KUROBE.get(),
               (EntityType)BeyondEntities.GUILD_TESTER.get(),
               (EntityType)BeyondEntities.GUILD_LADY.get(),
               (EntityType)BeyondEntities.TRAINING_DUMMY.get(),
               (EntityType)BeyondEntities.KILLER_DRYAD.get(),
               (EntityType)BeyondEntities.KAIJIN.get(),
               (EntityType)BeyondEntities.MJOLLMILE.get()
            }
         );
      this.tag(EntityTypes.NO_MIND_CONTROL)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(),
               (EntityType)BeyondEntities.GOZUL.get(),
               (EntityType)BeyondEntities.MEZUL.get(),
               (EntityType)BeyondEntities.EVIL_CENTIPEDE_BOSS.get(),
               (EntityType)BeyondEntities.OGRE_LORD.get(),
               (EntityType)BeyondEntities.TEMPEST_SERPENT_BOSS.get(),
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KUROBE.get(),
               (EntityType)BeyondEntities.GUILD_TESTER.get(),
               (EntityType)BeyondEntities.GUILD_LADY.get(),
               (EntityType)BeyondEntities.TRAINING_DUMMY.get(),
               (EntityType)BeyondEntities.KILLER_DRYAD.get(),
               (EntityType)BeyondEntities.KAIJIN.get(),
               (EntityType)BeyondEntities.MJOLLMILE.get()
            }
         );
      this.tag(EntityTypes.NO_CHARISMA)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(),
               (EntityType)BeyondEntities.GOZUL.get(),
               (EntityType)BeyondEntities.MEZUL.get(),
               (EntityType)BeyondEntities.EVIL_CENTIPEDE_BOSS.get(),
               (EntityType)BeyondEntities.OGRE_LORD.get(),
               (EntityType)BeyondEntities.TEMPEST_SERPENT_BOSS.get(),
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KUROBE.get(),
               (EntityType)BeyondEntities.GUILD_TESTER.get(),
               (EntityType)BeyondEntities.GUILD_LADY.get(),
               (EntityType)BeyondEntities.TRAINING_DUMMY.get(),
               (EntityType)BeyondEntities.LIVING_ARMOR.get(),
               (EntityType)BeyondEntities.PRIMED_CHEST.get(),
               (EntityType)BeyondEntities.KILLER_DRYAD.get(),
               (EntityType)BeyondEntities.LIVING_ARMOR.get(),
               (EntityType)BeyondEntities.MILIM_SLIME_SCARLET.get(),
               (EntityType)BeyondEntities.MAGE.get(),
               (EntityType)BeyondEntities.BABY_DRAGON.get(),
               (EntityType)BeyondEntities.SILVER_MIMIC_CHEST.get(),
               (EntityType)BeyondEntities.GOLD_MIMIC_CHEST.get(),
               (EntityType)BeyondEntities.KAIJIN.get(),
               (EntityType)BeyondEntities.MJOLLMILE.get()
            }
         );
      this.tag(EntityTypes.NO_BLOOD)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(),
               (EntityType)BeyondEntities.MINH_SLIME.get(),
               (EntityType)BeyondEntities.SILVER_MIMIC_CHEST.get(),
               (EntityType)BeyondEntities.GOLD_MIMIC_CHEST.get(),
               (EntityType)BeyondEntities.SLIME_WALL.get(),
               (EntityType)BeyondEntities.PRIMED_CHEST.get(),
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KILLER_DRYAD.get(),
               (EntityType)BeyondEntities.SLIME_WALL.get(),
               (EntityType)BeyondEntities.BLUE_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.GREEN_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.GREEN_SLIME_DOLL_MINION.get(),
               (EntityType)BeyondEntities.RED_SLIME_DOLL.get(),
               (EntityType)BeyondEntities.YELLOW_SLIME_DOLL.get()
            }
         );
      this.tag(BeyondTags.DUNGEON_BOSS)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get(),
               (EntityType)BeyondEntities.GOZUL.get(),
               (EntityType)BeyondEntities.MEZUL.get(),
               (EntityType)BeyondEntities.EVIL_CENTIPEDE_BOSS.get(),
               (EntityType)BeyondEntities.OGRE_LORD.get(),
               (EntityType)BeyondEntities.TEMPEST_SERPENT_BOSS.get()
            }
         );
      this.tag(BeyondTags.NOT_ELITE)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KUROBE.get(),
               (EntityType)BeyondEntities.GUILD_TESTER.get(),
               (EntityType)BeyondEntities.GUILD_LADY.get(),
               (EntityType)BeyondEntities.TRAINING_DUMMY.get(),
               (EntityType)BeyondEntities.KILLER_DRYAD.get(),
               (EntityType)BeyondEntities.KAIJIN.get(),
               (EntityType)TensuraEntityTypes.HINATA_SAKAGUCHI.get(),
               (EntityType)TensuraEntityTypes.MARK_LAUREN.get(),
               (EntityType)TensuraEntityTypes.FOLGEN.get(),
               (EntityType)TensuraEntityTypes.SHOGO_TAGUCHI.get(),
               (EntityType)TensuraEntityTypes.SHIN_RYUSEI.get(),
               (EntityType)TensuraEntityTypes.SHINJI_TANIMURA.get(),
               (EntityType)TensuraEntityTypes.MAI_FURUKI.get(),
               (EntityType)TensuraEntityTypes.KIRARA_MIZUTANI.get(),
               (EntityType)TensuraEntityTypes.KYOYA_TACHIBANA.get(),
               (EntityType)TensuraEntityTypes.FALMUTH_KNIGHT.get(),
               (EntityType)TensuraEntityTypes.SHIZU.get(),
               (EntityType)TensuraEntityTypes.CHARYBDIS.get(),
               (EntityType)TensuraEntityTypes.CHARYBDIS_CORE.get(),
               (EntityType)TensuraEntityTypes.DRAGON_PEACOCK.get(),
               (EntityType)TensuraEntityTypes.ONE_EYED_OWL.get(),
               (EntityType)TensuraEntityTypes.BULLDEER.get(),
               (EntityType)TensuraEntityTypes.SLIME.get(),
               (EntityType)BeyondEntities.MJOLLMILE.get(),
               (EntityType)BeyondEntities.KOBY.get()
            }
         );
      this.tag(BeyondTags.NOT_ELITE).addOptional(new ResourceLocation("dummmmmmy", "target_dummy"));
      this.tag(EntityTypes.NO_ENERGY_DRAIN)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KUROBE.get(),
               (EntityType)BeyondEntities.GUILD_TESTER.get(),
               (EntityType)BeyondEntities.GUILD_LADY.get(),
               (EntityType)BeyondEntities.TRAINING_DUMMY.get(),
               (EntityType)BeyondEntities.KILLER_DRYAD.get(),
               (EntityType)BeyondEntities.KAIJIN.get(),
               (EntityType)BeyondEntities.MJOLLMILE.get()
            }
         );
      this.tag(EntityTypes.NO_SPIRITUAL_DAMAGE)
         .add(
            new EntityType[]{
               (EntityType)BeyondEntities.DRYAD.get(),
               (EntityType)BeyondEntities.KUROBE.get(),
               (EntityType)BeyondEntities.GUILD_TESTER.get(),
               (EntityType)BeyondEntities.GUILD_LADY.get(),
               (EntityType)BeyondEntities.TRAINING_DUMMY.get(),
               (EntityType)BeyondEntities.KILLER_DRYAD.get(),
               (EntityType)BeyondEntities.KAIJIN.get(),
               (EntityType)BeyondEntities.MJOLLMILE.get()
            }
         );
   }
}
