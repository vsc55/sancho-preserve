package sancho.view;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import sancho.core.Sancho;
import sancho.view.statusline.CoreConsoleItem;
import sancho.view.statusline.IStatusItem;
import sancho.view.statusline.LinkEntry;
import sancho.view.statusline.LinkEntryItem;
import sancho.view.statusline.NetworkItem;
import sancho.view.statusline.RateItem;
import sancho.view.statusline.StatusConsole;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class StatusLine {
   protected CLabel cLabel;
   protected Composite linkEntryComposite;
   protected Composite consoleComposite;
   protected MainWindow mainWindow;
   protected List statusItemList;
   protected Composite statusLineComposite;
   protected StatusConsole statusConsole;

   public StatusLine(MainWindow mainWindow, SashForm sashForm, Composite composite, Composite composite2) {
      this.mainWindow = mainWindow;
      this.statusItemList = new ArrayList();
      this.createContents(sashForm, composite, composite2);
   }

   private void addSeparator(Composite composite) {
      Label label = new Label(composite, 514);
      label.setLayoutData(WidgetFactory.createGridData(1040, -1, 0));
   }

   public void clear() {
      if (!this.cLabel.isDisposed()) {
         this.cLabel.setText("");
         this.cLabel.setToolTipText("");
         this.cLabel.setImage(null);
      }
   }

   public void updateDisplay() {
      this.statusConsole.updateDisplay();
   }

   protected void createContents(SashForm sashForm, Composite composite, Composite composite2) {
      boolean hasCoreConsole = Sancho.getCoreConsole() != null;
      Composite containerComposite = new Composite(this.mainWindow.getMainComposite(), 2048);
      containerComposite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      containerComposite.setLayoutData(new GridData(768));
      this.linkEntryComposite = this.createHiddenComposite(containerComposite);
      new LinkEntry(this, this.linkEntryComposite);
      this.statusLineComposite = new Composite(containerComposite, 0);
      this.statusLineComposite.setLayout(WidgetFactory.createGridLayout(hasCoreConsole ? 9 : 7, 0, 0, 0, 0, false));
      this.statusLineComposite.setLayoutData(new GridData(768));
      this.statusItemList.add(new NetworkItem(this));
      this.addSeparator(this.statusLineComposite);
      if (hasCoreConsole) {
         new CoreConsoleItem(this);
         this.addSeparator(this.statusLineComposite);
      }

      Composite labelComposite = new Composite(this.statusLineComposite, 0);
      labelComposite.setLayout(new FillLayout());
      labelComposite.setLayoutData(new GridData(1808));
      this.cLabel = new CLabel(labelComposite, 0);
      this.cLabel.addMouseListener(new MouseAdapter() {
         public void mouseUp(MouseEvent event) {
            CLabel label = (CLabel)event.widget;
            if (label.getText().equals(SResources.getString("l.newMessage"))) {
               StatusLine.this.getMainWindow().switchToFriends();
            }
         }
      });
      this.cLabel.setText("");
      this.addSeparator(this.statusLineComposite);
      this.statusConsole = new StatusConsole(sashForm, composite, composite2);
      this.statusItemList.add(this.statusConsole);
      // The win32-specific link entry paired with the removed WebBrowserTab_win32;
      // the standard WebBrowserTab is now used everywhere, so use LinkEntryItem.
      new LinkEntryItem(this, this.statusConsole);

      this.addSeparator(this.statusLineComposite);
      this.statusItemList.add(new RateItem(this));
      if (Sancho.hasCollectionFactory()) {
         this.setText(Sancho.getCoreFactory().getStatusString());
      }
   }

   private Composite createHiddenComposite(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      composite.setLayoutData(WidgetFactory.createGridData(768, -1, 0));
      return composite;
   }

   public Composite getLinkEntryComposite() {
      return this.linkEntryComposite;
   }

   public Composite getConsoleComposite() {
      return this.consoleComposite;
   }

   public MainWindow getMainWindow() {
      return this.mainWindow;
   }

   public Composite getStatusline() {
      return this.statusLineComposite;
   }

   public void setConnected(boolean connected) {
      for (int i = 0; i < this.statusItemList.size(); i++) {
         ((IStatusItem)this.statusItemList.get(i)).setConnected(connected);
      }
   }

   public void setImage(Image image) {
      if (!this.cLabel.isDisposed()) {
         this.cLabel.setImage(image);
      }
   }

   public void setText(String text) {
      if (!this.cLabel.isDisposed()) {
         this.cLabel.setText(text);
         this.cLabel.update();
      }
   }

   public void setToolTip(String toolTip) {
      if (!this.cLabel.isDisposed()) {
         this.cLabel.setToolTipText(toolTip);
      }
   }
}
