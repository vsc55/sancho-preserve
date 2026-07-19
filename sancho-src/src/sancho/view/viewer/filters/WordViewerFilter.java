package sancho.view.viewer.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import sancho.model.mldonkey.Result;

public class WordViewerFilter extends ViewerFilter {
   private int wordFilterType = 0;
   public static final int PROFANITY_FILTER_TYPE = 1;
   public static final int PORNOGRAPHY_FILTER_TYPE = 2;

   public WordViewerFilter(int wordFilterType) {
      this.wordFilterType = wordFilterType;
   }

   public boolean select(Viewer viewer, Object parentElement, Object element) {
      if (element instanceof Result) {
         Result result = (Result)element;
         if (this.wordFilterType == 1 && result.containsProfanity() || this.wordFilterType == 2 && result.containsPornography()) {
            return false;
         }
      }

      return true;
   }
}
