package sancho.view.statistics.networkStats;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.utility.NetworkStat;
import sancho.utility.SwissArmy;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableMenuListener;

public class NetworkStatsTableMenuListener extends GTableMenuListener implements ISelectionChangedListener {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$utility$NetworkStat;

   public NetworkStatsTableMenuListener(NetworkStatsTableView var1) {
      super(var1);
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$utility$NetworkStat == null
            ? (class$sancho$model$mldonkey$utility$NetworkStat = class$("sancho.model.mldonkey.utility.NetworkStat"))
            : class$sancho$model$mldonkey$utility$NetworkStat
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         var1.add(new CopyNetworkStatToClipboardAction());
         var1.add(new CopyNetworkStatToClipboardHTMLAction());
         this.addSelectAllMenu(var1);
      }
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   // Context-menu action: copies the selected network stats as a plain-text table.
   private class CopyNetworkStatToClipboardAction extends Action {
      public CopyNetworkStatToClipboardAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void append(StringBuffer var1, int var2, String var3) {
         for (int var4 = 0; var4 < var2; var4++) {
            var1.append(var3);
         }
      }

      public void rj(StringBuffer var1, String var2, int var3) {
         int var4 = var2.length();

         for (int var5 = var4; var5 < var3; var5++) {
            var1.append(" ");
         }

         var1.append(var2);
         var1.append(" |");
      }

      public void lj(StringBuffer var1, String var2, int var3) {
         int var4 = var2.length();
         var1.append(var2);

         for (int var5 = var4; var5 < var3; var5++) {
            var1.append(" ");
         }

         var1.append(" |");
      }

      public void run() {
         String var1 = NetworkStatsTableMenuListener.this.gView.getColumnIDs();
         int[] var2 = new int[var1.length()];

         for (int var3 = 0; var3 < var1.length(); var3++) {
            var2[var3] = var1.charAt(var3) - 'A';
         }

         String[] var4 = NetworkStatsTableMenuListener.this.gView.getColumnLabels();
         int[] var5 = new int[var2.length];

         for (int var6 = 0; var6 < var2.length; var6++) {
            int var7 = var2[var6];
            String var8 = SResources.getString(var4[var7]);
            var5[var6] = var8.length();
         }

         for (int var19 = 0; var19 < NetworkStatsTableMenuListener.this.selectedObjects.size(); var19++) {
            NetworkStat var20 = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(var19);

            for (int var9 = 0; var9 < var2.length; var9++) {
               String var10 = NetworkStatsTableMenuListener.this.gView.getTableLabelProvider().getColumnText(var20, var9);
               var5[var9] = Math.max(var5[var9], var10.length());
            }
         }

         StringBuffer var21 = new StringBuffer(500);
         String var22 = System.getProperty("line.separator");
         var21.append(".");

         for (int var23 = 0; var23 < var2.length; var23++) {
            this.append(var21, var5[var23] + 2, "-");
            var21.append(".");
         }

         var21.append(var22);
         var21.append("|");

         for (int var11 = 0; var11 < var2.length; var11++) {
            int var12 = var2[var11];
            int var13 = NetworkStatsTableMenuListener.this.gView.getColumnAlignment()[var12];
            String var14 = SResources.getString(var4[var12]);
            var21.append(" ");
            if (var13 == 131072) {
               this.rj(var21, var14, var5[var11]);
            } else {
               this.lj(var21, var14, var5[var11]);
            }
         }

         var21.append(var22);
         var21.append("|");

         for (int var24 = 0; var24 < var2.length; var24++) {
            var21.append(" ");
            this.append(var21, var5[var24], "-");
            var21.append(" ");
            var21.append("|");
         }

         for (int var25 = 0; var25 < NetworkStatsTableMenuListener.this.selectedObjects.size(); var25++) {
            NetworkStat var26 = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(var25);
            var21.append(var22);
            var21.append("|");

            for (int var15 = 0; var15 < var2.length; var15++) {
               var21.append(" ");
               int var16 = var2[var15];
               String var17 = NetworkStatsTableMenuListener.this.gView.getTableLabelProvider().getColumnText(var26, var15);
               int var18 = NetworkStatsTableMenuListener.this.gView.getColumnAlignment()[var16];
               if (var18 == 131072) {
                  this.rj(var21, var17, var5[var15]);
               } else {
                  this.lj(var21, var17, var5[var15]);
               }
            }
         }

         var21.append(var22);
         var21.append("`");

         for (int var27 = 0; var27 < var2.length; var27++) {
            this.append(var21, var5[var27] + 2, "-");
            var21.append("'");
         }

         if (NetworkStatsTableMenuListener.this.selectedObjects.size() > 0) {
            NetworkStat var28 = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(0);
            var21.append(var22);
            var21.append("  [ ");
            var21.append(SwissArmy.calcStringOfSecondsFull((long)var28.getUptime()));
            String var29 = NetworkStatsTableMenuListener.this.gView.getCore().getCoreVersion();
            if (var29 != null && !var29.equals("")) {
               var21.append(" / ");
               var21.append(var29);
            }

            var21.append(" ]");
            var21.append(var22);
         }

         MainWindow.copyToClipboard(var21.toString());
      }
   }

   // Context-menu action: copies the selected network stats as an HTML table.
   private class CopyNetworkStatToClipboardHTMLAction extends Action {
      public CopyNetworkStatToClipboardHTMLAction() {
         super(SResources.getString("mi.copy") + " (html)");
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void append(StringBuffer var1, int var2, String var3) {
         for (int var4 = 0; var4 < var2; var4++) {
            var1.append(var3);
         }
      }

      public void rj(StringBuffer var1, String var2, int var3) {
         int var4 = var2.length();

         for (int var5 = var4; var5 < var3; var5++) {
            var1.append(" ");
         }

         var1.append(var2);
         var1.append(" |");
      }

      public void lj(StringBuffer var1, String var2, int var3) {
         int var4 = var2.length();
         var1.append(var2);

         for (int var5 = var4; var5 < var3; var5++) {
            var1.append(" ");
         }

         var1.append(" |");
      }

      public void run() {
         String var1 = NetworkStatsTableMenuListener.this.gView.getColumnIDs();
         int[] var2 = new int[var1.length()];

         for (int var3 = 0; var3 < var1.length(); var3++) {
            var2[var3] = var1.charAt(var3) - 'A';
         }

         String[] var4 = NetworkStatsTableMenuListener.this.gView.getColumnLabels();
         StringBuffer var5 = new StringBuffer(500);
         String var6 = System.getProperty("line.separator");
         var5.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td>");
         var5.append(
            "<table style=\"border: 2px solid #000; font-family: Verdana, Helvetica, Arial, Sans-serif; font-size: 10pt;\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\">"
         );
         var5.append(var6);
         var5.append("<tr>");

         for (int var7 = 0; var7 < var2.length; var7++) {
            int var8 = var2[var7];
            int var9 = NetworkStatsTableMenuListener.this.gView.getColumnAlignment()[var8];
            String var10 = SResources.getString(var4[var8]);
            var5.append("<th nowrap align=");
            if (var9 == 131072) {
               var5.append("\"right\"");
            } else {
               var5.append("\"left\"");
            }

            var5.append(">");
            var5.append(var10);
            var5.append("</th>");
         }

         var5.append("</tr>");

         for (int var14 = 0; var14 < NetworkStatsTableMenuListener.this.selectedObjects.size(); var14++) {
            NetworkStat var15 = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(var14);
            var5.append(var6);
            var5.append("<tr>");

            for (int var17 = 0; var17 < var2.length; var17++) {
               var5.append("<td nowrap");
               int var11 = var2[var17];
               String var12 = NetworkStatsTableMenuListener.this.gView.getTableLabelProvider().getColumnText(var15, var17);
               int var13 = NetworkStatsTableMenuListener.this.gView.getColumnAlignment()[var11];
               if (var13 == 131072) {
                  var5.append(" align=right");
               }

               var5.append(">");
               var5.append(var12);
               var5.append("</td>");
            }

            var5.append("</tr>");
         }

         var5.append(var6);
         var5.append("</table>");
         if (NetworkStatsTableMenuListener.this.selectedObjects.size() > 0) {
            NetworkStat var16 = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(0);
            var5.append(var6);
            var5.append(
               "<table width=\"100%\" style=\"font-family: Verdana, Helvetica, Arial, Sans-serif; font-size: 8pt;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td><center>"
            );
            var5.append(SwissArmy.calcStringOfSecondsFull((long)var16.getUptime()));
            String var18 = NetworkStatsTableMenuListener.this.gView.getCore().getCoreVersion();
            if (var18 != null && !var18.equals("")) {
               var5.append(" / ");
               var5.append(var18);
            }

            var5.append("</center></td></tr></table>");
            var5.append(var6);
         }

         var5.append("</td></tr></table>");
         MainWindow.copyToClipboard(var5.toString());
      }
   }
}
