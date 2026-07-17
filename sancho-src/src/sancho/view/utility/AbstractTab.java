package sancho.view.utility;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.view.MainWindow;
import sancho.view.viewFrame.ViewFrame;

public abstract class AbstractTab {
   private boolean active;
   protected Composite contentComposite;
   protected MainWindow mainWindow;
   protected ToolButton toolButton;
   protected List viewFrameList;

   public AbstractTab(MainWindow var1, String var2) {
      this.mainWindow = var1;
      this.contentComposite = new Composite(var1.getPageContainer(), 0);
      this.contentComposite.setLayout(new FillLayout());
      this.contentComposite.setLayoutData(new GridData(1808));
      this.createButton(var2);
      this.createContents(this.contentComposite);
   }

   public void addViewFrame(ViewFrame var1) {
      if (this.viewFrameList == null) {
         this.viewFrameList = new ArrayList();
      }

      this.viewFrameList.add(var1);
   }

   public void allViewFramesUpdateDisplay() {
      if (this.viewFrameList != null) {
         for (int var1 = 0; var1 < this.viewFrameList.size(); var1++) {
            ViewFrame var2 = (ViewFrame)this.viewFrameList.get(var1);
            var2.updateDisplay();
         }
      }
   }

   public void createButton(String var1) {
      this.toolButton = new ToolButton(this.mainWindow.getCoolBar().getToolBar(), 8);
      this.toolButton.addSelectionListener(new AbstractTab$1(this));
      this.toolButton.setText(SResources.getString(var1));
      this.toolButton.setToolTipText(SResources.getString(var1 + ".tooltip"));
      this.toolButton.setBigActiveImage(SResources.getImage(var1 + ".buttonActive"));
      this.toolButton.setBigInactiveImage(SResources.getImage(var1 + ".button"));
      this.toolButton.setSmallActiveImage(SResources.getImage(var1 + ".buttonSmallActive"));
      this.toolButton.setSmallInactiveImage(SResources.getImage(var1 + ".buttonSmall"));
      this.toolButton.useSmallButtons(this.mainWindow.getCoolBar().isToolbarSmallButtons());
      this.toolButton.setActive(false);
      this.toolButton.resetImage();
      this.mainWindow.getCoolBar().getMainToolButtons().add(this.toolButton);
   }

   protected abstract void createContents(Composite var1);

   public void dispose() {
      this.toolButton.dispose();

      for (int var1 = 0; var1 < this.viewFrameList.size(); var1++) {
         ViewFrame var2 = (ViewFrame)this.viewFrameList.get(var1);
         var2.dispose();
      }

      this.viewFrameList.clear();
      if (this.contentComposite != null && !this.contentComposite.isDisposed()) {
         this.contentComposite.dispose();
      }
   }

   public Composite getContent() {
      return this.contentComposite;
   }

   public ICore getCore() {
      return Sancho.getCore();
   }

   public MainWindow getMainWindow() {
      return this.mainWindow;
   }

   public ToolButton getToolButton() {
      return this.toolButton;
   }

   public List getViewFrameList() {
      return this.viewFrameList;
   }

   public boolean isActive() {
      return this.active;
   }

   public void onConnect() {
      if (this.getCore() != null && this.isActive()) {
         this.getCore().setActiveTab(this);
      }

      if (this.viewFrameList != null) {
         for (int var1 = 0; var1 < this.viewFrameList.size(); var1++) {
            ViewFrame var2 = (ViewFrame)this.viewFrameList.get(var1);
            var2.onConnect();
         }
      }
   }

   public void onDisconnect() {
      if (this.viewFrameList != null) {
         for (int var1 = 0; var1 < this.viewFrameList.size(); var1++) {
            ViewFrame var2 = (ViewFrame)this.viewFrameList.get(var1);
            var2.onDisconnect();
         }
      }
   }

   public void setVisible(boolean var1) {
      if (this.viewFrameList != null) {
         for (int var2 = 0; var2 < this.viewFrameList.size(); var2++) {
            ViewFrame var3 = (ViewFrame)this.viewFrameList.get(var2);
            if (var3 != null) {
               var3.setVisible(var1);
            }
         }
      }
   }

   public void removeViewFrame(ViewFrame var1) {
      this.viewFrameList.remove(var1);
   }

   public void setActive() {
      this.active = true;
      this.mainWindow.setActive(this);
      this.toolButton.setActive(true);
      this.toggleAllViewFramesActive(true);
      if (this.getCore() != null) {
         this.getCore().setActiveTab(this);
      }
   }

   public void setInActive() {
      this.active = false;
      this.toolButton.setActive(false);
      this.toggleAllViewFramesActive(false);
   }

   public void toggleAllViewFramesActive(boolean var1) {
      if (this.viewFrameList != null) {
         for (int var2 = 0; var2 < this.viewFrameList.size(); var2++) {
            ViewFrame var3 = (ViewFrame)this.viewFrameList.get(var2);
            if (var3 != null) {
               var3.setActive(var1);
            }
         }
      }
   }

   public void updateDisplay() {
      this.getContent().layout();
      this.allViewFramesUpdateDisplay();
   }
}
