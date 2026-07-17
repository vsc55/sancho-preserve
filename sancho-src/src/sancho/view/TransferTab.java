package sancho.view;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import sancho.view.transfer.clients.ClientTableView;
import sancho.view.transfer.clients.ClientViewFrame;
import sancho.view.transfer.downloads.DownloadTreeView;
import sancho.view.transfer.downloads.DownloadViewFrame;
import sancho.view.transfer.pending.PendingViewFrame;
import sancho.view.transfer.uploaders.UploadersViewFrame;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.ViewFrame;

public class TransferTab extends AbstractTab {
   public TransferTab(MainWindow var1, String var2) {
      super(var1, var2);
   }

   protected void createContents(Composite var1) {
      String var2 = "transferSash";
      SashForm var3 = WidgetFactory.createSashForm(var1, var2);
      this.createDownloadsViews(var3);
      this.createUploadsView(var3);
      WidgetFactory.loadSashForm(var3, var2);
   }

   private void createDownloadsViews(SashForm var1) {
      String var2 = "clientSash";
      SashForm var3 = WidgetFactory.createSashForm(var1, var2);
      this.createDownloadsView(var3);
      this.createClientsView(var3);
      WidgetFactory.loadSashForm(var3, var2);
      if (var3.getMaximizedControl() == null) {
         DownloadTreeView var4 = (DownloadTreeView)((ViewFrame)this.getViewFrameList().get(0)).getGView();
         var4.updateClientsTable(true);
      }
   }

   private void createUploadsView(SashForm var1) {
      String var2 = "uploadsSash";
      SashForm var3 = WidgetFactory.createSashForm(var1, var2);
      this.addViewFrame(new UploadersViewFrame(var3, "l.uploaders", "up_arrow_blue", this));
      this.addViewFrame(new PendingViewFrame(var3, "l.pending", "up_arrow_blue", this));
      WidgetFactory.loadSashForm(var3, var2);
   }

   private void createDownloadsView(SashForm var1) {
      this.addViewFrame(new DownloadViewFrame(var1, "l.downloads", "tab.transfers.buttonSmall", this));
   }

   private void createClientsView(SashForm var1) {
      DownloadTreeView var2 = (DownloadTreeView)((ViewFrame)this.getViewFrameList().get(0)).getGView();
      ClientViewFrame var3 = new ClientViewFrame(var1, "l.clients", "tab.transfers.buttonSmall", this, var2);
      var2.setClientTableView((ClientTableView)var3.getGView());
      this.addViewFrame(var3);
   }
}
