package sancho.view.preferences;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;

// Startup check for the Windows file/URL associations. When enabled (preference
// "checkAssociations", default on) and some associations are completely missing, it offers
// to create them at user or machine level — or to stop asking. The registry read runs off
// the UI thread; only the dialog touches SWT.
public class AssociationChecker {
   private AssociationChecker() {
   }

   public static void checkAndPrompt(final Shell shell) {
      if (shell == null || shell.isDisposed()) {
         return;
      }

      if (!WinRegAssociations.isWindows() || !PreferenceLoader.loadBoolean("checkAssociations")) {
         return;
      }

      final Display display = shell.getDisplay();
      Thread thread = new Thread(new Runnable() {
         public void run() {
            // Querying the registry spawns reg.exe per key, so do it off the UI thread.
            final List<WinRegAssociations.AssocItem> missing = WinRegAssociations.missingItems();
            if (missing.isEmpty() || display.isDisposed()) {
               return;
            }

            display.asyncExec(new Runnable() {
               public void run() {
                  if (!shell.isDisposed()) {
                     prompt(shell, missing);
                  }
               }
            });
         }
      });
      thread.setDaemon(true);
      thread.start();
   }

   private static void prompt(Shell shell, List<WinRegAssociations.AssocItem> missing) {
      StringBuilder message = new StringBuilder(SResources.getString("l.assoc.missingIntro"));
      for (WinRegAssociations.AssocItem item : missing) {
         message.append("\n    • ").append(item.label);
      }

      String[] buttons = new String[]{
         SResources.getString("l.assoc.associateUser"),
         SResources.getString("l.assoc.associateMachine"),
         SResources.getString("l.assoc.dontAskAgain"),
         SResources.getString("b.close")
      };
      MessageDialog dialog = new MessageDialog(
         shell, SResources.getString("l.assoc.checkTitle"), null, message.toString(), MessageDialog.QUESTION, buttons, 3
      );

      switch (dialog.open()) {
         case 0:
            associate(shell, missing, true);
            break;
         case 1:
            associate(shell, missing, false);
            break;
         case 2:
            // "Don't ask again" simply disables the startup check preference.
            PreferenceLoader.getPreferenceStore().setValue("checkAssociations", false);
            PreferenceLoader.saveStore();
            break;
         default:
            // Closed/cancelled: leave the associations as they are.
      }
   }

   private static void associate(Shell shell, List<WinRegAssociations.AssocItem> missing, boolean perUser) {
      List<WinRegAssociations.RegAction> actions = new ArrayList<>();
      for (WinRegAssociations.AssocItem item : missing) {
         actions.add(new WinRegAssociations.RegAction(item, true));
      }

      if (WinRegAssociations.writeAndImport(actions, perUser)) {
         MessageDialog.openInformation(shell, VersionInfo.getName(), SResources.getString("l.regUpdated"));
      } else {
         MessageDialog.openWarning(shell, VersionInfo.getName(), SResources.getString("l.regUpdateFailed"));
      }
   }
}
