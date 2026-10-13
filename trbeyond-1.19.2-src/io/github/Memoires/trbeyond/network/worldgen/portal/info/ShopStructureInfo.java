package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class ShopStructureInfo extends StraightStructureInfo {
   private final int shopOffset;

   protected ShopStructureInfo(ShopStructureInfo.ShopInfoBuilder builder) {
      super(builder);
      this.shopOffset = builder.shopOffset;
   }

   @NotNull
   public static ShopStructureInfo.ShopInfoBuilder builder() {
      return new ShopStructureInfo.ShopInfoBuilder();
   }

   public int getShopOffset() {
      return this.shopOffset;
   }

   public static class ShopInfoBuilder extends StraightStructureInfo.StraightInfoBuilder {
      private int shopOffset = 7;

      public ShopStructureInfo.ShopInfoBuilder setShopOffset(int shopOffset) {
         this.shopOffset = shopOffset;
         return this.getSelf();
      }

      public ShopStructureInfo.ShopInfoBuilder setAddress(String address) {
         this.address = address;
         return this.getSelf();
      }

      public ShopStructureInfo.ShopInfoBuilder setFacing(Direction facing) {
         this.facing = facing;
         return this.getSelf();
      }

      public ShopStructureInfo.ShopInfoBuilder setEntranceOffset(BlockPos entranceOffset) {
         this.entranceOffset = entranceOffset;
         return this.getSelf();
      }

      public ShopStructureInfo.ShopInfoBuilder setSize(BlockPos size) {
         this.size = size;
         return this.getSelf();
      }

      public ShopStructureInfo.ShopInfoBuilder setEntityPos(BlockPos entityPos) {
         this.entityPos = entityPos;
         return this.getSelf();
      }

      public ShopStructureInfo build() {
         return new ShopStructureInfo(this);
      }

      protected ShopStructureInfo.ShopInfoBuilder getSelf() {
         return this;
      }
   }
}
