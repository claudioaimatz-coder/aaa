package io.github.Memoires.trbeyond.util;

public class FunctionSpecifications {
   private String functionName;
   private String successDirection;
   private String notSuccessCase;
   private int hallwayLen;

   public FunctionSpecifications(String functionName) {
      this(functionName, " ", " ", 0);
   }

   public FunctionSpecifications(String functionName, String successDirection, String notSuccessCase, int hallwayLen) {
      this.functionName = functionName;
      this.successDirection = successDirection;
      this.notSuccessCase = notSuccessCase;
      this.hallwayLen = hallwayLen;
   }

   public String getFunctionName() {
      return this.functionName;
   }

   public String getSuccessDirection() {
      return this.successDirection;
   }

   public String getNotSuccessCase() {
      return this.notSuccessCase;
   }

   public int getHallwayLen() {
      return this.hallwayLen;
   }

   @Override
   public String toString() {
      return "Function Name: "
         + this.functionName
         + ", Direction: "
         + this.successDirection
         + ", Case: "
         + this.notSuccessCase
         + ", Hallway Length: "
         + this.hallwayLen;
   }
}
