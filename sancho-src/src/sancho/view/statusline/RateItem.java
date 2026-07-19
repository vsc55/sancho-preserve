package sancho.view.statusline;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.ClientStats;
import sancho.utility.MyObservable;
import sancho.view.MainWindow;
import sancho.view.StatusLine;
import sancho.view.statusline.actions.DNDBoxAction;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.utility.dialogs.BandwidthDialog;

public class RateItem implements IStatusItem {
   private Composite statusLineComposite;
   private CLabel downCLabel;
   private CLabel upCLabel;
   private boolean updateImages;
   private boolean connected;
   private MenuManager popupMenu;
   private StatusLine statusLine;
   private int oldLength;
   private MouseAdapter disconnectMouseAdapter;

   public RateItem(StatusLine statusLine) {
      this.statusLine = statusLine;
      this.statusLineComposite = statusLine.getStatusline();
      this.disconnectMouseAdapter = new MouseAdapter() {
         public void mouseDoubleClick(MouseEvent event) {
            Sancho.getCoreFactory().reconnect();
         }
      };
      this.createContent();
      this.updateImages = true;
      this.setConnected(true);
   }

   public void setConnected(boolean connected) {
      this.connected = connected;
      if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getClientStats().addObserver(this);
      } else {
         this.updateDisconnected();
      }
   }

   private void createContent() {
      this.statusLineComposite = new Composite(this.statusLineComposite, 0);
      this.statusLineComposite.setLayoutData(new GridData(1040));
      this.statusLineComposite.setLayout(WidgetFactory.createRowLayout(false, true, false, 256, 0, 0, 0, 0, 0));
      this.popupMenu = new MenuManager();
      this.popupMenu.setRemoveAllWhenShown(true);
      this.popupMenu.addMenuListener(new RateMenuListener());
      this.downCLabel = new CLabel(this.statusLineComposite, 131072);
      this.downCLabel.setLayoutData(new RowData());
      this.downCLabel.setMenu(this.popupMenu.createContextMenu(this.downCLabel));
      this.upCLabel = new CLabel(this.statusLineComposite, 0);
      this.upCLabel.setLayoutData(new RowData());
      this.upCLabel.setMenu(this.popupMenu.createContextMenu(this.upCLabel));
   }

   public void update(MyObservable source, Object value, int id) {
      if (source instanceof ClientStats && source != null && this.upCLabel != null && !this.upCLabel.isDisposed()) {
         final MyObservable observable = source;
         this.statusLineComposite.getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (RateItem.this.connected) {
                  RateItem.this.updateClientStats((ClientStats)observable);
               }
            }
         });
      }
   }

   public void updateClientStats(ClientStats stats) {
      if (this.upCLabel != null && !this.upCLabel.isDisposed()) {
         if (this.updateImages) {
            this.downCLabel.setImage(SResources.getImage("rateDownArrow"));
            this.upCLabel.setImage(SResources.getImage("rateUpArrow"));
            this.downCLabel.removeMouseListener(this.disconnectMouseAdapter);
         }

         this.downCLabel.setText(stats.getTcpDownRateString());
         this.upCLabel.setText(stats.getTcpUpRateString());
         this.downCLabel.setToolTipText(stats.getDownloadToolTip());
         this.upCLabel.setToolTipText(stats.getUploadToolTip());
         int totalLength = this.downCLabel.getText().length() + this.upCLabel.getText().length();
         if (totalLength != this.oldLength || this.updateImages) {
            this.oldLength = totalLength;
            this.statusLineComposite.getParent().layout();
            this.updateImages = false;
         }
      }
   }

   public void updateDisconnected() {
      if (this.upCLabel != null && !this.upCLabel.isDisposed()) {
         this.downCLabel.setImage(SResources.getImage("RedCrossSmall"));
         this.downCLabel.setText(SResources.getString("l.disconnected"));
         this.downCLabel.setToolTipText("");
         this.downCLabel.addMouseListener(this.disconnectMouseAdapter);
         this.upCLabel.setImage(null);
         this.upCLabel.setText("");
         this.upCLabel.setToolTipText("");
         this.statusLineComposite.getParent().layout();
         this.updateImages = true;
      }
   }

   // Copies the current up/down rate label text to the clipboard.
   private class CopyLabelsAction extends Action {
      public CopyLabelsAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         String text = "";
         if (RateItem.this.downCLabel != null && !RateItem.this.downCLabel.isDisposed()) {
            text = text + RateItem.this.downCLabel.getText();
         }

         if (RateItem.this.upCLabel != null && !RateItem.this.upCLabel.isDisposed()) {
            String upText = RateItem.this.upCLabel.getText();
            if (!upText.equals("")) {
               text = text + " | " + upText;
            }
         }

         MainWindow.copyToClipboard(text);
      }
   }

   // Builds the rate label context menu on demand.
   private class RateMenuListener implements IMenuListener {
      public void menuAboutToShow(IMenuManager menuManager) {
         if (!Sancho.monitorMode) {
            menuManager.add(new DNDBoxAction(RateItem.this.statusLine.getMainWindow()));
            new BandwidthDialog(RateItem.this.downCLabel.getShell(), menuManager);
            menuManager.add(new Separator());
            menuManager.add(new CopyLabelsAction());
         }
      }
   }
}
