package sancho.view.utility;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.model.mldonkey.ClientStats;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.view.MainWindow;
import sancho.view.preferences.PreferenceLoader;

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

   public DNDBox(MainWindow var1) {
      this.mainWindow = var1;
      this.dummyShell = new Shell();
      this.dummyShell.setVisible(false);
      this.shell = new Shell(this.dummyShell, 278536);
      this.shell.setLayout(new FillLayout());
      this.bColor = PreferenceLoader.loadColor("dndBackgroundColor");
      this.fColor = PreferenceLoader.loadColor("dndForegroundColor");
      this.textFont = PreferenceLoader.loadFont("dndFontData");
      GC var2 = new GC(this.shell);
      var2.setFont(this.textFont);
      this.cWidth = var2.getFontMetrics().getAverageCharWidth();
      this.cHeight = var2.getFontMetrics().getHeight();
      var2.dispose();
      int var3 = PreferenceLoader.loadInt("dndWidth");
      this.shellHeight = this.cHeight * 2 + 6;
      this.shellWidth = var3 * this.cWidth;
      this.shell.setBounds(0, 0, this.shellWidth, this.shellHeight);
      this.shell.addPaintListener(this);
      this.shellBounds = this.shell.getBounds();
      this.popupMenu = new MenuManager();
      this.popupMenu.addMenuListener(new DNDBox$DNDBoxMenuListener(this));
      this.popupMenu.setRemoveAllWhenShown(true);
      this.shell.setMenu(this.popupMenu.createContextMenu(this.shell));
      this.screenBounds = this.shell.getDisplay().getBounds();
      Rectangle var4 = PreferenceLoader.loadRectangle("dndBoxWindowBounds");
      if (var4.x != -1 && this.screenBounds.contains(var4.x, var4.y)) {
         this.shell.setLocation(var4.x, var4.y);
      } else {
         this.shell.setLocation(this.screenBounds.width - this.shellBounds.width, this.screenBounds.height - this.shellBounds.height - 40);
      }

      this.shell.addDisposeListener(new DNDBox$1(this));
      this.shell.open();
      WidgetFactory.createLinkDropTarget(this.shell);
      if (this.shell.getDisplay().getMonitors().length > 1) {
         this.multiMonitors = true;
      }

      DNDBox$2 var5 = new DNDBox$2(this);
      int[] var6 = new int[]{3, 4, 5, 8};

      for (int var7 = 0; var7 < var6.length; var7++) {
         this.shell.addListener(var6[var7], var5);
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

   public void onMouseDown(Event var1) {
      this.mouseDownPoint = new Point(var1.x, var1.y);
   }

   public void setConnected(boolean var1) {
      if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getClientStats().addObserver(this);
      }
   }

   public void onMouseDoubleClick(Event var1) {
      if (!Sancho.monitorMode) {
         Shell var2 = this.mainWindow.getShell();
         boolean var3 = var2.isVisible();
         if (var3) {
            var2.setVisible(false);
         } else {
            var2.setVisible(true);
            if (var2.getMinimized()) {
               var2.setMinimized(false);
            }
         }
      }
   }

   public void onMouseMove(Event var1) {
      if (this.mouseDownPoint != null) {
         this.shellLocation = this.shell.getLocation();
         this.screenBounds = this.shell.getDisplay().getBounds();
         int var2 = this.shellLocation.x - (this.mouseDownPoint.x - var1.x);
         int var3 = this.shellLocation.y - (this.mouseDownPoint.y - var1.y);
         if (!this.multiMonitors) {
            var2 = var2 < JUMP_MARGIN ? 0 : var2;
            var3 = var3 < JUMP_MARGIN ? 0 : var3;
            if (var2 > this.screenBounds.width - (this.shellBounds.width + JUMP_MARGIN)) {
               var2 = this.screenBounds.width - this.shellBounds.width;
            }

            if (var3 > this.screenBounds.height - (this.shellBounds.height + JUMP_MARGIN)) {
               var3 = this.screenBounds.height - this.shellBounds.height;
            }
         }

         this.shell.setLocation(var2, var3);
      }
   }

   public void onMouseUp(Event var1) {
      this.mouseDownPoint = null;
   }

   public void redrawImage(ClientStats var1) {
      if (this.shell != null && var1 != null && !this.shell.isDisposed()) {
         this.upString.setLength(0);
         this.downString.setLength(0);
         this.upString.append("U:" + var1.getTcpUpRateString());
         this.downString.append("D:" + var1.getTcpDownRateString());
         this.shell.redraw();
      }
   }

   public void paintControl(PaintEvent var1) {
      this.screenBounds = this.shell.getDisplay().getBounds();
      int var2 = this.shell.getBounds().x;
      int var3 = this.shell.getBounds().y;
      if (!this.screenBounds.contains(var2, var3)) {
         this.shell.setLocation(this.screenBounds.width - this.shellBounds.width, this.screenBounds.height - this.shellBounds.height - 40);
      }

      var1.gc.setBackground(this.bColor);
      var1.gc.fillRectangle(0, 0, this.shellWidth - 1, this.shellHeight - 1);
      var1.gc.setForeground(this.fColor);
      var1.gc.setFont(this.textFont);
      var1.gc.drawText(this.upString.toString(), 3, 2, true);
      var1.gc.drawText(this.downString.toString(), 3, this.cHeight + 4, true);
      var1.gc.drawRectangle(0, 0, this.shellWidth - 1, this.shellHeight - 1);
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (var1 instanceof ClientStats && this.shell != null && !this.shell.isDisposed()) {
         this.shell.getDisplay().asyncExec(new DNDBox$3(this, var1));
      }
   }
}
