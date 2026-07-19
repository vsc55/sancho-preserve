package sancho.view.transfer;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Network;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.utility.VersionInfo;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public abstract class AbstractDetailDialog extends Dialog implements MyObserver {
   protected ArrayList chunkCanvases = new ArrayList();
   protected static final int leftColumn = 100;
   protected static final int rightColumn = 300;

   protected AbstractDetailDialog(Shell shell) {
      super(shell);
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setImage(VersionInfo.getProgramIcon());
   }

   protected CLabel createLine(Composite composite, String text, boolean fullWidth) {
      Label label = new Label(composite, 0);
      label.setText(SResources.getString(text));
      GridData gridData = new GridData();
      gridData.widthHint = 100;
      label.setLayoutData(gridData);
      final CLabel valueLabel = new CLabel(composite, 0);
      Menu menu = new Menu(valueLabel);
      menu.addMenuListener(new MenuListener() {
         public void menuHidden(MenuEvent event) {
            valueLabel.setBackground(valueLabel.getDisplay().getSystemColor(22));
         }

         public void menuShown(MenuEvent event) {
            valueLabel.setBackground(valueLabel.getDisplay().getSystemColor(26));
         }
      });
      MenuItem menuItem = new MenuItem(menu, 8);
      menuItem.setText(SResources.getString("mi.copy"));
      menuItem.setImage(SResources.getImage("copy"));
      menuItem.addListener(13, new Listener() {
         public void handleEvent(Event event) {
            MainWindow.copyToClipboard(valueLabel.getText());
         }
      });
      valueLabel.setMenu(menu);
      gridData = new GridData();
      if (fullWidth) {
         gridData.widthHint = 300;
         gridData.horizontalSpan = 3;
      } else {
         gridData.widthHint = 100;
      }

      valueLabel.setLayoutData(gridData);
      return valueLabel;
   }

   protected ChunkCanvas createChunkGroup(Composite composite, String title, Client client, File file, Network network) {
      Group group = new Group(composite, 64);
      String suffix = "";
      if (network == null) {
         suffix = client == null ? " (" + file.getAvail().length() + ")" : "";
      } else if (file.hasAvails()) {
         suffix = " (" + file.getAvails(network).length() + ")";
      }

      group.setText(title + suffix);
      group.setLayout(WidgetFactory.createGridLayout(1, 5, 2, 0, 0, false));
      group.setLayoutData(new GridData(768));
      ChunkCanvas chunkCanvas = new ChunkCanvas(group, 262144, client, file, network, false);
      GridData gridData = new GridData(768);
      gridData.heightHint = 18;
      chunkCanvas.setLayoutData(gridData);
      this.chunkCanvases.add(chunkCanvas);
      return chunkCanvas;
   }

   protected void updateLabel(CLabel label, String text) {
      if (!label.isDisposed()) {
         label.setText(text);
         label.setToolTipText(text.length() > 10 ? text : "");
      }
   }

   public boolean close() {
      Iterator iterator = this.chunkCanvases.iterator();

      while (iterator.hasNext()) {
         ((ChunkCanvas)iterator.next()).dispose();
      }

      return super.close();
   }

   public abstract void updateLabels();

   public void updateViews(int id) {
   }

   public void update(MyObservable observable, Object arg, final int id) {
      if (this.getShell() != null && !this.getShell().isDisposed()) {
         this.getShell().getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (AbstractDetailDialog.this.getShell() != null && !AbstractDetailDialog.this.getShell().isDisposed()) {
                  AbstractDetailDialog.this.updateLabels();
                  AbstractDetailDialog.this.updateViews(id);
               }
            }
         });
      }
   }
}
