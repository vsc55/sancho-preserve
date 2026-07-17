package sancho.view.mainWindow;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;
import sancho.core.Sancho;
import sancho.model.mldonkey.ClientStats;
import sancho.utility.MyObservable;
import sancho.utility.VersionInfo;
import sancho.view.MainWindow;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.statusline.actions.DNDBoxAction;
import sancho.view.statusline.actions.PreferencesAction;
import sancho.view.utility.dialogs.BandwidthDialog;

public class MinimizerTray extends Minimizer implements DisposeListener, IMenuListener {
   protected static final String S_DL = "DL: ";
   protected static final String S_UL = ", UL: ";
   private TrayItem trayItem;
   private MenuManager popupMenu;
   private boolean closeMe = false;
   private MainWindow mainWindow;

   public MinimizerTray(MainWindow var1, String var2) {
      super(var1, var2);
      this.mainWindow = var1;
      this.createTrayIcon();
      this.createMenu();
      this.shell.addDisposeListener(this);
      this.setConnected(true);
   }

   public void setConnected(boolean var1) {
      super.setConnected(var1);
      if (var1 && Sancho.hasCollectionFactory()) {
         Sancho.getCore().getClientStats().addObserver(this);
      }
   }

   private void createTrayIcon() {
      this.trayItem = new TrayItem(this.mainWindow.getShell().getDisplay().getSystemTray(), 0);
      if (this.trayItem == null) {
         MessageBox var1 = new MessageBox(new Shell(), 32);
         var1.setMessage("trayItem is null");
         var1.open();
      } else {
         this.trayItem.setImage(VersionInfo.getTrayIcon());
         this.trayItem.setToolTipText(this.titleBarText);
         this.trayItem.addListener(35, new MinimizerTray$1(this));
         this.trayItem.addSelectionListener(new MinimizerTray$2(this));
      }
   }

   private void createMenu() {
      this.popupMenu = new MenuManager("");
      this.popupMenu.setRemoveAllWhenShown(true);
      this.popupMenu.addMenuListener(this);
   }

   public boolean close() {
      if (!this.closeMe && PreferenceLoader.loadBoolean("minimizeOnClose")) {
         this.hide();
         return false;
      } else {
         return true;
      }
   }

   public void hide() {
      if (!this.shell.getMinimized()) {
         this.shell.setMinimized(true);
      }

      if (this.shell.isVisible()) {
         this.shell.setVisible(false);
      }

      if (Sancho.getCoreConsole() != null) {
         Sancho.getCoreConsole().getShell().setVisible(false);
      }
   }

   public void forceClose() {
      this.closeMe = true;
   }

   public void startMin() {
      this.hide();
   }

   public boolean minimize(boolean var1) {
      if (!var1 && !PreferenceLoader.loadBoolean("systrayOnMinimize")) {
         return true;
      } else {
         this.hide();
         return false;
      }
   }

   public boolean minimize() {
      return this.minimize(false);
   }

   public void restore() {
      this.isRestoring = true;
      if (!this.shell.isVisible()) {
         this.shell.setVisible(true);
      }

      this.shell.forceActive();
      this.setTitleBarText();
      this.trayItem.setToolTipText(this.titleBarText);
      this.isRestoring = false;
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new DNDBoxAction(this.mainWindow));
      var1.add(new Separator());
      var1.add(new PreferencesAction(this.mainWindow));
      new BandwidthDialog(this.shell, var1);
      var1.add(new Separator());
      var1.add(new MinimizerTray$HideRestoreAction(this));
      var1.add(new MinimizerTray$CloseAction(this));
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (var1 instanceof ClientStats) {
         ClientStats var4 = (ClientStats)var1;
         if (var4 != null && this.shell != null && !this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new MinimizerTray$3(this, var4));
         }
      }
   }

   public void setTrayToolTip(ClientStats var1) {
      StringBuffer var2 = new StringBuffer(32);
      var2.append(this.titleBarText);
      var2.append("\n");
      var2.append(Sancho.getCoreFactory().getDescription());
      var2.append("\n");
      var2.append("DL: ");
      var2.append(var1.getTcpDownRateString());
      var2.append(", UL: ");
      var2.append(var1.getTcpUpRateString());
      this.trayItem.setToolTipText(var2.toString());
   }

   public void widgetDisposed(DisposeEvent var1) {
      if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getClientStats().deleteObserver(this);
      }
   }

   // $VF: synthetic method
   static MenuManager access$000(MinimizerTray var0) {
      return var0.popupMenu;
   }

   // $VF: synthetic method
   static TrayItem access$100(MinimizerTray var0) {
      return var0.trayItem;
   }

   // $VF: synthetic method
   static boolean access$202(MinimizerTray var0, boolean var1) {
      return var0.closeMe = var1;
   }
}
