package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.manascore.api.skills.ManasSkill;
import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.event.SkillActivationEvent;
import com.github.manasmods.tensura.ability.battlewill.utility.InstantMoveArt;
import com.github.manasmods.tensura.ability.magic.spiritual.space.ShrinkMagic;
import com.github.manasmods.tensura.ability.skill.intrinsic.CharmSkill;
import com.github.manasmods.tensura.ability.skill.intrinsic.PossessionSkill;
import com.github.manasmods.tensura.ability.skill.unique.BewilderSkill;
import com.github.manasmods.tensura.ability.skill.unique.ChosenOneSkill;
import com.github.manasmods.tensura.ability.skill.unique.GreedSkill;
import com.github.manasmods.tensura.ability.skill.unique.MartialMasterSkill;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "trbeyond",
   bus = Bus.FORGE
)
public class BannedAbilitiesHandler {
   @SubscribeEvent
   public static void bannedAbilities(SkillActivationEvent event) {
      if (event.getEntity().getLevel().dimension().equals(BeyondDungeon.DUNGEON_LEVEL_KEY)) {
         ManasSkillInstance manasSkillInstance = event.getSkillInstance();
         ManasSkill skill = manasSkillInstance.getSkill();
         if (manasSkillInstance.getOrCreateTag().getBoolean("disabled")) {
            event.setCanceled(true);
         }

         if (!(skill instanceof MartialMasterSkill)
            && !(skill instanceof PossessionSkill)
            && !(skill instanceof CharmSkill)
            && !(skill instanceof ShrinkMagic)
            && !(skill instanceof InstantMoveArt)) {
            if (skill instanceof BewilderSkill && manasSkillInstance.getMode() != 3) {
               event.setCanceled(true);
            } else if (skill instanceof ChosenOneSkill && manasSkillInstance.getMode() == 2) {
               event.setCanceled(true);
            } else if (skill instanceof GreedSkill && manasSkillInstance.getMode() == 1) {
               event.setCanceled(true);
            }

            if (event.isCanceled()) {
               event.getEntity().sendSystemMessage(Component.literal("This mode cannot be used in the dungeon.").withStyle(style -> style.withColor(16733525)));
            }
         } else {
            event.setCanceled(true);
            event.getEntity().sendSystemMessage(Component.literal("This ability cannot be used in the dungeon.").withStyle(style -> style.withColor(16733525)));
         }
      }
   }
}
