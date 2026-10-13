package io.github.Memoires.trbeyond.data;

import com.github.manasmods.manascore.api.data.gen.CustomDataProvider;
import com.github.manasmods.tensura.data.pack.EntityEPCount;
import com.github.manasmods.tensura.registry.battlewill.MeleeArts;
import com.github.manasmods.tensura.registry.battlewill.UtilityArts;
import com.github.manasmods.tensura.registry.skill.CommonSkills;
import com.github.manasmods.tensura.registry.skill.ExtraSkills;
import com.github.manasmods.tensura.registry.skill.IntrinsicSkills;
import com.github.manasmods.tensura.registry.skill.ResistanceSkills;
import com.github.manasmods.tensura.registry.skill.UniqueSkills;
import com.google.gson.JsonElement;
import io.github.Memoires.trbeyond.registry.entities.BeyondEntities;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

public class BeyondEntityEPProvider extends CustomDataProvider {
   public BeyondEntityEPProvider(GatherDataEvent gatherDataEvent) {
      super("entity/ep", gatherDataEvent.getGenerator());
   }

   @NotNull
   public String getName() {
      return "Beyond Entity EP";
   }

   protected void run(@NotNull BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      beyondMob(biConsumer);
   }

   protected static void beyondMob(BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
      List<ResourceLocation> blackSpiderSkills = new ArrayList<>();
      blackSpiderSkills.add(ExtraSkills.STICKY_STEEL_THREAD.getId());
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.BOSS_BLACK_SPIDER.get()), 70000, 80000, blackSpiderSkills).buildJson(biConsumer);
      List<ResourceLocation> gozulSkills = new ArrayList<>();
      gozulSkills.add(ExtraSkills.ULTRASPEED_REGENERATION.getId());
      gozulSkills.add(ExtraSkills.MAGIC_JAMMING.getId());
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.GOZUL.get()), 130000, 130000, gozulSkills).buildJson(biConsumer);
      List<ResourceLocation> slimebossfusionSkills = new ArrayList<>();
      slimebossfusionSkills.add(ExtraSkills.ULTRASPEED_REGENERATION.getId());
      slimebossfusionSkills.add(ResistanceSkills.PHYSICAL_ATTACK_NULLIFICATION.getId());
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.SLIME_DOLL_BOSS.get()), 60000, 130000, slimebossfusionSkills).buildJson(biConsumer);
      List<ResourceLocation> slimebossminionsSkills = new ArrayList<>();
      slimebossminionsSkills.add(CommonSkills.SELF_REGENERATION.getId());
      slimebossminionsSkills.add(ResistanceSkills.PHYSICAL_ATTACK_RESISTANCE.getId());
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.SLIME_DOLL_MINIONS.get()), 13000, 10000, slimebossminionsSkills).buildJson(biConsumer);
      List<ResourceLocation> mezulSkills = new ArrayList<>();
      mezulSkills.add(ExtraSkills.ULTRASPEED_REGENERATION.getId());
      mezulSkills.add(ExtraSkills.MAGIC_JAMMING.getId());
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.MEZUL.get()), 130000, 130000, mezulSkills).buildJson(biConsumer);
      List<ResourceLocation> slimeDollSkills = new ArrayList<>();
      slimeDollSkills.add(IntrinsicSkills.ABSORB_DISSOLVE.getId());
      slimeDollSkills.add(CommonSkills.SELF_REGENERATION.getId());
      slimeDollSkills.add(ResistanceSkills.PHYSICAL_ATTACK_RESISTANCE.getId());
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.BLUE_SLIME_DOLL.get()), 100000, 120000, slimeDollSkills).buildJson(biConsumer);
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.GREEN_SLIME_DOLL.get()), 100000, 120000, slimeDollSkills).buildJson(biConsumer);
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.GREEN_SLIME_DOLL_MINION.get()), 20000, 24000, slimeDollSkills).buildJson(biConsumer);
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.RED_SLIME_DOLL.get()), 100000, 120000, slimeDollSkills).buildJson(biConsumer);
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.YELLOW_SLIME_DOLL.get()), 100000, 120000, slimeDollSkills).buildJson(biConsumer);
      List<ResourceLocation> scarletSkills = new ArrayList<>();
      scarletSkills.add(CommonSkills.SELF_REGENERATION.getId());
      scarletSkills.add(IntrinsicSkills.ABSORB_DISSOLVE.getId());
      scarletSkills.add(ResistanceSkills.PHYSICAL_ATTACK_RESISTANCE.getId());
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.SCARLET_SLIME.get()), 80000, 80000, scarletSkills).buildJson(biConsumer);
      EntityEPCount.of(
            EntityType.getKey((EntityType)BeyondEntities.MOMIJI.get()),
            600000,
            600000,
            Stream.of(ExtraSkills.GODWOLF_SENSE.getId()).filter(Objects::nonNull).toList()
         )
         .buildJson(biConsumer);
      EntityEPCount.of(
            EntityType.getKey((EntityType)BeyondEntities.SHION.get()),
            4229140,
            4229140,
            Stream.of(
                  ExtraSkills.ALL_SEEING_EYE.getId(),
                  ExtraSkills.INFINITE_REGENERATION.getId(),
                  ExtraSkills.MULTILAYER_BARRIER.getId(),
                  ExtraSkills.SPATIAL_DOMINATION.getId(),
                  ExtraSkills.STEEL_STRENGTH.getId(),
                  ExtraSkills.STRENGTHEN_BODY.getId(),
                  MeleeArts.OGRE_SWORD_GUILLOTINE.getId(),
                  ResistanceSkills.ABNORMAL_CONDITION_NULLIFICATION.getId(),
                  ResistanceSkills.DARKNESS_ATTACK_RESISTANCE.getId(),
                  ResistanceSkills.HOLY_ATTACK_RESISTANCE.getId(),
                  ResistanceSkills.SPIRITUAL_ATTACK_NULLIFICATION.getId(),
                  ResistanceSkills.PHYSICAL_ATTACK_NULLIFICATION.getId(),
                  ResistanceSkills.PAIN_NULLIFICATION.getId(),
                  UtilityArts.DIAMOND_PATH.getId(),
                  UniqueSkills.COOK.getId()
               )
               .filter(Objects::nonNull)
               .toList()
         )
         .buildJson(biConsumer);
      EntityEPCount.of(EntityType.getKey((EntityType)BeyondEntities.GRAVE_LIZARD.get()), 8000, 9000, List.of()).buildJson(biConsumer);
      EntityEPCount.of(
            EntityType.getKey((EntityType)BeyondEntities.KING_CRAWLER.get()),
            9000,
            10000,
            Stream.of(IntrinsicSkills.PARALYSING_BREATH.getId()).filter(Objects::nonNull).toList()
         )
         .buildJson(biConsumer);
   }
}
