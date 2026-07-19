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

   public ClientDetailDialog(Shell shell, File file, Client client) {
      super(shell);
      this.file = file;
      this.client = client;
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText(SResources.getString("l.client") + " " + this.client.getId() + " " + SResources.getString("l.details").toLowerCase());
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 0, 5, false));
      this.createGeneralGroup(composite);
      if (this.file != null) {
         this.createChunkGroup(composite, "dd.c.localChunks", null);
         this.createChunkGroup(composite, "dd.c.clientChunks", this.client);
      }

      this.updateLabels();
      if (this.file != null) {
         this.file.addObserver(this);
      }

      this.client.addObserver(this);
      return composite;
   }

   public void createGeneralGroup(Composite composite) {
      Group group = new Group(composite, 64);
      group.setText(SResources.getString("dd.c.clientInformation"));
      group.setLayout(WidgetFactory.createGridLayout(4, 5, 2, 0, 0, false));
      group.setLayoutData(new GridData(768));
      this.clName = this.createLine(group, "dd.c.name", true);
      this.clNetwork = this.createLine(group, "dd.c.network", false);
      this.clRating = this.createLine(group, "dd.c.rating", false);
      this.clActivity = this.createLine(group, "dd.c.activity", false);
      this.clKind = this.createLine(group, "dd.c.kind", false);
      this.clSoftware = this.createLine(group, "dd.c.software", false);
      this.clHash = this.createLine(group, "dd.c.hash", false);
      this.clSockAddr = this.createLine(group, "dd.c.address", false);
      this.clPort = this.createLine(group, "dd.c.port", false);
      this.clUploaded = this.createLine(group, "dd.c.uploaded", false);
      this.clDownloaded = this.createLine(group, "dd.c.downloaded", false);
      this.clHasFiles = this.createLine(group, "dd.c.hasFiles", false);
      this.clsui = this.createLine(group, "dd.c.identification", false);
   }

   protected void createChunkGroup(Composite composite, String text, Client client) {
      super.createChunkGroup(composite, SResources.getString(text), client, this.file, null);
   }

   protected Control createButtonBar(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayoutData(new GridData(768));
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 0, false));
      Button addFriendButton = new Button(composite, 0);
      addFriendButton.setLayoutData(new GridData(640));
      addFriendButton.setText(SResources.getString("dd.c.addFriend"));
      if (this.client.getEnumClientType() == EnumClientType.FRIEND) {
         addFriendButton.setEnabled(false);
      }

      addFriendButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            ClientDetailDialog.this.client.addAsFriend();
            addFriendButton.setText(SResources.getString("b.ok"));
            addFriendButton.setEnabled(false);
         }
      });
      Button closeButton = new Button(composite, 0);
      closeButton.setFocus();
      closeButton.setLayoutData(new GridData(128));
      closeButton.setText(SResources.getString("b.close"));
      closeButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            ClientDetailDialog.this.close();
         }
      });
      return parent;
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
      String key;
      switch (this.client.getSUI()) {
         case 0:
            key = "dd.c.identification.invalid";
            break;
         case 1:
            key = "dd.c.identification.successful";
            break;
         default:
            key = "dd.c.identification.none";
      }

      this.updateLabel(this.clsui, SResources.getString(key));
   }

   public boolean close() {
      this.client.deleteObserver(this);
      if (this.file != null) {
         this.file.deleteObserver(this);
      }

      return super.close();
   }
}
