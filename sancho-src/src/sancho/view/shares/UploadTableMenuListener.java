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

   public UploadTableMenuListener(GTableView view) {
      super(view);
   }

   protected void onDeleteKey() {
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$SharedFile == null
            ? (class$sancho$model$mldonkey$SharedFile = class$("sancho.model.mldonkey.SharedFile"))
            : class$sancho$model$mldonkey$SharedFile
      );
   }

   protected void sendToStatusline(String text) {
      if (text != null) {
         this.gView.getViewFrame().getATab().getMainWindow().getStatusline().setText(text);
      }
   }

   public void menuAboutToShow(IMenuManager menu) {
      if (this.selectedObjects.size() > 0) {
         SharedFile sharedFile = (SharedFile)this.selectedObjects.get(0);
         if (sharedFile.getSubFileNames() != null) {
            menu.add(new SharedFileDetailAction());
         }

         if (Sancho.hasCollectionFactory() && Sancho.getCore().getProtocol() >= 34) {
            Network network = Sancho.getCore().getNetworkCollection().getByEnum(EnumNetwork.BT);
            if (network != null) {
               menu.add(new ComputeTorrentAction());
            }
         }

         IPreview[] previews = new IPreview[this.selectedObjects.size()];
         int[] ids = new int[this.selectedObjects.size()];

         for (int i = 0; i < this.selectedObjects.size(); i++) {
            previews[i] = (IPreview)this.selectedObjects.get(i);
            ids[i] = -1;
         }

         this.addPreview(menu, previews, ids);
         this.addClipboardMenu(menu);
         this.addWebServicesMenu(menu, sharedFile.getMD4(), sharedFile.getED2K(), sharedFile.getSize());
         this.addSelectAllMenu(menu);
      }
   }

   // $VF: synthetic method
   static Class class$(String name) {
      try {
         return Class.forName(name);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }

   // JFace Action: shows the detail dialog for the first selected shared file
   private class SharedFileDetailAction extends Action {
      public SharedFileDetailAction() {
         super(SResources.getString("m.d.fileDetails"));
         this.setImageDescriptor(SResources.getImageDescriptor("info"));
      }

      public void run() {
         SharedFile sharedFile = (SharedFile)selectedObjects.get(0);
         if (sharedFile != null) {
            new SharedFileDetailDialog(gView.getShell(), sharedFile).open();
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
         for (int i = 0; i < selectedObjects.size(); i++) {
            ComputeTorrentDialog dialog = new ComputeTorrentDialog(gView.getShell());
            if (dialog.open() == 0) {
               SharedFile sharedFile = (SharedFile)selectedObjects.get(i);
               sharedFile.computeTorrent(dialog.getTracker(), dialog.getComment());
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

      public ComputeTorrentDialog(Shell shell) {
         super(shell);
      }

      protected void configureShell(Shell shell) {
         super.configureShell(shell);
         shell.setImage(VersionInfo.getProgramIcon());
         shell.setText(SResources.getString("l.computeTorrent"));
      }

      public NoDuplicatesCombo createCombo(Composite parent, String name) {
         NoDuplicatesCombo combo = new NoDuplicatesCombo(parent, 2052);
         if (PreferenceLoader.loadBoolean("searchSaveEntries")) {
            combo.setItems(PreferenceLoader.loadStringArray(name + ".sArray"));
         }

         GridData gridData = new GridData(768);
         gridData.widthHint = 200;
         combo.setLayoutData(gridData);
         return combo;
      }

      protected Control createDialogArea(Composite parent) {
         Composite composite = (Composite)super.createDialogArea(parent);
         composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         Label label = new Label(composite, 0);
         label.setText(SResources.getString("l.tracker"));
         this.trackerCombo = this.createCombo(composite, "tracker");
         label = new Label(composite, 0);
         label.setText(SResources.getString("l.comment"));
         this.commentCombo = this.createCombo(composite, "comment");
         if (Sancho.hasCollectionFactory()) {
            Option option = (Option)Sancho.getCore().getCollectionFactory().getOptionCollection().get("BT-default_tracker");
            if (option != null) {
               this.trackerCombo.setText(option.getValue());
            }

            option = (Option)Sancho.getCore().getCollectionFactory().getOptionCollection().get("BT-default_comment");
            if (option != null) {
               this.commentCombo.setText(option.getValue());
            }
         }

         return composite;
      }

      protected void buttonPressed(int buttonId) {
         this.tracker = this.trackerCombo.getText();
         this.comment = this.commentCombo.getText();
         if (buttonId == 0) {
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

         super.buttonPressed(buttonId);
      }

      public String getTracker() {
         return this.tracker;
      }

      public String getComment() {
         return this.comment;
      }
   }
}
