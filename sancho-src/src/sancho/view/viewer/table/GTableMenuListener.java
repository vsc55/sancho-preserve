package sancho.view.viewer.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CustomTableViewer;
import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import sancho.model.mldonkey.IObject_UID;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;
import sancho.view.viewer.actions.CopyUIDLinksToClipboardAction;
import sancho.view.viewer.actions.SelectAllAction;
import sancho.view.viewer.actions.WebServicesAction;

public abstract class GTableMenuListener implements IMenuListener, ISelectionChangedListener {
   protected GView gView;
   protected List selectedObjects = Collections.synchronizedList(new ArrayList());
   protected CustomTableViewer tableViewer;

   public GTableMenuListener(GView var1) {
      this.gView = var1;
   }

   protected void addSelectAllMenu(IMenuManager var1) {
      var1.add(new Separator());
      var1.add(new SelectAllAction(this, true));
      var1.add(new SelectAllAction(this, false));
   }

   protected void addWebServicesMenu(IMenuManager var1, String var2, String var3, long var4) {
      if (VersionInfo.useWebServices()) {
         MyMenuManager var6 = new MyMenuManager(SResources.getString("mi.webServices"));
         var6.setImageString("earth");
         var6.add(new WebServicesAction(1, var2, var3, var4));
         var6.add(new WebServicesAction(2, var2, var3, var4));
         var6.add(new WebServicesAction(5, var2, var3, var4));
         var6.add(new WebServicesAction(6, var2, var3, var4));

         for (int var7 = 1; !PreferenceLoader.loadString("webServiceName" + var7).equals(""); var7++) {
            var6.add(new WebServicesAction(-var7, var2, var3, var4));
         }

         var1.add(var6);
      }
   }

   protected void addClipboardMenu(IMenuManager var1) {
      MyMenuManager var2 = new MyMenuManager(SResources.getString("m.d.copyTo"));
      var2.setImageString("copy");
      boolean var3 = true;
      boolean var4 = false;
      boolean var5 = false;

      for (int var6 = 0; var6 < this.selectedObjects.size(); var6++) {
         IObject_UID var7 = (IObject_UID)this.selectedObjects.get(var6);
         String[] var8 = var7.getUIDs();
         if (var8 == null) {
            var3 = true;
         } else {
            for (int var9 = 0; var9 < var8.length; var9++) {
               if (var8[var9].startsWith("urn:ed2k")) {
                  var3 = true;
               } else if (var8[var9].startsWith("urn:sig2dat")) {
                  var5 = true;
               } else if (var8[var9].startsWith("urn:sha1") || var8[var9].startsWith("urn:ttr")) {
                  var4 = true;
               }
            }
         }
      }

      int var10 = 0;
      if (var3) {
         this.addAllTypes(var2, CopyUIDLinksToClipboardAction.ED2K, this.selectedObjects);
         var10++;
      }

      if (var5) {
         if (var10 > 0) {
            var2.add(new Separator());
         }

         this.addAllTypes(var2, CopyUIDLinksToClipboardAction.SIG2DAT, this.selectedObjects);
         var10++;
      }

      if (var4) {
         if (var10 > 0) {
            var2.add(new Separator());
         }

         this.addAllTypes(var2, CopyUIDLinksToClipboardAction.MAGNET, this.selectedObjects);
      }

      var1.add(var2);
   }

   protected void addAllTypes(MenuManager var1, int var2, List var3) {
      int[] var4 = new int[]{CopyUIDLinksToClipboardAction.NORMAL, CopyUIDLinksToClipboardAction.HTML, CopyUIDLinksToClipboardAction.BBCODE};

      for (int var5 = 0; var5 < var4.length; var5++) {
         var1.add(new CopyUIDLinksToClipboardAction(var2, var4[var5], var3));
      }
   }

   protected void collectSelections(SelectionChangedEvent var1, Class var2) {
      IStructuredSelection var3 = (IStructuredSelection)var1.getSelection();
      this.selectedObjects.clear();

      for (Object var4 : var3) {
         if (var2.isInstance(var4)) {
            this.selectedObjects.add(var4);
         }
      }
   }

   public void deselectAll() {
      this.gView.deselectAll();
      this.selectedObjects.clear();
   }

   public void selectAll() {
      this.gView.selectAll();
      ICustomViewer var1 = (ICustomViewer)this.gView.getViewer();
      var1.updateSelection(this.gView.getViewer().getSelection());
   }

   public void initialize() {
      if (this.gView instanceof GTableView) {
         this.tableViewer = ((GTableView)this.gView).getTableViewer();
      }

      String var1 = SWT.getPlatform();
      if (!var1.equals("fox") && !var1.equals("carbon")) {
         this.gView.getComposite().addMouseListener(new GTableMenuListener$1(this));
      }

      this.gView.getComposite().addKeyListener(new GTableMenuListener$2(this));
   }

   protected void onDeleteKey() {
   }

   protected void onF2Key() {
   }

   public abstract void menuAboutToShow(IMenuManager var1);

   public abstract void selectionChanged(SelectionChangedEvent var1);
}
