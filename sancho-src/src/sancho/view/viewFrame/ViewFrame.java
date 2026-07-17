package sancho.view.viewFrame;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionListener;
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

   public ViewFrame(Composite var1, String var2, String var3, AbstractTab var4, boolean var5) {
      this.parent = var1;
      this.aTab = var4;
      this.prefString = var2;
      this.viewForm = WidgetFactory.createViewForm(this.parent, var5);
      this.childComposite = new Composite(this.viewForm, 0);
      this.childComposite.setLayout(new FillLayout());
      this.cLabel = WidgetFactory.createCLabel(this.viewForm, var2, var3);
      this.viewForm.setContent(this.childComposite);
      this.viewForm.setTopLeft(this.cLabel);
      this.cLabel.addMouseTrackListener(new ViewFrame$1(this, var3));
   }

   public void addPopupMenu(ToolBar var1) {
      MenuManager var2 = new MenuManager();
      var2.setRemoveAllWhenShown(true);
      var2.addMenuListener(new ViewFrame$RefineMenuListener());
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
      this.clearRefineToolItem.addSelectionListener(new ViewFrame$2(this));
      this.addPopupMenu(this.toolBar);
      ToolItem var1 = new ToolItem(this.toolBar, 2);
      this.refineText = new NoDuplicatesCombo(this.toolBar, 2048);
      this.refineText.setItems(PreferenceLoader.loadStringArray(this.prefString + ".refineSArray"));
      this.refineText.addDisposeListener(new ViewFrame$3(this));
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

      this.refineText.addSelectionListener(new ViewFrame$4(this));
      if (SWT.getPlatform().equals("fox")) {
         this.refineText.setSize(75, this.refineText.getSize().y);
         this.refineText.addKeyListener(new ViewFrame$5(this));
      } else {
         this.refineText.addKeyListener(new ViewFrame$6(this));
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
      this.cLabel.addDisposeListener(new ViewFrame$7(this));
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

   public void updateCLabelTextInGuiThread(String var1) {
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.getDisplay().asyncExec(new ViewFrame$8(this, var1));
      }
   }

   public void updateCLabelToolTip(String var1) {
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.setToolTipText(var1);
      }
   }

   public void updateCLabelToolTipInGuiThread(String var1) {
      if (this.cLabel != null && !this.cLabel.isDisposed()) {
         this.cLabel.getDisplay().asyncExec(new ViewFrame$9(this, var1));
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
}
