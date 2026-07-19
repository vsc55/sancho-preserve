package sancho.view.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.utility.VersionInfo;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.BSpinner;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.TabbedSashViewFrame;

public class ServerViewFrame extends TabbedSashViewFrame {
   public ServerViewFrame(SashForm sashForm, String prefString, String labelText, AbstractTab tab) {
      super(sashForm, prefString, labelText, tab, "server");
      this.gView = new ServerTableView(this);
      this.createViewListener(new ServerViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("ti.s.cleanOld", "minus", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            if (Sancho.hasCollectionFactory()) {
               ServerViewFrame.this.getCore().getServerCollection().cleanOldServers();
            }
         }
      });
      this.addToolItem("ti.s.addServer", "plus", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            if (Sancho.hasCollectionFactory()) {
               AddServerByIPDialog dialog = new AddServerByIPDialog(ServerViewFrame.this.gView.getShell());
               if (dialog.open() == 0) {
                  String name = dialog.getName();
                  short port = (short)dialog.getPort();
                  InetAddress address = null;

                  try {
                     address = InetAddress.getByName(name);
                  } catch (UnknownHostException unknownHostException) {
                     MessageBox messageBox = new MessageBox(ServerViewFrame.this.gView.getShell(), 40);
                     messageBox.setText(SResources.getString("l.lookupError"));
                     messageBox.setMessage(SResources.getString("l.resolveError"));
                     messageBox.open();
                  }

                  if (Sancho.hasCollectionFactory() && address != null) {
                     ServerViewFrame.this.gView.getCore().getServerCollection().addServer(dialog.getNetwork(), address, port);
                  }
               }
            }
         }
      });
      this.addToolItem("ti.s.addServerMet", "plus-globe", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            InputDialog dialog = new InputDialog(
               ServerViewFrame.this.gView.getShell(),
               SResources.getString("ti.s.addServerMet"),
               SResources.getString("t.srv.linkToMet"),
               SResources.getString("t.srv.linkToMetDefault"),
               new HTTPValidator()
            );
            dialog.open();
            String text = dialog.getValue();
            if (text != null && Sancho.hasCollectionFactory()) {
               ServerViewFrame.this.getCore().getServerCollection().addServerList(text);
            }
         }
      });
      this.addToolSeparator();
      this.addRefine();
   }

   // Modal dialog collecting host/port/network to add a server by IP.
   private class AddServerByIPDialog extends Dialog {
      private Combo combo;
      private Network network;
      private int port;
      private BSpinner spinner;
      private String name;
      private Text nameText;

      public AddServerByIPDialog(Shell shell) {
         super(shell);
      }

      protected void configureShell(Shell shell) {
         super.configureShell(shell);
         shell.setImage(VersionInfo.getProgramIcon());
         shell.setText(SResources.getString("ti.s.addServer"));
      }

      public void createLabel(Composite composite, String key) {
         new Label(composite, 0).setText(SResources.getString(key));
      }

      protected Control createDialogArea(Composite parent) {
         Composite composite = (Composite)super.createDialogArea(parent);
         composite.setLayout(WidgetFactory.createGridLayout(4, 5, 5, 10, 5, false));
         this.createLabel(composite, "hm.host");
         this.nameText = new Text(composite, 2048);
         this.nameText.setLayoutData(new GridData(768));
         this.createLabel(composite, "hm.port");
         this.spinner = new BSpinner(composite, 2048);
         this.spinner.setMaximum(65535);
         this.spinner.setSelection(4661);
         this.createLabel(composite, "s.network");
         this.combo = new Combo(composite, 8);
         GridData gridData = new GridData(768);
         gridData.horizontalSpan = 3;
         this.combo.setLayoutData(gridData);
         if (!Sancho.hasCollectionFactory()) {
            return composite;
         } else {
            Network[] networks = ServerViewFrame.this.gView.getCore().getNetworkCollection().getNetworks();

            for (int i = 0; i < networks.length; i++) {
               Network network = networks[i];
               if (network.isEnabled() && network.hasServers()) {
                  this.combo.add(network.getName());
                  this.combo.setData(network.getName(), network);
               }
            }

            this.combo.select(0);
            return composite;
         }
      }

      protected void buttonPressed(int buttonId) {
         int index = this.combo.getSelectionIndex();
         if (index != -1) {
            this.network = (Network)this.combo.getData(this.combo.getItem(this.combo.getSelectionIndex()));
         } else {
            this.network = null;
         }

         this.port = this.spinner.getSelection();
         this.name = this.nameText.getText();
         super.buttonPressed(buttonId);
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

   // Validates that entered text is an http(s) URL for the server.met input dialog.
   static class HTTPValidator implements IInputValidator {
      static Pattern regex;

      public String isValid(String text) {
         return regex.matcher(text).find() ? null : SResources.getString("l.invalidInput");
      }

      static {
         try {
            regex = Pattern.compile("http(s)?://\\S*");
         } catch (PatternSyntaxException patternSyntaxException) {
         }
      }
   }
}
