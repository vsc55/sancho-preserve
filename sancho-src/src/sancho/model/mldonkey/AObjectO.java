package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.utility.MyObservable;

public abstract class AObjectO extends MyObservable implements IObject {
   protected ICore core;

   AObjectO(ICore var1) {
      this.core = var1;
   }

   public final ICore getCore() {
      return this.core;
   }
}
