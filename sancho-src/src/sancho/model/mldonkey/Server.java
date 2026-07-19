package sancho.model.mldonkey;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import org.eclipse.swt.graphics.Image;
import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.Addr;
import sancho.model.mldonkey.utility.HostState;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.Tag;
import sancho.model.mldonkey.utility.UtilityFactory;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public class Server extends AObject {
   private static final String RS_UNKNOWN = SResources.getString("l.unknown");
   private static final String RS_HIGH_ID = SResources.getString("l.highID");
   private static final String RS_LOW_ID = SResources.getString("l.lowID");
   private static final String RS_TRUE = SResources.getString("l.true");
   private static final String RS_FALSE = SResources.getString("l.false");
   private Addr addr;
   private String description;
   private boolean preferred;
   private int id;
   private String name;
   private long numFiles;
   private long numUsers;
   private int port;
   private int score;
   private HostState state;
   private EnumHostState stateEnum;
   private Tag[] tagList;
   private Map userMap;
   private EnumNetwork networkEnum;

   Server(ICore core) {
      super(core);
      this.state = UtilityFactory.getHostState(core);
      this.addr = UtilityFactory.getAddr(core);
   }

   public synchronized void addUserInfo(User user) {
      this.getUserMap().put(user, null);
   }

   public void blacklist() {
      this.core.send((short)29, "bs " + this.getAddr().toString());
   }

   public void checkConnected(EnumHostState previousState) {
      EnumHostState currentState = this.getStateEnum();
      if (previousState != null && previousState != currentState) {
         if (currentState == EnumHostState.CONNECTED) {
            this.core.getServerCollection().setConnected(1);
         } else if (previousState == EnumHostState.CONNECTED) {
            this.core.getServerCollection().setConnected(-1);
         }
      }
   }

   private void checkRemovedState() {
      if (this.getStateEnum() == EnumHostState.REMOVE_HOST) {
         this.core.getServerCollection().remove(this);
      }
   }

   public void connect() {
      this.setState(EnumHostState.CONNECTING);
   }

   public void disconnect() {
      this.setState(EnumHostState.NOT_CONNECTED);
   }

   public boolean equals(Object object) {
      return object instanceof Server && this.getId() == ((Server)object).getId();
   }

   public Addr getAddr() {
      return this.addr;
   }

   public synchronized String getDescription() {
      return this.description != null && this.name != null && !this.name.equals("") ? this.description : "";
   }

   public String getHighLowIDString() {
      if (this.getEnumNetwork() == EnumNetwork.DONKEY && this.isConnected()) {
         return this.getState().getRank() == -2 ? RS_HIGH_ID : RS_LOW_ID;
      } else {
         return "";
      }
   }

   public Image getHighLowImage() {
      if (this.getEnumNetwork() == EnumNetwork.DONKEY && this.isConnected()) {
         boolean highId = this.getState().getRank() == -2;
         return SResources.getImage(highId ? "bulb-green" : "bulb-red");
      } else {
         return null;
      }
   }

   public synchronized int getId() {
      return this.id;
   }

   public String getLink() {
      return this.getEnumNetwork() == EnumNetwork.DONKEY
         ? "ed2k://|server|" + this.getAddr().toString() + "|" + this.getPort()
         : this.getAddr().toString() + ":" + this.getPort();
   }

   public synchronized String getName() {
      return this.name != null && !this.name.equals("") ? this.name : RS_UNKNOWN;
   }

   public synchronized long getNumFiles() {
      return this.numFiles;
   }

   public synchronized String getNumFilesString() {
      return SwissArmy.calcStringSizeGrouped(this.numFiles);
   }

   public synchronized long getNumUsers() {
      return this.numUsers;
   }

   public synchronized String getNumUsersString() {
      return SwissArmy.calcStringSizeGrouped(this.numUsers);
   }

   public synchronized int getPort() {
      return this.port;
   }

   public synchronized int getScore() {
      return this.score;
   }

   public void getServerUsers() {
      this.core.send((short)32, Integer.valueOf(this.getId()));
   }

   public HostState getState() {
      return this.state;
   }

   public synchronized EnumHostState getStateEnum() {
      return this.stateEnum;
   }

   public synchronized String getStateString() {
      return this.getAddr().isBlocked() ? SResources.getString("e.state.blocked") : this.getStateEnum().getName();
   }

   public synchronized Tag[] getTagList() {
      if (this.tagList != null && this.tagList.length != 0) {
         Tag[] tags = new Tag[this.tagList.length];
         System.arraycopy(this.tagList, 0, tags, 0, this.tagList.length);
         return tags;
      } else {
         return new Tag[0];
      }
   }

   protected synchronized Map getUserMap() {
      if (this.userMap == null) {
         this.userMap = Collections.synchronizedMap(new WeakHashMap());
      }

      return this.userMap;
   }

   public synchronized Object[] getUsers() {
      return SwissArmy.toArray(this.getUserMap().keySet());
   }

   public int hashCode() {
      return this.getId();
   }

   public synchronized boolean hasUsers() {
      return this.userMap != null;
   }

   public boolean isConnected() {
      return this.getStateEnum() == EnumHostState.CONNECTED;
   }

   public boolean isDisconnected() {
      return this.getStateEnum() == EnumHostState.NOT_CONNECTED;
   }

   public synchronized boolean isPreferred() {
      return this.preferred;
   }

   public synchronized EnumNetwork getEnumNetwork() {
      return this.networkEnum;
   }

   public synchronized String getNetworkName() {
      return this.networkEnum.getName();
   }

   public synchronized Image getNetworkImage() {
      return this.networkEnum.getImage();
   }

   public String getPreferredString() {
      if (this.getEnumNetwork() == EnumNetwork.DONKEY) {
         return this.isPreferred() ? RS_TRUE : RS_FALSE;
      } else {
         return "";
      }
   }

   public Image getPreferredImage() {
      return this.getEnumNetwork() == EnumNetwork.DONKEY ? SResources.getImage(this.isPreferred() ? "bulb-green" : "bulb-red") : null;
   }

   public void togglePreferred() {
      String command = "preferred " + (this.isPreferred() ? "false" : "true") + " " + this.getAddr().toString();
      this.core.send((short)29, command);
   }

   public void rename(String name) {
   }

   protected void read40(MessageBuffer buffer) {
   }

   public synchronized String getVersion() {
      return "";
   }

   public synchronized long getMaxUsers() {
      return 0L;
   }

   public String getMaxUsersString() {
      return SwissArmy.calcStringSizeGrouped(this.getMaxUsers());
   }

   public synchronized long getSoftLimit() {
      return 0L;
   }

   public String getSoftLimitString() {
      return SwissArmy.calcStringSizeGrouped(this.getSoftLimit());
   }

   public synchronized long getHardLimit() {
      return 0L;
   }

   public String getHardLimitString() {
      return SwissArmy.calcStringSizeGrouped(this.getHardLimit());
   }

   public synchronized long getLowIDUsers() {
      return 0L;
   }

   public String getLowIDUsersString() {
      return SwissArmy.calcStringSizeGrouped(this.getLowIDUsers());
   }

   public synchronized int getPing() {
      return 0;
   }

   public String getPingString() {
      return SwissArmy.calcStringSizeGrouped((long)this.getPing());
   }

   public void read(int id, int networkId, MessageBuffer buffer) {
      EnumHostState previousState = this.getStateEnum();
      synchronized (this) {
         this.id = id;
         this.networkEnum = this.readNetworkEnum(networkId);
         this.addr.read(buffer);
         this.port = this.readPort(buffer);
         this.score = buffer.getInt32();
         this.tagList = buffer.getTagList();
         this.numUsers = this.readNUsers(buffer);
         this.numFiles = this.readNFiles(buffer);
         this.stateEnum = this.state.read(buffer);
         this.name = buffer.getString();
         this.description = buffer.getString();
         this.preferred = this.readPreferred(buffer);
         this.read40(buffer);
      }

      this.checkConnected(previousState);
      this.checkRemovedState();
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getInt32(), buffer.getInt32(), buffer);
   }

   protected EnumNetwork readNetworkEnum(int networkId) {
      return this.core.getNetworkCollection().getNetworkEnum(networkId);
   }

   protected boolean readPreferred(MessageBuffer buffer) {
      return false;
   }

   protected long readNUsers(MessageBuffer buffer) {
      return (long)buffer.getInt32();
   }

   protected long readNFiles(MessageBuffer buffer) {
      return (long)buffer.getInt32();
   }

   protected int readPort(MessageBuffer buffer) {
      return (int)((long)buffer.getUInt16() & 65535L);
   }

   public void readUpdate(MessageBuffer buffer) {
      EnumHostState previousState = this.getStateEnum();
      synchronized (this) {
         this.stateEnum = this.state.read(buffer);
      }

      this.checkConnected(previousState);
      this.checkRemovedState();
   }

   public void remove() {
      this.setState(EnumHostState.REMOVE_HOST);
   }

   public void serverUser(MessageBuffer buffer) {
      User user = (User)this.core.getUserCollection().get(buffer.getInt32());
      if (user != null) {
         this.addUserInfo(user);
      }
   }

   public void setState(EnumHostState state) {
      byte opcode = 0;
      if (state == EnumHostState.NOT_CONNECTED) {
         opcode = 22;
      } else if (state == EnumHostState.REMOVE_HOST) {
         opcode = 9;
      } else if (state == EnumHostState.CONNECTING || state == EnumHostState.CONNECTED) {
         opcode = 21;
      }

      if (opcode != 0) {
         this.core.send(opcode, Integer.valueOf(this.getId()));
      }
   }
}
