package sancho.view;

import gnu.trove.TIntObjectHashMap;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
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

public class RoomsTab extends AbstractTab implements MyObserver {
   protected CTabFolder cTabFolder;
   protected RoomsTab$RoomCTabFolderViewFrame cTabFolderViewFrame;
   protected TIntObjectHashMap roomTabMap;

   public RoomsTab(MainWindow var1, String var2) {
      super(var1, var2);
   }

   public void closeAllClosedRooms() {
      Object[] var1 = this.roomTabMap.getValues();

      for (int var2 = 0; var2 < var1.length; var2++) {
         CTabItem var3 = (CTabItem)var1[var2];
         Room var4 = (Room)var3.getData("room");
         if (var4.getRoomState() == EnumRoomState.CLOSED) {
            this.closeTab(var3);
         }
      }
   }

   public void dispose() {
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getRoomCollection().deleteObserver(this);
      }

      if (this.cTabFolder != null && !this.cTabFolder.isDisposed()) {
         CTabItem[] var1 = this.cTabFolder.getItems();

         for (int var2 = 0; var2 < var1.length; var2++) {
            var1[var2].dispose();
         }

         this.cTabFolder.dispose();
         this.cTabFolder = null;
      }

      super.dispose();
   }

   public void closeAllTabs() {
      Object[] var1 = this.roomTabMap.getValues();

      for (int var2 = 0; var2 < var1.length; var2++) {
         CTabItem var3 = (CTabItem)var1[var2];
         this.closeTab(var3);
      }

      this.roomTabMap.clear();
   }

   public void closeTab(CTabItem var1) {
      Room var2 = (Room)var1.getData("room");
      RoomUsersViewFrame var3 = (RoomUsersViewFrame)var1.getData("roomUsersViewFrame");
      SashViewFrame var4 = (SashViewFrame)var1.getData("consoleViewFrame");
      this.removeViewFrame(var3);
      this.removeViewFrame(var4);
      this.roomTabMap.remove(var2.getId());
      var2.close();
      ((SashForm)var1.getData("roomSashForm")).dispose();
      ((RoomConsole)var1.getData("roomConsole")).dispose();
      var1.dispose();
      this.cTabFolderViewFrame.updateCLabelText("");
   }

   public void closeTab(int var1) {
      if (this.roomTabMap.contains(var1)) {
         this.closeTab((CTabItem)this.roomTabMap.get(var1));
      }
   }

   protected void createContents(Composite var1) {
      this.roomTabMap = new TIntObjectHashMap();
      String var2 = "roomsSash";
      SashForm var3 = WidgetFactory.createSashForm(var1, var2);
      this.addViewFrame(new RoomsViewFrame(var3, "t.r.availableRooms", "tab.rooms.buttonSmall", this));
      this.createRoomsCTabFolder(var3);
      WidgetFactory.loadSashForm(var3, var2);
      this.onConnect();
   }

   public void createRoomsCTabFolder(SashForm var1) {
      this.cTabFolderViewFrame = new RoomsTab$RoomCTabFolderViewFrame(this, var1, "t.r.rooms", "tab.rooms.buttonSmall", this);
      this.addViewFrame(this.cTabFolderViewFrame);
      int var2 = PreferenceLoader.loadBoolean("roomsCTabFolderTabsOnTop") ? 128 : 1024;
      this.cTabFolder = WidgetFactory.createCTabFolder(this.cTabFolderViewFrame.getChildComposite(), 8388672 | var2);
      WidgetFactory.addCTabFolderMenu(this.cTabFolder, "roomsCTabFolder");
      this.cTabFolder.setBorderVisible(false);
      this.cTabFolder.addCTabFolder2Listener(new RoomsTab$1(this));
      this.cTabFolder.addSelectionListener(new RoomsTab$2(this));
   }

   public void onConnect() {
      super.onConnect();
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getRoomCollection().addObserver(this);
         Room[] var1 = this.getCore().getRoomCollection().getAllOpenRooms();

         for (int var2 = 0; var2 < var1.length; var2++) {
            this.openTab(var1[var2]);
         }
      }
   }

   public void openTab(int var1) {
      Room var2 = this.getCore().getRoomCollection().getRoom(var1);
      if (var2 != null) {
         this.openTab(var2);
      }
   }

   public void openTab(Room var1) {
      CTabItem var2 = new CTabItem(this.cTabFolder, 0);
      var2.setText(var1.getName());
      this.roomTabMap.put(var1.getId(), var2);
      String var3 = "roomSash";
      SashForm var4 = WidgetFactory.createSashForm(this.cTabFolder, var3);
      RoomUsersViewFrame var5 = new RoomUsersViewFrame(var4, "t.r.roomUsers", "tab.friends.buttonSmall", this, var1);
      var5.setActive(true);
      var5.setVisible(true);
      this.addViewFrame(var5);
      RoomConsoleViewFrame var6 = new RoomConsoleViewFrame(var4, "t.r.roomConsole", "tab.console.buttonSmall", this);
      RoomConsole var7 = new RoomConsole(var6.getChildComposite(), 64, var1.getId());
      WidgetFactory.loadSashForm(var4, var3);
      var2.setData("roomSashForm", var4);
      var2.setData("roomUsersViewFrame", var5);
      var2.setData("roomConsoleViewFrame", var6);
      var2.setData("roomConsole", var7);
      var2.setData("room", var1);
      var2.setControl(var4);
      if (this.cTabFolder.getSelection() == null) {
         this.cTabFolder.setSelection(var2);
         this.setCTabFolderSelection(var2);
      }
   }

   public void processRoom(Room var1) {
      if (var1.getRoomState() == EnumRoomState.OPEN && !this.roomTabMap.contains(var1.getId())) {
         this.openTab(var1);
      } else if (var1.getRoomState() == EnumRoomState.CLOSED && this.roomTabMap.contains(var1.getId()) && PreferenceLoader.loadBoolean("autoCloseRooms")) {
         this.closeTab(var1.getId());
      }
   }

   public void processRoomMessage(RoomMessage var1) {
      String var2 = "";
      EnumMessage var3 = var1.getMessageType();
      if (var3 == EnumMessage.PRIVATE) {
         var2 = "(private) ";
      }

      if (var3 == EnumMessage.SERVER || var3 == EnumMessage.PRIVATE || var3 == EnumMessage.PUBLIC) {
         if (var1.getFrom() > 0) {
            User var4 = (User)this.getCore().getUserCollection().get(var1.getFrom());
            var2 = var2 + "<" + var4.getName() + "> ";
         }

         var2 = var2 + var1.getMessage();
         if (!this.roomTabMap.contains(var1.getRoomNumber()) && PreferenceLoader.loadBoolean("autoOpenRooms")) {
            this.openTab(var1.getRoomNumber());
         }

         if (this.roomTabMap.contains(var1.getRoomNumber())) {
            CTabItem var7 = (CTabItem)this.roomTabMap.get(var1.getRoomNumber());
            RoomConsole var5 = (RoomConsole)var7.getData("roomConsole");
            var5.append(var2 + var5.getLineDelimiter());
         }
      }
   }

   public void runUpdate(MyObservable var1, Object var2) {
      if (this.cTabFolder != null && !this.cTabFolder.isDisposed()) {
         if (var2 instanceof RoomMessage) {
            this.processRoomMessage((RoomMessage)var2);
         } else if (var2 instanceof Room) {
            this.processRoom((Room)var2);
         }
      }
   }

   public void setCTabFolderSelection(CTabItem var1) {
      Room var2 = (Room)var1.getData("room");
      RoomConsole var3 = (RoomConsole)var1.getData("roomConsole");
      this.cTabFolderViewFrame.updateCLabelText(var2.getName());
      var3.setFocus();
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (this.getContent() != null && !this.getContent().isDisposed()) {
         this.getContent().getDisplay().asyncExec(new RoomsTab$3(this, var1, var2));
      }
   }
}
