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

   private static Process openWebBrowser(String url) throws IOException {
      Process process = null;
      String[] command = new String[]{webBrowser, url};
      if (webBrowser == null || webBrowser.equals("")) {
         try {
            webBrowser = "mozilla";
            command[0] = webBrowser;
            process = Runtime.getRuntime().exec(command);
         } catch (IOException ioException) {
            try {
               webBrowser = "konqueror";
               command[0] = webBrowser;
               process = Runtime.getRuntime().exec(command);
            } catch (IOException ioException2) {
               process = null;
               webBrowser = "netscape";
               command[0] = webBrowser;
            }
         }
      }

      if (process == null) {
         try {
            process = Runtime.getRuntime().exec(command);
         } catch (IOException ioException) {
            Object ignored = null;
            throw ioException;
         }
      }

      return process;
   }

   public static void openLink(String url) {
      webBrowser = PreferenceLoader.loadStringEnv("defaultWebBrowser");
      if (url.startsWith("file:")) {
         url = url.substring(5);

         while (url.startsWith("/")) {
            url = url.substring(1);
         }

         url = "file:///" + url;
      }

      String link = url;
      Display display = Display.getCurrent();
      String platform = VersionInfo.getSWTPlatform();
      if ("win32".equals(platform) || "win32-fox".equals(platform)) {
         Program program = Program.findProgram("html");
         if (program != null && program.execute(url)) {
            return;
         }

         Program.launch(url);
      } else if ("carbon".equals(platform) || "cocoa".equals(platform)) {
         // "carbon" was the old 32-bit macOS SWT platform; modern macOS reports
         // "cocoa". Both open external links with the native `open` command.
         try {
            String[] command = new String[]{"/usr/bin/open", link};
            Runtime.getRuntime().exec(command);
         } catch (IOException ioException) {
            openWebBrowserError(display);
         }
      } else {
         // Linux/other. If the user configured a specific browser, honour it via the
         // background browser thread below. Otherwise use the desktop default through SWT's
         // Program.launch (xdg-open/gio) instead of the dead mozilla/konqueror/
         // netscape chain, falling back to xdg-open and finally the legacy path.
         if (webBrowser == null || webBrowser.equals("")) {
            if (Program.launch(url)) {
               return;
            }

            try {
               Runtime.getRuntime().exec(new String[]{"xdg-open", url});
               return;
            } catch (IOException ioException) {
            }
         }

         final String linkHref = link;
         Thread browserThread = new Thread("webBrowser") {
            public void run() {
               try {
                  if (!webBrowserOpened
                     || !webBrowser.equals("MozillaFirebird")
                        && !webBrowser.equals("netscape")
                        && !webBrowser.equals("mozilla")) {
                     Process process = openWebBrowser(linkHref);
                     webBrowserOpened = true;

                     try {
                        if (process != null) {
                           process.waitFor();
                        }
                     } catch (InterruptedException interruptedException) {
                        openWebBrowserError(display);
                     } finally {
                        webBrowserOpened = false;
                     }
                  } else {
                     String[] command = new String[]{webBrowser, "-remote", "openURL(" + linkHref + ")"};
                     Runtime.getRuntime().exec(command);
                  }
               } catch (IOException ioException) {
                  openWebBrowserError(display);
               }
            }
         };
         browserThread.start();
      }
   }

   private static void openWebBrowserError(final Display display) {
      display.asyncExec(new Runnable() {
         public void run() {
            MessageBox messageBox = new MessageBox(new Shell(display), 1);
            messageBox.setText(SResources.getString("l.webBrowserErrorTitle"));
            messageBox.setMessage(SResources.getString("l.webBrowserError"));
            messageBox.open();
         }
      });
   }
}
