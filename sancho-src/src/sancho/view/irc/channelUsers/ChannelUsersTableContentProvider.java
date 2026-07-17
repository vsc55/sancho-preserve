package sancho.view.irc.channelUsers;

import java.util.List;
import sancho.view.viewer.table.GTableContentProvider;

public class ChannelUsersTableContentProvider extends GTableContentProvider {
   public ChannelUsersTableContentProvider(ChannelUsersTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      return ((List)var1).toArray();
   }
}
