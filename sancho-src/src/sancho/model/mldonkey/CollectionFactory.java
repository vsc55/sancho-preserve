package sancho.model.mldonkey;

import java.util.ArrayList;
import java.util.List;
import sancho.core.ICore;
import sancho.model.mldonkey.utility.SearchQuery;

public class CollectionFactory {
   protected ICore core;
   protected static CollectionFactory factory;
   protected int protocolVersion;
   protected IObject clientStats;
   protected IObject consoleMessage;
   protected ICollection clientCollection;
   protected ICollection defineSearchesCollection;
   protected ICollection fileCollection;
   protected ICollection networkCollection;
   protected ICollection optionCollection;
   protected ICollection resultCollection;
   protected ICollection roomCollection;
   protected ICollection serverCollection;
   protected ICollection sharedFileCollection;
   protected ICollection userCollection;
   protected List collectionList = new ArrayList();
   protected List objectList = new ArrayList();

   public void disposeAll() {
      for (int var1 = 0; var1 < this.collectionList.size(); var1++) {
         ((ICollection)this.collectionList.get(var1)).dispose();
      }

      for (int var2 = 0; var2 < this.objectList.size(); var2++) {
         ((IObject)this.objectList.get(var2)).deleteObservers();
      }
   }

   public Client getClient() {
      if (this.protocolVersion >= 35) {
         return new Client35(this.core);
      } else if (this.protocolVersion >= 33) {
         return new Client33(this.core);
      } else if (this.protocolVersion >= 23) {
         return new Client23(this.core);
      } else if (this.protocolVersion >= 21) {
         return new Client21(this.core);
      } else if (this.protocolVersion >= 20) {
         return new Client20(this.core);
      } else {
         return (Client)(this.protocolVersion >= 19 ? new Client19(this.core) : new Client(this.core));
      }
   }

   public synchronized ClientCollection getClientCollection() {
      if (this.clientCollection == null) {
         this.clientCollection = new ClientCollection(this.core);
         this.collectionList.add(this.clientCollection);
      }

      return (ClientCollection)this.clientCollection;
   }

   public synchronized ClientStats getClientStats() {
      if (this.clientStats == null) {
         if (this.protocolVersion >= 18) {
            this.clientStats = new ClientStats18(this.core);
         } else {
            this.clientStats = new ClientStats(this.core);
         }

         this.objectList.add(this.clientStats);
      }

      return (ClientStats)this.clientStats;
   }

   public synchronized ConsoleMessage getConsoleMessage() {
      if (this.consoleMessage == null) {
         this.consoleMessage = new ConsoleMessage();
         this.objectList.add(this.consoleMessage);
      }

      return (ConsoleMessage)this.consoleMessage;
   }

   public synchronized DefineSearchesCollection getDefineSearchesCollection() {
      if (this.defineSearchesCollection == null) {
         this.defineSearchesCollection = new DefineSearchesCollection(this.core);
         this.collectionList.add(this.defineSearchesCollection);
      }

      return (DefineSearchesCollection)this.defineSearchesCollection;
   }

   public File getFile() {
      if (this.protocolVersion >= 41) {
         return new File41(this.core);
      } else if (this.protocolVersion >= 36) {
         return new File36(this.core);
      } else if (this.protocolVersion >= 31) {
         return new File31(this.core);
      } else if (this.protocolVersion >= 25) {
         return new File25(this.core);
      } else if (this.protocolVersion >= 24) {
         return new File24(this.core);
      } else if (this.protocolVersion >= 21) {
         return new File21(this.core);
      } else if (this.protocolVersion >= 20) {
         return new File20(this.core);
      } else {
         return (File)(this.protocolVersion >= 18 ? new File18(this.core) : new File(this.core));
      }
   }

   public synchronized FileCollection getFileCollection() {
      if (this.fileCollection == null) {
         this.fileCollection = new FileCollection(this.core);
         this.collectionList.add(this.fileCollection);
      }

      return (FileCollection)this.fileCollection;
   }

   public Network getNetwork() {
      if (this.protocolVersion >= 41) {
         return new Network41(this.core);
      } else {
         return (Network)(this.protocolVersion >= 18 ? new Network18(this.core) : new Network(this.core));
      }
   }

   public synchronized NetworkCollection getNetworkCollection() {
      if (this.networkCollection == null) {
         this.networkCollection = new NetworkCollection(this.core);
         this.collectionList.add(this.networkCollection);
      }

      return (NetworkCollection)this.networkCollection;
   }

   public Option getOption() {
      return (Option)(this.protocolVersion >= 17 ? new Option17(this.core) : new Option(this.core));
   }

   public synchronized OptionCollection getOptionCollection() {
      if (this.optionCollection == null) {
         this.optionCollection = new OptionCollection(this.core);
         this.collectionList.add(this.optionCollection);
      }

      return (OptionCollection)this.optionCollection;
   }

   public Result getResult() {
      if (this.protocolVersion >= 27) {
         return new Result27(this.core);
      } else {
         return (Result)(this.protocolVersion >= 25 ? new Result25(this.core) : new Result(this.core));
      }
   }

   public synchronized ResultCollection getResultCollection() {
      if (this.resultCollection == null) {
         this.resultCollection = new ResultCollection(this.core);
         this.collectionList.add(this.resultCollection);
      }

      return (ResultCollection)this.resultCollection;
   }

   public Room getRoom() {
      return (Room)(this.protocolVersion >= 3 ? new Room3(this.core) : new Room(this.core));
   }

   public synchronized RoomCollection getRoomCollection() {
      if (this.roomCollection == null) {
         this.roomCollection = new RoomCollection(this.core);
         this.collectionList.add(this.roomCollection);
      }

      return (RoomCollection)this.roomCollection;
   }

   public SearchQuery getSearchQuery() {
      return new SearchQuery(this.core);
   }

   public Server getServer() {
      if (this.protocolVersion >= 40) {
         return new Server40(this.core);
      } else if (this.protocolVersion >= 32) {
         return new Server32(this.core);
      } else if (this.protocolVersion >= 29) {
         return new Server29(this.core);
      } else {
         return (Server)(this.protocolVersion >= 28 ? new Server28(this.core) : new Server(this.core));
      }
   }

   public synchronized ServerCollection getServerCollection() {
      if (this.serverCollection == null) {
         this.serverCollection = new ServerCollection(this.core);
         this.collectionList.add(this.serverCollection);
      }

      return (ServerCollection)this.serverCollection;
   }

   public SharedFile getSharedFile() {
      if (this.protocolVersion >= 41) {
         return new SharedFile41(this.core);
      } else if (this.protocolVersion >= 37) {
         return new SharedFile37(this.core);
      } else if (this.protocolVersion >= 31) {
         return new SharedFile31(this.core);
      } else {
         return (SharedFile)(this.protocolVersion >= 25 ? new SharedFile25(this.core) : new SharedFile(this.core));
      }
   }

   public synchronized SharedFileCollection getSharedFileCollection() {
      if (this.sharedFileCollection == null) {
         this.sharedFileCollection = new SharedFileCollection(this.core);
         this.collectionList.add(this.sharedFileCollection);
      }

      return (SharedFileCollection)this.sharedFileCollection;
   }

   public User getUser() {
      return new User(this.core);
   }

   public synchronized UserCollection getUserCollection() {
      if (this.userCollection == null) {
         this.userCollection = new UserCollection(this.core);
         this.collectionList.add(this.userCollection);
      }

      return (UserCollection)this.userCollection;
   }

   public static void dispose() {
      if (factory != null) {
         factory.disposeAll();
      }

      factory = null;
      System.gc();
   }

   public static CollectionFactory getFactory(int var0, ICore var1) {
      if (factory == null) {
         factory = new CollectionFactory();
      }

      factory.protocolVersion = var0;
      factory.core = var1;
      return factory;
   }
}
