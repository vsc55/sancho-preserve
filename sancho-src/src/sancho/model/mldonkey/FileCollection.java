package sancho.model.mldonkey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.FileClient;
import sancho.view.utility.SResources;

public class FileCollection extends ACollection_Int2 {
   private static final String RS_ACTIVE = SResources.getString("l.active");
   private static final String RS_DOWNLOADED = SResources.getString("l.downloaded");
   private static final String RS_DOWNLOADS = SResources.getString("l.downloads");
   private static final String RS_PAUSED = SResources.getString("l.paused");
   private static final String RS_QUEUED = SResources.getString("l.queued");
   private static final String S_EMPTY_HASH = "00000000000000000000000000000000";
   private List hashList = Collections.synchronizedList(new ArrayList());
   private long[] totalArray;
   private boolean eta2;
   private int updateDelay;
   private long lastUpdate;
   public boolean verboseNumbers = true;
   boolean requiresRefresh;

   FileCollection(ICore var1) {
      super(var1);
      this.totalArray = new long[12];
      this.updatePreferences();
   }

   public synchronized boolean requiresRefresh() {
      boolean var1 = this.requiresRefresh;
      this.requiresRefresh = false;
      return var1;
   }

   public synchronized void setRequiresRefresh() {
      this.setChanged();
      this.requiresRefresh = true;
   }

   public void add(MessageBuffer var1) {
      int var2 = var1.getInt32();
      File var3 = this.getFile(var2);
      if (var3 != null) {
         var3.read(var2, var1);
         if (!var3.isActive()) {
            this.removeHash(var3.getMD4());
         }

         if (var3.isInteresting()) {
            this.addToUpdated(var3);
         } else {
            this.addToRemoved(var3);
         }
      } else {
         var3 = this.core.getCollectionFactory().getFile();
         var3.read(var2, var1);
         this.putFile(var2, var3);
         if (var3.isActive()) {
            this.addHash(var3.getMD4());
         }

         if (var3.isInteresting()) {
            this.addToAdded(var3);
         } else {
            this.addToRemoved(var3);
         }
      }

      this.setChanged();
   }

   public void addHash(String var1) {
      if (!this.containsHash(var1)) {
         this.hashList.add(var1.toUpperCase());
      }
   }

   public void addSource(MessageBuffer var1) {
      int var2 = var1.getInt32();
      int var3 = var1.getInt32();
      Client var4 = (Client)this.core.getClientCollection().get(var3);
      File var5 = (File)this.get(var2);
      if (var5 != null && var4 != null) {
         var5.addSource(var4);
         this.addFileToUpdated(var5);
      }
   }

   public void clean() {
      this.forEachValue(new FileCollection$ManualCleanAll());
      this.setRequiresRefresh();
   }

   public synchronized Object[] getAllInteresting() {
      FileCollection$GetAllInteresting var1 = new FileCollection$GetAllInteresting();
      this.forEachValue(var1);
      this.clearAllLists();
      return var1.getArray();
   }

   public void commitAll() {
      this.forEachValue(new FileCollection$CommitAll());
   }

   public boolean containsHash(String var1) {
      return var1.equals("00000000000000000000000000000000") ? false : this.hashList.contains(var1.toUpperCase());
   }

   public void dispose() {
      this.forEachValue(new FileCollection$DisposeAll());
      super.dispose();
   }

   public void dllink(String var1) {
      this.core.send((short)8, var1);
   }

   public boolean eta2() {
      return this.eta2;
   }

   public File getFile(int var1) {
      return (File)this.get(var1);
   }

   public synchronized String getHeaderText() {
      StringBuffer var1 = new StringBuffer(128);
      Object[] var2 = this.getValues();

      for (int var3 = 0; var3 < this.totalArray.length; var3++) {
         this.totalArray[var3] = 0L;
      }

      for (int var5 = 0; var5 < var2.length; var5++) {
         File var4 = (File)var2[var5];
         if (var4.isInteresting()) {
            this.totalArray[0]++;
            if (var4.getFileStateEnum() == EnumFileState.QUEUED) {
               this.totalArray[1] = this.totalArray[1] + var4.getSize();
               this.totalArray[2] = this.totalArray[2] + var4.getDownloaded();
               this.totalArray[3]++;
            } else if (var4.getFileStateEnum() == EnumFileState.DOWNLOADED) {
               this.totalArray[4]++;
               this.totalArray[5] = this.totalArray[5] + var4.getSize();
            } else if (var4.getFileStateEnum() == EnumFileState.PAUSED) {
               this.totalArray[6] = this.totalArray[6] + var4.getSize();
               this.totalArray[7] = this.totalArray[7] + var4.getDownloaded();
               this.totalArray[8]++;
            } else {
               this.totalArray[9] = this.totalArray[9] + var4.getSize();
               this.totalArray[10] = this.totalArray[10] + var4.getDownloaded();
               this.totalArray[11]++;
            }
         }
      }

      var1.append(RS_DOWNLOADS);
      var1.append("(");
      var1.append(this.totalArray[0]);
      var1.append(")");
      var1.append(": ");
      var1.append(this.totalArray[11]);
      var1.append(" ");
      var1.append(RS_ACTIVE);
      if (this.totalArray[11] > 0L) {
         var1.append(" (");
         var1.append(SwissArmy.calcStringSize(this.totalArray[10]));
         var1.append(" / ");
         var1.append(SwissArmy.calcStringSize(this.totalArray[9]));
         var1.append(")");
      }

      if (this.totalArray[8] > 0L) {
         var1.append(", ");
         var1.append(RS_PAUSED);
         var1.append(": ");
         var1.append(this.totalArray[8]);
         var1.append(" (");
         var1.append(SwissArmy.calcStringSize(this.totalArray[7]));
         var1.append(" / ");
         var1.append(SwissArmy.calcStringSize(this.totalArray[6]));
         var1.append(")");
      }

      if (this.totalArray[3] > 0L) {
         var1.append(", ");
         var1.append(RS_QUEUED);
         var1.append(": ");
         var1.append(this.totalArray[3]);
         var1.append(" (");
         var1.append(SwissArmy.calcStringSize(this.totalArray[2]));
         var1.append(" / ");
         var1.append(SwissArmy.calcStringSize(this.totalArray[1]));
         var1.append(")");
      }

      if (this.totalArray[4] > 0L) {
         var1.append(", ");
         var1.append(RS_DOWNLOADED);
         var1.append(": ");
         var1.append(this.totalArray[4]);
         var1.append(" (");
         var1.append(SwissArmy.calcStringSize(this.totalArray[5]));
         var1.append(")");
      }

      return var1.toString().intern();
   }

   public void putFile(int var1, File var2) {
      if (var2.isInteresting()) {
         this.addHash(var2.getMD4());
      }

      this.put(var1, var2);
   }

   public void read(MessageBuffer var1) {
      int var2 = var1.getUInt16();
      this.clear();
      this.hashList.clear();

      for (int var3 = 0; var3 < var2; var3++) {
         File var4 = this.core.getCollectionFactory().getFile();
         var4.read(var1);
         this.putFile(var4.getId(), var4);
         if (var4.isActive()) {
            this.addHash(var4.getMD4());
         }

         if (var4.isInteresting()) {
            this.addToAdded(var4);
         }
      }

      this.setRequiresRefresh();
   }

   public void removeHash(String var1) {
      this.hashList.remove(var1.toUpperCase());
   }

   public void setBrothers(int[] var1) {
      String var2 = "set_brothers";

      for (int var3 = 0; var3 < var1.length; var3++) {
         var2 = var2 + " " + var1[var3];
      }

      this.core.send((short)29, var2);
   }

   public void removeSource(MessageBuffer var1) {
      File var2 = this.getFile(var1.getInt32());
      if (var2 != null) {
         var2.removeSource(var1);
         this.addFileToUpdated(var2);
      }
   }

   public void requestAllFileInfos() {
      this.forEachValue(new FileCollection$RequestAllFileInfos());
   }

   public void sendUpdate() {
      if (this.hasChanged()) {
         if (this.updateDelay > 0) {
            long var1 = System.currentTimeMillis();
            if (this.lastUpdate + (long)(this.updateDelay * 777) > var1) {
               return;
            }

            this.lastUpdate = var1;
         }

         this.notifyObservers();
      }
   }

   public void sendUpdate(FileClient var1, int var2) {
      this.setChanged();
      this.notifyObservers(var1, var2);
   }

   public void update(MessageBuffer var1) {
      File var2 = this.getFile(var1.getInt32());
      if (var2 != null) {
         var2.readUpdate(var1);
         this.addToUpdated(var2);
         this.setChanged();
      }
   }

   public void updatePreferences() {
      this.eta2 = PreferenceLoader.loadString("downloadTableColumns").indexOf("O") >= 0;
      this.updateDelay = PreferenceLoader.loadInt("updateDelay");
      this.verboseNumbers = PreferenceLoader.loadBoolean("verboseNumbers");
   }

   public void addFileToUpdated(File var1) {
      this.addToUpdated(var1);
      this.setChanged();
   }
}
