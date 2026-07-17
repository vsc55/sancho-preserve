package sancho.view.viewer.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import sancho.model.mldonkey.Result;

public class WordViewerFilter extends ViewerFilter {
   private int wordFilterType = 0;
   public static final int PROFANITY_FILTER_TYPE = 1;
   public static final int PORNOGRAPHY_FILTER_TYPE = 2;

   public WordViewerFilter(int var1) {
      this.wordFilterType = var1;
   }

   public boolean select(Viewer var1, Object var2, Object var3) {
      if (var3 instanceof Result) {
         Result var4 = (Result)var3;
         if (this.wordFilterType == 1 && var4.containsProfanity() || this.wordFilterType == 2 && var4.containsPornography()) {
            return false;
         }
      }

      return true;
   }
}
