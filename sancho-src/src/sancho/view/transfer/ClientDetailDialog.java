package sancho.view.transfer;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumClientType;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class ClientDetailDialog extends AbstractDetailDialog {
   private File file;
   private Client client;
   private CLabel clName;
   private CLabel clRating;
   private CLabel clActivity;
   private CLabel clKind;
   private CLabel clNetwork;
   private CLabel clSockAddr;
   private CLabel clUploaded;
   private CLabel clDownloaded;
   private CLabel clSoftware;
   private CLabel clHash;
   private CLabel clPort;
   private CLabel clHasFiles;
   private CLabel clsui;

   public ClientDetailDialog(Shell var1, File var2, Client var3) {
      super(var1);
      this.file = var2;
      this.client = var3;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setText(SResources.getString("l.client") + " " + this.client.getId() + " " + SResources.getString("l.details").toLowerCase());
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 0, 5, false));
      this.createGeneralGroup(var2);
      if (this.file != null) {
         this.createChunkGroup(var2, "dd.c.localChunks", null);
         this.createChunkGroup(var2, "dd.c.clientChunks", this.client);
      }

      this.updateLabels();
      if (this.file != null) {
         this.file.addObserver(this);
      }

      this.client.addObserver(this);
      return var2;
   }

   public void createGeneralGroup(Composite var1) {
      Group var2 = new Group(var1, 64);
      var2.setText(SResources.getString("dd.c.clientInformation"));
      var2.setLayout(WidgetFactory.createGridLayout(4, 5, 2, 0, 0, false));
      var2.setLayoutData(new GridData(768));
      this.clName = this.createLine(var2, "dd.c.name", true);
      this.clNetwork = this.createLine(var2, "dd.c.network", false);
      this.clRating = this.createLine(var2, "dd.c.rating", false);
      this.clActivity = this.createLine(var2, "dd.c.activity", false);
      this.clKind = this.createLine(var2, "dd.c.kind", false);
      this.clSoftware = this.createLine(var2, "dd.c.software", false);
      this.clHash = this.createLine(var2, "dd.c.hash", false);
      this.clSockAddr = this.createLine(var2, "dd.c.address", false);
      this.clPort = this.createLine(var2, "dd.c.port", false);
      this.clUploaded = this.createLine(var2, "dd.c.uploaded", false);
      this.clDownloaded = this.createLine(var2, "dd.c.downloaded", false);
      this.clHasFiles = this.createLine(var2, "dd.c.hasFiles", false);
      this.clsui = this.createLine(var2, "dd.c.identification", false);
   }

   protected void createChunkGroup(Composite var1, String var2, Client var3) {
      super.createChunkGroup(var1, SResources.getString(var2), var3, this.file, null);
   }

   protected Control createButtonBar(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayoutData(new GridData(768));
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 0, false));
      Button var3 = new Button(var2, 0);
      var3.setLayoutData(new GridData(640));
      var3.setText(SResources.getString("dd.c.addFriend"));
      if (this.client.getEnumClientType() == EnumClientType.FRIEND) {
         var3.setEnabled(false);
      }

      var3.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            ClientDetailDialog.this.client.addAsFriend();
            var3.setText(SResources.getString("b.ok"));
            var3.setEnabled(false);
         }
      });
      Button var4 = new Button(var2, 0);
      var4.setFocus();
      var4.setLayoutData(new GridData(128));
      var4.setText(SResources.getString("b.close"));
      var4.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            ClientDetailDialog.this.close();
         }
      });
      return var1;
   }

   public void updateLabels() {
      this.updateLabel(this.clName, this.client.getName());
      this.updateLabel(this.clRating, String.valueOf(this.client.getRating()));
      this.updateLabel(this.clActivity, this.client.getClientActivity());
      this.updateLabel(this.clKind, this.client.getModeString());
      this.updateLabel(this.clNetwork, this.client.getEnumNetwork().getName());
      this.updateLabel(this.clSockAddr, this.client.getAddr().toString());
      this.updateLabel(this.clPort, String.valueOf(this.client.getPort()));
      this.updateLabel(this.clHash, this.client.getHash());
      this.updateLabel(this.clSoftware, this.client.getSoftware());
      this.updateLabel(this.clUploaded, this.client.getUploadedString());
      this.updateLabel(this.clDownloaded, this.client.getDownloadedString());
      this.updateLabel(this.clHasFiles, String.valueOf(this.client.hasFiles()));
      String var1;
      switch (this.client.getSUI()) {
         case 0:
            var1 = "dd.c.identification.invalid";
            break;
         case 1:
            var1 = "dd.c.identification.successful";
            break;
         default:
            var1 = "dd.c.identification.none";
      }

      this.updateLabel(this.clsui, SResources.getString(var1));
   }

   public boolean close() {
      this.client.deleteObserver(this);
      if (this.file != null) {
         this.file.deleteObserver(this);
      }

      return super.close();
   }
}
