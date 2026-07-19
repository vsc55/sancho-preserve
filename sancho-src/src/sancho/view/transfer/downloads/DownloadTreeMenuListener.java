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

   public DownloadTreeMenuListener(DownloadTreeView view) {
      super(view);
   }

   public void setClientView(GView clientView) {
      this.clientView = clientView;
   }

   protected void sendToStatusline(String text) {
      if (text != null) {
         this.gView.getViewFrame().getATab().getMainWindow().getStatusline().setText(text);
      }
   }

   public void doubleClick(DoubleClickEvent event) {
      IStructuredSelection selection = (IStructuredSelection)event.getSelection();
      Object element = selection.getFirstElement();
      CustomTreeViewer viewer = (CustomTreeViewer)this.gView.getViewer();
      if (element instanceof File) {
         File file = (File)element;
         switch (PreferenceLoader.loadInt("dlFileDoubleClick")) {
            case 2:
               Program program = this.selectedFile.getOSPreviewApp();
               if (program != null) {
                  this.sendToStatusline(file.preview(program, -1));
                  break;
               }
            case 1:
               this.sendToStatusline(file.preview(-1));
               break;
            default:
               if (viewer.getExpandedState(file)) {
                  viewer.collapseToLevel(file, -1);
               } else {
                  viewer.expandToLevel(file, -1);
               }
         }
      } else if (element instanceof FileClient && Sancho.hasCollectionFactory()) {
         FileClient fileClient = (FileClient)element;
         ClientDetailDialog dialog = new ClientDetailDialog(this.gView.getShell(), fileClient.getFile(), fileClient.getClient());
         dialog.open();
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
      byte operations = 23;
      DragSource dragSource = new DragSource(this.gView.getComposite(), operations);
      dragSource.setTransfer(new Transfer[]{TextTransfer.getInstance()});
      dragSource.addDragListener(new DragSourceAdapter() {
         public void dragStart(DragSourceEvent event) {
            if (selectedFile == null) {
               event.doit = false;
            } else {
               event.doit = true;
               myDrag = true;
            }
         }

         public void dragSetData(DragSourceEvent event) {
            event.data = selectedFile.getED2K();
         }

         public void dragFinished(DragSourceEvent event) {
            myDrag = false;
         }
      });
      DropTarget dropTarget = new DropTarget(this.gView.getComposite(), operations);
      UniformResourceLocator urlTransfer = UniformResourceLocator.getInstance();
      TextTransfer textTransfer = TextTransfer.getInstance();
      FileTransfer fileTransfer = FileTransfer.getInstance();
      dropTarget.setTransfer(new Transfer[]{urlTransfer, textTransfer, fileTransfer});
      dropTarget.addDropListener(new DropTargetAdapter() {
         public void dragEnter(DropTargetEvent event) {
            if (event.detail == 16) {
               if ((event.operations & 4) != 0) {
                  event.detail = 4;
               } else if ((event.operations & 1) != 0) {
                  event.detail = 1;
               } else if ((event.operations & 2) != 0) {
                  event.detail = 2;
               } else {
                  event.detail = 0;
               }
            }
         }

         public void drop(DropTargetEvent event) {
            if (event.data != null && !myDrag) {
               if (textTransfer.isSupportedType(event.currentDataType) || urlTransfer.isSupportedType(event.currentDataType)) {
                  SwissArmy.sendLink(gView.getCore(), (String)event.data);
               }

               if (fileTransfer.isSupportedType(event.currentDataType)) {
                  String[] links = (String[])event.data;

                  for (int i = 0; i < links.length; i++) {
                     SwissArmy.sendLink(gView.getCore(), links[i]);
                  }
               }
            }
         }
      });
   }

   public void selectionChanged(SelectionChangedEvent event) {
      IStructuredSelection selection = (IStructuredSelection)event.getSelection();
      this.selectedClients.clear();
      this.selectedObjects.clear();

      for (Object element : selection) {
         if (element instanceof File) {
            this.selectedObjects.add(element);
         } else if (element instanceof FileClient) {
            this.selectedClients.add(element);
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

   public void updateClientsTable(boolean visible) {
      if (visible) {
         if (this.clientTableVisible != visible && this.selectedFile != null) {
            this.clientView.getViewer().setInput(this.selectedFile);
         }
      } else {
         this.clientView.getViewer().setInput(null);
      }

      this.clientTableVisible = visible;
   }

   public void menuAboutToShow(IMenuManager menu) {
      if (this.selectedFile != null && this.selectedFileListContains(EnumFileState.DOWNLOADED)) {
         menu.add(new CommitAction());
      }

      if (this.selectedFile != null && this.selectedFile.getFileStateEnum() == EnumFileState.DOWNLOADED) {
         MyMenuManager commitAsMenu = new MyMenuManager(SResources.getString("m.d.commitAs"));
         commitAsMenu.setImageString("commit");
         commitAsMenu.add(new CommitAction(true));
         int maxItems = PreferenceLoader.loadInt("maxMenuItems");

         for (int i = 0; i < this.selectedFile.getNames().length && i < maxItems; i++) {
            commitAsMenu.add(new CommitAction(this.selectedFile.getNames()[i]));
         }

         menu.add(commitAsMenu);
      }

      if (this.selectedFile != null) {
         menu.add(new FileDetailAction());
         if (this.selectedFile.hasFileComments()) {
            menu.add(new FileCommentsAction());
         }
      }

      if (this.selectedFile != null && this.selectedFile.getEnumNetwork() == EnumNetwork.FILETP) {
         menu.add(new AddMirrorAction(this.selectedFile));
      }

      if (this.selectedFile != null && (this.selectedFileListContains(EnumFileState.DOWNLOADING) || this.selectedFileListContains(EnumFileState.QUEUED))) {
         menu.add(new PauseAction());
      }

      if (this.selectedFile != null && this.selectedFileListContains(EnumFileState.PAUSED)) {
         menu.add(new ResumeAction());
      }

      if (this.selectedFile != null && this.selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED)) {
         menu.add(new CancelAction());
      }

      if (this.selectedFile != null && this.selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED)) {
         MyMenuManager priorityMenu = new MyMenuManager(SResources.getString("m.d.priority"));
         priorityMenu.setImageString("priority");
         priorityMenu.add(new PriorityAction(EnumPriority.VERY_HIGH));
         priorityMenu.add(new PriorityAction(EnumPriority.HIGH));
         priorityMenu.add(new PriorityAction(EnumPriority.NORMAL));
         priorityMenu.add(new PriorityAction(EnumPriority.LOW));
         priorityMenu.add(new PriorityAction(EnumPriority.VERY_LOW));
         priorityMenu.add(new Separator());
         priorityMenu.add(new CustomPriorityAction(false));
         priorityMenu.add(new CustomPriorityAction(true));
         menu.add(priorityMenu);
      }

      if (this.selectedFile != null && (this.selectedFile.getDownloaded() > 0L || this.selectedFile.getFileStateEnum() == EnumFileState.DOWNLOADED)) {
         File[] files = new File[]{this.selectedFile};
         int[] indices = new int[]{-1};
         this.addPreview(menu, files, indices);
      }

      if (this.selectedFile != null && this.selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED)) {
         MyMenuManager renameMenu = new MyMenuManager(SResources.getString("m.d.rename"));
         renameMenu.setImageString("rename");
         renameMenu.add(new RenameAction(true));
         int maxItems = PreferenceLoader.loadInt("maxMenuItems");

         for (int i = 0; i < this.selectedFile.getNames().length && i < maxItems; i++) {
            renameMenu.add(new RenameAction(this.selectedFile.getNames()[i]));
         }

         menu.add(renameMenu);
      }

      if (this.selectedFile != null) {
         if (this.selectedFile.getFormat().getFormat() == EnumFormat.MP3) {
            menu.add(new EditMP3TagsAction());
         }

         menu.add(new ConnectAllAction());
         menu.add(new VerifyChunksAction());
         menu.add(new RequestFileInfoAction());
      }

      if (this.selectedClients.size() > 0) {
         FileClient firstFileClient = (FileClient)this.selectedClients.get(0);
         Client[] clients = new Client[this.selectedClients.size()];

         for (int i = 0; i < this.selectedClients.size(); i++) {
            FileClient fileClient = (FileClient)this.selectedClients.get(i);
            clients[i] = fileClient.getClient();
         }

         this.addClientActions(this.gView.getShell(), firstFileClient.getFile(), menu, clients);
      }

      if (this.selectedFile != null) {
         menu.add(new SearchSimilarAction());
      }

      if (this.selectedFile != null) {
         this.addClipboardMenu(menu);
      }

      if (this.selectedFile != null && this.gView.getCore().getProtocol() > 40) {
         MyMenuManager userGroupMenu = new MyMenuManager(SResources.getString("m.d.userGroup"));
         userGroupMenu.setImageString("user");
         userGroupMenu.add(new ChownAction());
         userGroupMenu.add(new ChgrpAction());
         menu.add(userGroupMenu);
      }

      if (this.selectedFile != null) {
         this.addWebServicesMenu(menu, this.selectedFile.getHash(), this.selectedFile.getED2K(), this.selectedFile.getSize());
      }

      if (this.selectedFile != null || this.selectedClients.size() > 0) {
         this.addSelectAllMenu(menu);
      }
   }

   private boolean selectedFileListContains(EnumFileState state) {
      for (int i = 0; i < this.selectedObjects.size(); i++) {
         if (((File)this.selectedObjects.get(i)).getFileStateEnum() == state) {
            return true;
         }
      }

      return false;
   }

   private boolean selectedFileListContainsOtherThan(EnumFileState state) {
      for (int i = 0; i < this.selectedObjects.size(); i++) {
         if (((File)this.selectedObjects.get(i)).getFileStateEnum() != state) {
            return true;
         }
      }

      return false;
   }

   private void cancelSelectedFiles() {
      MessageBox messageBox = new MessageBox(this.gView.getShell(), 196);
      messageBox.setMessage(SResources.getString("m.d.reallyCancel") + (this.selectedObjects.size() > 1 ? " (" + this.selectedObjects.size() + " selected)" : ""));
      int result = messageBox.open();
      if (result == 64) {
         for (int i = 0; i < this.selectedObjects.size(); i++) {
            File file = (File)this.selectedObjects.get(i);
            if (file.getFileStateEnum() != EnumFileState.DOWNLOADED) {
               file.setState(EnumFileState.CANCELLED);
            }
         }

         this.deselectAll();
      }
   }

   private void renameSelectedFiles() {
      for (int i = 0; i < this.selectedObjects.size(); i++) {
         File file = (File)this.selectedObjects.get(i);
         InputDialog dialog = new InputDialog(this.gView.getShell(), SResources.getString("m.d.rename"), SResources.getString("m.d.rename"), file.getName(), null);
         if (dialog.open() != 0) {
            break;
         }

         String newName = dialog.getValue();
         if (!newName.equals("") && file != null) {
            file.rename(newName);
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

      public AddMirrorAction(File file) {
         super(SResources.getString("dd.f.addMirror"));
         this.setImageDescriptor(SResources.getImageDescriptor("plus"));
         this.file = file;
      }

      public void run() {
         InputDialog dialog = new InputDialog(
            gView.getShell(),
            SResources.getString("dd.f.addMirror"),
            SResources.getString("dd.f.addMirrorInfo"),
            "",
            null
         );
         dialog.open();
         String value = dialog.getValue();
         if (value != null) {
            Sancho.send((short)29, "mirror " + this.file.getId() + " " + value);
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
         InputDialog dialog = new InputDialog(gView.getShell(), "chgrp", "chgrp", "", null);
         dialog.open();
         String group = dialog.getValue();

         for (int i = 0; i < selectedObjects.size(); i++) {
            File file = (File)selectedObjects.get(i);
            file.chgrp(group);
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
         InputDialog dialog = new InputDialog(gView.getShell(), "chown", "chown", "", null);
         dialog.open();
         String owner = dialog.getValue();

         for (int i = 0; i < selectedObjects.size(); i++) {
            File file = (File)selectedObjects.get(i);
            file.chown(owner);
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

      public CommitAction(String name) {
         super(name);
         this.manualInput = false;
         this.setImageDescriptor(SResources.getImageDescriptor("commit"));
         this.commitAs = name;
      }

      public CommitAction(boolean manualInput) {
         super(SResources.getString("m.d.commitInput"));
         this.manualInput = false;
         this.setImageDescriptor(SResources.getImageDescriptor("commit_question"));
         this.manualInput = manualInput;
      }

      public void run() {
         if (this.commitAs == null && !this.manualInput) {
            for (int i = 0; i < selectedObjects.size(); i++) {
               File file = (File)selectedObjects.get(i);
               if (file.getFileStateEnum() == EnumFileState.DOWNLOADED) {
                  file.saveFileAs(file.getName());
               }
            }
         } else if (this.manualInput) {
            if (selectedFile == null) {
               return;
            }

            InputDialog dialog = new InputDialog(
               gView.getShell(),
               SResources.getString("m.d.commitAs"),
               SResources.getString("m.d.commitAs"),
               selectedFile.getName(),
               null
            );
            if (dialog.open() == 0) {
               String value = dialog.getValue();
               if (!value.equals("") && selectedFile != null) {
                  selectedFile.saveFileAs(value);
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
         for (int i = 0; i < selectedObjects.size(); i++) {
            ((File)selectedObjects.get(i)).connectAll();
         }
      }
   }

   // Menu action: opens a dialog to set an absolute or relative custom priority.
   private class CustomPriorityAction extends Action {
      private boolean relative;

      public CustomPriorityAction(boolean relative) {
         super("", 2);
         this.relative = relative;
         if (relative) {
            this.setText(SResources.getString("m.d.priorityRelative"));
         } else {
            this.setText(SResources.getString("m.d.priorityAbsolute"));
         }
      }

      public void run() {
         String title = SResources.getString("m.d.priority")
            + " ("
            + (this.relative ? SResources.getString("m.d.priorityRelative") : SResources.getString("m.d.priorityAbsolute"))
            + ")";
         PriorityInputDialog dialog = new PriorityInputDialog(
            gView.getShell(), title, this.relative ? 0 : selectedFile.getPriority()
         );
         if (dialog.open() == 0) {
            int value = dialog.getIntValue();
            int increment = dialog.getIncIntValue();

            for (int i = 0; i < selectedObjects.size(); i++) {
               File file = (File)selectedObjects.get(i);
               if (file.getFileStateEnum() != EnumFileState.DOWNLOADED) {
                  file.sendPriority(this.relative, value);
                  int newValue = value += increment;
                  if (newValue <= 255 && newValue >= -255) {
                     value = newValue;
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
         for (int i = 0; i < selectedObjects.size(); i++) {
            File file = (File)selectedObjects.get(i);
            if (file.getFileStateEnum() != EnumFileState.PAUSED) {
               file.setState(EnumFileState.PAUSED);
            }
         }
      }
   }

   // Menu action: sets a preset priority on each selected file.
   private class PriorityAction extends Action {
      private EnumPriority enumPriority;

      public PriorityAction(EnumPriority priority) {
         super(priority.getName().toLowerCase(), 2);
         this.enumPriority = priority;
      }

      public void run() {
         for (int i = 0; i < selectedObjects.size(); i++) {
            File file = (File)selectedObjects.get(i);
            if (file.getFileStateEnum() != EnumFileState.DOWNLOADED) {
               file.sendPriority(this.enumPriority);
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

      public PriorityInputDialog(Shell shell, String title, int initialValue) {
         super(shell);
         this.initialValue = initialValue;
         this.title = title;
      }

      protected void configureShell(Shell shell) {
         super.configureShell(shell);
         shell.setImage(VersionInfo.getProgramIcon());
         shell.setText(this.title);
      }

      protected void createButtonsForButtonBar(Composite parent) {
         this.okButton = this.createButton(parent, 0, SResources.getString("b.ok"), true);
         this.createButton(parent, 1, SResources.getString("b.cancel"), false);
         this.spinner.setFocus();
      }

      protected Button getOkButton() {
         return this.okButton;
      }

      protected Control createDialogArea(Composite parent) {
         Composite composite = (Composite)super.createDialogArea(parent);
         composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         Scale scale = new Scale(composite, 256);
         GridData gridData = new GridData(768);
         gridData.widthHint = 300;
         scale.setLayoutData(gridData);
         scale.setMinimum(0);
         scale.setMaximum(200);
         scale.setIncrement(1);
         scale.setPageIncrement(5);
         if (this.initialValue < -100) {
            scale.setSelection(0);
         } else if (this.initialValue > 100) {
            scale.setSelection(200);
         } else {
            scale.setSelection(this.initialValue + 100);
         }

         scale.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               int value = scale.getSelection() - 100;
               spinner.setSelection(value);
            }
         });
         this.spinner = new CSpinner(composite, 2048);
         this.spinner.setMinimum(-200);
         this.spinner.setMaximum(200);
         this.spinner.setSelection(this.initialValue);
         this.spinner.addListener(31, new Listener() {
            public void handleEvent(Event event) {
               if (event.detail != 2 && event.detail == 4) {
                  onClose();
                  close();
               }
            }
         });
         Composite optionsComposite = new Composite(composite, 0);
         optionsComposite.setLayout(WidgetFactory.createGridLayout(1, 25, 5, 10, 5, false));
         gridData = new GridData(768);
         gridData.horizontalSpan = 2;
         optionsComposite.setLayoutData(gridData);
         optionsComposite.setLayoutData(gridData);
         Group group = new Group(optionsComposite, 0);
         group.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
         gridData = new GridData(768);
         group.setLayoutData(gridData);
         group.setText(SResources.getString("m.d.priorityOptInc"));
         Scale incScale = new Scale(group, 256);
         incScale.setLayoutData(new GridData(768));
         incScale.setMinimum(0);
         incScale.setMaximum(40);
         incScale.setIncrement(1);
         incScale.setPageIncrement(5);
         incScale.setSelection(20);
         incScale.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               int value = incScale.getSelection() - 20;
               spinner2.setSelection(value);
            }
         });
         this.spinner2 = new CSpinner(group, 2048);
         this.spinner2.setMinimum(-20);
         this.spinner2.setMaximum(20);
         this.spinner2.setSelection(0);
         this.spinner2.addListener(31, new Listener() {
            public void handleEvent(Event event) {
               if (event.detail != 2 && event.detail == 4) {
                  onClose();
                  close();
               }
            }
         });
         return composite;
      }

      protected void onClose() {
         this.intValue = this.spinner.getSelection();
         this.incValue = this.spinner2.getSelection();
      }

      protected void buttonPressed(int buttonId) {
         this.onClose();
         super.buttonPressed(buttonId);
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

      public RenameAction(String name) {
         super(name);
         this.manualInput = false;
         this.setImageDescriptor(selectedFile.getFileTypeImageDescriptor());
         this.renameAs = name;
      }

      public RenameAction(boolean manualInput) {
         super(SResources.getString("m.d.commitInput"));
         this.manualInput = false;
         this.setImageDescriptor(SResources.getImageDescriptor("commit_question"));
         this.manualInput = manualInput;
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
         for (int i = 0; i < selectedObjects.size(); i++) {
            File file = (File)selectedObjects.get(i);
            file.requestFileInfo();
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
         for (int i = 0; i < selectedObjects.size(); i++) {
            File file = (File)selectedObjects.get(i);
            if (file.getFileStateEnum() == EnumFileState.PAUSED) {
               file.setState(EnumFileState.DOWNLOADING);
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
         for (int i = 0; i < selectedObjects.size(); i++) {
            File file = (File)selectedObjects.get(i);
            String name = file.getName();
            String extension = file.getExtension();
            if (name.length() > extension.length() + 1) {
               int endIndex = name.length() - extension.length() - 1;
               name = name.substring(0, endIndex);
            }

            name = SwissArmy.replaceAll(name, "\\_", " ");
            name = SwissArmy.replaceAll(name, "\\.", " ");
            name = SwissArmy.replaceAll(name, "\\-", " ");
            name = SwissArmy.replaceAll(name, "\\?", " ");
            name = SwissArmy.replaceAll(name, "\\!", " ");
            gView.getViewFrame().getATab().getMainWindow().autoSearch(name);
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
         for (int i = 0; i < selectedObjects.size(); i++) {
            ((File)selectedObjects.get(i)).verifyChunks();
         }
      }
   }
}
