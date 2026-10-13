package io.github.Memoires.trbeyond.items.client;

import com.github.manasmods.tensura.item.armor.client.TensuraGeoArmorRenderer;
import io.github.Memoires.trbeyond.items.armor.BeyondArmorItem;

public class BeyondArmorRenderer extends TensuraGeoArmorRenderer<BeyondArmorItem> {
   public BeyondArmorRenderer() {
      super(new BeyondArmorModel());
      this.rightArmBone = "armorRightArm";
      this.leftArmBone = "armorLeftArm";
   }
}
