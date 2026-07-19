package sancho.model.mldonkey;

import gnu.trove.THash;
import gnu.trove.THashMap;
import gnu.trove.TIntObjectHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.eclipse.swt.graphics.Image;
import sancho.core.ICore;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.model.mldonkey.enums.EnumClientMode;
import sancho.model.mldonkey.enums.EnumClientType;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.Addr;
import sancho.model.mldonkey.utility.HostState;
import sancho.model.mldonkey.utility.Kind;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.Tag;
import sancho.model.mldonkey.utility.UtilityFactory;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public class Client extends AObjectO {
   public static final String RS_TRANSFERRING = SResources.getString("l.transferring");
   public static final String RS_RANK = SResources.getString("l.rank");
   public static final String RS_TRUE = SResources.getString("l.true");
   public static final String RS_FALSE = SResources.getString("l.false");
   protected static final String S_Q = " (Q: ";
   public static final int CONNECTED = 1;
   public static final int DISCONNECTED = 2;
   public static final int TRANSFERRING_ADD = 4;
   public static final int TRANSFERRING_REM = 8;
   public static final int CHANGED_AVAIL = 16;
   public static final int READ_CLIENT_FILE = 32;
   protected THash avail;
   protected int chatPort;
   protected THashMap clientFilesMap;
   protected EnumClientType enumClientType;
   protected int id;
   protected Kind kind;
   protected String name;
   protected EnumNetwork networkEnum;
   protected int rating;
   protected HostState state;
   protected Tag[] tag;
   protected EnumHostState stateEnum;
   protected EnumClientMode clientModeEnum;

   Client(ICore core) {
      super(core);
      this.state = UtilityFactory.getHostState(core);
      this.kind = UtilityFactory.getKind(core);
   }

   public void addAsFriend() {
      this.core.send((short)14, Integer.valueOf(this.getId()));
   }

   public void connect() {
   }

   public void disconnect() {
   }

   public boolean equals(Object object) {
      return object instanceof Client && this.getId() == ((Client)object).getId();
   }

   public Addr getAddr() {
      return this.kind.getAddr();
   }

   private TIntObjectHashMap getAvailMap() {
      if (this.avail == null) {
         this.avail = new TIntObjectHashMap();
      }

      return (TIntObjectHashMap)this.avail;
   }

   public String getClientActivity() {
      return this.getStateEnum() == EnumHostState.CONNECTED_DOWNLOADING ? RS_TRANSFERRING : RS_RANK + this.getStateRank();
   }

   public THashMap getClientFilesMap() {
      if (this.clientFilesMap == null) {
         this.clientFilesMap = new THashMap();
      }

      return this.clientFilesMap;
   }

   public synchronized Map getClientFilesResultMap(Object directory) {
      return this.clientFilesMap == null ? null : (Map)this.clientFilesMap.get(directory);
   }

   public synchronized EnumClientMode getClientModeEnum() {
      return this.clientModeEnum;
   }

   public int getConnectedTime() {
      return 0;
   }

   public String getConnectedTimeString() {
      return "";
   }

   public String getDetailedClientActivity() {
      EnumHostState state = this.getStateEnum();
      StringBuffer text = new StringBuffer();
      text.append(state.getName());
      if (state == EnumHostState.CONNECTED_DOWNLOADING && this.getStateFileNum() != -1) {
         text.append("(");
         text.append(this.getState().getFileNum());
         text.append(")");
         return text.toString().intern();
      } else if (state != EnumHostState.CONNECTED_DOWNLOADING && this.getStateRank() > 0) {
         text.append(" (Q: ");
         text.append(this.getState().getRank());
         text.append(")");
         return text.toString().intern();
      } else {
         return text.toString().intern();
      }
   }

   public long getDownloaded() {
      return 0L;
   }

   public String getDownloadedString() {
      return "";
   }

   public synchronized EnumClientType getEnumClientType() {
      return this.enumClientType;
   }

   public synchronized boolean isFriend() {
      return this.enumClientType == EnumClientType.FRIEND;
   }

   public synchronized EnumNetwork getEnumNetwork() {
      return this.networkEnum;
   }

   public synchronized String getFileAvailability(int fileId) {
      return (String)this.getAvailMap().get(fileId);
   }

   public synchronized String getFileAvailabilityPercentString(File file) {
      float percent = this.getFileAvailabilityPercent(file);
      return percent < 0.0F ? "" : SwissArmy.percentToString(percent);
   }

   public synchronized float getFileAvailabilityPercent(File file) {
      int fileId = file.getId();
      String availability = this.getFileAvailability(fileId);
      if (availability == null) {
         return -1.0F;
      } else {
         String chunks = file.getChunks();
         if (availability.length() > 0 && availability.length() == chunks.length()) {
            int availableCount = 0;
            int totalCount = 0;

            for (int i = 0; i < chunks.length(); i++) {
               int chunkState = chunks.charAt(i) - '0';
               int availState = availability.charAt(i) - '0';
               if (chunkState < 2) {
                  totalCount++;
                  if (availState > 0) {
                     availableCount++;
                  }
               }
            }

            return totalCount == 0 ? -1.0F : (float)availableCount / (float)totalCount * 100.0F;
         } else {
            return -1.0F;
         }
      }
   }

   public synchronized Object[] getFileDirectories() {
      return this.clientFilesMap.keySet().toArray();
   }

   public Map getFirstResultMap() {
      synchronized (this) {
         String directory = (String)this.getFileDirectories()[0];
         return this.getClientFilesResultMap(directory);
      }
   }

   public String getHash() {
      return this.kind.getHash();
   }

   public synchronized int getId() {
      return this.id;
   }

   public String getModeString() {
      return this.getClientModeEnum().getName();
   }

   public synchronized String getName() {
      return this.name != null ? this.name : "";
   }

   public synchronized Image getNameImage() {
      return this.isFriend() ? SResources.getImage("client_friend") : SResources.getImage("client");
   }

   public synchronized byte getSUI() {
      return 2;
   }

   public synchronized String getSUIString() {
      return SResources.getString("l.none");
   }

   public synchronized Image getSUIImage() {
      byte sui = this.getSUI();
      switch (sui) {
         case 0:
            return SResources.getImage("bulb-red");
         case 1:
            return SResources.getImage("bulb-green");
         default:
            return SResources.getImage("bulb-grey");
      }
   }

   public int getNumChunks(int fileId) {
      int count = 0;
      String availability = this.getFileAvailability(fileId);
      if (availability != null) {
         for (int i = 0; i < availability.length(); i++) {
            if (availability.charAt(i) == '1') {
               count++;
            }
         }
      }

      return count;
   }

   public synchronized int getPort() {
      return this.kind.getPort();
   }

   public synchronized int getRating() {
      return this.rating;
   }

   public String getSoftware() {
      return "";
   }

   public Image getSoftwareImage() {
      return null;
   }

   public synchronized HostState getState() {
      return this.state;
   }

   public synchronized EnumHostState getStateEnum() {
      return this.stateEnum;
   }

   public synchronized int getStateFileNum() {
      return this.state.getFileNum();
   }

   public synchronized int getStateRank() {
      return this.state.getRank();
   }

   public long getUploaded() {
      return 0L;
   }

   public String getUploadedString() {
      return "";
   }

   public String getUploadFilename() {
      return "";
   }

   public synchronized boolean hasFiles() {
      return this.clientFilesMap != null;
   }

   public synchronized String hasFilesString() {
      return this.hasFiles() ? RS_TRUE : RS_FALSE;
   }

   public synchronized Image hasFilesImage() {
      return SResources.getImage(this.hasFiles() ? "bulb-green" : "bulb-red");
   }

   public int hashCode() {
      return this.getId();
   }

   public boolean isConnected() {
      return this.isConnected(this.getStateEnum());
   }

   public boolean isConnected(AbstractEnum state) {
      return state == EnumHostState.CONNECTED_DOWNLOADING
         || state == EnumHostState.CONNECTED_INITIATING
         || state == EnumHostState.CONNECTED_AND_QUEUED
         || state == EnumHostState.CONNECTED;
   }

   public boolean isTransferring() {
      return this.isTransferring(this.getStateEnum());
   }

   public boolean isTransferring(AbstractEnum state) {
      return state == EnumHostState.CONNECTED_DOWNLOADING;
   }

   public boolean isTransferring(int fileNum) {
      return this.isTransferring() && this.getStateFileNum() == fileNum;
   }

   public boolean isUploader() {
      return false;
   }

   public boolean onChangedState(AbstractEnum previousState) {
      boolean changed = false;
      this.setChanged();
      byte changeType = 0;
      if (previousState != this.getStateEnum()) {
         if (this.isTransferring()) {
            if (this.isConnected(previousState)) {
               changeType = 4;
            } else {
               changeType = 5;
            }
         } else if (this.isTransferring(previousState)) {
            if (this.isConnected()) {
               changeType = 8;
            } else {
               changeType = 10;
            }
         } else if (this.isConnected(previousState)) {
            if (!this.isConnected()) {
               changeType = 2;
            }
         } else if (this.isConnected()) {
            changeType = 1;
         }
      }

      if (changeType != 0) {
         changed = true;
         this.notifyObservers(null, changeType);
      }

      this.clearChanged();
      return changed;
   }

   public void putAvail(int fileId, String availability) {
      synchronized (this) {
         this.getAvailMap().put(fileId, availability);
      }

      this.setChanged();
      this.notifyObservers(null, 16);
   }

   public void read(int id, MessageBuffer buffer) {
      EnumHostState previousState = this.getStateEnum();
      EnumClientType previousType = this.getEnumClientType();
      boolean changed = false;
      synchronized (this) {
         this.id = id;
         this.networkEnum = this.core.getNetworkCollection().getNetworkEnum(buffer.getInt32());
         this.clientModeEnum = this.kind.read(buffer);
         this.stateEnum = this.state.read(buffer);
         this.enumClientType = EnumClientType.byteToEnum(buffer.getByte());
         this.tag = buffer.getTagList();
         this.name = buffer.getString();
         this.rating = buffer.getInt32();
         changed = this.readMore(buffer);
      }

      this.onChangedType(previousType);
      boolean stateChanged = this.onChangedState(previousState);
      if (!stateChanged && changed) {
         this.setChanged();
         this.notifyObservers();
      }
   }

   protected void onChangedType(AbstractEnum previousType) {
      if (previousType != null && previousType != this.getEnumClientType()) {
         this.core.getClientCollection().updateFriends(this);
      }
   }

   protected boolean readMore(MessageBuffer buffer) {
      this.chatPort = buffer.getInt32();
      return false;
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getInt32(), buffer);
   }

   public void readClientFile(MessageBuffer buffer) {
      String directory = buffer.getString();
      int resultId = buffer.getInt32();
      Result result = this.core.getResultCollection().getResult(resultId);
      if (result != null) {
         synchronized (this) {
            THashMap filesMap = this.getClientFilesMap();
            WeakHashMap resultSet;
            if (filesMap.containsKey(directory)) {
               resultSet = (WeakHashMap)filesMap.get(directory);
            } else {
               resultSet = new WeakHashMap();
               filesMap.put(directory, resultSet);
            }

            resultSet.put(result, null);
         }

         this.setChanged();
         this.notifyObservers(null, 32);
      }
   }

   public void readUpdate(MessageBuffer buffer) {
      EnumHostState previousState = this.getStateEnum();
      synchronized (this) {
         this.stateEnum = this.state.read(buffer);
      }

      this.onChangedState(previousState);
   }

   public void removeAsFriend() {
      this.core.send((short)16, Integer.valueOf(this.getId()));
   }

   public void requestClientFiles() {
      this.core.send((short)33, Integer.valueOf(this.getId()));
   }
}
