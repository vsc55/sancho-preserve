package sancho.view.statistics.networkStats;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.utility.NetworkStat;
import sancho.utility.SwissArmy;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

public class NetworkStatsTableMenuListener$CopyNetworkStatToClipboardAction extends Action {
   // $VF: synthetic field
   private final NetworkStatsTableMenuListener this$0;

   public NetworkStatsTableMenuListener$CopyNetworkStatToClipboardAction(NetworkStatsTableMenuListener var1) {
      super(SResources.getString("mi.copy"));
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
      String var1 = NetworkStatsTableMenuListener.access$000(this.this$0).getColumnIDs();
      int[] var2 = new int[var1.length()];

      for (int var3 = 0; var3 < var1.length(); var3++) {
         var2[var3] = var1.charAt(var3) - 'A';
      }

      String[] var4 = NetworkStatsTableMenuListener.access$100(this.this$0).getColumnLabels();
      int[] var5 = new int[var2.length];

      for (int var6 = 0; var6 < var2.length; var6++) {
         int var7 = var2[var6];
         String var8 = SResources.getString(var4[var7]);
         var5[var6] = var8.length();
      }

      for (int var19 = 0; var19 < NetworkStatsTableMenuListener.access$200(this.this$0).size(); var19++) {
         NetworkStat var20 = (NetworkStat)NetworkStatsTableMenuListener.access$300(this.this$0).get(var19);

         for (int var9 = 0; var9 < var2.length; var9++) {
            String var10 = NetworkStatsTableMenuListener.access$400(this.this$0).getTableLabelProvider().getColumnText(var20, var9);
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
         int var13 = NetworkStatsTableMenuListener.access$500(this.this$0).getColumnAlignment()[var12];
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

      for (int var25 = 0; var25 < NetworkStatsTableMenuListener.access$600(this.this$0).size(); var25++) {
         NetworkStat var26 = (NetworkStat)NetworkStatsTableMenuListener.access$700(this.this$0).get(var25);
         var21.append(var22);
         var21.append("|");

         for (int var15 = 0; var15 < var2.length; var15++) {
            var21.append(" ");
            int var16 = var2[var15];
            String var17 = NetworkStatsTableMenuListener.access$800(this.this$0).getTableLabelProvider().getColumnText(var26, var15);
            int var18 = NetworkStatsTableMenuListener.access$900(this.this$0).getColumnAlignment()[var16];
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

      if (NetworkStatsTableMenuListener.access$1000(this.this$0).size() > 0) {
         NetworkStat var28 = (NetworkStat)NetworkStatsTableMenuListener.access$1100(this.this$0).get(0);
         var21.append(var22);
         var21.append("  [ ");
         var21.append(SwissArmy.calcStringOfSecondsFull((long)var28.getUptime()));
         String var29 = NetworkStatsTableMenuListener.access$1200(this.this$0).getCore().getCoreVersion();
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
