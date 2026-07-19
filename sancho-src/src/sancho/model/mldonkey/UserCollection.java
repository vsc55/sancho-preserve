package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class UserCollection extends ACollection_Int {
   UserCollection(ICore core) {
      super(core);
   }

   public void read(MessageBuffer buffer) {
      int id = buffer.getInt32();
      User user = (User)this.get(id);
      if (user != null) {
         user.read(id, buffer);
      } else {
         user = this.core.getCollectionFactory().getUser();
         user.read(id, buffer);
         this.put(id, user);
      }

      this.setChanged();
      this.notifyObservers(user);
   }
}
