package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;

public class File24 extends File21 {
   String comment;

   File24(ICore core) {
      super(core);
   }

   public synchronized String getAgeString() {
      return SwissArmy.calcStringOfSeconds(this.getAge());
   }

   public synchronized String getComment() {
      return this.comment;
   }

   protected long readAge(MessageBuffer buffer) {
      if (this.core.getFileCollection().eta2()) {
         this.ageTS = System.currentTimeMillis();
      }

      return (long)buffer.getInt32();
   }

   protected int[] readChunkAges(MessageBuffer buffer) {
      return buffer.getInt32List();
   }

   protected void readComment(MessageBuffer buffer) {
      this.comment = buffer.getString();
   }
}
