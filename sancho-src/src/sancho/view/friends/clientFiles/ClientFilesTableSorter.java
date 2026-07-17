package sancho.view.friends.clientFiles;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Result;
import sancho.view.viewer.GSorter;

public class ClientFilesTableSorter extends GSorter {
   public ClientFilesTableSorter(ClientFilesTableView var1) {
      super(var1);
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      Result var5 = (Result)var2;
      Result var6 = (Result)var3;
      switch (var4) {
         case 0:
            return this.compareStrings(var5.getName(), var6.getName());
         case 1:
            return this.compareLongs(var5.getSize(), var6.getSize());
         case 2:
            return this.compareStrings(var5.getFormat(), var6.getFormat());
         case 3:
            return this.compareStrings(var5.getType(), var6.getType());
         case 4:
            return this.compareStrings(var5.getCodecTag(), var6.getCodecTag());
         case 5:
            return this.compareInts(var5.getBitrateTag(), var6.getBitrateTag());
         case 6:
            return this.compareStrings(var5.getLengthTag(), var6.getLengthTag());
         case 7:
            return this.compareStrings(var5.getMD4(), var6.getMD4());
         default:
            return this.compareDefault((TableViewer)var1, this.columnIndex, var2, var3);
      }
   }
}
