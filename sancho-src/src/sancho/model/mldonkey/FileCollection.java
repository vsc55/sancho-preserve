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
import gnu.trove.TObjectProcedure;

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

   FileCollection(ICore core) {
      super(core);
      this.totalArray = new long[12];
      this.updatePreferences();
   }

   public synchronized boolean requiresRefresh() {
      boolean current = this.requiresRefresh;
      this.requiresRefresh = false;
      return current;
   }

   public synchronized void setRequiresRefresh() {
      this.setChanged();
      this.requiresRefresh = true;
   }

   public void add(MessageBuffer buffer) {
      int id = buffer.getInt32();
      File file = this.getFile(id);
      if (file != null) {
         file.read(id, buffer);
         if (!file.isActive()) {
            this.removeHash(file.getMD4());
         }

         if (file.isInteresting()) {
            this.addToUpdated(file);
         } else {
            this.addToRemoved(file);
         }
      } else {
         file = this.core.getCollectionFactory().getFile();
         file.read(id, buffer);
         this.putFile(id, file);
         if (file.isActive()) {
            this.addHash(file.getMD4());
         }

         if (file.isInteresting()) {
            this.addToAdded(file);
         } else {
            this.addToRemoved(file);
         }
      }

      this.setChanged();
   }

   public void addHash(String hash) {
      if (!this.containsHash(hash)) {
         this.hashList.add(hash.toUpperCase());
      }
   }

   public void addSource(MessageBuffer buffer) {
      int fileId = buffer.getInt32();
      int clientId = buffer.getInt32();
      Client client = (Client)this.core.getClientCollection().get(clientId);
      File file = (File)this.get(fileId);
      if (file != null && client != null) {
         file.addSource(client);
         this.addFileToUpdated(file);
      }
   }

   public void clean() {
      this.forEachValue(new ManualCleanAll());
      this.setRequiresRefresh();
   }

   public synchronized Object[] getAllInteresting() {
      GetAllInteresting collector = new GetAllInteresting();
      this.forEachValue(collector);
      this.clearAllLists();
      return collector.getArray();
   }

   public void commitAll() {
      this.forEachValue(new CommitAll());
   }

   public boolean containsHash(String hash) {
      return hash.equals(S_EMPTY_HASH) ? false : this.hashList.contains(hash.toUpperCase());
   }

   public void dispose() {
      this.forEachValue(new DisposeAll());
      super.dispose();
   }

   public void dllink(String link) {
      this.core.send((short)8, link);
   }

   public boolean eta2() {
      return this.eta2;
   }

   public File getFile(int id) {
      return (File)this.get(id);
   }

   public synchronized String getHeaderText() {
      StringBuffer text = new StringBuffer(128);
      Object[] values = this.getValues();

      for (int i = 0; i < this.totalArray.length; i++) {
         this.totalArray[i] = 0L;
      }

      for (int i = 0; i < values.length; i++) {
         File file = (File)values[i];
         if (file.isInteresting()) {
            this.totalArray[0]++;
            if (file.getFileStateEnum() == EnumFileState.QUEUED) {
               this.totalArray[1] = this.totalArray[1] + file.getSize();
               this.totalArray[2] = this.totalArray[2] + file.getDownloaded();
               this.totalArray[3]++;
            } else if (file.getFileStateEnum() == EnumFileState.DOWNLOADED) {
               this.totalArray[4]++;
               this.totalArray[5] = this.totalArray[5] + file.getSize();
            } else if (file.getFileStateEnum() == EnumFileState.PAUSED) {
               this.totalArray[6] = this.totalArray[6] + file.getSize();
               this.totalArray[7] = this.totalArray[7] + file.getDownloaded();
               this.totalArray[8]++;
            } else {
               this.totalArray[9] = this.totalArray[9] + file.getSize();
               this.totalArray[10] = this.totalArray[10] + file.getDownloaded();
               this.totalArray[11]++;
            }
         }
      }

      text.append(RS_DOWNLOADS);
      text.append("(");
      text.append(this.totalArray[0]);
      text.append(")");
      text.append(": ");
      text.append(this.totalArray[11]);
      text.append(" ");
      text.append(RS_ACTIVE);
      if (this.totalArray[11] > 0L) {
         text.append(" (");
         text.append(SwissArmy.calcStringSize(this.totalArray[10]));
         text.append(" / ");
         text.append(SwissArmy.calcStringSize(this.totalArray[9]));
         text.append(")");
      }

      if (this.totalArray[8] > 0L) {
         text.append(", ");
         text.append(RS_PAUSED);
         text.append(": ");
         text.append(this.totalArray[8]);
         text.append(" (");
         text.append(SwissArmy.calcStringSize(this.totalArray[7]));
         text.append(" / ");
         text.append(SwissArmy.calcStringSize(this.totalArray[6]));
         text.append(")");
      }

      if (this.totalArray[3] > 0L) {
         text.append(", ");
         text.append(RS_QUEUED);
         text.append(": ");
         text.append(this.totalArray[3]);
         text.append(" (");
         text.append(SwissArmy.calcStringSize(this.totalArray[2]));
         text.append(" / ");
         text.append(SwissArmy.calcStringSize(this.totalArray[1]));
         text.append(")");
      }

      if (this.totalArray[4] > 0L) {
         text.append(", ");
         text.append(RS_DOWNLOADED);
         text.append(": ");
         text.append(this.totalArray[4]);
         text.append(" (");
         text.append(SwissArmy.calcStringSize(this.totalArray[5]));
         text.append(")");
      }

      return text.toString();
   }

   public void putFile(int id, File file) {
      if (file.isInteresting()) {
         this.addHash(file.getMD4());
      }

      this.put(id, file);
   }

   public void read(MessageBuffer buffer) {
      int count = buffer.getUInt16();
      this.clear();
      this.hashList.clear();

      for (int i = 0; i < count; i++) {
         File file = this.core.getCollectionFactory().getFile();
         file.read(buffer);
         this.putFile(file.getId(), file);
         if (file.isActive()) {
            this.addHash(file.getMD4());
         }

         if (file.isInteresting()) {
            this.addToAdded(file);
         }
      }

      this.setRequiresRefresh();
   }

   public void removeHash(String hash) {
      this.hashList.remove(hash.toUpperCase());
   }

   public void setBrothers(int[] ids) {
      String command = "set_brothers";

      for (int i = 0; i < ids.length; i++) {
         command = command + " " + ids[i];
      }

      this.core.send((short)29, command);
   }

   public void removeSource(MessageBuffer buffer) {
      File file = this.getFile(buffer.getInt32());
      if (file != null) {
         file.removeSource(buffer);
         this.addFileToUpdated(file);
      }
   }

   public void requestAllFileInfos() {
      this.forEachValue(new RequestAllFileInfos());
   }

   public void sendUpdate() {
      if (this.hasChanged()) {
         if (this.updateDelay > 0) {
            long now = System.currentTimeMillis();
            if (this.lastUpdate + (long)(this.updateDelay * 777) > now) {
               return;
            }

            this.lastUpdate = now;
         }

         this.notifyObservers();
      }
   }

   public void sendUpdate(FileClient fileClient, int columnIndex) {
      this.setChanged();
      this.notifyObservers(fileClient, columnIndex);
   }

   public void update(MessageBuffer buffer) {
      File file = this.getFile(buffer.getInt32());
      if (file != null) {
         file.readUpdate(buffer);
         this.addToUpdated(file);
         this.setChanged();
      }
   }

   public void updatePreferences() {
      this.eta2 = PreferenceLoader.loadString("downloadTableColumns").indexOf("O") >= 0;
      this.updateDelay = PreferenceLoader.loadInt("updateDelay");
      this.verboseNumbers = PreferenceLoader.loadBoolean("verboseNumbers");
   }

   public void addFileToUpdated(File file) {
      this.addToUpdated(file);
      this.setChanged();
   }

   // Trove forEachValue: save every finished download under its own name.
   private static class CommitAll implements TObjectProcedure {
      public boolean execute(Object value) {
         File file = (File)value;
         if (file.getFileStateEnum() == EnumFileState.DOWNLOADED) {
            file.saveFileAs(file.getName());
         }

         return true;
      }
   }

   // Trove forEachValue: dispose every file (on collection dispose).
   private static class DisposeAll implements TObjectProcedure {
      public boolean execute(Object value) {
         ((File)value).dispose();
         return true;
      }
   }

   // Trove forEachValue: gather the files worth showing in the transfer view.
   private static class GetAllInteresting implements TObjectProcedure {
      List arrayList = new ArrayList();

      public GetAllInteresting() {
      }

      public Object[] getArray() {
         return this.arrayList.toArray();
      }

      public boolean execute(Object value) {
         File file = (File)value;
         if (file.isInteresting()) {
            this.arrayList.add(file);
         }

         return true;
      }
   }

   // Trove forEachValue: run a manual clean on every file.
   private static class ManualCleanAll implements TObjectProcedure {
      public boolean execute(Object value) {
         ((File)value).manualClean();
         return true;
      }
   }

   // Trove forEachValue: ask the core for fresh info on every downloading file.
   private static class RequestAllFileInfos implements TObjectProcedure {
      public boolean execute(Object value) {
         File file = (File)value;
         if (file.getFileStateEnum() == EnumFileState.DOWNLOADING) {
            file.requestFileInfo();
         }

         return true;
      }
   }
}
