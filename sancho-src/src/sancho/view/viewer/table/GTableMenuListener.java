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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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

   public GTableMenuListener(GView gView) {
      this.gView = gView;
   }

   protected void addSelectAllMenu(IMenuManager menuManager) {
      menuManager.add(new Separator());
      menuManager.add(new SelectAllAction(this, true));
      menuManager.add(new SelectAllAction(this, false));
   }

   protected void addWebServicesMenu(IMenuManager menuManager, String name, String hash, long size) {
      if (VersionInfo.useWebServices()) {
         MyMenuManager menu = new MyMenuManager(SResources.getString("mi.webServices"));
         menu.setImageString("earth");
         menu.add(new WebServicesAction(1, name, hash, size));
         menu.add(new WebServicesAction(2, name, hash, size));
         menu.add(new WebServicesAction(5, name, hash, size));
         menu.add(new WebServicesAction(6, name, hash, size));

         for (int i = 1; !PreferenceLoader.loadString("webServiceName" + i).equals(""); i++) {
            menu.add(new WebServicesAction(-i, name, hash, size));
         }

         menuManager.add(menu);
      }
   }

   protected void addClipboardMenu(IMenuManager menuManager) {
      MyMenuManager menu = new MyMenuManager(SResources.getString("m.d.copyTo"));
      menu.setImageString("copy");
      boolean hasEd2k = true;
      boolean hasSha1 = false;
      boolean hasSig2dat = false;

      for (int i = 0; i < this.selectedObjects.size(); i++) {
         IObject_UID object = (IObject_UID)this.selectedObjects.get(i);
         String[] uids = object.getUIDs();
         if (uids == null) {
            hasEd2k = true;
         } else {
            for (int j = 0; j < uids.length; j++) {
               if (uids[j].startsWith("urn:ed2k")) {
                  hasEd2k = true;
               } else if (uids[j].startsWith("urn:sig2dat")) {
                  hasSig2dat = true;
               } else if (uids[j].startsWith("urn:sha1") || uids[j].startsWith("urn:ttr")) {
                  hasSha1 = true;
               }
            }
         }
      }

      int typeCount = 0;
      if (hasEd2k) {
         this.addAllTypes(menu, CopyUIDLinksToClipboardAction.ED2K, this.selectedObjects);
         typeCount++;
      }

      if (hasSig2dat) {
         if (typeCount > 0) {
            menu.add(new Separator());
         }

         this.addAllTypes(menu, CopyUIDLinksToClipboardAction.SIG2DAT, this.selectedObjects);
         typeCount++;
      }

      if (hasSha1) {
         if (typeCount > 0) {
            menu.add(new Separator());
         }

         this.addAllTypes(menu, CopyUIDLinksToClipboardAction.MAGNET, this.selectedObjects);
      }

      menuManager.add(menu);
   }

   protected void addAllTypes(MenuManager menuManager, int uidType, List objects) {
      int[] formats = new int[]{CopyUIDLinksToClipboardAction.NORMAL, CopyUIDLinksToClipboardAction.HTML, CopyUIDLinksToClipboardAction.BBCODE};

      for (int i = 0; i < formats.length; i++) {
         menuManager.add(new CopyUIDLinksToClipboardAction(uidType, formats[i], objects));
      }
   }

   protected void collectSelections(SelectionChangedEvent event, Class type) {
      IStructuredSelection selection = (IStructuredSelection)event.getSelection();
      this.selectedObjects.clear();

      for (Object object : selection) {
         if (type.isInstance(object)) {
            this.selectedObjects.add(object);
         }
      }
   }

   public void deselectAll() {
      this.gView.deselectAll();
      this.selectedObjects.clear();
   }

   public void selectAll() {
      this.gView.selectAll();
      ICustomViewer customViewer = (ICustomViewer)this.gView.getViewer();
      customViewer.updateSelection(this.gView.getViewer().getSelection());
   }

   public void initialize() {
      if (this.gView instanceof GTableView) {
         this.tableViewer = ((GTableView)this.gView).getTableViewer();
      }

      String platform = SWT.getPlatform();
      // Skip the extra mouse listener on macOS (old "carbon" and modern "cocoa"),
      // matching the original intent — the platform handles the popup itself.
      if (!platform.equals("fox") && !platform.equals("carbon") && !platform.equals("cocoa")) {
         this.gView.getComposite().addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
               if (GTableMenuListener.this.gView.getItemAt(event.x, event.y) == null) {
                  GTableMenuListener.this.deselectAll();
               }
            }
         });
      }

      this.gView.getComposite().addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent event) {
            if ((event.stateMask & SWT.MOD1) != 0 && (event.stateMask & 131072) != 0 && event.character == 1) {
               GTableMenuListener.this.deselectAll();
            } else if (event.stateMask == SWT.MOD1 && event.character == 1) {
               GTableMenuListener.this.selectAll();
            } else if (event.keyCode == 127) {
               GTableMenuListener.this.onDeleteKey();
            } else if (event.keyCode == 16777227) {
               GTableMenuListener.this.onF2Key();
            }
         }
      });
   }

   protected void onDeleteKey() {
   }

   protected void onF2Key() {
   }

   public abstract void menuAboutToShow(IMenuManager menuManager);

   public abstract void selectionChanged(SelectionChangedEvent event);
}
