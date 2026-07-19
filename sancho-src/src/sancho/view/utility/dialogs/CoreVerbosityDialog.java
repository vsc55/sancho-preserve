package sancho.view.utility.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.model.mldonkey.Option;
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class CoreVerbosityDialog extends Dialog {
   String[] rawKeywords = new String[]{
      "mc",
      "mr",
      "mct",
      "ms",
      "verb",
      "sm",
      "net",
      "do",
      "up",
      "unk",
      "ov",
      "loc",
      "share",
      "md4",
      "connect",
      "udp",
      "super",
      "swarming",
      "hc",
      "hs",
      "act",
      "bw",
      "tor",
      "file",
      "redir",
      "unexp",
      "hid"
   };
   String[] keywords = new String[this.rawKeywords.length];
   Button[] buttonArray = new Button[this.rawKeywords.length + 1];
   Hashtable desc = new Hashtable();

   public CoreVerbosityDialog(Shell shell) {
      super(shell);
      Arrays.sort((Object[])this.rawKeywords);
      int sourceIndex = 0;
      int columnBase = 0;
      int rowCount = 0;

      for (int i = 0; i < this.rawKeywords.length; i++) {
         this.keywords[i] = this.rawKeywords[sourceIndex];
         sourceIndex += 9;
         if (rowCount++ == 2) {
            rowCount = 0;
            sourceIndex = ++columnBase;
         }
      }

      this.desc.put("all", "all");
      this.desc.put("mc", "client messages");
      this.desc.put("mr", "raw messages");
      this.desc.put("mct", "emule client tags");
      this.desc.put("ms", "server messages");
      this.desc.put("verb", "other");
      this.desc.put("sm", "source management");
      this.desc.put("net", "net");
      this.desc.put("do", "download warnings");
      this.desc.put("up", "upload warnings");
      this.desc.put("unk", "unknown messages");
      this.desc.put("ov", "overnet");
      this.desc.put("loc", "source research");
      this.desc.put("share", "sharing");
      this.desc.put("md4", "md4 computation");
      this.desc.put("connect", "connections");
      this.desc.put("udp", "udp messages");
      this.desc.put("super", "supernodes");
      this.desc.put("swarming", "swarming");
      this.desc.put("hc", "http_client messages");
      this.desc.put("hs", "http_server messages");
      this.desc.put("act", "activity");
      this.desc.put("bw", "bandwidth");
      this.desc.put("tor", "torrent loading");
      this.desc.put("file", "file handling");
      this.desc.put("redir", "redirector");
      this.desc.put("unexp", "unexpected messages");
      this.desc.put("hid", "hidden errors");
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      String versionSuffix = "";
      if (Sancho.hasCollectionFactory()) {
         String coreVersion = Sancho.getCore().getCoreVersion();
         if (coreVersion.length() > 0) {
            versionSuffix = " " + coreVersion;
         }
      }

      shell.setText(SResources.getString("menu.tools.coreVerbosity") + versionSuffix);
      shell.setImage(VersionInfo.getProgramIcon());
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(WidgetFactory.createGridLayout(3, 5, 5, 10, 3, false));
      if (!Sancho.hasCollectionFactory()) {
         return composite;
      } else {
         Option option = (Option)Sancho.getCore().getCollectionFactory().getOptionCollection().get("verbosity");
         if (option == null) {
            return composite;
         } else {
            ArrayList selectedKeywords = new ArrayList();
            StringTokenizer tokenizer = new StringTokenizer(option.getValue(), " ");

            while (tokenizer.hasMoreTokens()) {
               String keyword = tokenizer.nextToken().toLowerCase();
               if (keyword.equals("raw")) {
                  keyword = "mr";
               }

               if (keyword.equals("ultra")) {
                  keyword = "super";
               }

               selectedKeywords.add(keyword);
            }

            this.buttonArray[0] = new Button(composite, 32);
            this.buttonArray[0].setText("all: All");
            this.buttonArray[0].setData("keyword", "all");
            if (selectedKeywords.contains("all")) {
               this.buttonArray[0].setSelection(true);
            }

            GridData gridData = new GridData(768);
            gridData.horizontalSpan = 3;
            this.buttonArray[0].setLayoutData(gridData);
            Label label = new Label(composite, 258);
            gridData = new GridData(768);
            gridData.horizontalSpan = 3;
            label.setLayoutData(gridData);

            for (int i = 0; i < this.keywords.length; i++) {
               Button button = new Button(composite, 32);
               button.setLayoutData(new GridData(768));
               button.setText(this.keywords[i] + ": " + this.desc.get(this.keywords[i]));
               button.setData("keyword", this.keywords[i]);
               if (selectedKeywords.contains(this.keywords[i])) {
                  button.setSelection(true);
               }

               this.buttonArray[i + 1] = button;
            }

            label = new Label(composite, 258);
            gridData = new GridData(768);
            gridData.horizontalSpan = 3;
            label.setLayoutData(gridData);
            return composite;
         }
      }
   }

   protected void createButtonsForButtonBar(Composite parent) {
      Button button = this.createButton(parent, 666, SResources.getString("b.deselectAll"), false);
      button.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            for (int i = 0; i < CoreVerbosityDialog.this.buttonArray.length; i++) {
               if (CoreVerbosityDialog.this.buttonArray[i] != null) {
                  CoreVerbosityDialog.this.buttonArray[i].setSelection(false);
               }
            }
         }
      });
      // OK is the default button so Enter accepts instead of cancelling (which
      // discarded the verbosity changes).
      this.createButton(parent, 0, SResources.getString("b.ok"), true);
      this.createButton(parent, 1, SResources.getString("b.cancel"), false);
   }

   protected void buttonPressed(int buttonId) {
      if (buttonId == 0 && Sancho.hasCollectionFactory()) {
         Option option = (Option)Sancho.getCore().getCollectionFactory().getOptionCollection().get("verbosity");
         if (option != null) {
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < this.buttonArray.length; i++) {
               if (this.buttonArray[i].getSelection()) {
                  if (buffer.length() > 0) {
                     buffer.append(" ");
                  }

                  buffer.append(this.buttonArray[i].getData("keyword"));
               }
            }

            option.setValue(buffer.toString());
         }
      }

      super.buttonPressed(buttonId);
   }
}
