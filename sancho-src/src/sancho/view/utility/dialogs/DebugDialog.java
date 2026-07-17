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
   public DebugDialog(Shell var1) {
      super(var1);
      this.setShellStyle(this.getShellStyle() | 16);
      Runtime.getRuntime().runFinalization();
      Runtime.getRuntime().gc();
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setText(SResources.getString("menu.tools.debug"));
      var1.setImage(VersionInfo.getProgramIcon());
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      Text var3 = new Text(var2, 2826);
      var3.setFont(PreferenceLoader.loadFont("consoleFontData"));
      GridData var4 = new GridData(1808);
      var4.heightHint = 200;
      var3.setLayoutData(var4);
      StringBuffer var5 = new StringBuffer();
      String var6 = "\n";
      var5.append(System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version") + var6);
      var5.append("Config: " + PreferenceLoader.getPrefFile() + var6);
      var5.append(
         "Connect: "
            + Sancho.getCoreFactory().getNumRetries()
            + ": "
            + Sancho.getCoreFactory().getUptime()
            + " "
            + Sancho.getCoreFactory().getConnectedString()
            + var6
      );
      if (Sancho.hasCollectionFactory()) {
         ICore var7 = Sancho.getCore();
         var5.append("Core Protocol: " + var7.getProtocol() + var6);
         var5.append("Network Collection: " + var7.getNetworkCollection().size() + var6);
         var5.append("File Collection: " + var7.getFileCollection().size() + var6);
         var5.append("SharedFile Collection: " + var7.getSharedFileCollection().size() + var6);
         var5.append("Client Collection: " + var7.getClientCollection().size() + var6);
         var5.append("Server Collection: " + var7.getServerCollection().size() + var6);
         var5.append("User Collection: " + var7.getUserCollection().size() + var6);
         var5.append("Room Collection: " + var7.getRoomCollection().size() + var6);
         var5.append("Result Collection: " + var7.getResultCollection().getNumResults() + " " + var7.getResultCollection().getStats() + var6);
         var5.append("Uploaders Collection: " + var7.getClientCollection().getUploadersWeakMap().size() + var6);
         var5.append("Pending Collection: " + var7.getClientCollection().getPendingWeakMap().size() + var6);
         var5.append("Friends Collection: " + var7.getClientCollection().getFriendsWeakMap().size() + var6);
         this.appendCleanMap(var5, var7.getClientCollection().getHistoryMap(), "Clean clients: ", var6);
         this.appendCleanMap(var5, var7.getServerCollection().getHistoryMap(), "Clean servers: ", var6);
         var5.append(var7.getLastMessage());
      }

      var3.setText(var5.toString());
      return var2;
   }

   public void appendCleanMap(StringBuffer var1, TLongIntHashMap var2, String var3, String var4) {
      long[] var5 = var2.keys();
      Arrays.sort(var5);

      for (int var6 = 0; var6 < var5.length; var6++) {
         Date var7 = new Date(var5[var6]);
         var1.append(var3 + var2.get(var5[var6]) + " @ " + var7.toString() + var4);
      }
   }
}
