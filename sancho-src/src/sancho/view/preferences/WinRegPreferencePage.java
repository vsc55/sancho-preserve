package sancho.view.preferences;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class WinRegPreferencePage extends CPreferencePage {
   // Every association row across both tabs (protocols + file extensions), so the status
   // labels can be refreshed together.
   private final List<AssocRow> rows = new ArrayList<>();
   // false -> write machine-wide (HKEY_CLASSES_ROOT, needs administrator); default true ->
   // write per-user under HKEY_CURRENT_USER\Software\Classes, which needs no elevation.
   private boolean perUser = true;
   private Display display;

   protected WinRegPreferencePage(String title) {
      super(title);
   }

   protected Control createContents(Composite parent) {
      this.display = parent.getDisplay();
      Composite container = new Composite(parent, 0);
      container.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      final Button allUsersCheck = new Button(container, SWT.CHECK);
      allUsersCheck.setText(SResources.getString("b.regAllUsers"));
      allUsersCheck.setSelection(!this.perUser);
      allUsersCheck.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            WinRegPreferencePage.this.perUser = !allUsersCheck.getSelection();
         }
      });
      // Toggle for the startup check that offers to create missing associations. Persisted
      // via the inherited performOk() (setupBooleanEditor registers it in editorList).
      this.setupBooleanEditor("checkAssociations", "p.r.general.checkAssociations", container);
      TabFolder tabFolder = new TabFolder(container, 128);
      tabFolder.setLayoutData(new GridData(1808));
      this.createProtocolTab(tabFolder);
      this.createFileExtensionsTab(tabFolder);
      // Show which associations already exist (and at what level) on the freshly built page.
      this.refreshStatus();
      return container;
   }

   protected void createProtocolTab(TabFolder tabFolder) {
      Composite tab = this.createNewTab(tabFolder, "l.protocols");
      tab.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));

      for (WinRegAssociations.AssocItem item : WinRegAssociations.allItems()) {
         if (item.extension == null) {
            this.rows.add(new AssocRow(item, tab));
         }
      }

      this.createUpdateButton(tab);
      Point size = tab.computeSize(-1, -1);
      ((ScrolledComposite)tab.getParent()).setMinSize(size);
      tab.layout();
   }

   protected void createFileExtensionsTab(TabFolder tabFolder) {
      Composite tab = this.createNewTab(tabFolder, "l.fileExtensions");
      tab.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      this.createInformationLabel(tab, "p.registerInfo");

      for (WinRegAssociations.AssocItem item : WinRegAssociations.allItems()) {
         if (item.extension != null) {
            this.rows.add(new AssocRow(item, tab));
         }
      }

      this.createUpdateButton(tab);
      Point size = tab.computeSize(-1, -1);
      ((ScrolledComposite)tab.getParent()).setMinSize(size);
      tab.layout();
   }

   private void createUpdateButton(Composite tab) {
      Button updateButton = new Button(tab, 0);
      updateButton.setLayoutData(new GridData(768));
      updateButton.setText(SResources.getString("b.updateRegistry"));
      updateButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            // Either tab's button applies all pending selections (both tabs), as before.
            if (WinRegPreferencePage.this.hasPendingChanges()) {
               WinRegPreferencePage.this.updateRegistry();
            }
         }
      });
   }

   private boolean hasPendingChanges() {
      for (AssocRow row : this.rows) {
         if (row.getSelection() != AssocRow.NO_CHANGE) {
            return true;
         }
      }

      return false;
   }

   // Apply the pending register/unregister selections and report the result, then refresh the
   // status labels so they reflect what the registry now contains.
   private void updateRegistry() {
      if (!WinRegAssociations.isWindows()) {
         MessageDialog.openInformation(this.getShell(), VersionInfo.getName(), SResources.getString("l.regeditWin32Only"));
         return;
      }

      List<WinRegAssociations.RegAction> actions = new ArrayList<>();

      for (AssocRow row : this.rows) {
         if (row.getSelection() == AssocRow.REGISTER) {
            actions.add(new WinRegAssociations.RegAction(row.getItem(), true));
         } else if (row.getSelection() == AssocRow.UNREGISTER) {
            actions.add(new WinRegAssociations.RegAction(row.getItem(), false));
         }
      }

      if (WinRegAssociations.writeAndImport(actions, this.perUser)) {
         MessageDialog.openInformation(this.getShell(), VersionInfo.getName(), SResources.getString("l.regUpdated"));
      } else {
         MessageDialog.openWarning(this.getShell(), VersionInfo.getName(), SResources.getString("l.regUpdateFailed"));
      }

      this.refreshStatus();
   }

   // Query the current association level of every row off the UI thread (each query spawns
   // reg.exe), then update the per-row status labels back on the UI thread.
   private void refreshStatus() {
      if (this.display == null || this.display.isDisposed() || !WinRegAssociations.isWindows()) {
         return;
      }

      final AssocRow[] snapshot = this.rows.toArray(new AssocRow[0]);
      final Display uiDisplay = this.display;
      Thread thread = new Thread(new Runnable() {
         public void run() {
            final WinRegAssociations.Level[] levels = new WinRegAssociations.Level[snapshot.length];

            for (int i = 0; i < snapshot.length; i++) {
               levels[i] = WinRegAssociations.level(snapshot[i].getItem().progId);
            }

            if (uiDisplay.isDisposed()) {
               return;
            }

            uiDisplay.asyncExec(new Runnable() {
               public void run() {
                  for (int i = 0; i < snapshot.length; i++) {
                     snapshot[i].setStatus(levels[i]);
                  }
               }
            });
         }
      });
      thread.setDaemon(true);
      thread.start();
   }

   // A human-readable, localized description of an association's current level: whether it is
   // registered to Sancho or another application, and at user or machine level.
   static String statusText(WinRegAssociations.Level level) {
      switch (level) {
         case SANCHO_USER:
            return "✓ " + SResources.getString("l.assoc.yes") + " " + VersionInfo.getName()
               + " (" + SResources.getString("l.assoc.user") + ")";
         case SANCHO_MACHINE:
            return "✓ " + SResources.getString("l.assoc.yes") + " " + VersionInfo.getName()
               + " (" + SResources.getString("l.assoc.machine") + ")";
         case OTHER_USER:
            return SResources.getString("l.assoc.yes") + " " + SResources.getString("l.assoc.otherApp")
               + " (" + SResources.getString("l.assoc.user") + ")";
         case OTHER_MACHINE:
            return SResources.getString("l.assoc.yes") + " " + SResources.getString("l.assoc.otherApp")
               + " (" + SResources.getString("l.assoc.machine") + ")";
         default:
            return SResources.getString("l.assoc.none");
      }
   }

   // A three-way radio group (No change / Register / Unregister) for one association plus a
   // status line showing its current registration. Bound to the AssocItem it acts on, so it
   // works uniformly for URL protocols (ed2k://) and file extensions (.torrent).
   static class AssocRow {
      public static final int NO_CHANGE = 0;
      public static final int REGISTER = 1;
      public static final int UNREGISTER = 2;
      private final WinRegAssociations.AssocItem item;
      private int selection;
      private Label statusLabel;

      public AssocRow(WinRegAssociations.AssocItem item, Composite parent) {
         this.item = item;
         this.selection = NO_CHANGE;
         this.createContents(parent);
      }

      protected void createContents(Composite parent) {
         Group group = new Group(parent, 16);
         group.setLayoutData(new GridData(768));
         group.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
         group.setText(this.item.label);
         this.createButton(group, SResources.getString("b.noChange"), NO_CHANGE);
         this.createButton(group, SResources.getString("b.registerLink"), REGISTER);
         this.createButton(group, SResources.getString("b.unregisterLink"), UNREGISTER);
         this.statusLabel = new Label(group, 0);
         this.statusLabel.setLayoutData(new GridData(768));
         this.statusLabel.setText(SResources.getString("l.assoc.checking"));
      }

      private void createButton(Group group, String label, final int type) {
         Button button = new Button(group, 16);
         button.setLayoutData(new GridData(768));
         button.setText(label);
         button.setSelection(type == NO_CHANGE);
         button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               AssocRow.this.selection = type;
            }
         });
      }

      public void setStatus(WinRegAssociations.Level level) {
         if (this.statusLabel != null && !this.statusLabel.isDisposed()) {
            this.statusLabel.setText(statusText(level));
            this.statusLabel.getParent().layout();
         }
      }

      public int getSelection() {
         return this.selection;
      }

      public WinRegAssociations.AssocItem getItem() {
         return this.item;
      }
   }
}
