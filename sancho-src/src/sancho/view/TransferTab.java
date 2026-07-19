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
   public TransferTab(MainWindow mainWindow, String name) {
      super(mainWindow, name);
   }

   protected void createContents(Composite composite) {
      String sashName = "transferSash";
      SashForm sashForm = WidgetFactory.createSashForm(composite, sashName);
      this.createDownloadsViews(sashForm);
      this.createUploadsView(sashForm);
      WidgetFactory.loadSashForm(sashForm, sashName);
   }

   private void createDownloadsViews(SashForm sashForm) {
      String sashName = "clientSash";
      SashForm childSashForm = WidgetFactory.createSashForm(sashForm, sashName);
      this.createDownloadsView(childSashForm);
      this.createClientsView(childSashForm);
      WidgetFactory.loadSashForm(childSashForm, sashName);
      if (childSashForm.getMaximizedControl() == null) {
         DownloadTreeView downloadTreeView = (DownloadTreeView)((ViewFrame)this.getViewFrameList().get(0)).getGView();
         downloadTreeView.updateClientsTable(true);
      }
   }

   private void createUploadsView(SashForm sashForm) {
      String sashName = "uploadsSash";
      SashForm childSashForm = WidgetFactory.createSashForm(sashForm, sashName);
      this.addViewFrame(new UploadersViewFrame(childSashForm, "l.uploaders", "up_arrow_blue", this));
      this.addViewFrame(new PendingViewFrame(childSashForm, "l.pending", "up_arrow_blue", this));
      WidgetFactory.loadSashForm(childSashForm, sashName);
   }

   private void createDownloadsView(SashForm sashForm) {
      this.addViewFrame(new DownloadViewFrame(sashForm, "l.downloads", "tab.transfers.buttonSmall", this));
   }

   private void createClientsView(SashForm sashForm) {
      DownloadTreeView downloadTreeView = (DownloadTreeView)((ViewFrame)this.getViewFrameList().get(0)).getGView();
      ClientViewFrame clientViewFrame = new ClientViewFrame(sashForm, "l.clients", "tab.transfers.buttonSmall", this, downloadTreeView);
      downloadTreeView.setClientTableView((ClientTableView)clientViewFrame.getGView());
      this.addViewFrame(clientViewFrame);
   }
}
