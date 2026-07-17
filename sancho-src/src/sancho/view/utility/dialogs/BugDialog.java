package sancho.view.utility.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class BugDialog extends Dialog {
   String string;

   public BugDialog(Shell var1, String var2) {
      super(var1);
      this.string = var2;
      this.setShellStyle(this.getShellStyle() | 16);
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setText("Boog Ditekted!");
   }

   protected int boolean2Int(boolean var1) {
      return var1 ? 1 : 0;
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      String var3 = "";
      if (Sancho.getCore() != null) {
         ICore var4 = Sancho.getCore();
         var3 = var4.getProtocol()
            + "|"
            + var4.getFileCollection().size()
            + "|"
            + var4.getClientCollection().size()
            + "|"
            + var4.getUserCollection().size()
            + "|"
            + var4.getRoomCollection().size()
            + "|"
            + var4.getResultCollection().size()
            + "|"
            + var4.getResultCollection().getNumResults();
      } else {
         var3 = "No Core";
      }

      var3 = var3
         + "\nCF:"
         + Sancho.getCoreFactory().getNumRetries()
         + ":"
         + Sancho.getCoreFactory().getUptime()
         + ":"
         + Sancho.getCoreFactory().getConnectedString();
      String var10 = Runtime.getRuntime().freeMemory() + "/" + Runtime.getRuntime().totalMemory();
      String var5 = "CG:"
         + this.boolean2Int(PreferenceLoader.loadBoolean("displayChunkGraphs"))
         + "|CE:"
         + this.boolean2Int(!PreferenceLoader.loadString("coreExecutable").equals(""))
         + "|KC:"
         + this.boolean2Int(PreferenceLoader.loadBoolean("killCoreOnExit"))
         + "|";
      Text var6 = new Text(var2, 2826);
      var6.setFont(PreferenceLoader.loadFont("consoleFontData"));
      GridData var7 = new GridData(1808);
      var7.heightHint = 300;
      var6.setLayoutData(var7);
      var6.setText(
         "Please submit a bug report detailing *exactly* what you\nwere doing when this happened!!! Thank you.\n(Describe exactly how to _reproduce_ the bug if you can.)\n\n---<snip>--- PLEASE INCLUDE ALL OF THE FOLLOWING ---\n\n1. Are you using the latest version? (core and gui)\n\n2. Can you duplicate the error using the java binary\n   version? (this will report better stackTraces and\n   help locate the bug)\n\n3. Does the bug depend upon a certain configuration\n   setting?  Does renaming your ConfigFile and starting\n   with a new one fix the problem? Can you include the\n   ConfigFile that causes the problem with the bug report?\n\nConfigFile: "
            + PreferenceLoader.getPrefFile()
            + "\n"
            + System.getProperty("os.name")
            + " "
            + System.getProperty("os.version")
            + " "
            + System.getProperty("java.vm.specification.vendor")
            + " "
            + System.getProperty("java.version")
            + "\n"
            + "swt-"
            + VersionInfo.getSWTPlatform()
            + "-"
            + SWT.getVersion()
            + " "
            + Sancho.getUptime()
            + "\n"
            + VersionInfo.getName()
            + " "
            + VersionInfo.getVersion()
            + VersionInfo.getBrand()
            + "\n"
            + var10
            + "\n"
            + var3
            + "\n"
            + var5
            + "\n"
            + this.string
      );
      return var2;
   }

   protected Control createButtonBar(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      var2.setLayoutData(new GridData(768));
      Button var3 = new Button(var2, 0);
      var3.setLayoutData(new GridData(768));
      var3.setText(SResources.getString("b.reportBug"));
      var3.addSelectionListener(new BugDialog$1(this));
      Button var4 = new Button(var2, 0);
      var4.setLayoutData(new GridData(128));
      var4.setText(SResources.getString("b.close"));
      var4.addSelectionListener(new BugDialog$2(this));
      return var2;
   }
}
