package sancho.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import sancho.core.Sancho;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.utility.ClientMessage;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.utility.SwissArmy;
import sancho.view.console.MessageConsole;
import sancho.view.friends.FriendsTableView;
import sancho.view.friends.FriendsViewFrame;
import sancho.view.friends.clientDirectories.ClientDirectoriesTableView;
import sancho.view.friends.clientDirectories.ClientDirectoriesViewFrame;
import sancho.view.friends.clientFiles.ClientFilesTableView;
import sancho.view.friends.clientFiles.ClientFilesViewFrame;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class FriendsTab extends AbstractTab implements MyObserver {
   private CTabFolder cTabFolder;
   private Hashtable openTabs = new Hashtable();
   private MessagesViewFrame messagesViewFrame;
   private FriendsViewFrame friendsViewFrame;

   public FriendsTab(MainWindow mainWindow, String title) {
      super(mainWindow, title);
   }

   protected void createContents(Composite composite) {
      String sashName = "messagesSash";
      SashForm sashForm = WidgetFactory.createSashForm(composite, sashName);
      this.createLeftSash(sashForm);
      this.createRightSash(sashForm);
      WidgetFactory.loadSashForm(sashForm, sashName);
      this.onConnect();
   }

   public void onConnect() {
      super.onConnect();
      if (this.getCore() != null) {
         this.getCore().addObserver(this);
      }
   }

   public void dispose() {
      if (this.getCore() != null) {
         this.getCore().deleteObserver(this);
      }

      if (this.cTabFolder != null && !this.cTabFolder.isDisposed()) {
         CTabItem[] tabItems = this.cTabFolder.getItems();

         for (int i = 0; i < tabItems.length; i++) {
            tabItems[i].dispose();
         }

         this.cTabFolder.dispose();
         this.cTabFolder = null;
      }

      super.dispose();
   }

   private void createLeftSash(SashForm sashForm) {
      this.friendsViewFrame = new FriendsViewFrame(sashForm, "l.friends", "tab.friends.buttonSmall", this);
      this.addViewFrame(this.friendsViewFrame);
      this.friendsViewFrame.getGView().getComposite().addMouseListener(new MouseAdapter() {
         public void mouseDoubleClick(MouseEvent event) {
            if (event.widget instanceof Table) {
               Table table = (Table)event.widget;
               TableItem[] tableItems = table.getSelection();

               for (int i = 0; i < tableItems.length; i++) {
                  FriendsTab.this.openTab((Client)tableItems[i].getData());
               }
            }
         }
      });
   }

   private void createFilesView(SashForm sashForm) {
      String sashName = "directoriesFilesSash";
      SashForm childSashForm = WidgetFactory.createSashForm(sashForm, sashName);
      ClientDirectoriesViewFrame directoriesViewFrame = new ClientDirectoriesViewFrame(childSashForm, "l.clientDirectories", "tab.friends.buttonSmall", this);
      ((FriendsTableView)this.friendsViewFrame.getGView()).setDirectoryView((ClientDirectoriesTableView)directoriesViewFrame.getGView());
      ClientFilesViewFrame filesViewFrame = new ClientFilesViewFrame(childSashForm, "l.clientFiles", "tab.friends.buttonSmall", this);
      ((ClientDirectoriesTableView)directoriesViewFrame.getGView()).setFilesView((ClientFilesTableView)filesViewFrame.getGView());
      WidgetFactory.loadSashForm(childSashForm, sashName);
      this.addViewFrame(directoriesViewFrame);
      this.addViewFrame(filesViewFrame);
   }

   private void createRightSash(SashForm sashForm) {
      String sashName = "filesMessagesSash";
      SashForm childSashForm = WidgetFactory.createSashForm(sashForm, sashName);
      this.createFilesView(childSashForm);
      this.createMessagesView(childSashForm);
      WidgetFactory.loadSashForm(childSashForm, sashName);
   }

   private void createMessagesView(SashForm sashForm) {
      this.messagesViewFrame = new MessagesViewFrame(sashForm, "l.messageTabs", "tab.friends.buttonSmall", this);
      this.addViewFrame(this.messagesViewFrame);
      int style = PreferenceLoader.loadBoolean("messagesCTabFolderTabsOnTop") ? 128 : 1024;
      this.cTabFolder = WidgetFactory.createCTabFolder(this.messagesViewFrame.getChildComposite(), 8388608 | style);
      WidgetFactory.addCTabFolderMenu(this.cTabFolder, "messagesCTabFolder");
      this.cTabFolder.setBorderVisible(false);
      this.cTabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
         public void close(CTabFolderEvent event) {
            CTabItem tabItem = (CTabItem)event.item;
            MessageConsole messageConsole = (MessageConsole)tabItem.getData("messageConsole");
            Integer id = (Integer)tabItem.getData("id");
            FriendsTab.this.openTabs.remove(id);
            messageConsole.dispose();
            tabItem.dispose();
            FriendsTab.this.setTabsLabel();
         }
      });
      this.cTabFolder.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            CTabItem tabItem = (CTabItem)event.item;
            MessageConsole messageConsole = (MessageConsole)tabItem.getData("messageConsole");
            FriendsTab.this.setTabsLabel();
            messageConsole.setFocus();
         }
      });
   }

   public void closeAllTabs() {
      for (Object keyObject : this.openTabs.keySet()) { Integer id = (Integer)keyObject;
         CTabItem tabItem = (CTabItem)this.openTabs.get(id);
         if (tabItem != null) {
            MessageConsole messageConsole = (MessageConsole)tabItem.getData("messageConsole");
            if (messageConsole != null) {
               messageConsole.dispose();
            }

            tabItem.dispose();
         }
      }

      this.openTabs.clear();
      this.setTabsLabel();
   }

   public void update(MyObservable observable, Object data, int arg) {
      if (data instanceof ClientMessage && !this.cTabFolder.isDisposed()) {
         // Resolve the sender on THIS (core) thread: the client may not be in the
         // collection yet, and the lookup retries with a 1s sleep up to 3 times.
         // Doing it here keeps that sleep off the UI thread — messageFromClient used
         // to sleep inside the asyncExec runnable, freezing the GUI for up to 3s on
         // a message from a not-yet-collected client. Then marshal only the UI work.
         Client client = null;

         for (int i = 0; i < 3 && client == null && Sancho.hasCollectionFactory(); i++) {
            client = (Client)this.getCore().getClientCollection().get(((ClientMessage)data).getId());
            if (client == null) {
               SwissArmy.threadSleep(1000);
            }
         }

         // client is reassigned in the loop above, so capture it in a final for the runnable.
         final Client sender = client;
         this.cTabFolder.getDisplay().asyncExec(new Runnable() {
            public void run() {
               FriendsTab.this.messageFromClient((ClientMessage)data, sender);
            }
         });
      }
   }

   public void setTabsLabel() {
      String selectionText = "";
      if (this.cTabFolder.getSelection() != null) {
         selectionText = " -> " + this.cTabFolder.getSelection().getText();
      }

      this.messagesViewFrame.updateCLabelText(SResources.getString("l.messageTabs") + ": " + this.openTabs.size() + selectionText);
   }

   public void sendTabMessage(int id, String message) {
      CTabItem tabItem = (CTabItem)this.openTabs.get(Integer.valueOf(id));
      MessageConsole messageConsole = (MessageConsole)tabItem.getData("messageConsole");
      messageConsole.append(message + messageConsole.getLineDelimiter());
   }

   public void messageFromClient(ClientMessage clientMessage, Client sender) {
      // sender is the sender client, already resolved on the core thread by update()
      // (may be null if it never appeared) — so no blocking sleep runs here on the UI.
      if (this.cTabFolder != null && !this.cTabFolder.isDisposed() && Sancho.hasCollectionFactory()) {
         StatusLine statusLine = this.getMainWindow().getStatusline();
         statusLine.setText(SResources.getString("l.newMessage"));
         statusLine.setImage(SResources.getImage("new-message"));
         if (this.openTabs.containsKey(Integer.valueOf(clientMessage.getId()))) {
            String messageText;
            if (sender == null) {
               messageText = this.getTimeStamp() + clientMessage.getId() + ": <unknown>> " + clientMessage.getText();
            } else {
               messageText = this.getTimeStamp() + clientMessage.getId() + ": " + sender.getName() + "> " + clientMessage.getText();
            }

            this.sendTabMessage(clientMessage.getId(), messageText);
         } else {
            String header;
            if (sender == null) {
               header = clientMessage.getId() + ": <unknown>";
            } else {
               header = sender.getId() + ": " + sender.getName();
            }

            String messageLine = this.getTimeStamp() + header + "> " + clientMessage.getText();
            CTabItem tabItem = this.addCTabItem(clientMessage.getId(), "  " + header);
            if (this.cTabFolder.getItemCount() == 1) {
               this.setItemFocus(tabItem);
            }

            this.sendTabMessage(clientMessage.getId(), messageLine);
            this.setTabsLabel();
         }
      }
   }

   public CTabItem addCTabItem(int id, String text) {
      CTabItem tabItem = new CTabItem(this.cTabFolder, 0);
      tabItem.setText(text);
      MessageConsole messageConsole = new MessageConsole(this.cTabFolder, 64, id);
      tabItem.setControl(messageConsole.getComposite());
      tabItem.setData("id", Integer.valueOf(id));
      tabItem.setData("messageConsole", messageConsole);
      this.openTabs.put(Integer.valueOf(id), tabItem);
      return tabItem;
   }

   public void openTab(Client client) {
      if (!this.openTabs.containsKey(Integer.valueOf(client.getId()))) {
         String text = "  " + client.getId() + ": " + client.getName();
         this.setItemFocus(this.addCTabItem(client.getId(), text));
      } else {
         this.cTabFolder.setSelection((CTabItem)this.openTabs.get(Integer.valueOf(client.getId())));
      }

      this.setTabsLabel();
   }

   public String getTimeStamp() {
      SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss] ");
      return dateFormat.format(new Date());
   }

   public void setItemFocus(CTabItem tabItem) {
      this.cTabFolder.setSelection(tabItem);
      MessageConsole messageConsole = (MessageConsole)tabItem.getData("messageConsole");
      messageConsole.setFocus();
   }

   public void setActive() {
      super.setActive();
      if (this.getMainWindow().getStatusline() != null) {
         this.getMainWindow().getStatusline().clear();
      }

      if (this.cTabFolder.getSelection() != null) {
         this.setItemFocus(this.cTabFolder.getSelection());
      }
   }

   // View frame hosting the message tabs; contributes the "close all tabs" tool item.
   private class MessagesViewFrame extends SashViewFrame {
      public MessagesViewFrame(SashForm sashForm, String labelKey, String buttonKey, AbstractTab tab) {
         super(sashForm, labelKey, buttonKey, tab);
         this.createViewListener(new MessagesViewListener(this));
         this.createViewToolBar();
      }

      public void createViewToolBar() {
         super.createViewToolBar();
         this.addToolItem("ti.f.closeAllTabs", "x", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               FriendsTab.this.closeAllTabs();
            }
         });
      }
   }

   // Context-menu listener contributing the client-files sash actions.
   private static class MessagesViewListener extends SashViewListener {
      public MessagesViewListener(SashViewFrame sashViewFrame) {
         super(sashViewFrame);
      }

      public void menuAboutToShow(IMenuManager menuManager) {
         this.createSashActions(menuManager, "l.clientFiles");
      }
   }
}
