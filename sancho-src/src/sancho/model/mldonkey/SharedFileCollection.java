package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;

public class SharedFileCollection extends ACollection_Int2 {
   long totalSize;
   String totalSizeString;

   SharedFileCollection(ICore var1) {
      super(var1);
   }

   public void read(MessageBuffer var1) {
      int var2 = var1.getInt32();
      SharedFile var3 = (SharedFile)this.get(var2);
      if (var3 != null) {
         if (var3.readUpdate(var2, var1)) {
            this.addToUpdated(var3);
            this.setChanged();
            this.notifyObservers(var3);
         }
      } else {
         var3 = this.core.getCollectionFactory().getSharedFile();
         var3.read(var2, var1);
         this.put(var2, var3);
         this.addToAdded(var3);
         this.calculateTotalSize();
         this.setChanged();
         this.notifyObservers();
      }
   }

   public void reshare() {
      this.core.send((short)29, "reshare");
   }

   public void unshared(MessageBuffer var1) {
      int var2 = var1.getInt32();
      if (this.containsKey(var2)) {
         this.addToRemoved(this.remove(var2));
         this.calculateTotalSize();
         this.setChanged();
         this.notifyObservers();
      }
   }

   public void upload(MessageBuffer var1) {
      int var2 = var1.getInt32();
      SharedFile var3 = (SharedFile)this.get(var2);
      if (var3 != null && var3.upload(var2, var1)) {
         this.addToUpdated(var3);
         this.setChanged();
         this.notifyObservers();
      }
   }

   public synchronized void calculateTotalSize() {
      SharedFileCollection$CalculateTotalSize var1 = new SharedFileCollection$CalculateTotalSize();
      this.forEachValue(var1);
      this.totalSize = var1.getTotal();
      this.totalSizeString = SwissArmy.calcStringSize(this.totalSize);
   }

   public synchronized String getTotalSizeString() {
      return this.totalSizeString != null ? this.totalSizeString : "0";
   }
}
