package sancho.view.statistics.networkStats;

import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.view.viewer.GView;
import sancho.view.viewer.table.GTableMenuListener;

public class NetworkStatsTableMenuListener extends GTableMenuListener implements ISelectionChangedListener {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$utility$NetworkStat;

   public NetworkStatsTableMenuListener(NetworkStatsTableView var1) {
      super(var1);
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$utility$NetworkStat == null
            ? (class$sancho$model$mldonkey$utility$NetworkStat = class$("sancho.model.mldonkey.utility.NetworkStat"))
            : class$sancho$model$mldonkey$utility$NetworkStat
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         var1.add(new NetworkStatsTableMenuListener$CopyNetworkStatToClipboardAction(this));
         var1.add(new NetworkStatsTableMenuListener$CopyNetworkStatToClipboardHTMLAction(this));
         this.addSelectAllMenu(var1);
      }
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   // $VF: synthetic method
   static GView access$000(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$200(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$300(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$400(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$500(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$600(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$700(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$800(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$900(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$1000(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1100(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$1200(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$1300(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$1400(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$1500(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$1600(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$1700(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$1800(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$1900(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$2000(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$2100(NetworkStatsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$2200(NetworkStatsTableMenuListener var0) {
      return var0.gView;
   }
}
