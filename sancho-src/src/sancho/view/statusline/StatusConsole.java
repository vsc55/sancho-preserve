package sancho.view.statusline;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.ConsoleMessage;
import sancho.utility.MyObservable;
import sancho.view.console.Console;
import sancho.view.console.ConsoleViewFrame;
import sancho.view.utility.WidgetFactory;

public class StatusConsole implements IStatusItem {
   private Composite composite;
   private Composite pageContainer;
   private SashForm sashForm;
   private Console console;
   private ConsoleViewFrame viewFrame;
   private boolean isVisible;

   public StatusConsole(SashForm sashForm, Composite pageContainer, Composite composite) {
      this.sashForm = sashForm;
      this.pageContainer = pageContainer;
      this.composite = composite;
      this.viewFrame = new ConsoleViewFrame(this.composite, "tab.console", "tab.console.buttonSmall", null);
      this.console = new Console(this.viewFrame.getChildComposite(), 0);
      if (this.isVisible()) {
         this.isVisible = true;
         this.observe(true);
      }
   }

   public boolean isVisible() {
      return this.sashForm.getMaximizedControl() == null;
   }

   public void toggleVisible() {
      this.setVisible(!this.isVisible);
   }

   public void setVisible(boolean visible) {
      WidgetFactory.toggleMaximizedSashFormControl(this.sashForm, this.pageContainer);
      this.isVisible = visible;
      this.observe(visible);
   }

   public void update(MyObservable observable, Object value, int id) {
      if (observable instanceof ConsoleMessage) {
         this.updateConsole((String)value);
      }
   }

   public void updateConsole(final String text) {
      if (this.composite != null && !this.composite.isDisposed()) {
         this.composite.getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (Sancho.hasCollectionFactory() && StatusConsole.this.console != null && !StatusConsole.this.console.isDisposed()) {
                  StatusConsole.this.console.append(text);
               }
            }
         });
      }
   }

   public void observe(boolean enabled) {
      if (enabled) {
         if (Sancho.hasCollectionFactory()) {
            Sancho.getCore().getConsoleMessage().addObserver(this);
            this.updateConsole(Sancho.getCore().getConsoleMessage().getMessage());
         }
      } else if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getConsoleMessage().deleteObserver(this);
      }
   }

   public void updateDisplay() {
      this.viewFrame.updateDisplay();
      this.console.updateDisplay();
   }

   public void setConnected(boolean connected) {
      if (connected && this.isVisible) {
         this.observe(true);
      }
   }
}
