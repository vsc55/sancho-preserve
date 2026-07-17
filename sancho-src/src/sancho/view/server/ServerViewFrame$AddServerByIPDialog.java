package sancho.view.server;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.utility.VersionInfo;
import sancho.view.utility.BSpinner;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

class ServerViewFrame$AddServerByIPDialog extends Dialog {
   private Combo combo;
   private Network network;
   private int port;
   private BSpinner spinner;
   private String name;
   private Text nameText;
   // $VF: synthetic field
   private final ServerViewFrame this$0;

   public ServerViewFrame$AddServerByIPDialog(ServerViewFrame var1, Shell var2) {
      super(var2);
      this.this$0 = var1;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
      var1.setText(SResources.getString("ti.s.addServer"));
   }

   public void createLabel(Composite var1, String var2) {
      new Label(var1, 0).setText(SResources.getString(var2));
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(4, 5, 5, 10, 5, false));
      this.createLabel(var2, "hm.host");
      this.nameText = new Text(var2, 2048);
      this.nameText.setLayoutData(new GridData(768));
      this.createLabel(var2, "hm.port");
      this.spinner = new BSpinner(var2, 2048);
      this.spinner.setMaximum(65535);
      this.spinner.setSelection(4661);
      this.createLabel(var2, "s.network");
      this.combo = new Combo(var2, 8);
      GridData var3 = new GridData(768);
      var3.horizontalSpan = 3;
      this.combo.setLayoutData(var3);
      if (!Sancho.hasCollectionFactory()) {
         return var2;
      } else {
         Network[] var4 = ServerViewFrame.access$400(this.this$0).getCore().getNetworkCollection().getNetworks();

         for (int var5 = 0; var5 < var4.length; var5++) {
            Network var6 = var4[var5];
            if (var6.isEnabled() && var6.hasServers()) {
               this.combo.add(var6.getName());
               this.combo.setData(var6.getName(), var6);
            }
         }

         this.combo.select(0);
         return var2;
      }
   }

   protected void buttonPressed(int var1) {
      int var2 = this.combo.getSelectionIndex();
      if (var2 != -1) {
         this.network = (Network)this.combo.getData(this.combo.getItem(this.combo.getSelectionIndex()));
      } else {
         this.network = null;
      }

      this.port = this.spinner.getSelection();
      this.name = this.nameText.getText();
      super.buttonPressed(var1);
   }

   public String getName() {
      return this.name;
   }

   public int getPort() {
      return this.port;
   }

   public Network getNetwork() {
      return this.network;
   }
}
