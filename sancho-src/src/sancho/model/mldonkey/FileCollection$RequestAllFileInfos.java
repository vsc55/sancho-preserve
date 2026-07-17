package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;
import sancho.model.mldonkey.enums.EnumFileState;

class FileCollection$RequestAllFileInfos implements TObjectProcedure {
   public boolean execute(Object var1) {
      File var2 = (File)var1;
      if (var2.getFileStateEnum() == EnumFileState.DOWNLOADING) {
         var2.requestFileInfo();
      }

      return true;
   }
}
