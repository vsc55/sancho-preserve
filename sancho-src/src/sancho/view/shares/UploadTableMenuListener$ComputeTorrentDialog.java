package sancho.view.shares;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.model.mldonkey.Option;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.NoDuplicatesCombo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

class UploadTableMenuListener$ComputeTorrentDialog extends Dialog {
   String tracker;
   String comment;
   NoDuplicatesCombo trackerCombo;
   NoDuplicatesCombo commentCombo;

   public UploadTableMenuListener$ComputeTorrentDialog(Shell var1) {
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
