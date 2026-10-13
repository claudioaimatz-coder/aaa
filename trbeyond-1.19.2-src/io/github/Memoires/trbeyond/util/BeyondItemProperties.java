package io.github.Memoires.trbeyond.util;

import io.github.Memoires.trbeyond.items.weapons.MinosBardicheItem;
import io.github.Memoires.trbeyond.registry.items.BeyondToolItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class BeyondItemProperties {
   public static void addItemProperties(FMLClientSetupEvent event) {
      event.enqueueWork(
         () -> {
            ItemProperties.register(
               (Item)BeyondToolItems.SHIBKOBO_UNIQUE.get(),
               new ResourceLocation("trbeyond", "attack_range"),
               (stack, level, living, id) -> stack.getItem().getShareTag(stack).getFloat("attack_range")
            );
            ItemProperties.register(
               (Item)BeyondToolItems.SHIBKOBO_GOD.get(),
               new ResourceLocation("trbeyond", "attack_range"),
               (stack, level, living, id) -> stack.getItem().getShareTag(stack).getFloat("attack_range")
            );
            ItemProperties.register(
               (Item)BeyondToolItems.SHIBKOBO_LEGEND.get(),
               new ResourceLocation("trbeyond", "attack_range"),
               (stack, level, living, id) -> stack.getItem().getShareTag(stack).getFloat("attack_range")
            );
            ItemProperties.register(
               (Item)BeyondToolItems.MINOS_BARDICHE_UNIQUE.get(),
               new ResourceLocation("trbeyond", "lightning_cores"),
               (stack, level, living, id) -> (float)MinosBardicheItem.getWindCores(stack)
            );
            ItemProperties.register(
               (Item)BeyondToolItems.MINOS_BARDICHE_LEGEND.get(),
               new ResourceLocation("trbeyond", "lightning_cores"),
               (stack, level, living, id) -> (float)MinosBardicheItem.getWindCores(stack)
            );
            ItemProperties.register(
               (Item)BeyondToolItems.MINOS_BARDICHE_GOD.get(),
               new ResourceLocation("trbeyond", "lightning_cores"),
               (stack, level, living, id) -> (float)MinosBardicheItem.getWindCores(stack)
            );
            ItemProperties.register(
               (Item)BeyondToolItems.GUREN_GOD.get(),
               new ResourceLocation("trbeyond", "black_flame"),
               (stack, level, living, id) -> stack.getItem().getShareTag(stack).getFloat("black_flame")
            );
            ItemProperties.register(
               (Item)BeyondToolItems.GUREN_LEGEND.get(),
               new ResourceLocation("trbeyond", "black_flame"),
               (stack, level, living, id) -> stack.getItem().getShareTag(stack).getFloat("black_flame")
            );
            ItemProperties.register(
               (Item)BeyondToolItems.GUREN_UNIQUE.get(),
               new ResourceLocation("trbeyond", "black_flame"),
               (stack, level, living, id) -> stack.getItem().getShareTag(stack).getFloat("black_flame")
            );
            ItemProperties.register(
               (Item)BeyondToolItems.GORIKIMARU_GOD.get(),
               new ResourceLocation("trbeyond", "ogre_cannon"),
               (stack, level, living, id) -> stack.getItem().getShareTag(stack).getFloat("ogre_cannon")
            );
            ItemProperties.register(
               (Item)BeyondToolItems.GORIKIMARU_LEGEND.get(),
               new ResourceLocation("trbeyond", "ogre_cannon"),
               (stack, level, living, id) -> stack.getItem().getShareTag(stack).getFloat("ogre_cannon")
            );
            ItemProperties.register(
               (Item)BeyondToolItems.GORIKIMARU_UNIQUE.get(),
               new ResourceLocation("trbeyond", "ogre_cannon"),
               (stack, level, living, id) -> stack.getItem().getShareTag(stack).getFloat("ogre_cannon")
            );
         }
      );
   }
}
