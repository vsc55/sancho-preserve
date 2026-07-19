package sancho.view.utility;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

   public AbstractTab(MainWindow mainWindow, String buttonName) {
      this.mainWindow = mainWindow;
      this.contentComposite = new Composite(mainWindow.getPageContainer(), 0);
      this.contentComposite.setLayout(new FillLayout());
      this.contentComposite.setLayoutData(new GridData(1808));
      this.createButton(buttonName);
      this.createContents(this.contentComposite);
   }

   public void addViewFrame(ViewFrame viewFrame) {
      if (this.viewFrameList == null) {
         this.viewFrameList = new ArrayList();
      }

      this.viewFrameList.add(viewFrame);
   }

   public void allViewFramesUpdateDisplay() {
      if (this.viewFrameList != null) {
         for (int i = 0; i < this.viewFrameList.size(); i++) {
            ViewFrame viewFrame = (ViewFrame)this.viewFrameList.get(i);
            viewFrame.updateDisplay();
         }
      }
   }

   public void createButton(String buttonName) {
      this.toolButton = new ToolButton(this.mainWindow.getCoolBar().getToolBar(), 8);
      this.toolButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            if (!AbstractTab.this.isActive()) {
               AbstractTab.this.setActive();
            }
         }
      });
      this.toolButton.setText(SResources.getString(buttonName));
      this.toolButton.setToolTipText(SResources.getString(buttonName + ".tooltip"));
      this.toolButton.setBigActiveImage(SResources.getImage(buttonName + ".buttonActive"));
      this.toolButton.setBigInactiveImage(SResources.getImage(buttonName + ".button"));
      this.toolButton.setSmallActiveImage(SResources.getImage(buttonName + ".buttonSmallActive"));
      this.toolButton.setSmallInactiveImage(SResources.getImage(buttonName + ".buttonSmall"));
      this.toolButton.useSmallButtons(this.mainWindow.getCoolBar().isToolbarSmallButtons());
      this.toolButton.setActive(false);
      this.toolButton.resetImage();
      this.mainWindow.getCoolBar().getMainToolButtons().add(this.toolButton);
   }

   protected abstract void createContents(Composite composite);

   public void dispose() {
      this.toolButton.dispose();

      for (int i = 0; i < this.viewFrameList.size(); i++) {
         ViewFrame viewFrame = (ViewFrame)this.viewFrameList.get(i);
         viewFrame.dispose();
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
         for (int i = 0; i < this.viewFrameList.size(); i++) {
            ViewFrame viewFrame = (ViewFrame)this.viewFrameList.get(i);
            viewFrame.onConnect();
         }
      }
   }

   public void onDisconnect() {
      if (this.viewFrameList != null) {
         for (int i = 0; i < this.viewFrameList.size(); i++) {
            ViewFrame viewFrame = (ViewFrame)this.viewFrameList.get(i);
            viewFrame.onDisconnect();
         }
      }
   }

   public void setVisible(boolean visible) {
      if (this.viewFrameList != null) {
         for (int i = 0; i < this.viewFrameList.size(); i++) {
            ViewFrame viewFrame = (ViewFrame)this.viewFrameList.get(i);
            if (viewFrame != null) {
               viewFrame.setVisible(visible);
            }
         }
      }
   }

   public void removeViewFrame(ViewFrame viewFrame) {
      this.viewFrameList.remove(viewFrame);
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

   public void toggleAllViewFramesActive(boolean active) {
      if (this.viewFrameList != null) {
         for (int i = 0; i < this.viewFrameList.size(); i++) {
            ViewFrame viewFrame = (ViewFrame)this.viewFrameList.get(i);
            if (viewFrame != null) {
               viewFrame.setActive(active);
            }
         }
      }
   }

   public void updateDisplay() {
      this.getContent().layout();
      this.allViewFramesUpdateDisplay();
   }
}
