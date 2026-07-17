package sancho.view.statistics.networkStats;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.utility.NetworkStat;
import sancho.utility.SwissArmy;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

public class NetworkStatsTableMenuListener$CopyNetworkStatToClipboardHTMLAction extends Action {
   // $VF: synthetic field
   private final NetworkStatsTableMenuListener this$0;

   public NetworkStatsTableMenuListener$CopyNetworkStatToClipboardHTMLAction(NetworkStatsTableMenuListener var1) {
      super(SResources.getString("mi.copy") + " (html)");
      this.this$0 = var1;
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
      String var1 = NetworkStatsTableMenuListener.access$1300(this.this$0).getColumnIDs();
      int[] var2 = new int[var1.length()];

      for (int var3 = 0; var3 < var1.length(); var3++) {
         var2[var3] = var1.charAt(var3) - 'A';
      }

      String[] var4 = NetworkStatsTableMenuListener.access$1400(this.this$0).getColumnLabels();
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
         int var9 = NetworkStatsTableMenuListener.access$1500(this.this$0).getColumnAlignment()[var8];
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

      for (int var14 = 0; var14 < NetworkStatsTableMenuListener.access$1600(this.this$0).size(); var14++) {
         NetworkStat var15 = (NetworkStat)NetworkStatsTableMenuListener.access$1700(this.this$0).get(var14);
         var5.append(var6);
         var5.append("<tr>");

         for (int var17 = 0; var17 < var2.length; var17++) {
            var5.append("<td nowrap");
            int var11 = var2[var17];
            String var12 = NetworkStatsTableMenuListener.access$1800(this.this$0).getTableLabelProvider().getColumnText(var15, var17);
            int var13 = NetworkStatsTableMenuListener.access$1900(this.this$0).getColumnAlignment()[var11];
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
      if (NetworkStatsTableMenuListener.access$2000(this.this$0).size() > 0) {
         NetworkStat var16 = (NetworkStat)NetworkStatsTableMenuListener.access$2100(this.this$0).get(0);
         var5.append(var6);
         var5.append(
            "<table width=\"100%\" style=\"font-family: Verdana, Helvetica, Arial, Sans-serif; font-size: 8pt;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td><center>"
         );
         var5.append(SwissArmy.calcStringOfSecondsFull((long)var16.getUptime()));
         String var18 = NetworkStatsTableMenuListener.access$2200(this.this$0).getCore().getCoreVersion();
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
