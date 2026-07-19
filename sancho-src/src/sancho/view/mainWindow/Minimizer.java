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

   public Minimizer(MainWindow var1, String var2) {
      this.shell = var1.getShell();
      this.titleBarText = var2;
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

   public void setConnected(boolean var1) {
      this.setTitleBarText();
   }

   public boolean minimize(boolean var1) {
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

   public void update(MyObservable var1, Object var2, int var3) {
      if (var1 instanceof ClientStats) {
         final ClientStats var4 = (ClientStats)var1;
         if (this.shell != null && !this.shell.isDisposed()) {
            this.shell.getDisplay().asyncExec(new Runnable() {
               public void run() {
                  if (Minimizer.this.shell != null && !Minimizer.this.shell.isDisposed() && Minimizer.this.shell.getMinimized()) {
                     Minimizer.this.setTitleBar(var4);
                  }
               }
            });
         }
      }
   }

   public void setTitleBar(ClientStats var1) {
      StringBuffer var2 = new StringBuffer(32);
      var2.append("(");
      var2.append("D:");
      var2.append(var1.getTcpDownRateStringS());
      var2.append(")");
      var2.append("(");
      var2.append("U:");
      var2.append(var1.getTcpUpRateStringS());
      var2.append(")");
      var2.append("(");
      var2.append(Sancho.getCoreFactory().getDescription());
      var2.append(")");
      this.shell.setText(var2.toString());
   }
}
