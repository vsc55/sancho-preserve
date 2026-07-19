package sancho.view.friends.clientFiles;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Result;
import sancho.view.viewer.GSorter;

public class ClientFilesTableSorter extends GSorter {
   public ClientFilesTableSorter(ClientFilesTableView view) {
      super(view);
   }

   protected int _compare(Viewer viewer, Object element1, Object element2, int columnId) {
      Result result1 = (Result)element1;
      Result result2 = (Result)element2;
      switch (columnId) {
         case 0:
            return this.compareStrings(result1.getName(), result2.getName());
         case 1:
            return this.compareLongs(result1.getSize(), result2.getSize());
         case 2:
            return this.compareStrings(result1.getFormat(), result2.getFormat());
         case 3:
            return this.compareStrings(result1.getType(), result2.getType());
         case 4:
            return this.compareStrings(result1.getCodecTag(), result2.getCodecTag());
         case 5:
            return this.compareInts(result1.getBitrateTag(), result2.getBitrateTag());
         case 6:
            return this.compareStrings(result1.getLengthTag(), result2.getLengthTag());
         case 7:
            return this.compareStrings(result1.getMD4(), result2.getMD4());
         default:
            return this.compareDefault((TableViewer)viewer, this.columnIndex, element1, element2);
      }
   }
}
