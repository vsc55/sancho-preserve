package sancho.model.mldonkey.utility;

public class SearchWaiting {
   private int id;
   private int numWaiting;

   public int getId() {
      return this.id;
   }

   public int getNumWaiting() {
      return this.numWaiting;
   }

   public void read(MessageBuffer var1) {
      this.id = var1.getInt32();
      this.numWaiting = var1.getInt32();
   }
}
