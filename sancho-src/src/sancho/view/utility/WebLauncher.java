package sancho.view.utility;

import java.io.IOException;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;

public class WebLauncher {
   private static String webBrowser = "";
   private static boolean webBrowserOpened;

   private static Process openWebBrowser(String var0) throws IOException {
      Process var1 = null;
      String[] var2 = new String[]{webBrowser, var0};
      if (webBrowser == null || webBrowser.equals("")) {
         try {
            webBrowser = "mozilla";
            var2[0] = webBrowser;
            var1 = Runtime.getRuntime().exec(var2);
         } catch (IOException var7) {
            try {
               webBrowser = "konqueror";
               var2[0] = webBrowser;
               var1 = Runtime.getRuntime().exec(var2);
            } catch (IOException var6) {
               var1 = null;
               webBrowser = "netscape";
               var2[0] = webBrowser;
            }
         }
      }

      if (var1 == null) {
         try {
            var1 = Runtime.getRuntime().exec(var2);
         } catch (IOException var5) {
            Object var8 = null;
            throw var5;
         }
      }

      return var1;
   }

   public static void openLink(String var0) {
      webBrowser = PreferenceLoader.loadStringEnv("defaultWebBrowser");
      if (var0.startsWith("file:")) {
         var0 = var0.substring(5);

         while (var0.startsWith("/")) {
            var0 = var0.substring(1);
         }

         var0 = "file:///" + var0;
      }

      String var1 = var0;
      Display var2 = Display.getCurrent();
      String var3 = VersionInfo.getSWTPlatform();
      if ("win32".equals(var3) || "win32-fox".equals(var3)) {
         Program var8 = Program.findProgram("html");
         if (var8 != null && var8.execute(var0)) {
            return;
         }

         Program.launch(var0);
      } else if ("carbon".equals(var3) || "cocoa".equals(var3)) {
         // "carbon" was the old 32-bit macOS SWT platform; modern macOS reports
         // "cocoa". Both open external links with the native `open` command.
         try {
            String[] var4 = new String[]{"/usr/bin/open", var1};
            Runtime.getRuntime().exec(var4);
         } catch (IOException var5) {
            openWebBrowserError(var2);
         }
      } else {
         // Linux/other. If the user configured a specific browser, honour it via the
         // background browser thread below. Otherwise use the desktop default through SWT's
         // Program.launch (xdg-open/gio) instead of the dead mozilla/konqueror/
         // netscape chain, falling back to xdg-open and finally the legacy path.
         if (webBrowser == null || webBrowser.equals("")) {
            if (Program.launch(var0)) {
               return;
            }

            try {
               Runtime.getRuntime().exec(new String[]{"xdg-open", var0});
               return;
            } catch (IOException var6) {
            }
         }

         final String linkHref = var1;
         Thread browserThread = new Thread("webBrowser") {
            public void run() {
               try {
                  if (!webBrowserOpened
                     || !webBrowser.equals("MozillaFirebird")
                        && !webBrowser.equals("netscape")
                        && !webBrowser.equals("mozilla")) {
                     Process var11 = openWebBrowser(linkHref);
                     webBrowserOpened = true;

                     try {
                        if (var11 != null) {
                           var11.waitFor();
                        }
                     } catch (InterruptedException var8) {
                        openWebBrowserError(var2);
                     } finally {
                        webBrowserOpened = false;
                     }
                  } else {
                     String[] var1 = new String[]{webBrowser, "-remote", "openURL(" + linkHref + ")"};
                     Runtime.getRuntime().exec(var1);
                  }
               } catch (IOException var10) {
                  openWebBrowserError(var2);
               }
            }
         };
         browserThread.start();
      }
   }

   private static void openWebBrowserError(final Display var0) {
      var0.asyncExec(new Runnable() {
         public void run() {
            MessageBox var1 = new MessageBox(new Shell(var0), 1);
            var1.setText(SResources.getString("l.webBrowserErrorTitle"));
            var1.setMessage(SResources.getString("l.webBrowserError"));
            var1.open();
         }
      });
   }
}
