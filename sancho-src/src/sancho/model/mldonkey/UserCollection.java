package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class UserCollection extends ACollection_Int {
   UserCollection(ICore var1) {
      super(var1);
   }

   public void read(MessageBuffer var1) {
      int var2 = var1.getInt32();
      User var3 = (User)this.get(var2);
      if (var3 != null) {
         var3.read(var2, var1);
      } else {
         var3 = this.core.getCollectionFactory().getUser();
         var3.read(var2, var1);
         this.put(var2, var3);
      }

      this.setChanged();
      this.notifyObservers(var3);
   }
}
