package sancho.view.shares;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.utility.VersionInfo;
import sancho.view.utility.BSpinner;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

class UploadViewFrame$ShareDialog extends Dialog {
   private int priority;
   private String directory;
   private boolean share;
   private BSpinner spinner;
   private Combo stratCombo;
   private Text dirText;
   private String title;
   public int ADD_ID;
   private String strategy;
   // $VF: synthetic field
   private final UploadViewFrame this$0;

   public UploadViewFrame$ShareDialog(UploadViewFrame var1, Shell var2, String var3, boolean var4) {
      super(var2);
      this.this$0 = var1;
      this.ADD_ID = 999;
      this.share = var4;
      this.title = var3;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
      var1.setText(this.title);
   }

   protected void createButtonsForButtonBar(Composite var1) {
      this.createButton(var1, this.ADD_ID, SResources.getString("b.okNoClose"), false);
      super.createButtonsForButtonBar(var1);
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(2, 10, 10, 10, 10, false));
      this.dirText = new Text(var2, 2048);
      this.dirText.setLayoutData(new GridData(768));
      this.activateDropTarget(this.dirText);
      Button var3 = new Button(var2, 0);
      var3.setText(SResources.getString("b.browse"));
      var3.setLayoutData(new GridData(128));
      var3.addSelectionListener(new UploadViewFrame$4(this));
      if (this.share) {
         GridData var4 = new GridData(768);
         var4.horizontalSpan = 2;
         Composite var5 = new Composite(var2, 0);
         var5.setLayout(WidgetFactory.createGridLayout(4, 0, 0, 10, 0, false));
         var5.setLayoutData(var4);
         Label var6 = new Label(var5, 0);
         var6.setText(SResources.getString("m.d.priority"));
         this.spinner = new BSpinner(var5, 2048);
         this.spinner.setMaximum(999);
         this.spinner.setMinimum(0);
         var6 = new Label(var5, 0);
         var6.setText(SResources.getString("m.d.strategy"));
         this.stratCombo = new Combo(var5, 8);
         this.stratCombo.setItems(new String[]{"only_directory", "directories", "all_files", "mp3s", "avis", "incoming_files", "incoming_directories"});
         this.stratCombo.select(0);
      }

      return var2;
   }

   protected void buttonPressed(int var1) {
      if (this.share) {
         this.priority = this.spinner.getSelection();
         this.strategy = this.stratCombo.getText();
      }

      this.directory = this.dirText.getText();
      super.buttonPressed(var1);
      if (var1 == this.ADD_ID) {
         this.this$0.sendShareCommand(this.share);
         this.dirText.setText("");
      }
   }

   public String getStrategy() {
      return this.strategy;
   }

   public String getDirectory() {
      return this.directory;
   }

   public int getPriority() {
      return this.priority;
   }

   private void activateDropTarget(Text var1) {
      DropTarget var2 = new DropTarget(var1, 21);
      TextTransfer var3 = TextTransfer.getInstance();
      FileTransfer var4 = FileTransfer.getInstance();
      var2.setTransfer(new Transfer[]{var4, var3});
      var2.addDropListener(new UploadViewFrame$5(this, var4, var3, var1));
   }

   // $VF: synthetic method
   static Text access$200(UploadViewFrame$ShareDialog var0) {
      return var0.dirText;
   }

   // $VF: synthetic method
   static String access$302(UploadViewFrame$ShareDialog var0, String var1) {
      return var0.directory = var1;
   }

   // $VF: synthetic method
   static boolean access$400(UploadViewFrame$ShareDialog var0) {
      return var0.share;
   }

   // $VF: synthetic method
   static UploadViewFrame access$500(UploadViewFrame$ShareDialog var0) {
      return var0.this$0;
   }
}
