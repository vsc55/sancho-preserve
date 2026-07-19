package sancho.view.utility.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import sancho.view.utility.WebLauncher;
import sancho.view.utility.WidgetFactory;

public class BugDialog extends Dialog {
   String string;

   public BugDialog(Shell shell, String message) {
      super(shell);
      this.string = message;
      this.setShellStyle(this.getShellStyle() | 16);
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText("Boog Ditekted!");
   }

   protected int boolean2Int(boolean value) {
      return value ? 1 : 0;
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      String info = "";
      if (Sancho.getCore() != null) {
         ICore core = Sancho.getCore();
         info = core.getProtocol()
            + "|"
            + core.getFileCollection().size()
            + "|"
            + core.getClientCollection().size()
            + "|"
            + core.getUserCollection().size()
            + "|"
            + core.getRoomCollection().size()
            + "|"
            + core.getResultCollection().size()
            + "|"
            + core.getResultCollection().getNumResults();
      } else {
         info = "No Core";
      }

      info = info
         + "\nCF:"
         + Sancho.getCoreFactory().getNumRetries()
         + ":"
         + Sancho.getCoreFactory().getUptime()
         + ":"
         + Sancho.getCoreFactory().getConnectedString();
      String memoryInfo = Runtime.getRuntime().freeMemory() + "/" + Runtime.getRuntime().totalMemory();
      String prefsInfo = "CG:"
         + this.boolean2Int(PreferenceLoader.loadBoolean("displayChunkGraphs"))
         + "|CE:"
         + this.boolean2Int(!PreferenceLoader.loadString("coreExecutable").equals(""))
         + "|KC:"
         + this.boolean2Int(PreferenceLoader.loadBoolean("killCoreOnExit"))
         + "|";
      Text text = new Text(composite, 2826);
      text.setFont(PreferenceLoader.loadFont("consoleFontData"));
      GridData gridData = new GridData(1808);
      gridData.heightHint = 300;
      text.setLayoutData(gridData);
      text.setText(
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
            + memoryInfo
            + "\n"
            + info
            + "\n"
            + prefsInfo
            + "\n"
            + this.string
      );
      return composite;
   }

   protected Control createButtonBar(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      composite.setLayoutData(new GridData(768));
      Button reportButton = new Button(composite, 0);
      reportButton.setLayoutData(new GridData(768));
      reportButton.setText(SResources.getString("b.reportBug"));
      reportButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            WebLauncher.openLink("mailto:" + VersionInfo.getEmail());
         }
      });
      Button closeButton = new Button(composite, 0);
      closeButton.setLayoutData(new GridData(128));
      closeButton.setText(SResources.getString("b.close"));
      closeButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            BugDialog.this.close();
         }
      });
      return composite;
   }
}
