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

   public StatusConsole(SashForm var1, Composite var2, Composite var3) {
      this.sashForm = var1;
      this.pageContainer = var2;
      this.composite = var3;
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

   public void setVisible(boolean var1) {
      WidgetFactory.toggleMaximizedSashFormControl(this.sashForm, this.pageContainer);
      this.isVisible = var1;
      this.observe(var1);
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (var1 instanceof ConsoleMessage) {
         this.updateConsole((String)var2);
      }
   }

   public void updateConsole(String var1) {
      if (this.composite != null && !this.composite.isDisposed()) {
         this.composite.getDisplay().asyncExec(new StatusConsole$1(this, var1));
      }
   }

   public void observe(boolean var1) {
      if (var1) {
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

   public void setConnected(boolean var1) {
      if (var1 && this.isVisible) {
         this.observe(true);
      }
   }

   // $VF: synthetic method
   static Console access$000(StatusConsole var0) {
      return var0.console;
   }
}
