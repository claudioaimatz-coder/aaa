package io.github.Memoires.trbeyond.block.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class DungenerationBlockEntity {
   private static final int GRID_WIDTH = 10;
   private static final int GRID_HEIGHT = 10;
   private DungenerationBlockEntity.RoomType[][] dungeonGrid = new DungenerationBlockEntity.RoomType[10][10];
   private Random random = new Random();

   public DungenerationBlockEntity() {
      this.initializeGrid();
      this.placeBossRoom();
      this.generateLabyrinth(0, 0);
   }

   private void initializeGrid() {
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            this.dungeonGrid[i][j] = DungenerationBlockEntity.RoomType.EMPTY;
         }
      }
   }

   private void placeBossRoom() {
      boolean placed = false;

      while (!placed) {
         int bossX = this.random.nextInt(9);
         int bossY = this.random.nextInt(9);
         if (this.isAreaEmpty(bossX, bossY)) {
            this.dungeonGrid[bossX][bossY] = DungenerationBlockEntity.RoomType.BOSS_ROOM;
            this.dungeonGrid[bossX + 1][bossY] = DungenerationBlockEntity.RoomType.BOSS_ROOM;
            this.dungeonGrid[bossX][bossY + 1] = DungenerationBlockEntity.RoomType.BOSS_ROOM;
            this.dungeonGrid[bossX + 1][bossY + 1] = DungenerationBlockEntity.RoomType.BOSS_ROOM;
            placed = true;
         }
      }
   }

   private boolean isAreaEmpty(int x, int y) {
      return this.dungeonGrid[x][y] == DungenerationBlockEntity.RoomType.EMPTY
         && this.dungeonGrid[x + 1][y] == DungenerationBlockEntity.RoomType.EMPTY
         && this.dungeonGrid[x][y + 1] == DungenerationBlockEntity.RoomType.EMPTY
         && this.dungeonGrid[x + 1][y + 1] == DungenerationBlockEntity.RoomType.EMPTY;
   }

   private void generateLabyrinth(int startX, int startY) {
      this.dungeonGrid[startX][startY] = DungenerationBlockEntity.RoomType.ROOM;
      int[] directions = new int[]{0, 1, 2, 3};
      Collections.shuffle(Arrays.asList(directions));

      for (int direction : directions) {
         int newX = startX;
         int newY = startY;
         switch (direction) {
            case 0:
               newX = startX - 1;
               break;
            case 1:
               newX = startX + 1;
               break;
            case 2:
               newY = startY - 1;
               break;
            case 3:
               newY = startY + 1;
         }

         if (this.isInBounds(newX, newY) && this.dungeonGrid[newX][newY] == DungenerationBlockEntity.RoomType.EMPTY) {
            this.dungeonGrid[newX][newY] = DungenerationBlockEntity.RoomType.ROOM;
            this.generateLabyrinth(newX, newY);
         }
      }
   }

   private boolean isInBounds(int x, int y) {
      return x >= 0 && x < 10 && y >= 0 && y < 10;
   }

   public DungenerationBlockEntity.RoomType[][] getDungeonGrid() {
      return this.dungeonGrid;
   }

   public void generateDungeon() {
      this.initializeGrid();
      this.placeBossRoom();
      this.generateLabyrinth(0, 0);
   }

   public static enum RoomType {
      EMPTY,
      ROOM,
      BOSS_ROOM;
   }
}
