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

   public void read(MessageBuffer buffer) {
      this.id = buffer.getInt32();
      this.numWaiting = buffer.getInt32();
   }
}
