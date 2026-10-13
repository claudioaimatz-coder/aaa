package io.github.Memoires.trbeyond.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixTraversal {
   private List<FunctionSpecifications> functionList = new ArrayList<>();
   private double probabilityRight = 0.5;
   private double probabilityOne = 1.0;
   private double probabilityRoom = 0.0;
   private int numOfStructures;
   private boolean stairsPlaced;
   private double loopbackProb;
   private double deadendProb;
   private int rows;
   private int columns;
   private String[][] matrix;
   private int currentRow;
   private int currentCol;
   private String currentDirection;
   private Random random;

   public MatrixTraversal(int numOfStructures, double loopbackProb, double deadendProb, int rows, int columns, String direction) {
      this.numOfStructures = numOfStructures;
      this.stairsPlaced = false;
      this.loopbackProb = loopbackProb;
      this.deadendProb = deadendProb;
      this.rows = numOfStructures + 50;
      this.columns = numOfStructures + 50;
      if (this.columns % 2 == 0) {
         this.columns++;
      }

      if (this.rows < this.columns) {
         this.rows = this.columns;
      }

      this.matrix = new String[this.rows][this.columns];

      for (int i = 0; i < this.rows; i++) {
         for (int j = 0; j < this.columns; j++) {
            this.matrix[i][j] = " ";
         }
      }

      this.currentRow = this.rows / 2;
      this.currentCol = this.columns / 2;
      this.matrix[this.currentRow][this.currentCol] = "i";
      this.currentDirection = direction;
      this.random = new Random();
      this.moveUp("1");
   }

   public MatrixTraversal(int numOfStructures, double loopbackProb, double deadendProb, String direction) {
      this(numOfStructures, loopbackProb, deadendProb, 100, 100, direction);
   }

   public void displayMatrix() {
      for (int i = 0; i < this.rows; i++) {
         StringBuilder sb = new StringBuilder();

         for (int j = 0; j < this.columns; j++) {
            sb.append(this.matrix[i][j]).append(" ");
         }

         System.out.println(sb.toString().trim());
      }
   }

   public String readUp() {
      String var1 = this.currentDirection;
      switch (var1) {
         case "east":
            if (this.currentRow > 0) {
               this.currentRow--;
            }
            break;
         case "west":
            if (this.currentRow < this.rows - 1) {
               this.currentRow++;
            }
            break;
         case "south":
            if (this.currentCol < this.columns - 1) {
               this.currentCol++;
            }
            break;
         case "north":
            if (this.currentCol > 0) {
               this.currentCol--;
            }
      }

      return this.matrix[this.currentRow][this.currentCol];
   }

   public void moveUp(String value) {
      String var2 = this.currentDirection;
      switch (var2) {
         case "east":
            if (this.currentRow > 0) {
               this.currentRow--;
            }
            break;
         case "west":
            if (this.currentRow < this.rows - 1) {
               this.currentRow++;
            }
            break;
         case "south":
            if (this.currentCol < this.columns - 1) {
               this.currentCol++;
            }
            break;
         case "north":
            if (this.currentCol > 0) {
               this.currentCol--;
            }
      }

      this.matrix[this.currentRow][this.currentCol] = value;
   }

   public void moveDown(String value) {
      String var2 = this.currentDirection;
      switch (var2) {
         case "east":
            if (this.currentRow < this.rows - 1) {
               this.currentRow++;
            }
            break;
         case "west":
            if (this.currentRow > 0) {
               this.currentRow--;
            }
            break;
         case "south":
            if (this.currentCol > 0) {
               this.currentCol--;
            }
            break;
         case "north":
            if (this.currentCol < this.columns - 1) {
               this.currentCol++;
            }
      }

      this.matrix[this.currentRow][this.currentCol] = value;
   }

   public void moveLeft(String value) {
      String var2 = this.currentDirection;
      switch (var2) {
         case "east":
            if (this.currentCol > 0) {
               this.currentCol--;
            }
            break;
         case "west":
            if (this.currentCol < this.columns - 1) {
               this.currentCol++;
            }
            break;
         case "south":
            if (this.currentRow < this.rows - 1) {
               this.currentRow++;
            }
            break;
         case "north":
            if (this.currentRow > 0) {
               this.currentRow--;
            }
      }

      this.matrix[this.currentRow][this.currentCol] = value;
   }

   public void moveRight(String value) {
      String var2 = this.currentDirection;
      switch (var2) {
         case "east":
            if (this.currentCol < this.columns - 1) {
               this.currentCol++;
            }
            break;
         case "west":
            if (this.currentCol > 0) {
               this.currentCol--;
            }
            break;
         case "south":
            if (this.currentRow > 0) {
               this.currentRow--;
            }
            break;
         case "north":
            if (this.currentRow < this.rows - 1) {
               this.currentRow++;
            }
      }

      this.matrix[this.currentRow][this.currentCol] = value;
   }

   public void setDirection(String direction) {
      if (!direction.equals("east") && !direction.equals("west") && !direction.equals("north") && !direction.equals("south")) {
         throw new IllegalArgumentException("Invalid direction. Choose from 'east', 'west', 'north', 'south'.");
      } else {
         this.currentDirection = direction;
      }
   }

   public void placeDeadend() {
      this.moveUp("D");
      this.functionList.add(new FunctionSpecifications("deadend"));
   }

   public void placeCornerDeadend() {
      this.moveUp("2");
      this.changeRightPerspective();
      this.placeDeadend();
      if (!this.functionList.isEmpty()) {
         this.functionList.remove(this.functionList.size() - 1);
      }

      this.functionList.add(new FunctionSpecifications("corner_deadend"));
   }

   public void placeStairs() {
      if (!this.stairsPlaced) {
         this.moveUp("S");
         this.stairsPlaced = true;
         this.functionList.add(new FunctionSpecifications("stairs"));
      } else {
         this.placeDeadend();
      }
   }

   public void placeOne() {
      this.moveUp("1");
      this.functionList.add(new FunctionSpecifications("hallway"));
   }

   public void placeRoom() {
      this.moveUp("R");
      this.placeOne();
      if (!this.functionList.isEmpty()) {
         this.functionList.remove(this.functionList.size() - 1);
      }

      this.functionList.add(new FunctionSpecifications("room"));
      this.placeOne();
   }

   public void placeTwo() {
      this.moveUp("2");
      this.changeRightPerspective();
      this.placeOne();
      if (!this.functionList.isEmpty()) {
         this.functionList.remove(this.functionList.size() - 1);
      }

      this.functionList.add(new FunctionSpecifications("corner"));
   }

   public int determineHallwayLength() {
      int orgCol = this.currentCol;
      int orgRow = this.currentRow;
      int maxLength = 6;
      int length = 0;

      while (maxLength > 0) {
         maxLength--;
         if (this.readUp().equals(" ")) {
            length++;
         } else if (--length < 0) {
            length = 0;
         }
      }

      this.currentRow = orgRow;
      this.currentCol = orgCol;
      return length;
   }

   public void placeThree(String successDirectionLeftOrRight, String notSuccessLoopbackDeadendStairs, int hallwayLength) {
      this.moveUp("3");
      String originalDirection = this.currentDirection;
      int originalRow = this.currentRow;
      int originalCol = this.currentCol;
      if (successDirectionLeftOrRight.equals("right")) {
         this.changeLeftPerspective();
         if (notSuccessLoopbackDeadendStairs.equals("loopback")) {
            this.moveUp("L");
            this.placeTwo();
            this.placeCornerDeadend();
            if (this.functionList.size() >= 2) {
               this.functionList.remove(this.functionList.size() - 1);
               this.functionList.remove(this.functionList.size() - 1);
            }

            this.functionList.add(new FunctionSpecifications("three_way", "right", "loopback", 1));
         } else if (notSuccessLoopbackDeadendStairs.equals("deadend")) {
            int hallwayLen = this.determineHallwayLength();
            String st = "D" + (hallwayLen - 1);
            this.moveUp(st);

            for (int i = 0; i < hallwayLen - 1; i++) {
               this.moveUp("1");
            }

            this.placeDeadend();
            if (!this.functionList.isEmpty()) {
               this.functionList.remove(this.functionList.size() - 1);
            }

            this.functionList.add(new FunctionSpecifications("three_way", "right", "deadend", hallwayLen - 1));
         } else if (notSuccessLoopbackDeadendStairs.equals("stairs")) {
            int hallwayLen = this.determineHallwayLength();
            String st = "S" + (hallwayLen - 1);
            this.moveUp(st);

            for (int i = 0; i < hallwayLen - 1; i++) {
               this.moveUp("1");
            }

            this.placeStairs();
            if (!this.functionList.isEmpty()) {
               this.functionList.remove(this.functionList.size() - 1);
            }

            this.functionList.add(new FunctionSpecifications("three_way", "right", "stairs", hallwayLen - 1));
         }

         this.currentRow = originalRow;
         this.currentCol = originalCol;
         this.currentDirection = originalDirection;
         this.changeRightPerspective();
         this.moveUp("1");
      } else {
         this.changeRightPerspective();
         if (notSuccessLoopbackDeadendStairs.equals("loopback")) {
            this.moveUp("L");
            this.placeTwo();
            this.placeCornerDeadend();
            if (this.functionList.size() >= 2) {
               this.functionList.remove(this.functionList.size() - 1);
               this.functionList.remove(this.functionList.size() - 1);
            }

            this.functionList.add(new FunctionSpecifications("three_way", "left", "loopback", 1));
         } else if (notSuccessLoopbackDeadendStairs.equals("deadend")) {
            int hallwayLen = this.determineHallwayLength();
            String st = "D" + (hallwayLen - 1);
            this.moveUp(st);

            for (int i = 0; i < hallwayLen - 1; i++) {
               this.moveUp("1");
            }

            this.placeDeadend();
            if (!this.functionList.isEmpty()) {
               this.functionList.remove(this.functionList.size() - 1);
            }

            this.functionList.add(new FunctionSpecifications("three_way", "left", "deadend", hallwayLen - 1));
         } else if (notSuccessLoopbackDeadendStairs.equals("stairs")) {
            int hallwayLen = this.determineHallwayLength();
            String st = "S" + (hallwayLen - 1);
            this.moveUp(st);

            for (int i = 0; i < hallwayLen - 1; i++) {
               this.moveUp("1");
            }

            this.placeStairs();
            if (!this.functionList.isEmpty()) {
               this.functionList.remove(this.functionList.size() - 1);
            }

            this.functionList.add(new FunctionSpecifications("three_way", "left", "stairs", hallwayLen - 1));
         }

         this.currentRow = originalRow;
         this.currentCol = originalCol;
         this.currentDirection = originalDirection;
         this.changeLeftPerspective();
         this.moveUp("1");
      }
   }

   public void changeRightPerspective() {
      String var1 = this.currentDirection;
      switch (var1) {
         case "east":
            this.setDirection("south");
            break;
         case "south":
            this.setDirection("west");
            break;
         case "west":
            this.setDirection("north");
            break;
         case "north":
            this.setDirection("east");
      }
   }

   public void changeLeftPerspective() {
      String var1 = this.currentDirection;
      switch (var1) {
         case "east":
            this.setDirection("north");
            break;
         case "north":
            this.setDirection("west");
            break;
         case "west":
            this.setDirection("south");
            break;
         case "south":
            this.setDirection("east");
      }
   }

   public void exampleMaze() {
      this.placeOne();
      this.placeOne();
      this.placeTwo();
      this.placeOne();
      this.placeOne();
      this.placeRoom();
      this.placeThree("left", "loopback", 1);
      this.placeOne();
      this.placeRoom();
      this.placeOne();
      this.placeRoom();
      this.placeThree("left", "loopback", 1);
      this.placeOne();
      this.placeOne();
      this.placeOne();
      this.placeThree("right", "loopback", 1);
      this.placeOne();
      this.placeRoom();
      this.placeThree("right", "deadend", 5);
      this.placeOne();
      this.placeOne();
      this.placeThree("left", "deadend", 2);
      this.placeOne();
      this.placeTwo();
      this.placeOne();
      this.placeOne();
      this.placeOne();
      this.placeOne();
      this.placeOne();
      this.placeTwo();
      this.placeOne();
      this.placeOne();
      this.placeStairs();
   }

   public void placeThreeAndTurn(String direction) {
      double r = this.random.nextDouble();
      double currentLoopbackProb = this.loopbackProb;
      double currentDeadendProb = this.deadendProb;
      if (r < currentLoopbackProb) {
         this.placeThree(direction, "loopback", 1);
      } else if (!this.stairsPlaced) {
         double p = this.random.nextDouble();
         if (p < currentDeadendProb) {
            this.placeThree(direction, "deadend", 1);
         } else {
            this.placeThree(direction, "stairs", 1);
         }
      } else {
         this.placeThree(direction, "deadend", 1);
      }
   }

   public void placeTurn() {
      double r = this.random.nextDouble();
      if (r < this.probabilityRight) {
         double p = this.random.nextDouble();
         if (p < 0.5) {
            this.placeTwo();
            this.updateTurnProbabilities("right");
         } else {
            this.placeThreeAndTurn("right");
            this.updateTurnProbabilities("right");
         }
      } else {
         this.placeThreeAndTurn("left");
         this.updateTurnProbabilities("left");
      }
   }

   public void updateTurnProbabilities(String direction) {
      if (direction.equals("left")) {
         this.probabilityRight = 1.0;
      } else if (direction.equals("right")) {
         this.probabilityRight = 0.0;
      }
   }

   public void placeStraight() {
      double r = this.random.nextDouble();
      if (r < this.probabilityOne) {
         this.placeOne();
         this.updateStraightProbabilities("one");
      } else {
         this.placeRoom();
         this.updateStraightProbabilities("room");
      }
   }

   public void updateStraightProbabilities(String placed) {
      if (placed.equals("one")) {
         this.probabilityOne = Math.max(0.0, this.probabilityOne - 0.1);
         this.probabilityRoom = Math.min(1.0, this.probabilityRoom + 0.1);
      } else if (placed.equals("room")) {
         this.probabilityOne = 1.0;
         this.probabilityRoom = 0.0;
      }
   }

   public void generateMaze() {
      double probabilityStraight = 1.0;
      double probabilityTurn = 0.0;
      double minStraight = 0.2;
      double maxStraight = 1.0;
      double minTurn = 0.0;
      double maxTurn = 0.8;
      int placeOneCount = 2;

      while (this.numOfStructures > 1) {
         double r = this.random.nextDouble();
         if (r < probabilityStraight) {
            if (placeOneCount > 0) {
               this.placeStraight();
               placeOneCount--;
            } else {
               this.placeStraight();
               probabilityStraight = Math.max(minStraight, probabilityStraight - 0.3);
               probabilityTurn = 1.0 - probabilityStraight;
            }
         } else {
            this.placeTurn();
            probabilityStraight = 1.0;
            probabilityTurn = 0.0;
            placeOneCount = 2;
         }

         this.numOfStructures--;
      }

      this.placeStairs();
   }

   public List<FunctionSpecifications> readFunctions() {
      for (FunctionSpecifications func : this.functionList) {
         System.out.println(func.toString());
      }

      return this.functionList;
   }

   public List<FunctionSpecifications> getFunctions() {
      return this.functionList;
   }
}
