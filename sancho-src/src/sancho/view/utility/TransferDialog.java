package sancho.view.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import sancho.core.CoreFactory;
import sancho.core.Sancho;
import sancho.model.mldonkey.IPreview;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;

public class TransferDialog extends Dialog {
   boolean cancel;
   boolean cancelled;
   boolean transferring;
   String downloadDirectory;
   CLabel label1;
   CLabel label2;
   CLabel label3;
   IPreview iPreview;
   int[] subFileNums;

   public TransferDialog(Shell shell, IPreview iPreview, int[] subFileNums, String downloadDirectory) {
      super(new Shell());
      this.iPreview = iPreview;
      this.subFileNums = subFileNums;
      this.downloadDirectory = downloadDirectory;
      if (subFileNums.length == 1 && subFileNums[0] == -1) {
         String[] subFileNames = iPreview.getSubFileNames();
         if (subFileNames != null) {
            this.subFileNums = new int[subFileNames.length];
            int i = 0;

            while (i < subFileNames.length) {
               this.subFileNums[i] = i++;
            }
         }
      }

      this.setShellStyle(48 | (SWT.getPlatform().equals("fox") ? 0 : 0));
      this.setBlockOnOpen(false);
      this.startTransfer();
   }

   public boolean close() {
      // Signal cancel and close immediately. The transfer thread checks `cancel` each
      // read chunk and only touches widgets through updateLabel()/closeInThread(),
      // both isDisposed-guarded, so there's no need to busy-wait for it here — the old
      // loop slept up to 20 x 200ms = 4s on the UI thread, freezing the GUI on cancel.
      this.cancel = true;
      PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
      PreferenceConverter.setValue(preferenceStore, "transferDialogWindowBounds", this.getShell().getBounds());
      return super.close();
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setImage(VersionInfo.getProgramIcon());
      shell.setText(this.iPreview.getName());
   }

   protected void constrainShellSize() {
      super.constrainShellSize();
      Shell shell = this.getShell();
      if (PreferenceLoader.contains("transferDialogWindowBounds")) {
         shell.setBounds(PreferenceLoader.loadRectangle("transferDialogWindowBounds"));
      } else {
         shell.setSize(300, 175);
         Point location = shell.getLocation();
         this.getShell().setLocation(location.x - 200, location.y);
      }
   }

   protected void createButtonsForButtonBar(Composite parent) {
      this.createButton(parent, 1, SResources.getString("b.cancel"), false);
   }

   protected void buttonPressed(int buttonId) {
      if (buttonId == 1) {
         this.cancel = true;
      }

      super.buttonPressed(buttonId);
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 3, 2, false));
      new CLabel(composite, 0).setText(SResources.getString("td.file"));
      this.label1 = new CLabel(composite, 0);
      this.label1.setLayoutData(new GridData(768));
      new CLabel(composite, 0).setText(SResources.getString("td.destination"));
      this.label2 = new CLabel(composite, 0);
      new CLabel(composite, 0).setText(SResources.getString("td.status"));
      this.label2.setLayoutData(new GridData(768));
      this.label3 = new CLabel(composite, 0);
      this.label3.setLayoutData(new GridData(768));
      this.label1.setText(this.iPreview.getName());
      this.label2.setText(this.downloadDirectory);
      return composite;
   }

   public static long getLongLength(HttpURLConnection connection) {
      for (int i = 0; i < 20; i++) {
         String key = connection.getHeaderFieldKey(i);
         String value = connection.getHeaderField(i);
         if (value == null) {
            break;
         }

         if (key != null && key.toLowerCase().equals("content-length")) {
            return Long.valueOf(value);
         }
      }

      return (long)connection.getContentLength();
   }

   public void updateLabel(final CLabel label, final String text) {
      Shell shell = this.getShell();
      if (shell != null && !shell.isDisposed()) {
         shell.getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (label != null && !label.isDisposed()) {
                  label.setText(text);
               }
            }
         });
      }
   }

   public void closeInThread() {
      Shell shell = this.getShell();
      if (shell != null && !shell.isDisposed()) {
         shell.getDisplay().asyncExec(new Runnable() {
            public void run() {
               TransferDialog.this.close();
            }
         });
      }
   }

   public void startTransfer() {
      Thread transferThread = new Thread() {
         public void run() {
            try {
               for (int i = 0; i < TransferDialog.this.subFileNums.length; i++) {
                  URL url = new URL(TransferDialog.this.iPreview.getPreviewURL());
                  if (!new File(TransferDialog.this.downloadDirectory).exists()) {
                     TransferDialog.this.updateLabel(TransferDialog.this.label3, SResources.getString("td.invalidDirectory"));
                     return;
                  }

                  String name = TransferDialog.this.iPreview.getName();
                  int subFileNum = TransferDialog.this.subFileNums[i];
                  if (subFileNum != -1) {
                     String[] subFileNames = TransferDialog.this.iPreview.getSubFileNames();
                     if (subFileNames != null && subFileNames.length >= subFileNum) {
                        name = subFileNames[subFileNum];
                        if (name.indexOf("/") != -1) {
                           String subDirectory = name.substring(0, name.lastIndexOf("/"));
                           new File(TransferDialog.this.downloadDirectory + File.separator + subDirectory).mkdirs();
                        }
                     }
                  }

                  String uniqueName = name;
                  int suffix = 1;

                  while (new File(TransferDialog.this.downloadDirectory + File.separator + uniqueName).exists()) {
                     uniqueName = name + "." + suffix++;
                  }

                  TransferDialog.this.updateLabel(TransferDialog.this.label1, uniqueName);
                  HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                  CoreFactory coreFactory = Sancho.getCoreFactory();
                  String credentials = "";
                  if (!coreFactory.getPassword().equals("")) {
                     credentials = coreFactory.getUsername() + ":" + coreFactory.getPassword();
                  }

                  if (!credentials.equals("")) {
                     connection.setRequestProperty("Authorization", "Basic " + Base64.encode(credentials.getBytes()));
                  }

                  if (subFileNum != -1) {
                     connection.setRequestProperty("Range", TransferDialog.this.iPreview.getContentRange(subFileNum));
                  }

                  connection.connect();
                  int responseCode = connection.getResponseCode();
                  if (responseCode != 200 && responseCode != 206) {
                     TransferDialog.this.updateLabel(TransferDialog.this.label3, responseCode + ": " + connection.getResponseMessage() + ": " + TransferDialog.this.iPreview.getPreviewURL());
                     return;
                  }

                  String filePath = TransferDialog.this.downloadDirectory + File.separator + uniqueName;
                  BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
                  BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                  long contentLength = TransferDialog.getLongLength(connection);
                  int bufferSize = 131072;
                  byte[] buffer = new byte[bufferSize];
                  long totalBytes = 0L;
                  long startTime = System.currentTimeMillis();
                  long lastUpdateTime = startTime;
                  String totalSizeString = SwissArmy.calcStringSize(contentLength);
                  TransferDialog.this.transferring = true;

                  while (true) {
                     int bytesRead;
                     if ((bytesRead = inputStream.read(buffer, 0, bufferSize)) != -1) {
                        totalBytes += (long)bytesRead;
                        outputStream.write(buffer, 0, bytesRead);
                        long now = System.currentTimeMillis();
                        if (now > lastUpdateTime + 1001L || totalBytes == (long)bytesRead) {
                           lastUpdateTime = now;
                           long elapsed = now - startTime;
                           long rate = elapsed > 1000L && totalBytes > 0L ? totalBytes / (elapsed / 1000L) : totalBytes;
                           String rateString = SwissArmy.calcStringSize(rate) + "/s";
                           String statusString = SwissArmy.calcStringSize(totalBytes) + "/" + totalSizeString + " (" + rateString + ")";
                           TransferDialog.this.updateLabel(TransferDialog.this.label3, statusString);
                        }

                        if (!TransferDialog.this.cancel) {
                           continue;
                        }

                        TransferDialog.this.cancelled = true;
                     }

                     inputStream.close();
                     outputStream.close();
                     TransferDialog.this.transferring = false;
                     break;
                  }
               }

               if (!TransferDialog.this.cancelled) {
                  TransferDialog.this.closeInThread();
               }
            } catch (MalformedURLException malformedURL) {
               TransferDialog.this.updateLabel(TransferDialog.this.label1, malformedURL.toString());
            } catch (IOException ioException) {
               TransferDialog.this.updateLabel(TransferDialog.this.label1, ioException.toString());
            }
         }
      };
      transferThread.start();
   }
}
