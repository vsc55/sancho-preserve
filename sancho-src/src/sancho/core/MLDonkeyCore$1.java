package sancho.core;

import java.util.TimerTask;

class MLDonkeyCore$1 extends TimerTask {
   // $VF: synthetic field
   private final MLDonkeyCore this$0;

   MLDonkeyCore$1(MLDonkeyCore var1) {
      this.this$0 = var1;
   }

   public void run() {
      if (!this.this$0.isConnected()) {
         this.cancel();
      } else {
         long var1 = System.currentTimeMillis();
         if (var1 > this.this$0.lastPollForStats + (long)(this.this$0.pollDelay * 1000)) {
            MLDonkeyCore.access$000(this.this$0);
            this.this$0.lastPollForStats = var1;
         }

         if (var1 > this.this$0.lastStats + (long)(this.this$0.statsDelay * 1000)) {
            MLDonkeyCore.access$100(this.this$0);
            this.this$0.lastStats = var1;
         }

         if (this.this$0.requestFileInfoDelay > 0 && var1 > this.this$0.lastRequestFileInfos + (long)(this.this$0.requestFileInfoDelay * 1000)) {
            this.this$0.getFileCollection().requestAllFileInfos();
            this.this$0.lastRequestFileInfos = var1;
         }

         if (this.this$0.timerCounter++ == 5) {
            this.this$0.getClientCollection().cleanDeadClients();
         }

         this.this$0.getFileCollection().sendUpdate();
      }
   }
}
