package sancho.view.transfer;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Network;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public abstract class AbstractDetailDialog extends Dialog implements MyObserver {
   protected ArrayList chunkCanvases = new ArrayList();
   protected static final int leftColumn = 100;
   protected static final int rightColumn = 300;

   protected AbstractDetailDialog(Shell var1) {
      super(var1);
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
   }

   protected CLabel createLine(Composite var1, String var2, boolean var3) {
      Label var4 = new Label(var1, 0);
      var4.setText(SResources.getString(var2));
      GridData var5 = new GridData();
      var5.widthHint = 100;
      var4.setLayoutData(var5);
      CLabel var6 = new CLabel(var1, 0);
      Menu var7 = new Menu(var6);
      var7.addMenuListener(new AbstractDetailDialog$1(this, var6));
      MenuItem var8 = new MenuItem(var7, 8);
      var8.setText(SResources.getString("mi.copy"));
      var8.setImage(SResources.getImage("copy"));
      var8.addListener(13, new AbstractDetailDialog$2(this, var6));
      var6.setMenu(var7);
      var5 = new GridData();
      if (var3) {
         var5.widthHint = 300;
         var5.horizontalSpan = 3;
      } else {
         var5.widthHint = 100;
      }

      var6.setLayoutData(var5);
      return var6;
   }

   protected ChunkCanvas createChunkGroup(Composite var1, String var2, Client var3, File var4, Network var5) {
      Group var6 = new Group(var1, 64);
      String var7 = "";
      if (var5 == null) {
         var7 = var3 == null ? " (" + var4.getAvail().length() + ")" : "";
      } else if (var4.hasAvails()) {
         var7 = " (" + var4.getAvails(var5).length() + ")";
      }

      var6.setText(var2 + var7);
      var6.setLayout(WidgetFactory.createGridLayout(1, 5, 2, 0, 0, false));
      var6.setLayoutData(new GridData(768));
      ChunkCanvas var8 = new ChunkCanvas(var6, 262144, var3, var4, var5, false);
      GridData var9 = new GridData(768);
      var9.heightHint = 18;
      var8.setLayoutData(var9);
      this.chunkCanvases.add(var8);
      return var8;
   }

   protected void updateLabel(CLabel var1, String var2) {
      if (!var1.isDisposed()) {
         var1.setText(var2);
         var1.setToolTipText(var2.length() > 10 ? var2 : "");
      }
   }

   public boolean close() {
      Iterator var1 = this.chunkCanvases.iterator();

      while (var1.hasNext()) {
         ((ChunkCanvas)var1.next()).dispose();
      }

      return super.close();
   }

   public abstract void updateLabels();

   public void updateViews(int var1) {
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (this.getShell() != null && !this.getShell().isDisposed()) {
         this.getShell().getDisplay().asyncExec(new AbstractDetailDialog$3(this, var3));
      }
   }
}
