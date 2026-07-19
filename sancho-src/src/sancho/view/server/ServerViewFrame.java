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
   public ServerViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4, "server");
      this.gView = new ServerTableView(this);
      this.createViewListener(new ServerViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("ti.s.cleanOld", "minus", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            if (Sancho.hasCollectionFactory()) {
               ServerViewFrame.this.getCore().getServerCollection().cleanOldServers();
            }
         }
      });
      this.addToolItem("ti.s.addServer", "plus", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            if (Sancho.hasCollectionFactory()) {
               AddServerByIPDialog var2 = new AddServerByIPDialog(ServerViewFrame.this.gView.getShell());
               if (var2.open() == 0) {
                  String var3 = var2.getName();
                  short var4 = (short)var2.getPort();
                  InetAddress var5 = null;

                  try {
                     var5 = InetAddress.getByName(var3);
                  } catch (UnknownHostException var8) {
                     MessageBox var7 = new MessageBox(ServerViewFrame.this.gView.getShell(), 40);
                     var7.setText(SResources.getString("l.lookupError"));
                     var7.setMessage(SResources.getString("l.resolveError"));
                     var7.open();
                  }

                  if (Sancho.hasCollectionFactory() && var5 != null) {
                     ServerViewFrame.this.gView.getCore().getServerCollection().addServer(var2.getNetwork(), var5, var4);
                  }
               }
            }
         }
      });
      this.addToolItem("ti.s.addServerMet", "plus-globe", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            InputDialog var2 = new InputDialog(
               ServerViewFrame.this.gView.getShell(),
               SResources.getString("ti.s.addServerMet"),
               SResources.getString("t.srv.linkToMet"),
               SResources.getString("t.srv.linkToMetDefault"),
               new HTTPValidator()
            );
            var2.open();
            String var3 = var2.getValue();
            if (var3 != null && Sancho.hasCollectionFactory()) {
               ServerViewFrame.this.getCore().getServerCollection().addServerList(var3);
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

      public AddServerByIPDialog(Shell var1) {
         super(var1);
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
            Network[] var4 = ServerViewFrame.this.gView.getCore().getNetworkCollection().getNetworks();

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

   // Validates that entered text is an http(s) URL for the server.met input dialog.
   static class HTTPValidator implements IInputValidator {
      static Pattern regex;

      public String isValid(String var1) {
         return regex.matcher(var1).find() ? null : SResources.getString("l.invalidInput");
      }

      static {
         try {
            regex = Pattern.compile("http(s)?://\\S*");
         } catch (PatternSyntaxException var1) {
         }
      }
   }
}
