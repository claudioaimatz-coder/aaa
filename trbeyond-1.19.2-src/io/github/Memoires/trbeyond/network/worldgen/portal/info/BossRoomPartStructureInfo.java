package io.github.Memoires.trbeyond.network.worldgen.portal.info;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class BossRoomPartStructureInfo extends BaseStructureInfo<BossRoomPartStructureInfo.BossRoomPartInfoBuilder> {
   private final BossRoomPartStructureInfo.BossSpawnerProvider provider;
   private final BlockPos exitPosition;

   protected BossRoomPartStructureInfo(@NotNull BossRoomPartStructureInfo.BossRoomPartInfoBuilder builder) {
      super(builder);
      this.provider = builder.provider;
      this.exitPosition = builder.exitPosition;
   }

   @NotNull
   public static BossRoomPartStructureInfo.BossRoomPartInfoBuilder builder() {
      return new BossRoomPartStructureInfo.BossRoomPartInfoBuilder();
   }

   public BossRoomPartStructureInfo.BossSpawnerProvider getBossSpawner() {
      return this.provider;
   }

   private BlockPos getExitPosition() {
      return this.exitPosition;
   }

   public BlockPos calculateExitPosition(@NotNull BlockPos target, Direction facing) {
      return target.relative(facing, this.getExitPosition().getX())
         .offset(0, this.getExitPosition().getY(), 0)
         .relative(facing.getClockWise(), this.getExitPosition().getZ());
   }

   @Override
   public BlockPos calculatePlacementPos(BlockPos target, Direction facing) {
      return super.calculatePlacementPos(target, facing);
   }

   @Override
   public List<DungeonStructureInfo.DungeonCursor> calculateNewCursors(BlockPos target, Direction facing) {
      return List.of();
   }

   public static class BossRoomPartInfoBuilder extends InfoBuilder<BossRoomPartStructureInfo.BossRoomPartInfoBuilder> {
      private BossRoomPartStructureInfo.BossSpawnerProvider provider;
      private BlockPos exitPosition;

      public BossRoomPartStructureInfo build() {
         return new BossRoomPartStructureInfo(this);
      }

      public BossRoomPartStructureInfo.BossRoomPartInfoBuilder setProvider(BossRoomPartStructureInfo.BossSpawnerProvider provider) {
         this.provider = provider;
         return this.getSelf();
      }

      public BossRoomPartStructureInfo.BossRoomPartInfoBuilder setExitPosition(BlockPos exitPosition) {
         this.exitPosition = exitPosition;
         return this.getSelf();
      }

      protected BossRoomPartStructureInfo.BossRoomPartInfoBuilder getSelf() {
         return this;
      }
   }

   @FunctionalInterface
   public interface BossSpawnerProvider {
      void spawnBoss(ServerLevel var1, BlockPos var2, boolean var3, int var4, boolean var5);
   }
}
