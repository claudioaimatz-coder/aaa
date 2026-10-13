package io.github.Memoires.trbeyond.handlers;

import com.github.manasmods.manascore.api.skills.ManasSkillInstance;
import com.github.manasmods.manascore.api.skills.SkillAPI;
import io.github.Memoires.trbeyond.config.ServerConfig;
import io.github.Memoires.trbeyond.network.worldgen.dimension.BeyondDungeon;
import java.util.List;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(
   modid = "trbeyond"
)
public class ConfigHandler {
   @SubscribeEvent
   public static void onEnterDungeon(PlayerChangedDimensionEvent event) {
      List<ManasSkillInstance> skills = SkillAPI.getSkillsFrom(event.getEntity())
         .getLearnedSkills()
         .stream()
         .filter(instance -> ((List)ServerConfig.INSTANCE.disableSkillsInDungeon.get()).contains(instance.getSkillId().toString()))
         .toList();
      if (event.getEntity().getLevel().dimension() == BeyondDungeon.DUNGEON_LEVEL_KEY) {
         skills.forEach(instance -> instance.getOrCreateTag().putBoolean("disabled", true));
      } else {
         skills.forEach(instance -> instance.getOrCreateTag().remove("disabled"));
      }
   }

   @SubscribeEvent
   static void onPlayerRespawn(PlayerRespawnEvent e) {
      List<ManasSkillInstance> skills = SkillAPI.getSkillsFrom(e.getEntity())
         .getLearnedSkills()
         .stream()
         .filter(instance -> ((List)ServerConfig.INSTANCE.disableSkillsInDungeon.get()).contains(instance.getSkillId().toString()))
         .toList();
      if (e.getEntity().getLevel().dimension() != BeyondDungeon.DUNGEON_LEVEL_KEY) {
         skills.forEach(instance -> instance.getOrCreateTag().remove("disabled"));
      }
   }
}
