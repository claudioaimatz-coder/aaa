package io.github.Memoires.trbeyond.block;

import com.github.manasmods.tensura.block.AllSidesDirectionalBlock;
import com.github.manasmods.tensura.entity.BlackSpiderEntity;
import com.github.manasmods.tensura.registry.entity.TensuraEntityTypes;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BossSpiderEggBlock extends AllSidesDirectionalBlock {
   public BossSpiderEggBlock() {
      super(
         Properties.of(Material.EGG, MaterialColor.TERRACOTTA_WHITE)
            .strength(0.5F)
            .lightLevel(blockState -> 3)
            .speedFactor(0.8F)
            .jumpFactor(0.9F)
            .sound(SoundType.SHROOMLIGHT)
            .randomTicks()
            .noOcclusion()
      );
   }

   public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
      pEntity.makeStuckInBlock(pState, new Vec3(0.2, 0.04F, 0.2));
   }

   public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
      pLevel.playSound(null, pPos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
      pLevel.removeBlock(pPos, false);
      pLevel.levelEvent(2001, pPos, Block.getId(pState));
      int count = pLevel.getRandom().nextInt(6, 9);

      for (int i = 0; i < count; i++) {
         BlackSpiderEntity spider = (BlackSpiderEntity)((EntityType)TensuraEntityTypes.BLACK_SPIDER.get()).create(pLevel);
         if (spider != null) {
            spider.setAge(-24000);
            spider.moveTo((double)pPos.getX() + 0.3, (double)pPos.getY(), (double)pPos.getZ() + 0.3, 0.0F, 0.0F);
            spider.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(spider.blockPosition()), MobSpawnType.BREEDING, null, null);
            pLevel.addFreshEntity(spider);
         }
      }
   }

   public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
      if (!pLevel.isClientSide) {
         pLevel.levelEvent(2005, pPos, 0);
      }
   }

   public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      Entity entity = pContext instanceof EntityCollisionContext entityCollision ? entityCollision.getEntity() : null;
      return entity != null ? Shapes.empty() : super.getCollisionShape(pState, pLevel, pPos, pContext);
   }

   @Nullable
   public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
      return BlockPathTypes.OPEN;
   }

   public PushReaction getPistonPushReaction(BlockState pState) {
      return PushReaction.NORMAL;
   }
}
