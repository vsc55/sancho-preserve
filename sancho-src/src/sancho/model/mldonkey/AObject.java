package sancho.model.mldonkey;

import sancho.core.ICore;

public abstract class AObject {
   protected ICore core;

   AObject(ICore var1) {
      this.core = var1;
   }

   public final ICore getCore() {
      return this.core;
   }
}
