package sancho.model.mldonkey;

import sancho.model.mldonkey.utility.MessageBuffer;

public interface IObject {
   void read(MessageBuffer buffer);

   void deleteObservers();
}
