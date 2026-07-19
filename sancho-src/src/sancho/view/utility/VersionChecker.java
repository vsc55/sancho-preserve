package sancho.view.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.utility.VersionInfo;
import sancho.view.StatusLine;
import sancho.view.preferences.PreferenceLoader;

public class VersionChecker implements Runnable {
   URL url;
   Shell shell;
   String newVersion;
   StatusLine statusLine;

   public VersionChecker(Shell var1, StatusLine var2, int var3) {
      this.shell = var1;
      this.statusLine = var2;
      var1.getDisplay().timerExec(var3, this);
   }

   public void run() {
      // Background worker: fetch the latest published version off the UI thread.
      Thread thread = new Thread(new Runnable() {
         public void run() {
            try {
               VersionChecker.this.url = new URL(VersionInfo.getHomePage2() + "/version.php");
            } catch (MalformedURLException var5) {
               Sancho.pDebug("VersionChecker: " + var5);
               return;
            }

            try {
               URLConnection var1 = VersionChecker.this.url.openConnection();
               StringBuffer var6 = new StringBuffer(64);
               var6.append(VersionInfo.getName());
               var6.append("/");
               var6.append(VersionInfo.getVersion());
               var6.append(" ");
               var6.append("(");
               var6.append(System.getProperty("os.name"));
               var6.append(" ");
               var6.append(System.getProperty("os.version"));
               var6.append("/");
               var6.append(VersionInfo.getSWTPlatform());
               var6.append(")");
               var6.append(" ");
               var6.append("(");
               var6.append(System.getProperty("java.vm.name"));
               var6.append(" ");
               var6.append(System.getProperty("java.vm.version"));
               var6.append(")");
               var1.setRequestProperty("User-Agent", var6.toString());
               BufferedReader var3 = new BufferedReader(new InputStreamReader(var1.getInputStream()));
               VersionChecker.this.newVersion = var3.readLine();
               var3.close();
               if (!VersionChecker.this.shell.isDisposed() && !VersionChecker.this.shell.getDisplay().isDisposed()) {
                  // Success: report the fetched version back on the UI thread.
                  VersionChecker.this.shell.getDisplay().asyncExec(new Runnable() {
                     public void run() {
                        VersionChecker var2 = VersionChecker.this;
                        String var1 = var2.newVersion;
                        // A dead/parked update host can answer with HTML (or an empty body -> null)
                        // instead of a version string. Only act when it actually looks like a version,
                        // so we don't show a bogus "latest version" text or a false new-version popup
                        // (and don't NPE on a null line).
                        boolean var3 = var1 != null && var1.matches("\\d+(\\.\\d+)*(-\\d+)?");
                        if (var2.shell != null && !var2.shell.isDisposed()) {
                           if (var2.statusLine != null && var3) {
                              var2.statusLine
                                 .setText(
                                    "["
                                       + VersionInfo.getName()
                                       + "] "
                                       + SResources.getString("l.current")
                                       + VersionInfo.getVersion()
                                       + " / "
                                       + SResources.getString("l.latest")
                                       + var1
                                 );
                           }

                           if (var3 && !var1.equals(VersionInfo.getVersion()) && PreferenceLoader.loadBoolean("versionCheckPopup")) {
                              new VersionDialog(var2.shell, var1).open();
                           }
                        }
                     }
                  });
               }
            } catch (IOException var4) {
               Sancho.pDebug("VersionChecker: " + var4);
               String var2 = var4.toString();
               // Failure: note that the version check could not reach the server.
               VersionChecker.this.shell.getDisplay().asyncExec(new Runnable() {
                  public void run() {
                     VersionChecker.this.statusLine.setText(SResources.getString("l.versionCheckUnavailable"));
                  }
               });
            }
         }
      });
      thread.setDaemon(true);
      thread.start();
   }

   // Modal dialog shown when a newer release is available.
   private static class VersionDialog extends Dialog {
      String string;

      public VersionDialog(Shell var1, String var2) {
         super(var1);
         this.string = var2;
      }

      protected void configureShell(Shell var1) {
         super.configureShell(var1);
         var1.setText(SResources.getString("l.newVersionTitle"));
      }

      protected Control createDialogArea(Composite var1) {
         Composite var2 = (Composite)super.createDialogArea(var1);
         var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
         Label var3 = new Label(var2, 0);
         GridData var4 = new GridData(832);
         var4.horizontalSpan = 2;
         var3.setLayoutData(var4);
         var3.setText(VersionInfo.getName() + ": " + SResources.getString("l.newVersionText"));
         this.createLabel(var2, SResources.getString("l.current"), 128);
         this.createLabel(var2, VersionInfo.getVersion(), 32);
         this.createLabel(var2, SResources.getString("l.latest"), 128);
         this.createLabel(var2, this.string, 32);
         return var2;
      }

      private void createLabel(Composite var1, String var2, int var3) {
         Label var4 = new Label(var1, 0);
         var4.setLayoutData(new GridData(512 | var3));
         var4.setText(var2);
      }

      protected Control createButtonBar(Composite var1) {
         Composite var2 = new Composite(var1, 0);
         var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
         var2.setLayoutData(new GridData(768));
         this.createButton(var2, 128, SResources.getString("b.close"), new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               VersionDialog.this.close();
            }
         });
         this.createButton(
            var2,
            768,
            SResources.getString("l.visit") + " " + SResources.getString("ti.web.sancho"),
            new SelectionAdapter() {
               public void widgetSelected(SelectionEvent var1) {
                  WebLauncher.openLink(VersionInfo.getHomePage2());
                  VersionDialog.this.close();
               }
            }
         );
         return var2;
      }

      protected void createButton(Composite var1, int var2, String var3, SelectionListener var4) {
         Button var5 = new Button(var1, 0);
         var5.setLayoutData(new GridData(var2));
         var5.setText(var3);
         var5.addSelectionListener(var4);
      }
   }
}
