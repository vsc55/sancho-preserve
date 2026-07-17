package sancho.view.transfer.downloads;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CustomTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.MessageBox;
import sancho.core.Sancho;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.model.mldonkey.enums.EnumFormat;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.enums.EnumPriority;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.ClientDetailDialog;
import sancho.view.transfer.FileClient;
import sancho.view.transfer.UniformResourceLocator;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
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
      var2.addDragListener(new DownloadTreeMenuListener$1(this));
      DropTarget var3 = new DropTarget(this.gView.getComposite(), var1);
      UniformResourceLocator var4 = UniformResourceLocator.getInstance();
      TextTransfer var5 = TextTransfer.getInstance();
      FileTransfer var6 = FileTransfer.getInstance();
      var3.setTransfer(new Transfer[]{var4, var5, var6});
      var3.addDropListener(new DownloadTreeMenuListener$2(this, var5, var4, var6));
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
         var1.add(new DownloadTreeMenuListener$CommitAction(this));
      }

      if (this.selectedFile != null && this.selectedFile.getFileStateEnum() == EnumFileState.DOWNLOADED) {
         MyMenuManager var2 = new MyMenuManager(SResources.getString("m.d.commitAs"));
         var2.setImageString("commit");
         var2.add(new DownloadTreeMenuListener$CommitAction(this, true));
         int var3 = PreferenceLoader.loadInt("maxMenuItems");

         for (int var4 = 0; var4 < this.selectedFile.getNames().length && var4 < var3; var4++) {
            var2.add(new DownloadTreeMenuListener$CommitAction(this, this.selectedFile.getNames()[var4]));
         }

         var1.add(var2);
      }

      if (this.selectedFile != null) {
         var1.add(new DownloadTreeMenuListener$FileDetailAction(this));
         if (this.selectedFile.hasFileComments()) {
            var1.add(new DownloadTreeMenuListener$FileCommentsAction(this));
         }
      }

      if (this.selectedFile != null && this.selectedFile.getEnumNetwork() == EnumNetwork.FILETP) {
         var1.add(new DownloadTreeMenuListener$AddMirrorAction(this, this.selectedFile));
      }

      if (this.selectedFile != null && (this.selectedFileListContains(EnumFileState.DOWNLOADING) || this.selectedFileListContains(EnumFileState.QUEUED))) {
         var1.add(new DownloadTreeMenuListener$PauseAction(this));
      }

      if (this.selectedFile != null && this.selectedFileListContains(EnumFileState.PAUSED)) {
         var1.add(new DownloadTreeMenuListener$ResumeAction(this));
      }

      if (this.selectedFile != null && this.selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED)) {
         var1.add(new DownloadTreeMenuListener$CancelAction(this));
      }

      if (this.selectedFile != null && this.selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED)) {
         MyMenuManager var6 = new MyMenuManager(SResources.getString("m.d.priority"));
         var6.setImageString("priority");
         var6.add(new DownloadTreeMenuListener$PriorityAction(this, EnumPriority.VERY_HIGH));
         var6.add(new DownloadTreeMenuListener$PriorityAction(this, EnumPriority.HIGH));
         var6.add(new DownloadTreeMenuListener$PriorityAction(this, EnumPriority.NORMAL));
         var6.add(new DownloadTreeMenuListener$PriorityAction(this, EnumPriority.LOW));
         var6.add(new DownloadTreeMenuListener$PriorityAction(this, EnumPriority.VERY_LOW));
         var6.add(new Separator());
         var6.add(new DownloadTreeMenuListener$CustomPriorityAction(this, false));
         var6.add(new DownloadTreeMenuListener$CustomPriorityAction(this, true));
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
         var8.add(new DownloadTreeMenuListener$RenameAction(this, true));
         int var12 = PreferenceLoader.loadInt("maxMenuItems");

         for (int var14 = 0; var14 < this.selectedFile.getNames().length && var14 < var12; var14++) {
            var8.add(new DownloadTreeMenuListener$RenameAction(this, this.selectedFile.getNames()[var14]));
         }

         var1.add(var8);
      }

      if (this.selectedFile != null) {
         if (this.selectedFile.getFormat().getFormat() == EnumFormat.MP3) {
            var1.add(new DownloadTreeMenuListener$EditMP3TagsAction(this));
         }

         var1.add(new DownloadTreeMenuListener$ConnectAllAction(this));
         var1.add(new DownloadTreeMenuListener$VerifyChunksAction(this));
         var1.add(new DownloadTreeMenuListener$RequestFileInfoAction(this));
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
         var1.add(new DownloadTreeMenuListener$SearchSimilarAction(this));
      }

      if (this.selectedFile != null) {
         this.addClipboardMenu(var1);
      }

      if (this.selectedFile != null && this.gView.getCore().getProtocol() > 40) {
         MyMenuManager var10 = new MyMenuManager(SResources.getString("m.d.userGroup"));
         var10.setImageString("user");
         var10.add(new DownloadTreeMenuListener$ChownAction(this));
         var10.add(new DownloadTreeMenuListener$ChgrpAction(this));
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

   // $VF: synthetic method
   static File access$000(DownloadTreeMenuListener var0) {
      return var0.selectedFile;
   }

   // $VF: synthetic method
   static boolean access$102(DownloadTreeMenuListener var0, boolean var1) {
      return var0.myDrag = var1;
   }

   // $VF: synthetic method
   static boolean access$100(DownloadTreeMenuListener var0) {
      return var0.myDrag;
   }

   // $VF: synthetic method
   static GView access$200(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$300(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$400(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$500(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$600(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$700(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$800(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$900(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$1000(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1100(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1200(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1300(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$1400(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static void access$1500(DownloadTreeMenuListener var0) {
      var0.renameSelectedFiles();
   }

   // $VF: synthetic method
   static List access$1600(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1700(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$1800(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$1900(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$2000(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$2100(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$2200(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$2300(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static void access$2400(DownloadTreeMenuListener var0) {
      var0.cancelSelectedFiles();
   }

   // $VF: synthetic method
   static GView access$2500(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$2600(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$2700(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$2800(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$2900(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$3000(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$3100(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$3200(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$3300(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$3400(DownloadTreeMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$3500(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$3600(DownloadTreeMenuListener var0) {
      return var0.selectedObjects;
   }
}
