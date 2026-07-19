package sancho.view.utility.dialogs;

import gnu.trove.TLongIntHashMap;
import java.util.Arrays;
import java.util.Date;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

public class DebugDialog extends Dialog {
   public DebugDialog(Shell shell) {
      super(shell);
      this.setShellStyle(this.getShellStyle() | 16);
      Runtime.getRuntime().runFinalization();
      Runtime.getRuntime().gc();
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText(SResources.getString("menu.tools.debug"));
      shell.setImage(VersionInfo.getProgramIcon());
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      Text text = new Text(composite, 2826);
      text.setFont(PreferenceLoader.loadFont("consoleFontData"));
      GridData gridData = new GridData(1808);
      gridData.heightHint = 200;
      text.setLayoutData(gridData);
      StringBuffer buffer = new StringBuffer();
      String newline = "\n";
      buffer.append(System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version") + newline);
      buffer.append("Config: " + PreferenceLoader.getPrefFile() + newline);
      buffer.append(
         "Connect: "
            + Sancho.getCoreFactory().getNumRetries()
            + ": "
            + Sancho.getCoreFactory().getUptime()
            + " "
            + Sancho.getCoreFactory().getConnectedString()
            + newline
      );
      if (Sancho.hasCollectionFactory()) {
         ICore core = Sancho.getCore();
         buffer.append("Core Protocol: " + core.getProtocol() + newline);
         buffer.append("Network Collection: " + core.getNetworkCollection().size() + newline);
         buffer.append("File Collection: " + core.getFileCollection().size() + newline);
         buffer.append("SharedFile Collection: " + core.getSharedFileCollection().size() + newline);
         buffer.append("Client Collection: " + core.getClientCollection().size() + newline);
         buffer.append("Server Collection: " + core.getServerCollection().size() + newline);
         buffer.append("User Collection: " + core.getUserCollection().size() + newline);
         buffer.append("Room Collection: " + core.getRoomCollection().size() + newline);
         buffer.append("Result Collection: " + core.getResultCollection().getNumResults() + " " + core.getResultCollection().getStats() + newline);
         buffer.append("Uploaders Collection: " + core.getClientCollection().getUploadersWeakMap().size() + newline);
         buffer.append("Pending Collection: " + core.getClientCollection().getPendingWeakMap().size() + newline);
         buffer.append("Friends Collection: " + core.getClientCollection().getFriendsWeakMap().size() + newline);
         this.appendCleanMap(buffer, core.getClientCollection().getHistoryMap(), "Clean clients: ", newline);
         this.appendCleanMap(buffer, core.getServerCollection().getHistoryMap(), "Clean servers: ", newline);
         buffer.append(core.getLastMessage());
      }

      text.setText(buffer.toString());
      return composite;
   }

   public void appendCleanMap(StringBuffer buffer, TLongIntHashMap map, String label, String newline) {
      long[] keys = map.keys();
      Arrays.sort(keys);

      for (int i = 0; i < keys.length; i++) {
         Date date = new Date(keys[i]);
         buffer.append(label + map.get(keys[i]) + " @ " + date.toString() + newline);
      }
   }
}
