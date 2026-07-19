package sancho.view.shares;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.model.mldonkey.IPreview;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.Option;
import sancho.model.mldonkey.SharedFile;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.NoDuplicatesCombo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewer.table.GTableMenuListenerClient;
import sancho.view.viewer.table.GTableView;

public class UploadTableMenuListener extends GTableMenuListenerClient {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$SharedFile;

   public UploadTableMenuListener(GTableView var1) {
      super(var1);
   }

   protected void onDeleteKey() {
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$SharedFile == null
            ? (class$sancho$model$mldonkey$SharedFile = class$("sancho.model.mldonkey.SharedFile"))
            : class$sancho$model$mldonkey$SharedFile
      );
   }

   protected void sendToStatusline(String var1) {
      if (var1 != null) {
         this.gView.getViewFrame().getATab().getMainWindow().getStatusline().setText(var1);
      }
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         SharedFile var2 = (SharedFile)this.selectedObjects.get(0);
         if (var2.getSubFileNames() != null) {
            var1.add(new SharedFileDetailAction());
         }

         if (Sancho.hasCollectionFactory() && Sancho.getCore().getProtocol() >= 34) {
            Network var3 = Sancho.getCore().getNetworkCollection().getByEnum(EnumNetwork.BT);
            if (var3 != null) {
               var1.add(new ComputeTorrentAction());
            }
         }

         IPreview[] var6 = new IPreview[this.selectedObjects.size()];
         int[] var4 = new int[this.selectedObjects.size()];

         for (int var5 = 0; var5 < this.selectedObjects.size(); var5++) {
            var6[var5] = (IPreview)this.selectedObjects.get(var5);
            var4[var5] = -1;
         }

         this.addPreview(var1, var6, var4);
         this.addClipboardMenu(var1);
         this.addWebServicesMenu(var1, var2.getMD4(), var2.getED2K(), var2.getSize());
         this.addSelectAllMenu(var1);
      }
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   // JFace Action: shows the detail dialog for the first selected shared file
   private class SharedFileDetailAction extends Action {
      public SharedFileDetailAction() {
         super(SResources.getString("m.d.fileDetails"));
         this.setImageDescriptor(SResources.getImageDescriptor("info"));
      }

      public void run() {
         SharedFile var1 = (SharedFile)selectedObjects.get(0);
         if (var1 != null) {
            new SharedFileDetailDialog(gView.getShell(), var1).open();
         }
      }
   }

   // JFace Action: prompts for tracker/comment and computes a torrent per selected shared file
   private class ComputeTorrentAction extends Action {
      public ComputeTorrentAction() {
         super(SResources.getString("l.computeTorrent"));
         this.setImageDescriptor(EnumNetwork.BT.getImageDescriptor());
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            ComputeTorrentDialog var2 = new ComputeTorrentDialog(gView.getShell());
            if (var2.open() == 0) {
               SharedFile var3 = (SharedFile)selectedObjects.get(var1);
               var3.computeTorrent(var2.getTracker(), var2.getComment());
            }
         }
      }
   }

   // Dialog: collects tracker and comment values used when computing a torrent
   private static class ComputeTorrentDialog extends Dialog {
      String tracker;
      String comment;
      NoDuplicatesCombo trackerCombo;
      NoDuplicatesCombo commentCombo;

      public ComputeTorrentDialog(Shell var1) {
         super(var1);
      }

      protected void configureShell(Shell var1) {
         super.configureShell(var1);
         var1.setImage(VersionInfo.getProgramIcon());
         var1.setText(SResources.getString("l.computeTorrent"));
      }

      public NoDuplicatesCombo createCombo(Composite var1, String var2) {
         NoDuplicatesCombo var3 = new NoDuplicatesCombo(var1, 2052);
         if (PreferenceLoader.loadBoolean("searchSaveEntries")) {
            var3.setItems(PreferenceLoader.loadStringArray(var2 + ".sArray"));
         }

         GridData var4 = new GridData(768);
         var4.widthHint = 200;
         var3.setLayoutData(var4);
         return var3;
      }

      protected Control createDialogArea(Composite var1) {
         Composite var2 = (Composite)super.createDialogArea(var1);
         var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         Label var3 = new Label(var2, 0);
         var3.setText(SResources.getString("l.tracker"));
         this.trackerCombo = this.createCombo(var2, "tracker");
         var3 = new Label(var2, 0);
         var3.setText(SResources.getString("l.comment"));
         this.commentCombo = this.createCombo(var2, "comment");
         if (Sancho.hasCollectionFactory()) {
            Option var4 = (Option)Sancho.getCore().getCollectionFactory().getOptionCollection().get("BT-default_tracker");
            if (var4 != null) {
               this.trackerCombo.setText(var4.getValue());
            }

            var4 = (Option)Sancho.getCore().getCollectionFactory().getOptionCollection().get("BT-default_comment");
            if (var4 != null) {
               this.commentCombo.setText(var4.getValue());
            }
         }

         return var2;
      }

      protected void buttonPressed(int var1) {
         this.tracker = this.trackerCombo.getText();
         this.comment = this.commentCombo.getText();
         if (var1 == 0) {
            this.trackerCombo.add(this.trackerCombo.getText(), 0);
            this.commentCombo.add(this.commentCombo.getText(), 0);
            if (PreferenceLoader.loadBoolean("searchSaveEntries")) {
               PreferenceLoader.setValue("tracker.sArray", this.trackerCombo.getItems(), 25);
               PreferenceLoader.setValue("comment.sArray", this.commentCombo.getItems(), 25);
            } else {
               PreferenceLoader.getPreferenceStore().setValue("tracker.sArray", "");
               PreferenceLoader.getPreferenceStore().setValue("comment.sArray", "");
            }
         }

         super.buttonPressed(var1);
      }

      public String getTracker() {
         return this.tracker;
      }

      public String getComment() {
         return this.comment;
      }
   }
}
