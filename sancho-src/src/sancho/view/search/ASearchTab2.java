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

   public ASearchTab2(ResultViewFrame resultViewFrame, SearchTab searchTab) {
      super(resultViewFrame, searchTab);
   }

   public void addMaxSizeToQuery(SearchQuery query, Combo combo) {
      if (!combo.getText().equals("")) {
         query.setMaxSize(this.parseSizeCombo(combo));
      }
   }

   public void addMinSizeToQuery(SearchQuery query, Combo combo) {
      if (!combo.getText().equals("")) {
         query.setMinSize(this.parseSizeCombo(combo));
      }
   }

   public void addMaxResultsToQuery(SearchQuery query, Combo combo) {
      if (!combo.getText().equals("")) {
         int maxResults = 0;

         try {
            maxResults = Integer.parseInt(this.resultCombo.getText());
         } catch (NumberFormatException notANumber) {
            maxResults = 100;
         }

         query.setMaxSearchResults(maxResults);
      }
   }

   protected Combo createMinMaxCombo(Composite composite, int style, String labelKey) {
      String[] items = new String[]{"", "100 KB", "200 KB", "500 KB", "1 MB", "5 MB", "50 MB", "100 MB", "250 MB", "500 MB", "1 GB", "2 GB", "3 GB"};
      return this.createCombo(composite, style, labelKey, items);
   }

   public int parseMinAvail() {
      int minAvail;
      try {
         minAvail = Integer.parseInt(this.minAvailCombo.getText());
      } catch (NumberFormatException notANumber) {
         minAvail = 0;
      }

      return minAvail;
   }

   public long parseSizeCombo(Combo combo) {
      String size = "";
      String unit = "MB";
      StringTokenizer tokenizer = new StringTokenizer(combo.getText());
      size = tokenizer.nextToken();
      if (tokenizer.hasMoreTokens()) {
         unit = tokenizer.nextToken();
      }

      return SwissArmy.stringSizeToLong(size, unit);
   }
}
