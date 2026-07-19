package sancho.view.search.result;

import java.util.Arrays;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.model.mldonkey.Result;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class ResultDetailDialog extends Dialog {
   Result result;

   public ResultDetailDialog(Shell shell, Result result) {
      super(shell);
      this.result = result;
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setImage(this.result.getFileTypeImage());
      shell.setText(SResources.getString("s.r.resultDetails"));
   }

   protected void createButtonsForButtonBar(Composite parent) {
      this.createButton(parent, 0, SResources.getString("b.ok"), true);
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 0, 5, false));
      Text text = new Text(composite, 2058);
      text.setLayoutData(new GridData(768));
      text.setText(this.result.getToolTip());
      String[] names = this.result.getNames();
      if (names != null && names.length > 1) {
         GC gc = new GC(parent);
         int charHeight = gc.getFontMetrics().getHeight();
         gc.dispose();
         int count = this.result.getNames().length;
         count = count > 6 ? 6 : count;
         String[] sortedNames = new String[names.length];
         System.arraycopy(names, 0, sortedNames, 0, names.length);
         Arrays.sort(sortedNames, String.CASE_INSENSITIVE_ORDER);
         List namesList = new List(composite, 2816);
         namesList.setItems(sortedNames);
         GridData gridData = new GridData(768);
         gridData.heightHint = count * charHeight;
         namesList.setLayoutData(gridData);
      }

      return composite;
   }
}
