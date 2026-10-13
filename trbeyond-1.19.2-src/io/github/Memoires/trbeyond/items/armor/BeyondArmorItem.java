package io.github.Memoires.trbeyond.items.armor;

import java.util.function.Predicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class BeyondArmorItem extends GeoArmorItem implements IAnimatable {
   private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

   public BeyondArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
      super(materialIn, slot, builder);
   }

   public void registerControllers(AnimationData data) {
   }

   public String getIdForRenderer() {
      return this.getMaterial().getName();
   }

   public static boolean hasFullSet(LivingEntity entity, Predicate<ItemStack> predicate, int number) {
      return hasFullSet(entity, predicate, number, false);
   }

   public static boolean hasFullSet(LivingEntity entity, Predicate<ItemStack> predicate, int number, boolean include) {
      int i = 0;

      for (EquipmentSlot slot : EquipmentSlot.values()) {
         if (!slot.equals(EquipmentSlot.OFFHAND) && (include || !slot.equals(EquipmentSlot.MAINHAND))) {
            if (predicate.test(entity.getItemBySlot(slot))) {
               i++;
            }

            if (i >= number) {
               return true;
            }
         }
      }

      return false;
   }

   public AnimationFactory getFactory() {
      return this.factory;
   }
}
