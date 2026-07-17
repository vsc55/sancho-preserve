package sancho.core;

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
import sancho.utility.MyObserver;
import sancho.view.utility.AbstractTab;

public interface ICore extends Runnable {
   void addObserver(MyObserver var1);

   void deleteObserver(MyObserver var1);

   void deleteObservers();

   void connect();

   void disconnect();

   boolean initialized();

   boolean isInvalidPassword();

   boolean isConnectionDenied();

   boolean isConnected();

   boolean semaphore();

   int getProtocol();

   String getCoreVersion();

   String getLastMessage();

   ClientCollection getClientCollection();

   ClientStats getClientStats();

   ConsoleMessage getConsoleMessage();

   DefineSearchesCollection getDefineSearchesCollection();

   FileCollection getFileCollection();

   CollectionFactory getCollectionFactory();

   NetworkCollection getNetworkCollection();

   OptionCollection getOptionCollection();

   ResultCollection getResultCollection();

   RoomCollection getRoomCollection();

   ServerCollection getServerCollection();

   SharedFileCollection getSharedFileCollection();

   UserCollection getUserCollection();

   void send(short var1, Object[] var2);

   void send(short var1, Object var2);

   void send(short var1);

   void setActiveTab(AbstractTab var1);

   void updatePreferences();
}
