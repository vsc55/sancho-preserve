package sancho.view.viewer.actions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import sancho.view.utility.IDSelector;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;

public class ColumnSelectorAction extends Action {
   private List gViewList;

   private ColumnSelectorAction() {
      super(SResources.getString("l.tableColumns") + " " + SResources.getString("l.selector"));
      this.setImageDescriptor(SResources.getImageDescriptor("preferences"));
      this.gViewList = new ArrayList();
   }

   public ColumnSelectorAction(GView var1) {
      this();
      this.gViewList.add(var1);
   }

   public ColumnSelectorAction(CTabFolder var1) {
      this();

      for (int var2 = 0; var2 < var1.getItems().length; var2++) {
         CTabItem var3 = var1.getItems()[var2];
         if (var3.getData("gView") != null) {
            this.gViewList.add(var3.getData("gView"));
         }
      }
   }

   public void run() {
      if (this.gViewList.size() != 0) {
         GView var1 = (GView)this.gViewList.get(0);
         IDSelector var2 = new IDSelector(var1.getShell(), var1.getColumnLabels(), var1.getPreferenceString(), "TableColumns", "l.tableColumns");
         if (var2.open() == 0) {
            var2.savePrefs();

            for (int var3 = 0; var3 < this.gViewList.size(); var3++) {
               ((GView)this.gViewList.get(var3)).resetColumns();
            }
         }
      }
   }
}
