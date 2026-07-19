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

   public VersionChecker(Shell shell, StatusLine statusLine, int delay) {
      this.shell = shell;
      this.statusLine = statusLine;
      shell.getDisplay().timerExec(delay, this);
   }

   public void run() {
      // Background worker: fetch the latest published version off the UI thread.
      Thread thread = new Thread(new Runnable() {
         public void run() {
            try {
               VersionChecker.this.url = new URL(VersionInfo.getHomePage2() + "/version.php");
            } catch (MalformedURLException malformedURLException) {
               Sancho.pDebug("VersionChecker: " + malformedURLException);
               return;
            }

            try {
               URLConnection connection = VersionChecker.this.url.openConnection();
               StringBuffer buffer = new StringBuffer(64);
               buffer.append(VersionInfo.getName());
               buffer.append("/");
               buffer.append(VersionInfo.getVersion());
               buffer.append(" ");
               buffer.append("(");
               buffer.append(System.getProperty("os.name"));
               buffer.append(" ");
               buffer.append(System.getProperty("os.version"));
               buffer.append("/");
               buffer.append(VersionInfo.getSWTPlatform());
               buffer.append(")");
               buffer.append(" ");
               buffer.append("(");
               buffer.append(System.getProperty("java.vm.name"));
               buffer.append(" ");
               buffer.append(System.getProperty("java.vm.version"));
               buffer.append(")");
               connection.setRequestProperty("User-Agent", buffer.toString());
               BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
               VersionChecker.this.newVersion = reader.readLine();
               reader.close();
               if (!VersionChecker.this.shell.isDisposed() && !VersionChecker.this.shell.getDisplay().isDisposed()) {
                  // Success: report the fetched version back on the UI thread.
                  VersionChecker.this.shell.getDisplay().asyncExec(new Runnable() {
                     public void run() {
                        VersionChecker checker = VersionChecker.this;
                        String version = checker.newVersion;
                        // A dead/parked update host can answer with HTML (or an empty body -> null)
                        // instead of a version string. Only act when it actually looks like a version,
                        // so we don't show a bogus "latest version" text or a false new-version popup
                        // (and don't NPE on a null line).
                        boolean isVersion = version != null && version.matches("\\d+(\\.\\d+)*(-\\d+)?");
                        if (checker.shell != null && !checker.shell.isDisposed()) {
                           if (checker.statusLine != null && isVersion) {
                              checker.statusLine
                                 .setText(
                                    "["
                                       + VersionInfo.getName()
                                       + "] "
                                       + SResources.getString("l.current")
                                       + VersionInfo.getVersion()
                                       + " / "
                                       + SResources.getString("l.latest")
                                       + version
                                 );
                           }

                           if (isVersion && !version.equals(VersionInfo.getVersion()) && PreferenceLoader.loadBoolean("versionCheckPopup")) {
                              new VersionDialog(checker.shell, version).open();
                           }
                        }
                     }
                  });
               }
            } catch (IOException ioException) {
               Sancho.pDebug("VersionChecker: " + ioException);
               String text = ioException.toString();
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

      public VersionDialog(Shell shell, String version) {
         super(shell);
         this.string = version;
      }

      protected void configureShell(Shell shell) {
         super.configureShell(shell);
         shell.setText(SResources.getString("l.newVersionTitle"));
      }

      protected Control createDialogArea(Composite parent) {
         Composite composite = (Composite)super.createDialogArea(parent);
         composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
         Label label = new Label(composite, 0);
         GridData gridData = new GridData(832);
         gridData.horizontalSpan = 2;
         label.setLayoutData(gridData);
         label.setText(VersionInfo.getName() + ": " + SResources.getString("l.newVersionText"));
         this.createLabel(composite, SResources.getString("l.current"), 128);
         this.createLabel(composite, VersionInfo.getVersion(), 32);
         this.createLabel(composite, SResources.getString("l.latest"), 128);
         this.createLabel(composite, this.string, 32);
         return composite;
      }

      private void createLabel(Composite composite, String text, int style) {
         Label label = new Label(composite, 0);
         label.setLayoutData(new GridData(512 | style));
         label.setText(text);
      }

      protected Control createButtonBar(Composite parent) {
         Composite composite = new Composite(parent, 0);
         composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
         composite.setLayoutData(new GridData(768));
         this.createButton(composite, 128, SResources.getString("b.close"), new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               VersionDialog.this.close();
            }
         });
         this.createButton(
            composite,
            768,
            SResources.getString("l.visit") + " " + SResources.getString("ti.web.sancho"),
            new SelectionAdapter() {
               public void widgetSelected(SelectionEvent event) {
                  WebLauncher.openLink(VersionInfo.getHomePage2());
                  VersionDialog.this.close();
               }
            }
         );
         return composite;
      }

      protected void createButton(Composite composite, int style, String text, SelectionListener listener) {
         Button button = new Button(composite, 0);
         button.setLayoutData(new GridData(style));
         button.setText(text);
         button.addSelectionListener(listener);
      }
   }
}
