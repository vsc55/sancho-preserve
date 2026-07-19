package sancho.view.transfer.downloads;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CustomTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.model.mldonkey.enums.EnumFormat;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.enums.EnumPriority;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.ClientDetailDialog;
import sancho.view.transfer.FileClient;
import sancho.view.transfer.FileDetailDialog;
import sancho.view.transfer.UniformResourceLocator;
import sancho.view.utility.CSpinner;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewer.GView;
import sancho.view.viewer.table.GTableMenuListenerClient;

public class DownloadTreeMenuListener extends GTableMenuListenerClient implements IDoubleClickListener {
   private File selectedFile;
   private List selectedClients = new ArrayList();
   private GView clientView;
   private boolean clientTableVisible = false;
   private boolean myDrag = false;
   public static final int FDBLCLICK_EXPAND = 0;
   public static final int FDBLCLICK_PREVIEW = 1;
   public static final int FDBLCLICK_PREVIEW_OS = 2;

   public DownloadTreeMenuListener(DownloadTreeView var1) {
      super(var1);
   }

   public void setClientView(GView var1) {
      this.clientView = var1;
   }

   protected void sendToStatusline(String var1) {
      if (var1 != null) {
         this.gView.getViewFrame().getATab().getMainWindow().getStatusline().setText(var1);
      }
   }

   public void doubleClick(DoubleClickEvent var1) {
      IStructuredSelection var2 = (IStructuredSelection)var1.getSelection();
      Object var3 = var2.getFirstElement();
      CustomTreeViewer var4 = (CustomTreeViewer)this.gView.getViewer();
      if (var3 instanceof File) {
         File var5 = (File)var3;
         switch (PreferenceLoader.loadInt("dlFileDoubleClick")) {
            case 2:
               Program var6 = this.selectedFile.getOSPreviewApp();
               if (var6 != null) {
                  this.sendToStatusline(var5.preview(var6, -1));
                  break;
               }
            case 1:
               this.sendToStatusline(var5.preview(-1));
               break;
            default:
               if (var4.getExpandedState(var5)) {
                  var4.collapseToLevel(var5, -1);
               } else {
                  var4.expandToLevel(var5, -1);
               }
         }
      } else if (var3 instanceof FileClient && Sancho.hasCollectionFactory()) {
         FileClient var7 = (FileClient)var3;
         ClientDetailDialog var8 = new ClientDetailDialog(this.gView.getShell(), var7.getFile(), var7.getClient());
         var8.open();
      }
   }

   public void initialize() {
      super.initialize();
      if (PreferenceLoader.loadBoolean("dragAndDrop")) {
         this.activateDragAndDrop();
      }
   }

   public void deselectAll() {
      super.deselectAll();
      this.selectedClients.clear();
      this.selectedFile = null;
      if (this.clientTableVisible) {
         this.clientView.getViewer().setInput(null);
      }
   }

   public void activateDragAndDrop() {
      byte var1 = 23;
      DragSource var2 = new DragSource(this.gView.getComposite(), var1);
      var2.setTransfer(new Transfer[]{TextTransfer.getInstance()});
      var2.addDragListener(new DragSourceAdapter() {
         public void dragStart(DragSourceEvent var1) {
            if (selectedFile == null) {
               var1.doit = false;
            } else {
               var1.doit = true;
               myDrag = true;
            }
         }

         public void dragSetData(DragSourceEvent var1) {
            var1.data = selectedFile.getED2K();
         }

         public void dragFinished(DragSourceEvent var1) {
            myDrag = false;
         }
      });
      DropTarget var3 = new DropTarget(this.gView.getComposite(), var1);
      UniformResourceLocator var4 = UniformResourceLocator.getInstance();
      TextTransfer var5 = TextTransfer.getInstance();
      FileTransfer var6 = FileTransfer.getInstance();
      var3.setTransfer(new Transfer[]{var4, var5, var6});
      var3.addDropListener(new DropTargetAdapter() {
         public void dragEnter(DropTargetEvent var1) {
            if (var1.detail == 16) {
               if ((var1.operations & 4) != 0) {
                  var1.detail = 4;
               } else if ((var1.operations & 1) != 0) {
                  var1.detail = 1;
               } else if ((var1.operations & 2) != 0) {
                  var1.detail = 2;
               } else {
                  var1.detail = 0;
               }
            }
         }

         public void drop(DropTargetEvent var1) {
            if (var1.data != null && !myDrag) {
               if (var5.isSupportedType(var1.currentDataType) || var4.isSupportedType(var1.currentDataType)) {
                  SwissArmy.sendLink(gView.getCore(), (String)var1.data);
               }

               if (var6.isSupportedType(var1.currentDataType)) {
                  String[] var2 = (String[])var1.data;

                  for (int var3 = 0; var3 < var2.length; var3++) {
                     SwissArmy.sendLink(gView.getCore(), var2[var3]);
                  }
               }
            }
         }
      });
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      IStructuredSelection var2 = (IStructuredSelection)var1.getSelection();
      this.selectedClients.clear();
      this.selectedObjects.clear();

      for (Object var4 : var2) {
         if (var4 instanceof File) {
            this.selectedObjects.add(var4);
         } else if (var4 instanceof FileClient) {
            this.selectedClients.add(var4);
         }
      }

      if (this.selectedObjects.size() > 0) {
         this.selectedFile = (File)this.selectedObjects.get(0);
         if (this.clientTableVisible && this.clientView.getViewer().getInput() != this.selectedFile) {
            this.clientView.getViewer().setInput(this.selectedFile);
         }
      } else {
         this.selectedFile = null;
      }
   }

   public void updateClientsTable(boolean var1) {
      if (var1) {
         if (this.clientTableVisible != var1 && this.selectedFile != null) {
            this.clientView.getViewer().setInput(this.selectedFile);
         }
      } else {
         this.clientView.getViewer().setInput(null);
      }

      this.clientTableVisible = var1;
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedFile != null && this.selectedFileListContains(EnumFileState.DOWNLOADED)) {
         var1.add(new CommitAction());
      }

      if (this.selectedFile != null && this.selectedFile.getFileStateEnum() == EnumFileState.DOWNLOADED) {
         MyMenuManager var2 = new MyMenuManager(SResources.getString("m.d.commitAs"));
         var2.setImageString("commit");
         var2.add(new CommitAction(true));
         int var3 = PreferenceLoader.loadInt("maxMenuItems");

         for (int var4 = 0; var4 < this.selectedFile.getNames().length && var4 < var3; var4++) {
            var2.add(new CommitAction(this.selectedFile.getNames()[var4]));
         }

         var1.add(var2);
      }

      if (this.selectedFile != null) {
         var1.add(new FileDetailAction());
         if (this.selectedFile.hasFileComments()) {
            var1.add(new FileCommentsAction());
         }
      }

      if (this.selectedFile != null && this.selectedFile.getEnumNetwork() == EnumNetwork.FILETP) {
         var1.add(new AddMirrorAction(this.selectedFile));
      }

      if (this.selectedFile != null && (this.selectedFileListContains(EnumFileState.DOWNLOADING) || this.selectedFileListContains(EnumFileState.QUEUED))) {
         var1.add(new PauseAction());
      }

      if (this.selectedFile != null && this.selectedFileListContains(EnumFileState.PAUSED)) {
         var1.add(new ResumeAction());
      }

      if (this.selectedFile != null && this.selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED)) {
         var1.add(new CancelAction());
      }

      if (this.selectedFile != null && this.selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED)) {
         MyMenuManager var6 = new MyMenuManager(SResources.getString("m.d.priority"));
         var6.setImageString("priority");
         var6.add(new PriorityAction(EnumPriority.VERY_HIGH));
         var6.add(new PriorityAction(EnumPriority.HIGH));
         var6.add(new PriorityAction(EnumPriority.NORMAL));
         var6.add(new PriorityAction(EnumPriority.LOW));
         var6.add(new PriorityAction(EnumPriority.VERY_LOW));
         var6.add(new Separator());
         var6.add(new CustomPriorityAction(false));
         var6.add(new CustomPriorityAction(true));
         var1.add(var6);
      }

      if (this.selectedFile != null && (this.selectedFile.getDownloaded() > 0L || this.selectedFile.getFileStateEnum() == EnumFileState.DOWNLOADED)) {
         File[] var7 = new File[]{this.selectedFile};
         int[] var11 = new int[]{-1};
         this.addPreview(var1, var7, var11);
      }

      if (this.selectedFile != null && this.selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED)) {
         MyMenuManager var8 = new MyMenuManager(SResources.getString("m.d.rename"));
         var8.setImageString("rename");
         var8.add(new RenameAction(true));
         int var12 = PreferenceLoader.loadInt("maxMenuItems");

         for (int var14 = 0; var14 < this.selectedFile.getNames().length && var14 < var12; var14++) {
            var8.add(new RenameAction(this.selectedFile.getNames()[var14]));
         }

         var1.add(var8);
      }

      if (this.selectedFile != null) {
         if (this.selectedFile.getFormat().getFormat() == EnumFormat.MP3) {
            var1.add(new EditMP3TagsAction());
         }

         var1.add(new ConnectAllAction());
         var1.add(new VerifyChunksAction());
         var1.add(new RequestFileInfoAction());
      }

      if (this.selectedClients.size() > 0) {
         FileClient var9 = (FileClient)this.selectedClients.get(0);
         Client[] var13 = new Client[this.selectedClients.size()];

         for (int var15 = 0; var15 < this.selectedClients.size(); var15++) {
            FileClient var5 = (FileClient)this.selectedClients.get(var15);
            var13[var15] = var5.getClient();
         }

         this.addClientActions(this.gView.getShell(), var9.getFile(), var1, var13);
      }

      if (this.selectedFile != null) {
         var1.add(new SearchSimilarAction());
      }

      if (this.selectedFile != null) {
         this.addClipboardMenu(var1);
      }

      if (this.selectedFile != null && this.gView.getCore().getProtocol() > 40) {
         MyMenuManager var10 = new MyMenuManager(SResources.getString("m.d.userGroup"));
         var10.setImageString("user");
         var10.add(new ChownAction());
         var10.add(new ChgrpAction());
         var1.add(var10);
      }

      if (this.selectedFile != null) {
         this.addWebServicesMenu(var1, this.selectedFile.getHash(), this.selectedFile.getED2K(), this.selectedFile.getSize());
      }

      if (this.selectedFile != null || this.selectedClients.size() > 0) {
         this.addSelectAllMenu(var1);
      }
   }

   private boolean selectedFileListContains(EnumFileState var1) {
      for (int var2 = 0; var2 < this.selectedObjects.size(); var2++) {
         if (((File)this.selectedObjects.get(var2)).getFileStateEnum() == var1) {
            return true;
         }
      }

      return false;
   }

   private boolean selectedFileListContainsOtherThan(EnumFileState var1) {
      for (int var2 = 0; var2 < this.selectedObjects.size(); var2++) {
         if (((File)this.selectedObjects.get(var2)).getFileStateEnum() != var1) {
            return true;
         }
      }

      return false;
   }

   private void cancelSelectedFiles() {
      MessageBox var1 = new MessageBox(this.gView.getShell(), 196);
      var1.setMessage(SResources.getString("m.d.reallyCancel") + (this.selectedObjects.size() > 1 ? " (" + this.selectedObjects.size() + " selected)" : ""));
      int var2 = var1.open();
      if (var2 == 64) {
         for (int var3 = 0; var3 < this.selectedObjects.size(); var3++) {
            File var4 = (File)this.selectedObjects.get(var3);
            if (var4.getFileStateEnum() != EnumFileState.DOWNLOADED) {
               var4.setState(EnumFileState.CANCELLED);
            }
         }

         this.deselectAll();
      }
   }

   private void renameSelectedFiles() {
      for (int var1 = 0; var1 < this.selectedObjects.size(); var1++) {
         File var2 = (File)this.selectedObjects.get(var1);
         InputDialog var3 = new InputDialog(this.gView.getShell(), SResources.getString("m.d.rename"), SResources.getString("m.d.rename"), var2.getName(), null);
         if (var3.open() != 0) {
            break;
         }

         String var4 = var3.getValue();
         if (!var4.equals("") && var2 != null) {
            var2.rename(var4);
         }
      }
   }

   protected void onDeleteKey() {
      if (this.selectedFile != null) {
         this.cancelSelectedFiles();
      }
   }

   protected void onF2Key() {
      if (this.selectedFile != null) {
         this.renameSelectedFiles();
      }
   }

   // Menu action: adds a mirror URL for a FileTP download.
   private class AddMirrorAction extends Action {
      File file;

      public AddMirrorAction(File var2) {
         super(SResources.getString("dd.f.addMirror"));
         this.setImageDescriptor(SResources.getImageDescriptor("plus"));
         this.file = var2;
      }

      public void run() {
         InputDialog var1 = new InputDialog(
            gView.getShell(),
            SResources.getString("dd.f.addMirror"),
            SResources.getString("dd.f.addMirrorInfo"),
            "",
            null
         );
         var1.open();
         String var2 = var1.getValue();
         if (var2 != null) {
            Sancho.send((short)29, "mirror " + this.file.getId() + " " + var2);
         }
      }
   }

   // Menu action: cancels the selected downloads after confirmation.
   private class CancelAction extends Action {
      public CancelAction() {
         super(SResources.getString("m.d.cancel"));
         this.setImageDescriptor(SResources.getImageDescriptor("cancel"));
      }

      public void run() {
         cancelSelectedFiles();
      }
   }

   // Menu action: changes the group of each selected file.
   private class ChgrpAction extends Action {
      public ChgrpAction() {
         super("chgrp");
         this.setImageDescriptor(SResources.getImageDescriptor("group"));
      }

      public void run() {
         InputDialog var1 = new InputDialog(gView.getShell(), "chgrp", "chgrp", "", null);
         var1.open();
         String var2 = var1.getValue();

         for (int var3 = 0; var3 < selectedObjects.size(); var3++) {
            File var4 = (File)selectedObjects.get(var3);
            var4.chgrp(var2);
         }
      }
   }

   // Menu action: changes the owner of each selected file.
   private class ChownAction extends Action {
      public ChownAction() {
         super("chown");
         this.setImageDescriptor(SResources.getImageDescriptor("user"));
      }

      public void run() {
         InputDialog var1 = new InputDialog(gView.getShell(), "chown", "chown", "", null);
         var1.open();
         String var2 = var1.getValue();

         for (int var3 = 0; var3 < selectedObjects.size(); var3++) {
            File var4 = (File)selectedObjects.get(var3);
            var4.chown(var2);
         }
      }
   }

   // Menu action: commits (saves) downloaded files, optionally under a chosen name.
   private class CommitAction extends Action {
      private String commitAs;
      private boolean manualInput;

      public CommitAction() {
         super(SResources.getString("m.d.commitSelected"));
         this.manualInput = false;
         this.setImageDescriptor(SResources.getImageDescriptor("commit"));
      }

      public CommitAction(String var2) {
         super(var2);
         this.manualInput = false;
         this.setImageDescriptor(SResources.getImageDescriptor("commit"));
         this.commitAs = var2;
      }

      public CommitAction(boolean var2) {
         super(SResources.getString("m.d.commitInput"));
         this.manualInput = false;
         this.setImageDescriptor(SResources.getImageDescriptor("commit_question"));
         this.manualInput = var2;
      }

      public void run() {
         if (this.commitAs == null && !this.manualInput) {
            for (int var3 = 0; var3 < selectedObjects.size(); var3++) {
               File var4 = (File)selectedObjects.get(var3);
               if (var4.getFileStateEnum() == EnumFileState.DOWNLOADED) {
                  var4.saveFileAs(var4.getName());
               }
            }
         } else if (this.manualInput) {
            if (selectedFile == null) {
               return;
            }

            InputDialog var1 = new InputDialog(
               gView.getShell(),
               SResources.getString("m.d.commitAs"),
               SResources.getString("m.d.commitAs"),
               selectedFile.getName(),
               null
            );
            if (var1.open() == 0) {
               String var2 = var1.getValue();
               if (!var2.equals("") && selectedFile != null) {
                  selectedFile.saveFileAs(var2);
               }
            }
         } else if (selectedFile != null) {
            selectedFile.saveFileAs(this.commitAs);
         }
      }
   }

   // Menu action: connects to all sources for each selected file.
   private class ConnectAllAction extends Action {
      public ConnectAllAction() {
         super(SResources.getString("m.d.connectAll"));
         this.setImageDescriptor(SResources.getImageDescriptor("menu-connect"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            ((File)selectedObjects.get(var1)).connectAll();
         }
      }
   }

   // Menu action: opens a dialog to set an absolute or relative custom priority.
   private class CustomPriorityAction extends Action {
      private boolean relative;

      public CustomPriorityAction(boolean var2) {
         super("", 2);
         this.relative = var2;
         if (var2) {
            this.setText(SResources.getString("m.d.priorityRelative"));
         } else {
            this.setText(SResources.getString("m.d.priorityAbsolute"));
         }
      }

      public void run() {
         String var1 = SResources.getString("m.d.priority")
            + " ("
            + (this.relative ? SResources.getString("m.d.priorityRelative") : SResources.getString("m.d.priorityAbsolute"))
            + ")";
         PriorityInputDialog var2 = new PriorityInputDialog(
            gView.getShell(), var1, this.relative ? 0 : selectedFile.getPriority()
         );
         if (var2.open() == 0) {
            int var3 = var2.getIntValue();
            int var4 = var2.getIncIntValue();

            for (int var5 = 0; var5 < selectedObjects.size(); var5++) {
               File var6 = (File)selectedObjects.get(var5);
               if (var6.getFileStateEnum() != EnumFileState.DOWNLOADED) {
                  var6.sendPriority(this.relative, var3);
                  int var7 = var3 += var4;
                  if (var7 <= 255 && var7 >= -255) {
                     var3 = var7;
                  }
               }
            }
         }
      }

      public boolean isChecked() {
         return false;
      }
   }

   // Menu action: opens the MP3 tag editor for the selected file.
   private class EditMP3TagsAction extends Action {
      public EditMP3TagsAction() {
         super(SResources.getString("m.d.editMP3Tags"));
         this.setImageDescriptor(SResources.getImageDescriptor("preferences"));
      }

      public void run() {
         if (selectedFile != null) {
            new EditMP3TagsDialog(gView.getShell(), selectedFile).open();
         }
      }
   }

   // Menu action: opens the file-comments view for the selected file.
   private class FileCommentsAction extends Action {
      public FileCommentsAction() {
         super(SResources.getString("m.d.fileComments"));
         this.setImageDescriptor(SResources.getImageDescriptor("comments"));
      }

      public void run() {
         if (selectedFile != null) {
            new FileDetailDialog(gView.getShell(), selectedFile, true).open();
         }
      }
   }

   // Menu action: opens the file-details dialog for the selected file.
   private class FileDetailAction extends Action {
      public FileDetailAction() {
         super(SResources.getString("m.d.fileDetails"));
         this.setImageDescriptor(SResources.getImageDescriptor("info"));
      }

      public void run() {
         if (selectedFile != null) {
            new FileDetailDialog(gView.getShell(), selectedFile).open();
         }
      }
   }

   // Menu action: pauses each selected download.
   private class PauseAction extends Action {
      public PauseAction() {
         super(SResources.getString("m.d.pause"));
         this.setImageDescriptor(SResources.getImageDescriptor("pause"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            File var2 = (File)selectedObjects.get(var1);
            if (var2.getFileStateEnum() != EnumFileState.PAUSED) {
               var2.setState(EnumFileState.PAUSED);
            }
         }
      }
   }

   // Menu action: sets a preset priority on each selected file.
   private class PriorityAction extends Action {
      private EnumPriority enumPriority;

      public PriorityAction(EnumPriority var2) {
         super(var2.getName().toLowerCase(), 2);
         this.enumPriority = var2;
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            File var2 = (File)selectedObjects.get(var1);
            if (var2.getFileStateEnum() != EnumFileState.DOWNLOADED) {
               var2.sendPriority(this.enumPriority);
            }
         }
      }

      public boolean isChecked() {
         return selectedFile.getPriorityEnum() == this.enumPriority;
      }
   }

   // Dialog: prompts for a custom priority value and optional increment.
   private static class PriorityInputDialog extends Dialog {
      int initialValue;
      int intValue;
      int incValue;
      String title;
      CSpinner spinner;
      CSpinner spinner2;
      Button okButton;

      public PriorityInputDialog(Shell var1, String var2, int var3) {
         super(var1);
         this.initialValue = var3;
         this.title = var2;
      }

      protected void configureShell(Shell var1) {
         super.configureShell(var1);
         var1.setImage(VersionInfo.getProgramIcon());
         var1.setText(this.title);
      }

      protected void createButtonsForButtonBar(Composite var1) {
         this.okButton = this.createButton(var1, 0, SResources.getString("b.ok"), true);
         this.createButton(var1, 1, SResources.getString("b.cancel"), false);
         this.spinner.setFocus();
      }

      protected Button getOkButton() {
         return this.okButton;
      }

      protected Control createDialogArea(Composite var1) {
         Composite var2 = (Composite)super.createDialogArea(var1);
         var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         Scale var3 = new Scale(var2, 256);
         GridData var4 = new GridData(768);
         var4.widthHint = 300;
         var3.setLayoutData(var4);
         var3.setMinimum(0);
         var3.setMaximum(200);
         var3.setIncrement(1);
         var3.setPageIncrement(5);
         if (this.initialValue < -100) {
            var3.setSelection(0);
         } else if (this.initialValue > 100) {
            var3.setSelection(200);
         } else {
            var3.setSelection(this.initialValue + 100);
         }

         var3.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               int var2 = var3.getSelection() - 100;
               spinner.setSelection(var2);
            }
         });
         this.spinner = new CSpinner(var2, 2048);
         this.spinner.setMinimum(-200);
         this.spinner.setMaximum(200);
         this.spinner.setSelection(this.initialValue);
         this.spinner.addListener(31, new Listener() {
            public void handleEvent(Event var1) {
               if (var1.detail != 2 && var1.detail == 4) {
                  onClose();
                  close();
               }
            }
         });
         Composite var5 = new Composite(var2, 0);
         var5.setLayout(WidgetFactory.createGridLayout(1, 25, 5, 10, 5, false));
         var4 = new GridData(768);
         var4.horizontalSpan = 2;
         var5.setLayoutData(var4);
         var5.setLayoutData(var4);
         Group var6 = new Group(var5, 0);
         var6.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         var4 = new GridData(768);
         var6.setLayoutData(var4);
         var6.setText(SResources.getString("m.d.priorityOptInc"));
         Scale var7 = new Scale(var6, 256);
         var7.setLayoutData(new GridData(768));
         var7.setMinimum(0);
         var7.setMaximum(40);
         var7.setIncrement(1);
         var7.setPageIncrement(5);
         var7.setSelection(20);
         var7.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               int var2 = var7.getSelection() - 20;
               spinner2.setSelection(var2);
            }
         });
         this.spinner2 = new CSpinner(var6, 2048);
         this.spinner2.setMinimum(-20);
         this.spinner2.setMaximum(20);
         this.spinner2.setSelection(0);
         this.spinner2.addListener(31, new Listener() {
            public void handleEvent(Event var1) {
               if (var1.detail != 2 && var1.detail == 4) {
                  onClose();
                  close();
               }
            }
         });
         return var2;
      }

      protected void onClose() {
         this.intValue = this.spinner.getSelection();
         this.incValue = this.spinner2.getSelection();
      }

      protected void buttonPressed(int var1) {
         this.onClose();
         super.buttonPressed(var1);
      }

      public int getIntValue() {
         return this.intValue;
      }

      public int getIncIntValue() {
         return this.incValue;
      }
   }

   // Menu action: renames the selected file, either to a preset name or via input.
   private class RenameAction extends Action {
      private String renameAs;
      private boolean manualInput;

      public RenameAction(String var2) {
         super(var2);
         this.manualInput = false;
         this.setImageDescriptor(selectedFile.getFileTypeImageDescriptor());
         this.renameAs = var2;
      }

      public RenameAction(boolean var2) {
         super(SResources.getString("m.d.commitInput"));
         this.manualInput = false;
         this.setImageDescriptor(SResources.getImageDescriptor("commit_question"));
         this.manualInput = var2;
      }

      public void run() {
         if (this.manualInput) {
            renameSelectedFiles();
         } else if (selectedFile != null) {
            selectedFile.rename(this.renameAs);
         }
      }
   }

   // Menu action: requests fresh file info for each selected file.
   private class RequestFileInfoAction extends Action {
      public RequestFileInfoAction() {
         super(SResources.getString("m.d.requestFileInfo"));
         this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            File var2 = (File)selectedObjects.get(var1);
            var2.requestFileInfo();
         }
      }
   }

   // Menu action: resumes each paused selected download.
   private class ResumeAction extends Action {
      public ResumeAction() {
         super(SResources.getString("m.d.resume"));
         this.setImageDescriptor(SResources.getImageDescriptor("resume"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            File var2 = (File)selectedObjects.get(var1);
            if (var2.getFileStateEnum() == EnumFileState.PAUSED) {
               var2.setState(EnumFileState.DOWNLOADING);
            }
         }
      }
   }

   // Menu action: launches a search for files with a similar name.
   private class SearchSimilarAction extends Action {
      public SearchSimilarAction() {
         super(SResources.getString("m.d.searchSimilar"));
         this.setImageDescriptor(SResources.getImageDescriptor("search_small"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            File var2 = (File)selectedObjects.get(var1);
            String var3 = var2.getName();
            String var4 = var2.getExtension();
            if (var3.length() > var4.length() + 1) {
               int var5 = var3.length() - var4.length() - 1;
               var3 = var3.substring(0, var5);
            }

            var3 = SwissArmy.replaceAll(var3, "\\_", " ");
            var3 = SwissArmy.replaceAll(var3, "\\.", " ");
            var3 = SwissArmy.replaceAll(var3, "\\-", " ");
            var3 = SwissArmy.replaceAll(var3, "\\?", " ");
            var3 = SwissArmy.replaceAll(var3, "\\!", " ");
            gView.getViewFrame().getATab().getMainWindow().autoSearch(var3);
         }
      }
   }

   // Menu action: verifies downloaded chunks for each selected file.
   private class VerifyChunksAction extends Action {
      public VerifyChunksAction() {
         super(SResources.getString("m.d.verifyChunks"));
         this.setImageDescriptor(SResources.getImageDescriptor("verify"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            ((File)selectedObjects.get(var1)).verifyChunks();
         }
      }
   }
}
