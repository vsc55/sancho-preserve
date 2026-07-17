package sancho.view.statusline;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.ClientStats;
import sancho.utility.MyObservable;
import sancho.view.StatusLine;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

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

   public RateItem(StatusLine var1) {
      this.statusLine = var1;
      this.statusLineComposite = var1.getStatusline();
      this.disconnectMouseAdapter = new RateItem$1(this);
      this.createContent();
      this.updateImages = true;
      this.setConnected(true);
   }

   public void setConnected(boolean var1) {
      this.connected = var1;
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
      this.popupMenu.addMenuListener(new RateItem$RateMenuListener(this));
      this.downCLabel = new CLabel(this.statusLineComposite, 131072);
      this.downCLabel.setLayoutData(new RowData());
      this.downCLabel.setMenu(this.popupMenu.createContextMenu(this.downCLabel));
      this.upCLabel = new CLabel(this.statusLineComposite, 0);
      this.upCLabel.setLayoutData(new RowData());
      this.upCLabel.setMenu(this.popupMenu.createContextMenu(this.upCLabel));
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (var1 instanceof ClientStats && var1 != null && this.upCLabel != null && !this.upCLabel.isDisposed()) {
         this.statusLineComposite.getDisplay().asyncExec(new RateItem$2(this, var1));
      }
   }

   public void updateClientStats(ClientStats var1) {
      if (this.upCLabel != null && !this.upCLabel.isDisposed()) {
         if (this.updateImages) {
            this.downCLabel.setImage(SResources.getImage("rateDownArrow"));
            this.upCLabel.setImage(SResources.getImage("rateUpArrow"));
            this.downCLabel.removeMouseListener(this.disconnectMouseAdapter);
         }

         this.downCLabel.setText(var1.getTcpDownRateString());
         this.upCLabel.setText(var1.getTcpUpRateString());
         this.downCLabel.setToolTipText(var1.getDownloadToolTip());
         this.upCLabel.setToolTipText(var1.getUploadToolTip());
         int var2 = this.downCLabel.getText().length() + this.upCLabel.getText().length();
         if (var2 != this.oldLength || this.updateImages) {
            this.oldLength = var2;
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

   // $VF: synthetic method
   static boolean access$000(RateItem var0) {
      return var0.connected;
   }

   // $VF: synthetic method
   static StatusLine access$100(RateItem var0) {
      return var0.statusLine;
   }

   // $VF: synthetic method
   static CLabel access$200(RateItem var0) {
      return var0.downCLabel;
   }

   // $VF: synthetic method
   static CLabel access$300(RateItem var0) {
      return var0.upCLabel;
   }
}
