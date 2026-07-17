package sancho.view.search.result;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Result;
import sancho.view.viewer.GSorter;

public class ResultTableSorter extends GSorter {
   public ResultTableSorter(ResultTableView var1) {
      super(var1);
   }

   public boolean sortOrder(int var1) {
      switch (this.cViewer.getColumnIDs()[var1]) {
         case 0:
         case 1:
         case 5:
            return true;
         default:
            return false;
      }
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      Result var5 = (Result)var2;
      Result var6 = (Result)var3;
      switch (var4) {
         case 0:
            return this.compareStrings(var5.getNetworkName(), var6.getNetworkName());
         case 1:
            return this.compareStrings(var5.getName(), var6.getName());
         case 2:
            return this.compareLongs(var5.getSize(), var6.getSize());
         case 3:
            return this.compareStrings(var5.getFormat(), var6.getFormat());
         case 4:
            return this.compareStrings(var5.getType(), var6.getType());
         case 5:
            return this.compareStrings(var5.getCodecTag(), var6.getCodecTag());
         case 6:
            return this.compareInts(var5.getBitrateTag(), var6.getBitrateTag());
         case 7:
            return this.compareStrings(var5.getLengthTag(), var6.getLengthTag());
         case 8:
            return this.compareInts(var5.getAvail(), var6.getAvail());
         case 9:
            return this.compareInts(var5.getCompleteSources(), var6.getCompleteSources());
         default:
            return this.compareDefault((TableViewer)var1, this.columnIndex, var2, var3);
      }
   }
}
