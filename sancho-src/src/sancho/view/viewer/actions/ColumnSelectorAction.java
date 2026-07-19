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

   public ColumnSelectorAction(GView gView) {
      this();
      this.gViewList.add(gView);
   }

   public ColumnSelectorAction(CTabFolder cTabFolder) {
      this();

      for (int i = 0; i < cTabFolder.getItems().length; i++) {
         CTabItem item = cTabFolder.getItems()[i];
         if (item.getData("gView") != null) {
            this.gViewList.add(item.getData("gView"));
         }
      }
   }

   public void run() {
      if (this.gViewList.size() != 0) {
         GView gView = (GView)this.gViewList.get(0);
         IDSelector idSelector = new IDSelector(gView.getShell(), gView.getColumnLabels(), gView.getPreferenceString(), "TableColumns", "l.tableColumns");
         if (idSelector.open() == 0) {
            idSelector.savePrefs();

            for (int i = 0; i < this.gViewList.size(); i++) {
               ((GView)this.gViewList.get(i)).resetColumns();
            }
         }
      }
   }
}
