package sancho.model.mldonkey;

import sancho.core.ICore;

public abstract class AObject {
   protected ICore core;

   AObject(ICore core) {
      this.core = core;
   }

   public final ICore getCore() {
      return this.core;
   }
}
