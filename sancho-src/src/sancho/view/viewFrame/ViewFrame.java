package sancho.view.viewFrame;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import sancho.core.ICore;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.HeaderBarMouseAdapter;
import sancho.view.utility.MyViewForm;
import sancho.view.utility.NoDuplicatesCombo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewer.GView;

public class ViewFrame {
   protected boolean active;
   protected AbstractTab aTab;
   protected Composite childComposite;
   protected CLabel cLabel;
   protected ToolItem clearRefineToolItem;
   protected GView gView;
   protected MenuManager menuManager;
   protected Composite parent;
   protected String prefString;
   protected Combo refineText;
   protected ToolBar toolBar;
   protected MyViewForm viewForm;
   protected boolean visible;

   public ViewFrame(Composite var1, String var2, String var3, AbstractTab var4) {
      this(var1, var2, var3, var4, false);
   }

   public ViewFrame(Composite var1, final String var2, final String var3, AbstractTab var4, boolean var5) {
      this.parent = var1;
      this.aTab = var4;
      this.prefString = var2;
      this.viewForm = WidgetFactory.createViewForm(this.parent, var5);
      this.childComposite = new Composite(this.viewForm, 0);
      this.childComposite.setLayout(new FillLayout());
      this.cLabel = WidgetFactory.createCLabel(this.viewForm, var2, var3);
      this.viewForm.setContent(this.childComposite);
      this.viewForm.setTopLeft(this.cLabel);
      this.cLabel.addMouseTrackListener(new MouseTrackListener() {
         private Image newImage;

         public void mouseHover(MouseEvent var1) {
         }

         public void disposeImage() {
            if (this.newImage != null) {
               this.newImage.dispose();
               this.newImage = null;
            }
         }

         public void mouseEnter(MouseEvent var1) {
            this.disposeImage();
            this.newImage = SResources.createActiveImage(SResources.getImageDescriptor(var3));
            ViewFrame.this.cLabel.setImage(this.newImage);
         }

         public void mouseExit(MouseEvent var1) {
            ViewFrame.this.cLabel.setImage(SResources.getImage(var3));
            this.disposeImage();
         }
      });
   }

   public void addPopupMenu(ToolBar var1) {
      MenuManager var2 = new MenuManager();
      var2.setRemoveAllWhenShown(true);
      var2.addMenuListener(new RefineMenuListener());
      var1.setMenu(var2.createContextMenu(var1));
   }

   public void setRefineText(String var1) {
      if (this.refineText != null) {
         this.refineText.setText(var1);
      }
   }

   public void addRefine() {
      this.clearRefineToolItem = new ToolItem(this.toolBar, 0);
      this.clearRefineToolItem.setImage(SResources.getImage("refine"));
      this.clearRefineToolItem.setToolTipText(SResources.getString("ti.clearRefine"));
      this.clearRefineToolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            if (ViewFrame.this.refineText != null) {
               ViewFrame.this.refineText.add(ViewFrame.this.refineText.getText(), 0);
               ViewFrame.this.refineText.setText("");
            }

            if (ViewFrame.this.getGView() != null) {
               ViewFrame.this.getGView().setRefineString("");
            }
         }
      });
      this.addPopupMenu(this.toolBar);
      ToolItem var1 = new ToolItem(this.toolBar, 2);
      this.refineText = new NoDuplicatesCombo(this.toolBar, 2048);
      this.refineText.setItems(PreferenceLoader.loadStringArray(this.prefString + ".refineSArray"));
      this.refineText.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent var1) {
            NoDuplicatesCombo var2 = (NoDuplicatesCombo)var1.widget;
            String[] var3 = var2.getItems();

            for (int var4 = 0; var4 < var3.length; var4++) {
               if (var3[var4].length() > 1000) {
                  var3[var4] = var3[var4].substring(0, 1000);
               }
            }

            PreferenceLoader.setValue(ViewFrame.this.prefString + ".refineSArray", var3, 25);
         }
      });
      this.refineText.setToolTipText(SResources.getString("ti.refine"));
      this.refineText.setSize(75, -1);
      if (SWT.getPlatform().equals("fox")) {
         var1.setControl(this.refineText);
      }

      var1.setWidth(75);
      this.refineText.pack();
      if (!SWT.getPlatform().equals("fox")) {
         var1.setControl(this.refineText);
      }

      this.refineText.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            NoDuplicatesCombo var2 = (NoDuplicatesCombo)var1.widget;
            if (var2.getSelectionIndex() > -1) {
               ViewFrame.this.refineText.setText(var2.getItem(var2.getSelectionIndex()));
            }

            if (ViewFrame.this.getGView() != null) {
               ViewFrame.this.getGView().setRefineString(ViewFrame.this.refineText.getText());
            }
         }
      });
      if (SWT.getPlatform().equals("fox")) {
         this.refineText.setSize(75, this.refineText.getSize().y);
         this.refineText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent var1) {
               ViewFrame.this.updateRefine(var1);
            }
         });
      } else {
         this.refineText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent var1) {
               ViewFrame.this.updateRefine(var1);
            }
         });
      }

      if (this.getGView() == null) {
         this.refineText.setEnabled(false);
         this.clearRefineToolItem.setEnabled(false);
      }
   }

   public ToolItem addToolItem(String var1, String var2, SelectionListener var3) {
      ToolItem var4 = new ToolItem(this.toolBar, 0);
      var4.setToolTipText(SResources.getString(var1));
      var4.setImage(SResources.getImage(var2));
      var4.addSelectionListener(var3);
      return var4;
   }

   public void addToolSeparator() {
      new ToolItem(this.toolBar, 2);
   }

   public void createViewListener(ViewListener var1) {
      this.setupViewListener(var1);
      this.cLabel.addMouseListener(new HeaderBarMouseAdapter(this.cLabel, this.menuManager));
   }

   public void createViewToolBar() {
      Composite var1 = new Composite(this.viewForm, 0);
      var1.setLayout(WidgetFactory.createGridLayout(1, 1, 1, 0, 0, false));
      this.toolBar = new ToolBar(var1, 8519680);
      this.toolBar.setBackground(this.toolBar.getDisplay().getSystemColor(22));
      this.viewForm.setTopRight(var1);
   }

   public Composite getChildComposite() {
      return this.childComposite;
   }

   public CLabel getCLabel() {
      return this.cLabel;
   }

   public Control getControl() {
      return this.getViewForm();
   }

   public ICore getCore() {
      return this.getATab().getMainWindow().getCore();
   }

   public AbstractTab getATab() {
      return this.aTab;
   }

   public GView getGView() {
      return this.gView;
   }

   public Composite getParent() {
      return this.parent;
   }

   public MyViewForm getViewForm() {
      return this.viewForm;
   }

   public boolean isActive() {
      return this.active;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public void onConnect() {
      if (this.gView != null) {
         this.gView.setInput();
         this.gView.onConnect();
      }
   }

   public void onDisconnect() {
      if (this.gView != null) {
         this.gView.unsetInput();
         this.gView.onDisconnect();
      }

      this.resetLabel();
   }

   public void resetLabel() {
      if (this.cLabel != null) {
         this.cLabel.setText(SResources.getString(this.prefString));
      }
   }

   public void setActive(boolean var1) {
      this.active = var1;
      if (this.gView != null) {
         this.gView.setActive(var1);
      }
   }

   protected void setupViewListener(ViewListener var1) {
      this.menuManager = new MenuManager("");
      this.menuManager.setRemoveAllWhenShown(true);
      this.menuManager.addMenuListener(var1);
      this.cLabel.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent var1) {
            ViewFrame.this.menuManager.dispose();
         }
      });
   }

   public void setVisible(boolean var1) {
      this.visible = var1;
      if (this.gView != null) {
         this.gView.setVisible(var1);
      }
   }

   public void updateCLabelText(String var1) {
      if (this.cLabel != null && !this.cLabel.isDisposed() && !this.cLabel.getText().equals(var1)) {
         this.cLabel.setText(var1);
      }
   }

   public void updateCLabelTextInGuiThread(final String var1) {
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.getDisplay().asyncExec(new Runnable() {
            public void run() {
               ViewFrame.this.updateCLabelText(var1);
            }
         });
      }
   }

   public void updateCLabelToolTip(String var1) {
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.setToolTipText(var1);
      }
   }

   public void updateCLabelToolTipInGuiThread(final String var1) {
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.getDisplay().asyncExec(new Runnable() {
            public void run() {
               ViewFrame.this.updateCLabelToolTip(var1);
            }
         });
      }
   }

   public void dispose() {
      this.aTab = null;
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.dispose();
         this.cLabel = null;
      }

      if (this.menuManager != null) {
         IContributionItem[] var1 = this.menuManager.getItems();

         for (int var2 = 0; var2 < var1.length; var2++) {
            var1[var2].dispose();
         }

         this.menuManager.removeAll();
         this.menuManager.dispose();
         this.menuManager = null;
      }

      if (this.toolBar != null) {
         ToolItem[] var3 = this.toolBar.getItems();

         for (int var5 = 0; var5 < var3.length; var5++) {
            var3[var5].dispose();
         }

         this.toolBar.dispose();
         this.toolBar = null;
      }

      if (this.gView != null) {
         Composite var4 = this.gView.getComposite();
         if (var4 != null && !var4.isDisposed()) {
            var4.dispose();
         }
      }
   }

   public void updateDisplay() {
      this.cLabel.setFont(PreferenceLoader.loadFont("headerFontData"));
      if (this.gView != null) {
         this.gView.updateDisplay();
      }
   }

   public void updateRefine(KeyEvent var1) {
      switch (var1.keyCode) {
         default:
            if (this.getGView() != null) {
               this.getGView().setRefineString(this.refineText.getText());
            }
         case 13:
         case 16777217:
         case 16777218:
         case 16777219:
         case 16777220:
         case 16777221:
         case 16777222:
         case 16777296:
      }
   }

   // Menu listener that populates the refine tool-bar popup with the filter-toggle actions.
   private static class RefineMenuListener implements IMenuListener {
      public void menuAboutToShow(IMenuManager var1) {
         var1.add(new ToggleRefineAction("mi.refineFilterNegation", "refineFilterNegation"));
         var1.add(new ToggleRefineAction("mi.refineFilterAlternates", "refineFilterAlternates"));
      }
   }

   // Checkbox menu action that toggles a boolean refine-filter preference on/off.
   private static class ToggleRefineAction extends Action {
      String prefString;

      public ToggleRefineAction(String var1, String var2) {
         super(SResources.getString(var1), 2);
         this.prefString = var2;
      }

      public boolean isChecked() {
         return PreferenceLoader.loadBoolean(this.prefString);
      }

      public void run() {
         PreferenceLoader.getPreferenceStore().setValue(this.prefString, !this.isChecked());
         PreferenceLoader.saveStore();
      }
   }
}
