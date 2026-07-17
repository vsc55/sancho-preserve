package sancho.view.irc.channelUsers;

import org.eclipse.swt.graphics.Image;
import sancho.view.irc.CUser;
import sancho.view.viewer.table.GTableLabelProvider;

public class ChannelUsersTableLabelProvider extends GTableLabelProvider {
   public ChannelUsersTableLabelProvider(ChannelUsersTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      switch (this.cViewer.getColumnIDs()[var2]) {
         default:
            return null;
      }
   }

   public String getColumnText(Object var1, int var2) {
      CUser var3 = (CUser)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.toString();
         default:
            return "??";
      }
   }
}
