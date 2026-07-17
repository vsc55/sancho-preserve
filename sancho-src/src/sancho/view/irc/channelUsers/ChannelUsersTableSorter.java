package sancho.view.irc.channelUsers;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.view.irc.CUser;
import sancho.view.viewer.GSorter;

public class ChannelUsersTableSorter extends GSorter {
   public ChannelUsersTableSorter(ChannelUsersTableView var1) {
      super(var1);
      this.setDirection(true);
   }

   public boolean sortOrder(int var1) {
      return true;
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      CUser var5 = (CUser)var2;
      CUser var6 = (CUser)var3;
      switch (var4) {
         case 0:
            if (var5.isOp() && var6.isOp()) {
               return this.compareStrings(var5.getNick(), var6.getNick());
            } else if (var5.isOp()) {
               return -1;
            } else if (var6.isOp()) {
               return 1;
            } else if (var5.hasVoice() && var6.hasVoice()) {
               return this.compareStrings(var5.getNick(), var6.getNick());
            } else if (var5.hasVoice()) {
               return -1;
            } else {
               if (var6.hasVoice()) {
                  return 1;
               }

               return this.compareStrings(var5.getNick(), var6.getNick());
            }
         default:
            return this.compareDefault((TableViewer)var1, this.columnIndex, var2, var3);
      }
   }
}
