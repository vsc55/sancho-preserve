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

   public UploadViewFrame(Composite parent, String name, String text, AbstractTab tab) {
      super(parent, name, text, tab, "uploads");
      this.gView = new UploadTableView(this);
      this.createViewListener(new UploadViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("ti.u.unshare", "minus", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
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
         public void widgetSelected(SelectionEvent event) {
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
         public void widgetSelected(SelectionEvent event) {
            if (Sancho.hasCollectionFactory()) {
               UploadViewFrame.this.getCore().getSharedFileCollection().reshare();
            }
         }
      });
      this.addToolSeparator();
      this.addRefine();
   }

   public void sendShareCommand(boolean share) {
      if (this.shareDialog != null && !this.shareDialog.getDirectory().equals("") && Sancho.hasCollectionFactory()) {
         String command = share ? "share " + this.shareDialog.getPriority() : "unshare";
         command = command + " \"" + this.shareDialog.getDirectory() + "\"";
         if (share) {
            command = command + " " + this.shareDialog.getStrategy();
         }

         Sancho.send((short)29, command);
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

      public ShareDialog(Shell shell, String title, boolean share) {
         super(shell);
         this.ADD_ID = 999;
         this.share = share;
         this.title = title;
      }

      protected void configureShell(Shell shell) {
         super.configureShell(shell);
         shell.setImage(VersionInfo.getProgramIcon());
         shell.setText(this.title);
      }

      protected void createButtonsForButtonBar(Composite parent) {
         this.createButton(parent, this.ADD_ID, SResources.getString("b.okNoClose"), false);
         super.createButtonsForButtonBar(parent);
      }

      protected Control createDialogArea(Composite parent) {
         Composite composite = (Composite)super.createDialogArea(parent);
         composite.setLayout(WidgetFactory.createGridLayout(2, 10, 10, 10, 10, false));
         this.dirText = new Text(composite, 2048);
         this.dirText.setLayoutData(new GridData(768));
         this.activateDropTarget(this.dirText);
         Button button = new Button(composite, 0);
         button.setText(SResources.getString("b.browse"));
         button.setLayoutData(new GridData(128));
         button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               Button button = (Button)event.widget;
               DirectoryDialog directoryDialog = new DirectoryDialog(button.getShell(), 0);
               String path;
               if ((path = directoryDialog.open()) != null) {
                  ShareDialog.this.dirText.setText(path);
               }
            }
         });
         if (this.share) {
            GridData gridData = new GridData(768);
            gridData.horizontalSpan = 2;
            Composite subComposite = new Composite(composite, 0);
            subComposite.setLayout(WidgetFactory.createGridLayout(4, 0, 0, 10, 0, false));
            subComposite.setLayoutData(gridData);
            Label label = new Label(subComposite, 0);
            label.setText(SResources.getString("m.d.priority"));
            this.spinner = new BSpinner(subComposite, 2048);
            this.spinner.setMaximum(999);
            this.spinner.setMinimum(0);
            label = new Label(subComposite, 0);
            label.setText(SResources.getString("m.d.strategy"));
            this.stratCombo = new Combo(subComposite, 8);
            this.stratCombo.setItems(new String[]{"only_directory", "directories", "all_files", "mp3s", "avis", "incoming_files", "incoming_directories"});
            this.stratCombo.select(0);
         }

         return composite;
      }

      protected void buttonPressed(int buttonId) {
         if (this.share) {
            this.priority = this.spinner.getSelection();
            this.strategy = this.stratCombo.getText();
         }

         this.directory = this.dirText.getText();
         super.buttonPressed(buttonId);
         if (buttonId == this.ADD_ID) {
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
            public void dragEnter(DropTargetEvent event) {
               if (event.detail == 16) {
                  if ((event.operations & 1) != 0) {
                     event.detail = 1;
                  } else {
                     event.detail = 0;
                  }
               }

               for (int i = 0; i < event.dataTypes.length; i++) {
                  if (fileTransfer.isSupportedType(event.dataTypes[i])) {
                     event.currentDataType = event.dataTypes[i];
                     if (event.detail != 1) {
                        event.detail = 0;
                     }
                     break;
                  }
               }
            }

            public void drop(DropTargetEvent event) {
               if (textTransfer.isSupportedType(event.currentDataType)) {
                  text.append((String)event.data);
               }

               if (fileTransfer.isSupportedType(event.currentDataType)) {
                  String[] files = (String[])event.data;
                  if (files.length > 1) {
                     for (int i = 0; i < files.length; i++) {
                        text.setText(files[i]);
                        ShareDialog.this.directory = files[i];
                        UploadViewFrame.this.sendShareCommand(ShareDialog.this.share);
                        text.setText("");
                     }
                  } else if (files.length == 1) {
                     text.append(files[0]);
                  }
               }
            }
         });
      }
   }
}
