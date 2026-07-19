package sancho.view;

import gnu.trove.TIntObjectHashMap;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.Room;
import sancho.model.mldonkey.User;
import sancho.model.mldonkey.enums.EnumMessage;
import sancho.model.mldonkey.enums.EnumRoomState;
import sancho.model.mldonkey.utility.RoomMessage;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.view.console.RoomConsole;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.rooms.RoomsViewFrame;
import sancho.view.rooms.roomConsole.RoomConsoleViewFrame;
import sancho.view.rooms.roomUsers.RoomUsersViewFrame;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class RoomsTab extends AbstractTab implements MyObserver {
   protected CTabFolder cTabFolder;
   protected RoomCTabFolderViewFrame cTabFolderViewFrame;
   protected TIntObjectHashMap roomTabMap;

   public RoomsTab(MainWindow mainWindow, String id) {
      super(mainWindow, id);
   }

   public void closeAllClosedRooms() {
      Object[] values = this.roomTabMap.getValues();

      for (int i = 0; i < values.length; i++) {
         CTabItem item = (CTabItem)values[i];
         Room room = (Room)item.getData("room");
         if (room.getRoomState() == EnumRoomState.CLOSED) {
            this.closeTab(item);
         }
      }
   }

   public void dispose() {
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getRoomCollection().deleteObserver(this);
      }

      if (this.cTabFolder != null && !this.cTabFolder.isDisposed()) {
         CTabItem[] items = this.cTabFolder.getItems();

         for (int i = 0; i < items.length; i++) {
            items[i].dispose();
         }

         this.cTabFolder.dispose();
         this.cTabFolder = null;
      }

      super.dispose();
   }

   public void closeAllTabs() {
      Object[] values = this.roomTabMap.getValues();

      for (int i = 0; i < values.length; i++) {
         CTabItem item = (CTabItem)values[i];
         this.closeTab(item);
      }

      this.roomTabMap.clear();
   }

   public void closeTab(CTabItem item) {
      Room room = (Room)item.getData("room");
      RoomUsersViewFrame roomUsersViewFrame = (RoomUsersViewFrame)item.getData("roomUsersViewFrame");
      SashViewFrame consoleViewFrame = (SashViewFrame)item.getData("consoleViewFrame");
      this.removeViewFrame(roomUsersViewFrame);
      this.removeViewFrame(consoleViewFrame);
      this.roomTabMap.remove(room.getId());
      room.close();
      ((SashForm)item.getData("roomSashForm")).dispose();
      ((RoomConsole)item.getData("roomConsole")).dispose();
      item.dispose();
      this.cTabFolderViewFrame.updateCLabelText("");
   }

   public void closeTab(int roomId) {
      if (this.roomTabMap.contains(roomId)) {
         this.closeTab((CTabItem)this.roomTabMap.get(roomId));
      }
   }

   protected void createContents(Composite composite) {
      this.roomTabMap = new TIntObjectHashMap();
      String sashPrefName = "roomsSash";
      SashForm sashForm = WidgetFactory.createSashForm(composite, sashPrefName);
      this.addViewFrame(new RoomsViewFrame(sashForm, "t.r.availableRooms", "tab.rooms.buttonSmall", this));
      this.createRoomsCTabFolder(sashForm);
      WidgetFactory.loadSashForm(sashForm, sashPrefName);
      this.onConnect();
   }

   public void createRoomsCTabFolder(SashForm sashForm) {
      this.cTabFolderViewFrame = new RoomCTabFolderViewFrame(sashForm, "t.r.rooms", "tab.rooms.buttonSmall", this);
      this.addViewFrame(this.cTabFolderViewFrame);
      int style = PreferenceLoader.loadBoolean("roomsCTabFolderTabsOnTop") ? 128 : 1024;
      this.cTabFolder = WidgetFactory.createCTabFolder(this.cTabFolderViewFrame.getChildComposite(), 8388672 | style);
      WidgetFactory.addCTabFolderMenu(this.cTabFolder, "roomsCTabFolder");
      this.cTabFolder.setBorderVisible(false);
      this.cTabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
         public void close(CTabFolderEvent event) {
            RoomsTab.this.closeTab((CTabItem)event.item);
         }
      });
      this.cTabFolder.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            RoomsTab.this.setCTabFolderSelection((CTabItem)event.item);
         }
      });
   }

   public void onConnect() {
      super.onConnect();
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getRoomCollection().addObserver(this);
         Room[] rooms = this.getCore().getRoomCollection().getAllOpenRooms();

         for (int i = 0; i < rooms.length; i++) {
            this.openTab(rooms[i]);
         }
      }
   }

   public void openTab(int roomId) {
      Room room = this.getCore().getRoomCollection().getRoom(roomId);
      if (room != null) {
         this.openTab(room);
      }
   }

   public void openTab(Room room) {
      CTabItem item = new CTabItem(this.cTabFolder, 0);
      item.setText(room.getName());
      this.roomTabMap.put(room.getId(), item);
      String sashPrefName = "roomSash";
      SashForm sashForm = WidgetFactory.createSashForm(this.cTabFolder, sashPrefName);
      RoomUsersViewFrame roomUsersViewFrame = new RoomUsersViewFrame(sashForm, "t.r.roomUsers", "tab.friends.buttonSmall", this, room);
      roomUsersViewFrame.setActive(true);
      roomUsersViewFrame.setVisible(true);
      this.addViewFrame(roomUsersViewFrame);
      RoomConsoleViewFrame roomConsoleViewFrame = new RoomConsoleViewFrame(sashForm, "t.r.roomConsole", "tab.console.buttonSmall", this);
      RoomConsole roomConsole = new RoomConsole(roomConsoleViewFrame.getChildComposite(), 64, room.getId());
      WidgetFactory.loadSashForm(sashForm, sashPrefName);
      item.setData("roomSashForm", sashForm);
      item.setData("roomUsersViewFrame", roomUsersViewFrame);
      item.setData("roomConsoleViewFrame", roomConsoleViewFrame);
      item.setData("roomConsole", roomConsole);
      item.setData("room", room);
      item.setControl(sashForm);
      if (this.cTabFolder.getSelection() == null) {
         this.cTabFolder.setSelection(item);
         this.setCTabFolderSelection(item);
      }
   }

   public void processRoom(Room room) {
      if (room.getRoomState() == EnumRoomState.OPEN && !this.roomTabMap.contains(room.getId())) {
         this.openTab(room);
      } else if (room.getRoomState() == EnumRoomState.CLOSED && this.roomTabMap.contains(room.getId()) && PreferenceLoader.loadBoolean("autoCloseRooms")) {
         this.closeTab(room.getId());
      }
   }

   public void processRoomMessage(RoomMessage roomMessage) {
      String text = "";
      EnumMessage messageType = roomMessage.getMessageType();
      if (messageType == EnumMessage.PRIVATE) {
         text = "(private) ";
      }

      if (messageType == EnumMessage.SERVER || messageType == EnumMessage.PRIVATE || messageType == EnumMessage.PUBLIC) {
         if (roomMessage.getFrom() > 0) {
            User user = (User)this.getCore().getUserCollection().get(roomMessage.getFrom());
            text = text + "<" + user.getName() + "> ";
         }

         text = text + roomMessage.getMessage();
         if (!this.roomTabMap.contains(roomMessage.getRoomNumber()) && PreferenceLoader.loadBoolean("autoOpenRooms")) {
            this.openTab(roomMessage.getRoomNumber());
         }

         if (this.roomTabMap.contains(roomMessage.getRoomNumber())) {
            CTabItem item = (CTabItem)this.roomTabMap.get(roomMessage.getRoomNumber());
            RoomConsole roomConsole = (RoomConsole)item.getData("roomConsole");
            roomConsole.append(text + roomConsole.getLineDelimiter());
         }
      }
   }

   public void runUpdate(MyObservable observable, Object arg) {
      if (this.cTabFolder != null && !this.cTabFolder.isDisposed()) {
         if (arg instanceof RoomMessage) {
            this.processRoomMessage((RoomMessage)arg);
         } else if (arg instanceof Room) {
            this.processRoom((Room)arg);
         }
      }
   }

   public void setCTabFolderSelection(CTabItem item) {
      Room room = (Room)item.getData("room");
      RoomConsole roomConsole = (RoomConsole)item.getData("roomConsole");
      this.cTabFolderViewFrame.updateCLabelText(room.getName());
      roomConsole.setFocus();
   }

   public void update(final MyObservable observable, final Object arg, int eventType) {
      if (this.getContent() != null && !this.getContent().isDisposed()) {
         this.getContent().getDisplay().asyncExec(new Runnable() {
            public void run() {
               RoomsTab.this.runUpdate(observable, arg);
            }
         });
      }
   }

   // Context-menu listener contributing the available-rooms sash actions.
   private static class RoomCTabFolderViewListener extends SashViewListener {
      public RoomCTabFolderViewListener(SashViewFrame viewFrame) {
         super(viewFrame);
      }

      public void menuAboutToShow(IMenuManager menuManager) {
         this.createSashActions(menuManager, "t.r.availableRooms");
      }
   }

   // View frame hosting the rooms CTabFolder, with close-closed / close-all toolbar actions.
   private class RoomCTabFolderViewFrame extends SashViewFrame {
      public RoomCTabFolderViewFrame(SashForm sashForm, String title, String buttonId, AbstractTab tab) {
         super(sashForm, title, buttonId, tab);
         this.createViewListener(new RoomCTabFolderViewListener(this));
         this.createViewToolBar();
      }

      public void createViewToolBar() {
         super.createViewToolBar();
         this.addToolItem("t.r.closeClosed", "x-light", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               RoomsTab.this.closeAllClosedRooms();
            }
         });
         this.addToolItem("t.r.closeAll", "x", new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               RoomsTab.this.closeAllTabs();
            }
         });
      }
   }
}
