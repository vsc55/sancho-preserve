package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Client33 extends Client23 {
   public Client33(ICore var1) {
      super(var1);
   }

   protected boolean readMore(MessageBuffer var1) {
      boolean var2 = super.readMore(var1);
      String var3 = var1.getString();
      if (var3.length() > 0) {
         this.software = this.software + " " + var3;
      }

      return var2;
   }
}
