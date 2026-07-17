package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;

public class File24 extends File21 {
   String comment;

   File24(ICore var1) {
      super(var1);
   }

   public synchronized String getAgeString() {
      return SwissArmy.calcStringOfSeconds(this.getAge());
   }

   public synchronized String getComment() {
      return this.comment;
   }

   protected long readAge(MessageBuffer var1) {
      if (this.core.getFileCollection().eta2()) {
         this.ageTS = System.currentTimeMillis();
      }

      return (long)var1.getInt32();
   }

   protected int[] readChunkAges(MessageBuffer var1) {
      return var1.getInt32List();
   }

   protected void readComment(MessageBuffer var1) {
      this.comment = var1.getString();
   }
}
