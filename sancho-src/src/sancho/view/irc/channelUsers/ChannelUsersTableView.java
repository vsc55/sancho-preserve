package sancho.view.irc.channelUsers;

import java.util.List;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Composite;
import sancho.view.irc.IRCClient;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class ChannelUsersTableView extends GTableView {
   public static final int NAME = 0;

   public ChannelUsersTableView(ViewFrame var1) {
      super(var1);
      this.preferenceString = "channelUsers";
      this.columnLabels = new String[]{"channelUsers.name"};
      this.columnAlignment = new int[]{16384};
      this.columnDefaultWidths = new int[]{150};
      this.gSorter = new ChannelUsersTableSorter(this);
      this.tableContentProvider = new ChannelUsersTableContentProvider(this);
      this.tableLabelProvider = new ChannelUsersTableLabelProvider(this);
      this.tableMenuListener = new ChannelUsersTableMenuListener(this);
      this.createContents(var1.getChildComposite());
   }

   public void setIRCClient(IRCClient var1) {
      ((ChannelUsersTableMenuListener)this.getTableMenuListener()).setIRCClient(var1);
   }

   protected void createContents(Composite var1) {
      super.createContents(var1);
      this.sViewer.addSelectionChangedListener((ChannelUsersTableMenuListener)this.tableMenuListener);
      this.addMenuListener();
   }

   public void setInput() {
   }

   public void setInput(List var1) {
      this.getTable().getDisplay().asyncExec(new ChannelUsersTableView$1(this, var1));
   }

   public void add(Object var1) {
      this.getTable().getDisplay().syncExec(new ChannelUsersTableView$2(this, var1));
   }

   public void remove(Object var1) {
      this.getTable().getDisplay().syncExec(new ChannelUsersTableView$3(this, var1));
   }

   public void update(Object var1) {
      this.getTable().getDisplay().syncExec(new ChannelUsersTableView$4(this, var1));
   }

   public void updateHeader() {
      this.getViewFrame().updateCLabelText(SResources.getString("l.users") + ": " + this.getTable().getItemCount());
   }

   // $VF: synthetic method
   static StructuredViewer access$000(ChannelUsersTableView var0) {
      return var0.sViewer;
   }

   // $VF: synthetic method
   static StructuredViewer access$100(ChannelUsersTableView var0) {
      return var0.sViewer;
   }

   // $VF: synthetic method
   static StructuredViewer access$200(ChannelUsersTableView var0) {
      return var0.sViewer;
   }

   // $VF: synthetic method
   static StructuredViewer access$300(ChannelUsersTableView var0) {
      return var0.sViewer;
   }
}
