package sancho.view.search;

import java.util.StringTokenizer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.utility.SearchQuery;
import sancho.utility.SwissArmy;
import sancho.view.SearchTab;
import sancho.view.search.result.ResultViewFrame;

public abstract class ASearchTab2 extends ASearchTab {
   protected Combo formatCombo;
   protected Combo maxCombo;
   protected Combo minAvailCombo;
   protected Combo minCombo;
   protected Combo resultCombo;

   public ASearchTab2(ResultViewFrame var1, SearchTab var2) {
      super(var1, var2);
   }

   public void addMaxSizeToQuery(SearchQuery var1, Combo var2) {
      if (!var2.getText().equals("")) {
         var1.setMaxSize(this.parseSizeCombo(var2));
      }
   }

   public void addMinSizeToQuery(SearchQuery var1, Combo var2) {
      if (!var2.getText().equals("")) {
         var1.setMinSize(this.parseSizeCombo(var2));
      }
   }

   public void addMaxResultsToQuery(SearchQuery var1, Combo var2) {
      if (!var2.getText().equals("")) {
         int var3 = 0;

         try {
            var3 = Integer.parseInt(this.resultCombo.getText());
         } catch (NumberFormatException var5) {
            var3 = 100;
         }

         var1.setMaxSearchResults(var3);
      }
   }

   protected Combo createMinMaxCombo(Composite var1, int var2, String var3) {
      String[] var4 = new String[]{"", "100 KB", "200 KB", "500 KB", "1 MB", "5 MB", "50 MB", "100 MB", "250 MB", "500 MB", "1 GB", "2 GB", "3 GB"};
      return this.createCombo(var1, var2, var3, var4);
   }

   public int parseMinAvail() {
      int var1;
      try {
         var1 = Integer.parseInt(this.minAvailCombo.getText());
      } catch (NumberFormatException var3) {
         var1 = 0;
      }

      return var1;
   }

   public long parseSizeCombo(Combo var1) {
      String var2 = "";
      String var3 = "MB";
      StringTokenizer var4 = new StringTokenizer(var1.getText());
      var2 = var4.nextToken();
      if (var4.hasMoreTokens()) {
         var3 = var4.nextToken();
      }

      return SwissArmy.stringSizeToLong(var2, var3);
   }
}
