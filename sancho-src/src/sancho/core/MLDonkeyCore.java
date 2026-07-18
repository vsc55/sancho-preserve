package sancho.core;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import sancho.model.mldonkey.ClientCollection;
import sancho.model.mldonkey.ClientStats;
import sancho.model.mldonkey.CollectionFactory;
import sancho.model.mldonkey.ConsoleMessage;
import sancho.model.mldonkey.DefineSearchesCollection;
import sancho.model.mldonkey.FileCollection;
import sancho.model.mldonkey.NetworkCollection;
import sancho.model.mldonkey.OptionCollection;
import sancho.model.mldonkey.ResultCollection;
import sancho.model.mldonkey.RoomCollection;
import sancho.model.mldonkey.ServerCollection;
import sancho.model.mldonkey.SharedFileCollection;
import sancho.model.mldonkey.UserCollection;
import sancho.model.mldonkey.utility.ClientMessage;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.MessageEncoder;
import sancho.model.mldonkey.utility.UtilityFactory;
import sancho.utility.MyObservable;
import sancho.utility.SwissArmy;
import sancho.view.SharesTab;
import sancho.view.TransferTab;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;

public class MLDonkeyCore extends MyObservable implements ICore {
   protected static final int MAX_PROTOCOL = 41;
   protected int activeProtocol;
   protected AbstractTab activeTab;
   protected CollectionFactory collectionFactory;
   protected boolean connected;
   protected boolean connectionDenied;
   protected int coreProtocol;
   protected boolean initialized;
   protected boolean invalidPassword = true;
   protected int max_from_gui;
   protected int max_to_gui;
   protected MessageEncoder messageEncoder;
   protected MessageBuffer messageBuffer;
   protected String password;
   protected boolean pollMode;
   protected boolean pollPending;
   protected boolean pollUploaders;
   protected boolean pollUpStats;
   protected int pollDelay;
   protected int statsDelay;
   protected Socket socket;
   protected Timer timer;
   protected String username;
   protected boolean semaphore;
   protected long lastPollForStats;
   protected long lastStats;
   protected int requestFileInfoDelay;
   protected long lastRequestFileInfos;
   protected int timerCounter;
   protected String mldonkeyVersion = "";
   protected int opCode;

   public MLDonkeyCore(Socket socket, String username, String password, boolean pollMode) {
      this.socket = socket;
      this.username = username;
      this.password = password;
      this.pollMode = pollMode;
      this.semaphore = false;
      this.messageEncoder = new MessageEncoder(socket);
      this.updatePreferences();
   }

   public void checkIfDenied() {
      if (!this.initialized) {
         this.connected = false;
         this.connectionDenied = true;
         this.semaphore = true;
      }
   }

   public synchronized void connect() {
      this.connected = true;
   }

   public synchronized void disconnect() {
      this.connected = false;
   }

   protected void enablePollMode() {
      Object[] args = new Object[]{Short.valueOf((short)1), Integer.valueOf(1), Byte.valueOf((byte)1)};
      this.send((short)47, args);
   }

   public synchronized ClientCollection getClientCollection() {
      return this.getCollectionFactory().getClientCollection();
   }

   public synchronized ClientStats getClientStats() {
      return this.getCollectionFactory().getClientStats();
   }

   public synchronized CollectionFactory getCollectionFactory() {
      return this.collectionFactory;
   }

   public synchronized ConsoleMessage getConsoleMessage() {
      return this.getCollectionFactory().getConsoleMessage();
   }

   public synchronized DefineSearchesCollection getDefineSearchesCollection() {
      return this.getCollectionFactory().getDefineSearchesCollection();
   }

   public synchronized FileCollection getFileCollection() {
      return this.getCollectionFactory().getFileCollection();
   }

   public synchronized NetworkCollection getNetworkCollection() {
      return this.getCollectionFactory().getNetworkCollection();
   }

   public synchronized OptionCollection getOptionCollection() {
      return this.getCollectionFactory().getOptionCollection();
   }

   public int getProtocol() {
      return this.activeProtocol;
   }

   public synchronized ResultCollection getResultCollection() {
      return this.getCollectionFactory().getResultCollection();
   }

   public synchronized RoomCollection getRoomCollection() {
      return this.getCollectionFactory().getRoomCollection();
   }

   public synchronized ServerCollection getServerCollection() {
      return this.getCollectionFactory().getServerCollection();
   }

   public synchronized SharedFileCollection getSharedFileCollection() {
      return this.getCollectionFactory().getSharedFileCollection();
   }

   public synchronized UserCollection getUserCollection() {
      return this.getCollectionFactory().getUserCollection();
   }

   public String getCoreVersion() {
      return this.mldonkeyVersion != null ? this.mldonkeyVersion : "";
   }

   public synchronized boolean isConnected() {
      return this.connected;
   }

   public boolean initialized() {
      return this.initialized;
   }

   public boolean semaphore() {
      return this.semaphore;
   }

   public boolean isConnectionDenied() {
      return this.connectionDenied;
   }

   public boolean isInvalidPassword() {
      return this.invalidPassword;
   }

   public void notifyInitialized() {
      if (!this.initialized) {
         this.invalidPassword = false;
         this.semaphore = true;
         this.initialized = true;
      }
   }

   public void notifyObject(Object object) {
      this.setChanged();
      this.notifyObservers(object);
   }

   private void onIOException(IOException exception) {
      this.disconnect();
      this.setChanged();
      this.notifyObservers(exception);
   }

   private void pollStats() {
      this.getNetworkCollection().getAllStats();
   }

   private void pollForStats() {
      if (this.pollUpStats && this.activeTab instanceof SharesTab) {
         this.send((short)49);
      }

      if (this.activeTab instanceof TransferTab) {
         if (this.getProtocol() >= 23) {
            if (this.pollUploaders) {
               this.send((short)57);
            }

            if (this.pollPending) {
               this.send((short)58);
            }
         } else if (this.pollUploaders && Sancho.hasCollectionFactory()) {
            this.getClientCollection().updateUploaders(this);
         }
      }
   }

   protected void readCoreProtocol(MessageBuffer buffer) {
      this.coreProtocol = buffer.getInt32();
      this.activeProtocol = Math.min(this.coreProtocol, 41);
      if (this.activeProtocol > 29) {
         this.max_to_gui = buffer.getInt32();
         this.max_from_gui = buffer.getInt32();
      }

      if (this.pollMode) {
         this.enablePollMode();
      }

      this.sendPassword();
      this.sendInterestedInSources();
      this.sendGetVersion();
      this.collectionFactory = CollectionFactory.getFactory(this.activeProtocol, this);
      this.startTimer();
   }

   void processMessage(int opcode, MessageBuffer buffer) throws Exception {
      switch (opcode) {
         case 0:
            this.readCoreProtocol(buffer);
            break;
         case 1:
            if (!this.initialized) {
               this.notifyInitialized();
            }

            this.getOptionCollection().read(buffer);
         case 2:
         case 7:
         case 8:
         case 11:
         case 14:
         case 17:
         case 22:
         case 25:
         case 28:
         case 29:
         case 30:
         case 33:
         case 37:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 54:
         case 57:
         default:
            break;
         case 3:
            this.getDefineSearchesCollection().read(buffer);
            break;
         case 4:
            this.getResultCollection().resultInfo(buffer);
            break;
         case 5:
            this.getResultCollection().read(buffer);
            break;
         case 6:
            this.getResultCollection().searchWaiting(buffer);
            break;
         case 9:
            this.getClientCollection().updateAvailability(buffer);
            break;
         case 10:
            this.getFileCollection().addSource(buffer);
            break;
         case 12:
            this.getServerCollection().serverUser(buffer);
            break;
         case 13:
            this.getServerCollection().readUpdate(buffer);
            break;
         case 15:
            this.getClientCollection().read(buffer);
            break;
         case 16:
            this.getClientCollection().readUpdate(buffer);
            break;
         case 18:
            this.getClientCollection().clientFile(buffer);
            break;
         case 19:
            this.getConsoleMessage().read(buffer);
            break;
         case 20:
            this.getNetworkCollection().read(buffer);
            break;
         case 21:
            this.getUserCollection().read(buffer);
            break;
         case 23:
            this.getRoomCollection().roomMessage(buffer);
            break;
         case 24:
            this.getRoomCollection().addUser(buffer);
            break;
         case 26:
            this.getServerCollection().read(buffer);
            break;
         case 27:
            ClientMessage clientMessage = UtilityFactory.getClientMessage(this);
            clientMessage.read(buffer);
            this.setChanged();
            this.notifyObservers(clientMessage);
            break;
         case 31:
            this.getRoomCollection().read(buffer);
            break;
         case 32:
            this.getRoomCollection().removeUser(buffer);
            break;
         case 34:
            this.getSharedFileCollection().upload(buffer);
            break;
         case 35:
            this.getSharedFileCollection().unshared(buffer);
            break;
         case 36:
            this.getOptionCollection().addSectionOption(buffer);
            break;
         case 38:
            this.getOptionCollection().addPluginOption(buffer);
            break;
         case 46:
            this.getFileCollection().update(buffer);
            break;
         case 47:
            this.disconnect();
            this.semaphore = true;
            break;
         case 48:
            this.getSharedFileCollection().read(buffer);
            break;
         case 49:
            if (!this.initialized) {
               this.notifyInitialized();
            }

            this.getClientStats().read(buffer);
            break;
         case 50:
            this.getFileCollection().removeSource(buffer);
            break;
         case 51:
            this.getClientCollection().clean(buffer);
            this.getServerCollection().clean(buffer);
            this.getFileCollection().clean();
            break;
         case 52:
            this.getFileCollection().add(buffer);
            break;
         case 53:
            this.getFileCollection().read(buffer);
            break;
         case 55:
            this.getClientCollection().uploaders(buffer);
            break;
         case 56:
            this.getClientCollection().pending(buffer);
            break;
         case 58:
            this.mldonkeyVersion = buffer.getString();
            break;
         case 59:
            this.getNetworkCollection().readStats(buffer);
      }
   }

   public String getLastMessage() {
      return this.messageBuffer == null
         ? "NMB"
         : this.opCode + "/" + this.coreProtocol + "/" + this.messageBuffer.getLastLength() + ": \n" + this.messageBuffer.getLastMessage();
   }

   public void run() throws RuntimeException {
      this.opCode = -1;

      try {
         this.messageBuffer = new MessageBuffer(this, new BufferedInputStream(this.socket.getInputStream()));
         this.sendProtocolVersion();

         while (this.connected) {
            this.opCode = this.messageBuffer.readMessage();
            this.processMessage(this.opCode, this.messageBuffer);
         }
      } catch (SocketException socketException) {
         socketException.printStackTrace();
         this.checkIfDenied();
      } catch (IOException ioException) {
         this.checkIfDenied();
         this.onIOException(ioException);
      } catch (Exception exception) {
         String lastMessage = "";
         int lastLength = -1;
         if (this.messageBuffer != null) {
            lastMessage = this.messageBuffer.getLastMessage();
            lastLength = this.messageBuffer.getLastLength();
         }

         Sancho.threadException(
            "Core o(" + this.opCode + ") l(" + lastLength + ") p(" + this.coreProtocol + ") m(" + this.getCoreVersion() + ") t(" + this.activeTab + ")\n\n" + lastMessage,
            exception
         );
         this.onIOException(new IOException());
      }

      this.stopTimer();
      if (this.collectionFactory != null) {
         CollectionFactory.dispose();
      }
   }

   public void send(short opcode) {
      this.send(opcode, null);
   }

   public void send(short opcode, Object arg) {
      this.send(opcode, new Object[]{arg});
   }

   public void send(short opcode, Object[] args) {
      try {
         this.messageEncoder.send(opcode, args);
      } catch (IOException ioException) {
         this.onIOException(ioException);
      }
   }

   protected void sendInterestedInSources() {
      this.sendInterestedInSources(PreferenceLoader.loadBoolean("mldonkey.InterestedInSources"));
   }

   protected void sendInterestedInSources(boolean interested) {
      if (this.getProtocol() >= 27) {
         this.send((short)64, Byte.valueOf((byte)(interested ? 1 : 0)));
      }
   }

   protected void sendPassword() {
      this.send((short)52, new String[]{this.password, this.username});
   }

   protected void sendGetVersion() {
      if (this.getProtocol() > 29) {
         this.send((short)65);
      }
   }

   protected void sendProtocolVersion() {
      this.send((short)0, Integer.valueOf(41));
   }

   public void setActiveTab(AbstractTab tab) {
      this.activeTab = tab;
      this.pollForStats();
   }

   private void startTimer() {
      this.lastRequestFileInfos = System.currentTimeMillis();
      if (this.timer != null) {
         this.timer.cancel();
      }

      this.timer = new Timer();
      this.timer.scheduleAtFixedRate(new TimerTask() {
         public void run() {
            if (!MLDonkeyCore.this.isConnected()) {
               this.cancel();
            } else {
               long now = System.currentTimeMillis();
               if (now > MLDonkeyCore.this.lastPollForStats + (long)(MLDonkeyCore.this.pollDelay * 1000)) {
                  MLDonkeyCore.this.pollForStats();
                  MLDonkeyCore.this.lastPollForStats = now;
               }

               if (now > MLDonkeyCore.this.lastStats + (long)(MLDonkeyCore.this.statsDelay * 1000)) {
                  MLDonkeyCore.this.pollStats();
                  MLDonkeyCore.this.lastStats = now;
               }

               if (MLDonkeyCore.this.requestFileInfoDelay > 0
                  && now > MLDonkeyCore.this.lastRequestFileInfos + (long)(MLDonkeyCore.this.requestFileInfoDelay * 1000)) {
                  MLDonkeyCore.this.getFileCollection().requestAllFileInfos();
                  MLDonkeyCore.this.lastRequestFileInfos = now;
               }

               if (MLDonkeyCore.this.timerCounter++ == 5) {
                  MLDonkeyCore.this.getClientCollection().cleanDeadClients();
               }

               MLDonkeyCore.this.getFileCollection().sendUpdate();
            }
         }
      }, 0L, 777L);
   }

   private void stopTimer() {
      if (this.timer != null) {
         this.timer.cancel();
      }
   }

   public void updatePreferences() {
      SwissArmy.updatePreferences();
      this.pollUpStats = PreferenceLoader.loadBoolean("pollUpStats");
      this.pollUploaders = PreferenceLoader.loadBoolean("pollUploaders");
      this.pollPending = PreferenceLoader.loadBoolean("pollPending");
      this.pollDelay = PreferenceLoader.loadInt("pollDelay");
      this.requestFileInfoDelay = PreferenceLoader.loadInt("requestFileInfoDelay");
      this.statsDelay = PreferenceLoader.loadInt("statsDelay");
      this.sendInterestedInSources();
      if (this.getCollectionFactory() != null) {
         this.getServerCollection().updatePreferences();
         this.getFileCollection().updatePreferences();
         this.getResultCollection().updatePreferences();
      }
   }
}
