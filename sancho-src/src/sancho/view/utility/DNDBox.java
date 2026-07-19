package sancho.view.utility;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.model.mldonkey.ClientStats;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.view.MainWindow;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.statusline.actions.DNDBoxAction;
import sancho.view.statusline.actions.PreferencesAction;
import sancho.view.utility.dialogs.BandwidthDialog;

public class DNDBox implements MyObserver, PaintListener {
   static int JUMP_MARGIN = 10;
   MainWindow mainWindow;
   Point mouseDownPoint;
   boolean multiMonitors;
   MenuManager popupMenu;
   Rectangle screenBounds;
   Shell shell;
   Rectangle shellBounds;
   Point shellLocation;
   Shell dummyShell;
   StringBuffer upString = new StringBuffer();
   StringBuffer downString = new StringBuffer();
   int cWidth;
   int cHeight;
   Font textFont;
   Color bColor;
   Color fColor;
   int shellWidth;
   int shellHeight;

   public DNDBox(MainWindow mainWindow) {
      this.mainWindow = mainWindow;
      this.dummyShell = new Shell();
      this.dummyShell.setVisible(false);
      this.shell = new Shell(this.dummyShell, 278536);
      this.shell.setLayout(new FillLayout());
      this.bColor = PreferenceLoader.loadColor("dndBackgroundColor");
      this.fColor = PreferenceLoader.loadColor("dndForegroundColor");
      this.textFont = PreferenceLoader.loadFont("dndFontData");
      GC gc = new GC(this.shell);
      gc.setFont(this.textFont);
      this.cWidth = gc.getFontMetrics().getAverageCharWidth();
      this.cHeight = gc.getFontMetrics().getHeight();
      gc.dispose();
      int width = PreferenceLoader.loadInt("dndWidth");
      this.shellHeight = this.cHeight * 2 + 6;
      this.shellWidth = width * this.cWidth;
      this.shell.setBounds(0, 0, this.shellWidth, this.shellHeight);
      this.shell.addPaintListener(this);
      this.shellBounds = this.shell.getBounds();
      this.popupMenu = new MenuManager();
      this.popupMenu.addMenuListener(new DNDBoxMenuListener());
      this.popupMenu.setRemoveAllWhenShown(true);
      this.shell.setMenu(this.popupMenu.createContextMenu(this.shell));
      this.screenBounds = this.shell.getDisplay().getBounds();
      Rectangle bounds = PreferenceLoader.loadRectangle("dndBoxWindowBounds");
      if (bounds.x != -1 && this.screenBounds.contains(bounds.x, bounds.y)) {
         this.shell.setLocation(bounds.x, bounds.y);
      } else {
         this.shell.setLocation(this.screenBounds.width - this.shellBounds.width, this.screenBounds.height - this.shellBounds.height - 40);
      }

      this.shell.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent event) {
            PreferenceConverter.setValue(PreferenceLoader.getPreferenceStore(), "dndBoxWindowBounds", DNDBox.this.shell.getBounds());
            if (Sancho.hasCollectionFactory()) {
               Sancho.getCore().getClientStats().deleteObserver(DNDBox.this);
            }
         }
      });
      this.shell.open();
      WidgetFactory.createLinkDropTarget(this.shell);
      if (this.shell.getDisplay().getMonitors().length > 1) {
         this.multiMonitors = true;
      }

      Listener listener = new Listener() {
         public void handleEvent(Event event) {
            switch (event.type) {
               case 3:
                  DNDBox.this.onMouseDown(event);
                  break;
               case 4:
                  DNDBox.this.onMouseUp(event);
                  break;
               case 5:
                  DNDBox.this.onMouseMove(event);
               case 6:
               case 7:
               default:
                  break;
               case 8:
                  DNDBox.this.onMouseDoubleClick(event);
            }
         }
      };
      int[] eventTypes = new int[]{3, 4, 5, 8};

      for (int i = 0; i < eventTypes.length; i++) {
         this.shell.addListener(eventTypes[i], listener);
      }

      this.setConnected(true);
   }

   public void close() {
      if (this.shell != null && !this.shell.isDisposed()) {
         this.shell.close();
      }

      if (this.dummyShell != null && !this.dummyShell.isDisposed()) {
         this.dummyShell.close();
      }

      if (this.shell != null && !this.shell.isDisposed()) {
         this.shell.dispose();
      }
   }

   public void onMouseDown(Event event) {
      this.mouseDownPoint = new Point(event.x, event.y);
   }

   public void setConnected(boolean connected) {
      if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getClientStats().addObserver(this);
      }
   }

   public void onMouseDoubleClick(Event event) {
      if (!Sancho.monitorMode) {
         Shell shell = this.mainWindow.getShell();
         boolean visible = shell.isVisible();
         if (visible) {
            shell.setVisible(false);
         } else {
            shell.setVisible(true);
            if (shell.getMinimized()) {
               shell.setMinimized(false);
            }
         }
      }
   }

   public void onMouseMove(Event event) {
      if (this.mouseDownPoint != null) {
         this.shellLocation = this.shell.getLocation();
         this.screenBounds = this.shell.getDisplay().getBounds();
         int x = this.shellLocation.x - (this.mouseDownPoint.x - event.x);
         int y = this.shellLocation.y - (this.mouseDownPoint.y - event.y);
         if (!this.multiMonitors) {
            x = x < JUMP_MARGIN ? 0 : x;
            y = y < JUMP_MARGIN ? 0 : y;
            if (x > this.screenBounds.width - (this.shellBounds.width + JUMP_MARGIN)) {
               x = this.screenBounds.width - this.shellBounds.width;
            }

            if (y > this.screenBounds.height - (this.shellBounds.height + JUMP_MARGIN)) {
               y = this.screenBounds.height - this.shellBounds.height;
            }
         }

         this.shell.setLocation(x, y);
      }
   }

   public void onMouseUp(Event event) {
      this.mouseDownPoint = null;
   }

   public void redrawImage(ClientStats clientStats) {
      if (this.shell != null && clientStats != null && !this.shell.isDisposed()) {
         this.upString.setLength(0);
         this.downString.setLength(0);
         this.upString.append("U:" + clientStats.getTcpUpRateString());
         this.downString.append("D:" + clientStats.getTcpDownRateString());
         this.shell.redraw();
      }
   }

   public void paintControl(PaintEvent event) {
      this.screenBounds = this.shell.getDisplay().getBounds();
      int x = this.shell.getBounds().x;
      int y = this.shell.getBounds().y;
      if (!this.screenBounds.contains(x, y)) {
         this.shell.setLocation(this.screenBounds.width - this.shellBounds.width, this.screenBounds.height - this.shellBounds.height - 40);
      }

      event.gc.setBackground(this.bColor);
      event.gc.fillRectangle(0, 0, this.shellWidth - 1, this.shellHeight - 1);
      event.gc.setForeground(this.fColor);
      event.gc.setFont(this.textFont);
      event.gc.drawText(this.upString.toString(), 3, 2, true);
      event.gc.drawText(this.downString.toString(), 3, this.cHeight + 4, true);
      event.gc.drawRectangle(0, 0, this.shellWidth - 1, this.shellHeight - 1);
   }

   public void update(final MyObservable observable, Object arg, int id) {
      if (observable instanceof ClientStats && this.shell != null && !this.shell.isDisposed()) {
         this.shell.getDisplay().asyncExec(new Runnable() {
            public void run() {
               DNDBox.this.redrawImage((ClientStats)observable);
            }
         });
      }
   }

   // Context-menu listener that builds the DND box popup on demand.
   private class DNDBoxMenuListener implements IMenuListener {
      public void menuAboutToShow(IMenuManager menuManager) {
         if (Sancho.monitorMode) {
            menuManager.add(new ExitAction(DNDBox.this.mainWindow.getShell()));
         } else {
            menuManager.add(new HideRestoreAction(DNDBox.this.mainWindow.getShell()));
            if (DNDBox.this.mainWindow.getShell().isVisible()) {
               menuManager.add(new DNDBoxAction(DNDBox.this.mainWindow));
            }

            menuManager.add(new Separator());
            menuManager.add(new PreferencesAction(DNDBox.this.mainWindow));
            new BandwidthDialog(DNDBox.this.shell, menuManager);
         }
      }
   }

   // Menu action that closes the given shell (monitor-mode exit).
   private static class ExitAction extends Action {
      Shell shell;

      public ExitAction(Shell shell) {
         super(SResources.getString("menu.file.exit"));
         this.shell = shell;
      }

      public void run() {
         this.shell.close();
      }
   }

   // Menu action that toggles the main shell between hidden and restored.
   private static class HideRestoreAction extends Action {
      Shell shell;

      public HideRestoreAction(Shell shell) {
         super(SResources.getString(shell.isVisible() ? "mi.hide" : "mi.restore"));
         this.setImageDescriptor(SResources.getImageDescriptor(shell.isVisible() ? "minus" : "plus"));
         this.shell = shell;
      }

      public void run() {
         this.shell.setVisible(!this.shell.isVisible());
      }
   }
}
