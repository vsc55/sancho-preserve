package sancho.view.preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import sancho.core.Sancho;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class WinRegPreferencePage extends CPreferencePage {
   WinRegPreferencePage$RegisterLink[] registerLinks;
   WinRegPreferencePage$RegisterExtension[] registerExtensions;

   protected WinRegPreferencePage(String var1) {
      super(var1);
   }

   protected Control createContents(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      TabFolder var3 = new TabFolder(var2, 128);
      var3.setLayoutData(new GridData(1808));
      this.createProtocolTab(var3);
      this.createFileExtensionsTab(var3);
      return var2;
   }

   protected void createFileExtensionsTab(TabFolder var1) {
      Composite var2 = this.createNewTab(var1, "l.fileExtensions");
      var2.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      this.createInformationLabel(var2, "p.registerInfo");
      this.registerExtensions = new WinRegPreferencePage$RegisterExtension[1];
      this.registerExtensions[0] = new WinRegPreferencePage$RegisterExtension("bittorrent (.torrent)", var2);
      Button var3 = new Button(var2, 0);
      var3.setLayoutData(new GridData(768));
      var3.setText(SResources.getString("b.updateRegistry"));
      var3.addSelectionListener(new WinRegPreferencePage$1(this));
      Point var4 = var2.computeSize(-1, -1);
      ((ScrolledComposite)var2.getParent()).setMinSize(var4);
      var2.layout();
   }

   private boolean changedExtPrefs() {
      // Bound by the array actually indexed (registerExtensions, length 1) — the old
      // registerLinks.length (4) ran off the end and threw AIOOBE when the ".torrent"
      // option was left unchanged and "Update Registry" was clicked.
      for (int var1 = 0; var1 < this.registerExtensions.length; var1++) {
         if (this.registerExtensions[var1].getSelection() != 0) {
            return true;
         }
      }

      return false;
   }

   protected void createProtocolTab(TabFolder var1) {
      Composite var2 = this.createNewTab(var1, "l.protocols");
      var2.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      this.registerLinks = new WinRegPreferencePage$RegisterLink[4];
      this.registerLinks[0] = new WinRegPreferencePage$RegisterLink("ed2k", var2);
      this.registerLinks[1] = new WinRegPreferencePage$RegisterLink("magnet", var2);
      this.registerLinks[2] = new WinRegPreferencePage$RegisterLink("sig2dat", var2);
      this.registerLinks[3] = new WinRegPreferencePage$RegisterLink("sfdl", var2);
      Button var3 = new Button(var2, 0);
      var3.setLayoutData(new GridData(768));
      var3.setText(SResources.getString("b.updateRegistry"));
      var3.addSelectionListener(new WinRegPreferencePage$2(this));
      Point var4 = var2.computeSize(-1, -1);
      ((ScrolledComposite)var2.getParent()).setMinSize(var4);
      var2.layout();
   }

   private boolean changedLinkPrefs() {
      for (int var1 = 0; var1 < this.registerLinks.length; var1++) {
         if (this.registerLinks[var1].getSelection() != 0) {
            return true;
         }
      }

      return false;
   }

   private void createRegFile() {
      String var1 = System.getProperty("user.dir") + System.getProperty("file.separator");
      String var4 = System.getProperty("gnu.gcj.progname");

      try {
         String var5 = var1 + VersionInfo.getName() + ".reg";
         String var6 = var1 + VersionInfo.getName() + ".exe";
         if (var4 != null && !var4.toLowerCase().endsWith("exe")) {
            var4 = var4 + ".exe";
         }

         if (!new File(var6).exists() && var4 != null) {
            var6 = var4;
         }

         String var7 = System.getProperty("java.class.path");
         if (var7 != null && var7.toLowerCase().endsWith(".exe") && new File(var7).exists()) {
            var6 = var7;
         }

         var6 = SwissArmy.replaceAll(var6, "\\\\", "\\\\");
         FileOutputStream var2 = new FileOutputStream(var5);
         PrintStream var3 = new PrintStream(var2);
         var3.println("REGEDIT4");

         for (int var8 = 0; var8 < this.registerLinks.length; var8++) {
            switch (this.registerLinks[var8].getSelection()) {
               case 1:
                  this.registerType(var3, this.registerLinks[var8].getText(), var6, this.createExtra());
                  break;
               case 2:
                  this.unregisterType(var3, this.registerLinks[var8].getText());
            }
         }

         for (int var9 = 0; var9 < this.registerExtensions.length; var9++) {
            switch (this.registerExtensions[var9].getSelection()) {
               case 1:
                  this.registerTorrent(var3, var6, this.createExtra());
                  break;
               case 2:
                  this.unregisterTorrent(var3);
            }
         }

         var3.close();
         this.updateRegistry(var5);
      } catch (Exception var10) {
         Sancho.pDebug("createRegFile: " + var10);
      }
   }

   private void updateRegistry(String var1) {
      // regedit.exe is win32-only; off Windows (e.g. a debug preview of this page on
      // Linux/macOS) tell the user it's unsupported instead of failing to launch a
      // Windows binary.
      if (!VersionInfo.getSWTPlatform().equals("win32")) {
         MessageDialog.openInformation(this.getShell(), VersionInfo.getName(), SResources.getString("l.regeditWin32Only"));
      } else {
         String[] var2 = new String[]{"regedit.exe", "/s", var1};
         Runtime var3 = Runtime.getRuntime();

         try {
            Process var4 = var3.exec(var2);
            var4.waitFor();
         } catch (Exception var5) {
            Sancho.pDebug("updateRegistry: " + var5);
         }
      }

      if (!Sancho.debug) {
         File var6 = new File(var1);
         if (var6.exists()) {
            var6.delete();
         }
      }
   }

   private void registerTorrent(PrintStream var1, String var2, String var3) {
      var1.println("[HKEY_CLASSES_ROOT\\.torrent]");
      var1.println("@=\"bittorrent\"");
      var1.println("[HKEY_CLASSES_ROOT\\bittorrent]");
      var1.println("@=\"TORRENT File\"");
      var1.println("[HKEY_CLASSES_ROOT\\bittorrent\\shell]");
      var1.println("@=\"open\"");
      var1.println("[HKEY_CLASSES_ROOT\\bittorrent\\shell\\open]");
      var1.println("[HKEY_CLASSES_ROOT\\bittorrent\\shell\\open\\command]");
      this.printCommand(var1, var2, var3);
   }

   private void unregisterTorrent(PrintStream var1) {
      var1.println("[-HKEY_CLASSES_ROOT\\bittorrent\\shell\\open\\command]");
      var1.println("[-HKEY_CLASSES_ROOT\\bittorrent\\shell\\open]");
      var1.println("[-HKEY_CLASSES_ROOT\\bittorrent\\shell]");
      var1.println("[-HKEY_CLASSES_ROOT\\bittorrent]");
      var1.println("[-HKEY_CLASSES_ROOT\\.torrent]");
   }

   private void registerType(PrintStream var1, String var2, String var3, String var4) {
      var1.println("[HKEY_CLASSES_ROOT\\" + var2 + "]");
      var1.println("@=\"URL: " + var2 + " Protocol\"");
      var1.println("\"URL Protocol\"=\"\"");
      var1.println("[HKEY_CLASSES_ROOT\\" + var2 + "\\shell]");
      var1.println("[HKEY_CLASSES_ROOT\\" + var2 + "\\shell\\open]");
      var1.println("[HKEY_CLASSES_ROOT\\" + var2 + "\\shell\\open\\command]");
      this.printCommand(var1, var3, var4);
   }

   private void printCommand(PrintStream var1, String var2, String var3) {
      var1.println("@=\"\\\"" + var2 + "\\\" " + var3 + "\\\"-l\\\" \\\"%1\\\"\"");
   }

   private String createExtra() {
      String var1 = "";
      String var2 = PreferenceLoader.getPrefFile();
      String var3 = PreferenceLoader.getHomeDirectory();
      if (var2 != null) {
         var2 = SwissArmy.replaceAll(var2, "\\\\", "\\\\");
      }

      if (var3 != null) {
         if (var3.endsWith("\\")) {
            var3 = var3.substring(0, var3.length() - 1);
         }

         var3 = SwissArmy.replaceAll(var3, "\\\\", "\\\\");
      }

      if (PreferenceLoader.jvm != null) {
         String var4 = PreferenceLoader.jvm;
         var4 = SwissArmy.replaceAll(var4, "\\\\", "\\\\");
         var1 = var1 + "\\\"-r\\\" \\\"" + var4 + "\\\" ";
      }

      if (PreferenceLoader.customPrefFile) {
         var1 = var1 + "\\\"-c\\\" \\\"" + var2 + "\\\" ";
      }

      if (PreferenceLoader.customHomeDir) {
         var1 = var1 + "\\\"-j\\\" \\\"" + var3 + "\\\" ";
      }

      return var1;
   }

   private void unregisterType(PrintStream var1, String var2) {
      var1.println("[-HKEY_CLASSES_ROOT\\" + var2 + "\\shell\\open\\command]");
      var1.println("[-HKEY_CLASSES_ROOT\\" + var2 + "\\shell\\open]");
      var1.println("[-HKEY_CLASSES_ROOT\\" + var2 + "\\shell]");
      var1.println("[-HKEY_CLASSES_ROOT\\" + var2 + "]");
   }

   // $VF: synthetic method
   static boolean access$000(WinRegPreferencePage var0) {
      return var0.changedExtPrefs();
   }

   // $VF: synthetic method
   static void access$100(WinRegPreferencePage var0) {
      var0.createRegFile();
   }

   // $VF: synthetic method
   static boolean access$200(WinRegPreferencePage var0) {
      return var0.changedLinkPrefs();
   }
}
