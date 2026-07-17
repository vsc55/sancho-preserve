package sancho.view;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.ConsoleMessage;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.view.console.Console;
import sancho.view.console.ConsoleViewFrame;
import sancho.view.utility.AbstractTab;

public class ConsoleTab extends AbstractTab implements MyObserver {
   private Console console;

   public ConsoleTab(MainWindow var1, String var2) {
      super(var1, var2);
   }

   protected void createContents(Composite var1) {
      ConsoleViewFrame var2 = new ConsoleViewFrame(var1, "tab.console", "tab.console.buttonSmall", this);
      this.addViewFrame(var2);
      this.console = new Console(var2.getChildComposite(), 0);
   }

   public void setInActive() {
      super.setInActive();
      this.console.setInactive();
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getConsoleMessage().deleteObserver(this);
      }
   }

   public void setActive() {
      super.setActive();
      this.setObservers();
      this.console.setActive();
      this.console.setFocus();
   }

   public void setObservers() {
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getConsoleMessage().addObserver(this);
         this.updateConsole(this.getCore().getConsoleMessage().getMessage());
      }
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (var1 instanceof ConsoleMessage) {
         this.updateConsole((String)var2);
      }
   }

   public void updateConsole(String var1) {
      if (this.getContent() != null && !this.getContent().isDisposed()) {
         this.getContent().getDisplay().asyncExec(new ConsoleTab$1(this, var1));
      }
   }

   public void onConnect() {
      if (this.isActive()) {
         this.setObservers();
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.console.updateDisplay();
   }

   public void dispose() {
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getConsoleMessage().deleteObserver(this);
      }

      this.console.dispose();
      super.dispose();
   }

   // $VF: synthetic method
   static Console access$000(ConsoleTab var0) {
      return var0.console;
   }
}
