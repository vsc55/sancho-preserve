package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;
import gnu.trove.TObjectProcedure;

public class SharedFileCollection extends ACollection_Int2 {
   long totalSize;
   String totalSizeString;

   SharedFileCollection(ICore core) {
      super(core);
   }

   public void read(MessageBuffer buffer) {
      int id = buffer.getInt32();
      SharedFile sharedFile = (SharedFile)this.get(id);
      if (sharedFile != null) {
         if (sharedFile.readUpdate(id, buffer)) {
            this.addToUpdated(sharedFile);
            this.setChanged();
            this.notifyObservers(sharedFile);
         }
      } else {
         sharedFile = this.core.getCollectionFactory().getSharedFile();
         sharedFile.read(id, buffer);
         this.put(id, sharedFile);
         this.addToAdded(sharedFile);
         this.calculateTotalSize();
         this.setChanged();
         this.notifyObservers();
      }
   }

   public void reshare() {
      this.core.send((short)29, "reshare");
   }

   public void unshared(MessageBuffer buffer) {
      int id = buffer.getInt32();
      if (this.containsKey(id)) {
         this.addToRemoved(this.remove(id));
         this.calculateTotalSize();
         this.setChanged();
         this.notifyObservers();
      }
   }

   public void upload(MessageBuffer buffer) {
      int id = buffer.getInt32();
      SharedFile sharedFile = (SharedFile)this.get(id);
      if (sharedFile != null && sharedFile.upload(id, buffer)) {
         this.addToUpdated(sharedFile);
         this.setChanged();
         this.notifyObservers();
      }
   }

   public synchronized void calculateTotalSize() {
      CalculateTotalSize sizer = new CalculateTotalSize();
      this.forEachValue(sizer);
      this.totalSize = sizer.getTotal();
      this.totalSizeString = SwissArmy.calcStringSize(this.totalSize);
   }

   public synchronized String getTotalSizeString() {
      return this.totalSizeString != null ? this.totalSizeString : "0";
   }

   // Trove forEachValue: sum the size of every shared file.
   private static class CalculateTotalSize implements TObjectProcedure {
      long total;

      public boolean execute(Object value) {
         SharedFile sharedFile = (SharedFile)value;
         this.total = this.total + sharedFile.getSize();
         return true;
      }

      public long getTotal() {
         return this.total;
      }
   }
}
