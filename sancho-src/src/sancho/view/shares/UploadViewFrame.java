package sancho.view.shares;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.core.Sancho;
import sancho.utility.VersionInfo;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.BSpinner;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.TabbedViewFrame;

public class UploadViewFrame extends TabbedViewFrame {
   ShareDialog shareDialog;

   public UploadViewFrame(Composite var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4, "uploads");
      this.gView = new UploadTableView(this);
      this.createViewListener(new UploadViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("ti.u.unshare", "minus", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            UploadViewFrame.this.shareDialog = UploadViewFrame.this.new ShareDialog(
               UploadViewFrame.this.gView.getShell(), SResources.getString("l.unshareDirectory"), false
            );
            if (UploadViewFrame.this.shareDialog.open() == 0) {
               UploadViewFrame.this.sendShareCommand(false);
            }

            UploadViewFrame.this.shareDialog = null;
         }
      });
      this.addToolItem("ti.u.share", "plus", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            UploadViewFrame.this.shareDialog = UploadViewFrame.this.new ShareDialog(
               UploadViewFrame.this.gView.getShell(), SResources.getString("l.shareDirectory"), true
            );
            if (UploadViewFrame.this.shareDialog.open() == 0) {
               UploadViewFrame.this.sendShareCommand(true);
            }

            UploadViewFrame.this.shareDialog = null;
         }
      });
      this.addToolItem("ti.u.reshare", "rotate", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            if (Sancho.hasCollectionFactory()) {
               UploadViewFrame.this.getCore().getSharedFileCollection().reshare();
            }
         }
      });
      this.addToolSeparator();
      this.addRefine();
   }

   public void sendShareCommand(boolean var1) {
      if (this.shareDialog != null && !this.shareDialog.getDirectory().equals("") && Sancho.hasCollectionFactory()) {
         String var2 = var1 ? "share " + this.shareDialog.getPriority() : "unshare";
         var2 = var2 + " \"" + this.shareDialog.getDirectory() + "\"";
         if (var1) {
            var2 = var2 + " " + this.shareDialog.getStrategy();
         }

         Sancho.send((short)29, var2);
      }
   }

   // Modal dialog for sharing/unsharing a directory (with drag-and-drop support).
   private class ShareDialog extends Dialog {
      private int priority;
      private String directory;
      private boolean share;
      private BSpinner spinner;
      private Combo stratCombo;
      private Text dirText;
      private String title;
      public int ADD_ID;
      private String strategy;

      public ShareDialog(Shell var2, String var3, boolean var4) {
         super(var2);
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
         var3.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               Button var2 = (Button)var1.widget;
               DirectoryDialog var3 = new DirectoryDialog(var2.getShell(), 0);
               String var4;
               if ((var4 = var3.open()) != null) {
                  ShareDialog.this.dirText.setText(var4);
               }
            }
         });
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
            UploadViewFrame.this.sendShareCommand(this.share);
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

      private void activateDropTarget(final Text text) {
         DropTarget dropTarget = new DropTarget(text, 21);
         final TextTransfer textTransfer = TextTransfer.getInstance();
         final FileTransfer fileTransfer = FileTransfer.getInstance();
         dropTarget.setTransfer(new Transfer[]{fileTransfer, textTransfer});
         dropTarget.addDropListener(new DropTargetAdapter() {
            public void dragEnter(DropTargetEvent var1) {
               if (var1.detail == 16) {
                  if ((var1.operations & 1) != 0) {
                     var1.detail = 1;
                  } else {
                     var1.detail = 0;
                  }
               }

               for (int var2 = 0; var2 < var1.dataTypes.length; var2++) {
                  if (fileTransfer.isSupportedType(var1.dataTypes[var2])) {
                     var1.currentDataType = var1.dataTypes[var2];
                     if (var1.detail != 1) {
                        var1.detail = 0;
                     }
                     break;
                  }
               }
            }

            public void drop(DropTargetEvent var1) {
               if (textTransfer.isSupportedType(var1.currentDataType)) {
                  text.append((String)var1.data);
               }

               if (fileTransfer.isSupportedType(var1.currentDataType)) {
                  String[] var2 = (String[])var1.data;
                  if (var2.length > 1) {
                     for (int var3 = 0; var3 < var2.length; var3++) {
                        text.setText(var2[var3]);
                        ShareDialog.this.directory = var2[var3];
                        UploadViewFrame.this.sendShareCommand(ShareDialog.this.share);
                        text.setText("");
                     }
                  } else if (var2.length == 1) {
                     text.append(var2[0]);
                  }
               }
            }
         });
      }
   }
}
