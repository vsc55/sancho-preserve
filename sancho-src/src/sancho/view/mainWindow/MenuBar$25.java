package sancho.view.mainWindow;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Scale;

class MenuBar$25 extends SelectionAdapter {
   // $VF: synthetic field
   private final Scale val$scale;
   // $VF: synthetic field
   private final MenuBar$AlphaInputDialog this$0;

   MenuBar$25(MenuBar$AlphaInputDialog var1, Scale var2) {
      this.this$0 = var1;
      this.val$scale = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      int var2 = this.val$scale.getSelection();
      this.this$0.mainShell.setAlpha(var2);
      this.this$0.getShell().setText(String.valueOf(var2));
   }
}
