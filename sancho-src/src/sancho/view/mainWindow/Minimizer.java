package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.model.mldonkey.ClientStats;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.view.MainWindow;

public class Minimizer implements MyObserver {
   protected Shell shell;
   protected String titleBarText;
   protected boolean isRestoring;
   protected static final String S_D = "D:";
   protected static final String S_U = "U:";

   public boolean isRestoring() {
      return this.isRestoring;
   }

   public Minimizer(MainWindow mainWindow, String titleBarText) {
      this.shell = mainWindow.getShell();
      this.titleBarText = titleBarText;
   }

   public void setTitleBarText() {
      if (Sancho.getCore() == null) {
         this.shell.setText(this.titleBarText);
      } else {
         this.shell.setText(this.titleBarText + " (" + Sancho.getCoreFactory().getDescription() + ")");
      }
   }

   public boolean close() {
      return true;
   }

   public void setConnected(boolean connected) {
      this.setTitleBarText();
   }

   public boolean minimize(boolean force) {
      if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getClientStats().addObserver(this);
      }

      return true;
   }

   public boolean minimize() {
      return this.minimize(false);
   }

   public void forceClose() {
   }

   public void restore() {
      if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getClientStats().deleteObserver(this);
      }

      this.setTitleBarText();
   }

   public void update(MyObservable observable, Object arg, int id) {
      if (observable instanceof ClientStats) {
         final ClientStats clientStats = (ClientStats)observable;
         if (this.shell != null && !this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable() {
               public void run() {
                  if (Minimizer.this.shell != null && !Minimizer.this.shell.isDisposed() && Minimizer.this.shell.getMinimized()) {
                     Minimizer.this.setTitleBar(clientStats);
                  }
               }
            });
         }
      }
   }

   public void setTitleBar(ClientStats clientStats) {
      StringBuffer buffer = new StringBuffer(32);
      buffer.append("(");
      buffer.append("D:");
      buffer.append(clientStats.getTcpDownRateStringS());
      buffer.append(")");
      buffer.append("(");
      buffer.append("U:");
      buffer.append(clientStats.getTcpUpRateStringS());
      buffer.append(")");
      buffer.append("(");
      buffer.append(Sancho.getCoreFactory().getDescription());
      buffer.append(")");
      this.shell.setText(buffer.toString());
   }
}
