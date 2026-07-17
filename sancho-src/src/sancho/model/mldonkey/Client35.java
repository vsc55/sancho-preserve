package sancho.model.mldonkey;

import org.eclipse.swt.graphics.Image;
import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.view.utility.SResources;

public class Client35 extends Client33 {
   byte sui_verified;

   public Client35(ICore var1) {
      super(var1);
   }

   public synchronized Image getNameImage() {
      if (this.sui_verified == 1) {
         return this.isFriend() ? SResources.getImage("client_friend_check") : SResources.getImage("client_check");
      } else {
         return super.getNameImage();
      }
   }

   public synchronized String getSUIString() {
      switch (this.sui_verified) {
         case 0:
            return SResources.getString("l.failed");
         case 1:
            return SResources.getString("l.passed");
         default:
            return super.getSUIString();
      }
   }

   public synchronized byte getSUI() {
      return this.sui_verified;
   }

   protected boolean readMore(MessageBuffer var1) {
      boolean var2 = super.readMore(var1);
      this.sui_verified = var1.getBoolOption();
      return var2;
   }
}
