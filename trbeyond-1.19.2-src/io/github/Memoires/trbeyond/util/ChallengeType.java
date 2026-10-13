package io.github.Memoires.trbeyond.util;

public class ChallengeType {
   String name;
   String category;
   String objective;
   int count;
   boolean onSpawn;
   String reward;
   int rewardAmount;
   int timeLimit;
   boolean random;
   int waveDelay;
   int countDone;
   int time;

   public ChallengeType(
      String name, String category, String objective, int count, boolean onSpawn, String reward, int rewardAmount, int timeLimit, boolean random
   ) {
      this.name = name;
      this.category = category;
      this.objective = objective;
      this.count = count;
      this.onSpawn = onSpawn;
      this.reward = reward;
      this.rewardAmount = rewardAmount;
      this.timeLimit = timeLimit;
      this.random = random;
      this.waveDelay = -1;
      this.countDone = 0;
   }

   public ChallengeType(
      String name, String category, String objective, int count, boolean onSpawn, String reward, int rewardAmount, int timeLimit, boolean random, int waveDelay
   ) {
      this.name = name;
      this.category = category;
      this.objective = objective;
      this.count = count;
      this.onSpawn = onSpawn;
      this.reward = reward;
      this.rewardAmount = rewardAmount;
      this.timeLimit = timeLimit;
      this.random = random;
      this.waveDelay = waveDelay;
      this.countDone = 0;
   }

   public static ChallengeType getChallengeType(
      String name, String category, String objective, int count, boolean onSpawn, String reward, int rewardAmount, int timeLimit, boolean random
   ) {
      return new ChallengeType(name, category, objective, count, onSpawn, reward, rewardAmount, timeLimit, random);
   }

   public static ChallengeType getChallengeType(ChallengeType challengeType) {
      return new ChallengeType(
         challengeType.getName(),
         challengeType.getCategory(),
         challengeType.getObjective(),
         challengeType.getCount(),
         challengeType.isOnSpawn(),
         challengeType.getReward(),
         challengeType.getRewardAmount(),
         challengeType.getTimeLimit(),
         challengeType.isRandom(),
         challengeType.getWaveDelay()
      );
   }

   public String getName() {
      return this.name;
   }

   public String getCategory() {
      return this.category;
   }

   public String getObjective() {
      return this.objective;
   }

   public int getCount() {
      return this.count;
   }

   public boolean isOnSpawn() {
      return this.onSpawn;
   }

   public String getReward() {
      return this.reward;
   }

   public int getRewardAmount() {
      return this.rewardAmount;
   }

   public int getTimeLimit() {
      return this.timeLimit;
   }

   public boolean isRandom() {
      return this.random;
   }

   public int getWaveDelay() {
      return this.waveDelay;
   }

   public int getCountDone() {
      return this.countDone;
   }

   public void setCountDone(int countDone) {
      this.countDone = countDone;
   }

   public int getTime() {
      return this.time;
   }

   public void setTime(int time) {
      this.time = time;
   }
}
