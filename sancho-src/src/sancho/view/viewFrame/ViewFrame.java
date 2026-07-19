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

   public ViewFrame(Composite parent, String prefString, String imageString, AbstractTab tab) {
      this(parent, prefString, imageString, tab, false);
   }

   public ViewFrame(Composite parent, final String prefString, final String imageString, AbstractTab tab, boolean flag) {
      this.parent = parent;
      this.aTab = tab;
      this.prefString = prefString;
      this.viewForm = WidgetFactory.createViewForm(this.parent, flag);
      this.childComposite = new Composite(this.viewForm, 0);
      this.childComposite.setLayout(new FillLayout());
      this.cLabel = WidgetFactory.createCLabel(this.viewForm, prefString, imageString);
      this.viewForm.setContent(this.childComposite);
      this.viewForm.setTopLeft(this.cLabel);
      this.cLabel.addMouseTrackListener(new MouseTrackListener() {
         private Image newImage;

         public void mouseHover(MouseEvent event) {
         }

         public void disposeImage() {
            if (this.newImage != null) {
               this.newImage.dispose();
               this.newImage = null;
            }
         }

         public void mouseEnter(MouseEvent event) {
            this.disposeImage();
            this.newImage = SResources.createActiveImage(SResources.getImageDescriptor(imageString));
            ViewFrame.this.cLabel.setImage(this.newImage);
         }

         public void mouseExit(MouseEvent event) {
            ViewFrame.this.cLabel.setImage(SResources.getImage(imageString));
            this.disposeImage();
         }
      });
   }

   public void addPopupMenu(ToolBar toolBar) {
      MenuManager menuManager = new MenuManager();
      menuManager.setRemoveAllWhenShown(true);
      menuManager.addMenuListener(new RefineMenuListener());
      toolBar.setMenu(menuManager.createContextMenu(toolBar));
   }

   public void setRefineText(String text) {
      if (this.refineText != null) {
         this.refineText.setText(text);
      }
   }

   public void addRefine() {
      this.clearRefineToolItem = new ToolItem(this.toolBar, 0);
      this.clearRefineToolItem.setImage(SResources.getImage("refine"));
      this.clearRefineToolItem.setToolTipText(SResources.getString("ti.clearRefine"));
      this.clearRefineToolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
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
      ToolItem toolItem = new ToolItem(this.toolBar, 2);
      this.refineText = new NoDuplicatesCombo(this.toolBar, 2048);
      this.refineText.setItems(PreferenceLoader.loadStringArray(this.prefString + ".refineSArray"));
      this.refineText.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent event) {
            NoDuplicatesCombo combo = (NoDuplicatesCombo)event.widget;
            String[] items = combo.getItems();

            for (int i = 0; i < items.length; i++) {
               if (items[i].length() > 1000) {
                  items[i] = items[i].substring(0, 1000);
               }
            }

            PreferenceLoader.setValue(ViewFrame.this.prefString + ".refineSArray", items, 25);
         }
      });
      this.refineText.setToolTipText(SResources.getString("ti.refine"));
      this.refineText.setSize(75, -1);
      if (SWT.getPlatform().equals("fox")) {
         toolItem.setControl(this.refineText);
      }

      toolItem.setWidth(75);
      this.refineText.pack();
      if (!SWT.getPlatform().equals("fox")) {
         toolItem.setControl(this.refineText);
      }

      this.refineText.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            NoDuplicatesCombo combo = (NoDuplicatesCombo)event.widget;
            if (combo.getSelectionIndex() > -1) {
               ViewFrame.this.refineText.setText(combo.getItem(combo.getSelectionIndex()));
            }

            if (ViewFrame.this.getGView() != null) {
               ViewFrame.this.getGView().setRefineString(ViewFrame.this.refineText.getText());
            }
         }
      });
      if (SWT.getPlatform().equals("fox")) {
         this.refineText.setSize(75, this.refineText.getSize().y);
         this.refineText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
               ViewFrame.this.updateRefine(event);
            }
         });
      } else {
         this.refineText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
               ViewFrame.this.updateRefine(event);
            }
         });
      }

      if (this.getGView() == null) {
         this.refineText.setEnabled(false);
         this.clearRefineToolItem.setEnabled(false);
      }
   }

   public ToolItem addToolItem(String tooltip, String imageString, SelectionListener listener) {
      ToolItem toolItem = new ToolItem(this.toolBar, 0);
      toolItem.setToolTipText(SResources.getString(tooltip));
      toolItem.setImage(SResources.getImage(imageString));
      toolItem.addSelectionListener(listener);
      return toolItem;
   }

   public void addToolSeparator() {
      new ToolItem(this.toolBar, 2);
   }

   public void createViewListener(ViewListener viewListener) {
      this.setupViewListener(viewListener);
      this.cLabel.addMouseListener(new HeaderBarMouseAdapter(this.cLabel, this.menuManager));
   }

   public void createViewToolBar() {
      Composite composite = new Composite(this.viewForm, 0);
      composite.setLayout(WidgetFactory.createGridLayout(1, 1, 1, 0, 0, false));
      this.toolBar = new ToolBar(composite, 8519680);
      this.toolBar.setBackground(this.toolBar.getDisplay().getSystemColor(22));
      this.viewForm.setTopRight(composite);
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

   public void setActive(boolean active) {
      this.active = active;
      if (this.gView != null) {
         this.gView.setActive(active);
      }
   }

   protected void setupViewListener(ViewListener viewListener) {
      this.menuManager = new MenuManager("");
      this.menuManager.setRemoveAllWhenShown(true);
      this.menuManager.addMenuListener(viewListener);
      this.cLabel.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent event) {
            ViewFrame.this.menuManager.dispose();
         }
      });
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
      if (this.gView != null) {
         this.gView.setVisible(visible);
      }
   }

   public void updateCLabelText(String text) {
      if (this.cLabel != null && !this.cLabel.isDisposed() && !this.cLabel.getText().equals(text)) {
         this.cLabel.setText(text);
      }
   }

   public void updateCLabelTextInGuiThread(final String text) {
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.getDisplay().asyncExec(new Runnable() {
            public void run() {
               ViewFrame.this.updateCLabelText(text);
            }
         });
      }
   }

   public void updateCLabelToolTip(String text) {
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.setToolTipText(text);
      }
   }

   public void updateCLabelToolTipInGuiThread(final String text) {
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.getDisplay().asyncExec(new Runnable() {
            public void run() {
               ViewFrame.this.updateCLabelToolTip(text);
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
         IContributionItem[] items = this.menuManager.getItems();

         for (int i = 0; i < items.length; i++) {
            items[i].dispose();
         }

         this.menuManager.removeAll();
         this.menuManager.dispose();
         this.menuManager = null;
      }

      if (this.toolBar != null) {
         ToolItem[] toolItems = this.toolBar.getItems();

         for (int i = 0; i < toolItems.length; i++) {
            toolItems[i].dispose();
         }

         this.toolBar.dispose();
         this.toolBar = null;
      }

      if (this.gView != null) {
         Composite composite = this.gView.getComposite();
         if (composite != null && !composite.isDisposed()) {
            composite.dispose();
         }
      }
   }

   public void updateDisplay() {
      this.cLabel.setFont(PreferenceLoader.loadFont("headerFontData"));
      if (this.gView != null) {
         this.gView.updateDisplay();
      }
   }

   public void updateRefine(KeyEvent event) {
      switch (event.keyCode) {
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
      public void menuAboutToShow(IMenuManager menuManager) {
         menuManager.add(new ToggleRefineAction("mi.refineFilterNegation", "refineFilterNegation"));
         menuManager.add(new ToggleRefineAction("mi.refineFilterAlternates", "refineFilterAlternates"));
      }
   }

   // Checkbox menu action that toggles a boolean refine-filter preference on/off.
   private static class ToggleRefineAction extends Action {
      String prefString;

      public ToggleRefineAction(String text, String prefString) {
         super(SResources.getString(text), 2);
         this.prefString = prefString;
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
