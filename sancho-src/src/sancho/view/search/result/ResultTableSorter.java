package sancho.view.search.result;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Result;
import sancho.view.viewer.GSorter;

public class ResultTableSorter extends GSorter {
   public ResultTableSorter(ResultTableView view) {
      super(view);
   }

   public boolean sortOrder(int columnIndex) {
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
         case 1:
         case 5:
            return true;
         default:
            return false;
      }
   }

   protected int _compare(Viewer viewer, Object first, Object second, int columnId) {
      Result firstResult = (Result)first;
      Result secondResult = (Result)second;
      switch (columnId) {
         case 0:
            return this.compareStrings(firstResult.getNetworkName(), secondResult.getNetworkName());
         case 1:
            return this.compareStrings(firstResult.getName(), secondResult.getName());
         case 2:
            return this.compareLongs(firstResult.getSize(), secondResult.getSize());
         case 3:
            return this.compareStrings(firstResult.getFormat(), secondResult.getFormat());
         case 4:
            return this.compareStrings(firstResult.getType(), secondResult.getType());
         case 5:
            return this.compareStrings(firstResult.getCodecTag(), secondResult.getCodecTag());
         case 6:
            return this.compareInts(firstResult.getBitrateTag(), secondResult.getBitrateTag());
         case 7:
            return this.compareStrings(firstResult.getLengthTag(), secondResult.getLengthTag());
         case 8:
            return this.compareInts(firstResult.getAvail(), secondResult.getAvail());
         case 9:
            return this.compareInts(firstResult.getCompleteSources(), secondResult.getCompleteSources());
         default:
            return this.compareDefault((TableViewer)viewer, this.columnIndex, first, second);
      }
   }
}
