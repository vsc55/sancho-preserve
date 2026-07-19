package sancho.view.statistics.networkStats;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.utility.NetworkStat;
import sancho.utility.SwissArmy;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableMenuListener;

public class NetworkStatsTableMenuListener extends GTableMenuListener implements ISelectionChangedListener {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$utility$NetworkStat;

   public NetworkStatsTableMenuListener(NetworkStatsTableView tableView) {
      super(tableView);
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$utility$NetworkStat == null
            ? (class$sancho$model$mldonkey$utility$NetworkStat = class$("sancho.model.mldonkey.utility.NetworkStat"))
            : class$sancho$model$mldonkey$utility$NetworkStat
      );
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         menuManager.add(new CopyNetworkStatToClipboardAction());
         menuManager.add(new CopyNetworkStatToClipboardHTMLAction());
         this.addSelectAllMenu(menuManager);
      }
   }

   // $VF: synthetic method
   static Class class$(String name) {
      try {
         return Class.forName(name);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }

   // Context-menu action: copies the selected network stats as a plain-text table.
   private class CopyNetworkStatToClipboardAction extends Action {
      public CopyNetworkStatToClipboardAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void append(StringBuffer buffer, int count, String text) {
         for (int i = 0; i < count; i++) {
            buffer.append(text);
         }
      }

      public void rj(StringBuffer buffer, String text, int width) {
         int length = text.length();

         for (int i = length; i < width; i++) {
            buffer.append(" ");
         }

         buffer.append(text);
         buffer.append(" |");
      }

      public void lj(StringBuffer buffer, String text, int width) {
         int length = text.length();
         buffer.append(text);

         for (int i = length; i < width; i++) {
            buffer.append(" ");
         }

         buffer.append(" |");
      }

      public void run() {
         String columnIDs = NetworkStatsTableMenuListener.this.gView.getColumnIDs();
         int[] columnIndices = new int[columnIDs.length()];

         for (int i = 0; i < columnIDs.length(); i++) {
            columnIndices[i] = columnIDs.charAt(i) - 'A';
         }

         String[] columnLabels = NetworkStatsTableMenuListener.this.gView.getColumnLabels();
         int[] columnWidths = new int[columnIndices.length];

         for (int i = 0; i < columnIndices.length; i++) {
            int columnIndex = columnIndices[i];
            String label = SResources.getString(columnLabels[columnIndex]);
            columnWidths[i] = label.length();
         }

         for (int i = 0; i < NetworkStatsTableMenuListener.this.selectedObjects.size(); i++) {
            NetworkStat stat = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(i);

            for (int j = 0; j < columnIndices.length; j++) {
               String text = NetworkStatsTableMenuListener.this.gView.getTableLabelProvider().getColumnText(stat, j);
               columnWidths[j] = Math.max(columnWidths[j], text.length());
            }
         }

         StringBuffer buffer = new StringBuffer(500);
         String separator = System.getProperty("line.separator");
         buffer.append(".");

         for (int i = 0; i < columnIndices.length; i++) {
            this.append(buffer, columnWidths[i] + 2, "-");
            buffer.append(".");
         }

         buffer.append(separator);
         buffer.append("|");

         for (int i = 0; i < columnIndices.length; i++) {
            int columnIndex = columnIndices[i];
            int alignment = NetworkStatsTableMenuListener.this.gView.getColumnAlignment()[columnIndex];
            String label = SResources.getString(columnLabels[columnIndex]);
            buffer.append(" ");
            if (alignment == 131072) {
               this.rj(buffer, label, columnWidths[i]);
            } else {
               this.lj(buffer, label, columnWidths[i]);
            }
         }

         buffer.append(separator);
         buffer.append("|");

         for (int i = 0; i < columnIndices.length; i++) {
            buffer.append(" ");
            this.append(buffer, columnWidths[i], "-");
            buffer.append(" ");
            buffer.append("|");
         }

         for (int i = 0; i < NetworkStatsTableMenuListener.this.selectedObjects.size(); i++) {
            NetworkStat stat = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(i);
            buffer.append(separator);
            buffer.append("|");

            for (int j = 0; j < columnIndices.length; j++) {
               buffer.append(" ");
               int columnIndex = columnIndices[j];
               String text = NetworkStatsTableMenuListener.this.gView.getTableLabelProvider().getColumnText(stat, j);
               int alignment = NetworkStatsTableMenuListener.this.gView.getColumnAlignment()[columnIndex];
               if (alignment == 131072) {
                  this.rj(buffer, text, columnWidths[j]);
               } else {
                  this.lj(buffer, text, columnWidths[j]);
               }
            }
         }

         buffer.append(separator);
         buffer.append("`");

         for (int i = 0; i < columnIndices.length; i++) {
            this.append(buffer, columnWidths[i] + 2, "-");
            buffer.append("'");
         }

         if (NetworkStatsTableMenuListener.this.selectedObjects.size() > 0) {
            NetworkStat stat = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(0);
            buffer.append(separator);
            buffer.append("  [ ");
            buffer.append(SwissArmy.calcStringOfSecondsFull((long)stat.getUptime()));
            String coreVersion = NetworkStatsTableMenuListener.this.gView.getCore().getCoreVersion();
            if (coreVersion != null && !coreVersion.equals("")) {
               buffer.append(" / ");
               buffer.append(coreVersion);
            }

            buffer.append(" ]");
            buffer.append(separator);
         }

         MainWindow.copyToClipboard(buffer.toString());
      }
   }

   // Context-menu action: copies the selected network stats as an HTML table.
   private class CopyNetworkStatToClipboardHTMLAction extends Action {
      public CopyNetworkStatToClipboardHTMLAction() {
         super(SResources.getString("mi.copy") + " (html)");
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void append(StringBuffer buffer, int count, String text) {
         for (int i = 0; i < count; i++) {
            buffer.append(text);
         }
      }

      public void rj(StringBuffer buffer, String text, int width) {
         int length = text.length();

         for (int i = length; i < width; i++) {
            buffer.append(" ");
         }

         buffer.append(text);
         buffer.append(" |");
      }

      public void lj(StringBuffer buffer, String text, int width) {
         int length = text.length();
         buffer.append(text);

         for (int i = length; i < width; i++) {
            buffer.append(" ");
         }

         buffer.append(" |");
      }

      public void run() {
         String columnIDs = NetworkStatsTableMenuListener.this.gView.getColumnIDs();
         int[] columnIndices = new int[columnIDs.length()];

         for (int i = 0; i < columnIDs.length(); i++) {
            columnIndices[i] = columnIDs.charAt(i) - 'A';
         }

         String[] columnLabels = NetworkStatsTableMenuListener.this.gView.getColumnLabels();
         StringBuffer buffer = new StringBuffer(500);
         String separator = System.getProperty("line.separator");
         buffer.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td>");
         buffer.append(
            "<table style=\"border: 2px solid #000; font-family: Verdana, Helvetica, Arial, Sans-serif; font-size: 10pt;\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\">"
         );
         buffer.append(separator);
         buffer.append("<tr>");

         for (int i = 0; i < columnIndices.length; i++) {
            int columnIndex = columnIndices[i];
            int alignment = NetworkStatsTableMenuListener.this.gView.getColumnAlignment()[columnIndex];
            String label = SResources.getString(columnLabels[columnIndex]);
            buffer.append("<th nowrap align=");
            if (alignment == 131072) {
               buffer.append("\"right\"");
            } else {
               buffer.append("\"left\"");
            }

            buffer.append(">");
            buffer.append(label);
            buffer.append("</th>");
         }

         buffer.append("</tr>");

         for (int i = 0; i < NetworkStatsTableMenuListener.this.selectedObjects.size(); i++) {
            NetworkStat stat = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(i);
            buffer.append(separator);
            buffer.append("<tr>");

            for (int j = 0; j < columnIndices.length; j++) {
               buffer.append("<td nowrap");
               int columnIndex = columnIndices[j];
               String text = NetworkStatsTableMenuListener.this.gView.getTableLabelProvider().getColumnText(stat, j);
               int alignment = NetworkStatsTableMenuListener.this.gView.getColumnAlignment()[columnIndex];
               if (alignment == 131072) {
                  buffer.append(" align=right");
               }

               buffer.append(">");
               buffer.append(text);
               buffer.append("</td>");
            }

            buffer.append("</tr>");
         }

         buffer.append(separator);
         buffer.append("</table>");
         if (NetworkStatsTableMenuListener.this.selectedObjects.size() > 0) {
            NetworkStat stat = (NetworkStat)NetworkStatsTableMenuListener.this.selectedObjects.get(0);
            buffer.append(separator);
            buffer.append(
               "<table width=\"100%\" style=\"font-family: Verdana, Helvetica, Arial, Sans-serif; font-size: 8pt;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td><center>"
            );
            buffer.append(SwissArmy.calcStringOfSecondsFull((long)stat.getUptime()));
            String coreVersion = NetworkStatsTableMenuListener.this.gView.getCore().getCoreVersion();
            if (coreVersion != null && !coreVersion.equals("")) {
               buffer.append(" / ");
               buffer.append(coreVersion);
            }

            buffer.append("</center></td></tr></table>");
            buffer.append(separator);
         }

         buffer.append("</td></tr></table>");
         MainWindow.copyToClipboard(buffer.toString());
      }
   }
}
