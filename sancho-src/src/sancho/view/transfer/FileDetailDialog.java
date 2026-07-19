package sancho.view.transfer;

import java.util.Arrays;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.core.Sancho;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.fileComments.FileCommentsViewFrame;
import sancho.view.transfer.fileDialog.SubfilesViewFrame;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class FileDetailDialog extends AbstractDetailDialog {
   private File file;
   private Button fileActionButton;
   private Button fileCancelButton;
   private CLabel clFileName;
   private CLabel clHash;
   private CLabel clSize;
   private CLabel clAge;
   private CLabel clSources;
   private CLabel clChunks;
   private CLabel clTransferred;
   private CLabel clRelativeAvail;
   private CLabel clLast;
   private CLabel clPriority;
   private CLabel clRate;
   private CLabel clETA;
   private CLabel clComment;
   private CLabel clRemaining;
   private CLabel clMagic;
   private CLabel clUser;
   private CLabel clGroup;
   private List renameList;
   private Text renameText;
   private FileCommentsViewFrame fileCommentsViewFrame;
   private boolean openComments;

   public FileDetailDialog(Shell var1, File var2) {
      this(var1, var2, false);
   }

   public FileDetailDialog(Shell var1, File var2, boolean var3) {
      super(var1);
      this.file = var2;
      this.openComments = var3;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setText(SResources.getString("l.file") + " " + this.file.getId() + " " + SResources.getString("l.details").toLowerCase());
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      boolean var4 = this.file.getSubFileNames() != null;
      boolean var5 = var4 || this.file.hasFileComments();
      CTabFolder var6 = null;
      Composite var3;
      if (var5) {
         var6 = WidgetFactory.createCTabFolder(var2);
         var6.setLayoutData(new GridData(1808));
         CTabItem var7 = new CTabItem(var6, 0);
         var6.setSelection(var7);
         var6.setBorderVisible(false);
         var7.setText(SResources.getString("l.general"));
         var3 = new Composite(var6, 0);
         var7.setControl(var3);
      } else {
         var3 = new Composite(var2, 0);
      }

      var3.setLayout(WidgetFactory.createGridLayout(1, 0, 5, 0, 5, false));
      this.createFileGeneralGroup(var3);
      this.createFileTransferGroup(var3);
      this.createChunkGroup(var3, SResources.getString("dd.f.chunksInformation"), null);
      if (this.file.hasAvails()) {
         Network[] var10 = this.file.getAllAvailNetworks();
         if (var10.length > 1) {
            for (int var8 = 0; var8 < var10.length; var8++) {
               Network var9 = var10[var8];
               if (var9.isEnabled()) {
                  this.createChunkGroup(var3, var9.getName(), var9);
               }
            }
         }
      }

      this.createRenameGroup(var3);
      if (this.file.getEnumNetwork() == EnumNetwork.FILETP) {
         this.createMirrorGroup(var3);
      }

      Label var11 = new Label(var3, 258);
      var11.setLayoutData(new GridData(768));
      this.updateLabels();
      this.file.addObserver(this);
      if (var5) {
         if (this.file.hasFileComments()) {
            CTabItem var12 = new CTabItem(var6, 0);
            Composite var14 = new Composite(var6, 0) {
               public Point computeSize(int var1, int var2, boolean var3) {
                  return new Point(-1, -1);
               }
            };
            var14.setLayout(new FillLayout());
            this.fileCommentsViewFrame = new FileCommentsViewFrame(var14, "l.fileComments", "tab.transfers.buttonSmall", null, this.file);
            var12.setControl(var14);
            var12.setText(SResources.getString("l.fileComments"));
            if (this.openComments) {
               var6.setSelection(var12);
            }
         }

         if (var4) {
            CTabItem var13 = new CTabItem(var6, 0);
            Composite var15 = new Composite(var6, 0) {
               public Point computeSize(int var1, int var2, boolean var3) {
                  return new Point(-1, -1);
               }
            };
            var15.setLayout(new FillLayout());
            new SubfilesViewFrame(var15, "l.subFiles", "tab.transfers.buttonSmall", null, this.file);
            var13.setControl(var15);
            var13.setText(SResources.getString("l.subFiles"));
         }
      }

      return var2;
   }

   private void renameFile() {
      String var1 = "";
      if (!this.renameText.getText().equals("") && !this.renameText.getText().equals(this.file.getName())) {
         var1 = this.renameText.getText();
      } else if (this.renameList.getSelection().length > 0 && !this.renameList.getSelection()[0].equals(this.file.getName())) {
         var1 = this.renameList.getSelection()[0];
      }

      if (!var1.equals("")) {
         this.file.rename(var1);
      }
   }

   private void createFileGeneralGroup(Composite var1) {
      Group var2 = new Group(var1, 64);
      var2.setText(SResources.getString("dd.f.fileInformation"));
      var2.setLayout(WidgetFactory.createGridLayout(4, 5, 0, 0, 0, false));
      var2.setLayoutData(new GridData(768));
      this.clFileName = this.createLine(var2, "dd.f.fileName", true);
      this.clHash = this.createLine(var2, "dd.f.hash", true);
      this.clSize = this.createLine(var2, "dd.f.size", false);
      this.clAge = this.createLine(var2, "dd.f.age", false);
      this.clUser = this.createLine(var2, "dd.f.user", false);
      this.clGroup = this.createLine(var2, "dd.f.group", false);
      this.clMagic = this.createLine(var2, "dd.f.magic", true);
      this.clComment = this.createLine(var2, "dd.f.comment", true);
   }

   private void createFileTransferGroup(Composite var1) {
      Group var2 = new Group(var1, 64);
      var2.setText(SResources.getString("dd.f.transferInformation"));
      var2.setLayout(WidgetFactory.createGridLayout(4, 5, 0, 0, 0, false));
      var2.setLayoutData(new GridData(768));
      this.clSources = this.createLine(var2, "dd.f.sources", false);
      this.clChunks = this.createLine(var2, "dd.f.chunks", false);
      this.clTransferred = this.createLine(var2, "dd.f.transferred", false);
      this.clRelativeAvail = this.createLine(var2, "dd.f.availability", false);
      this.clRemaining = this.createLine(var2, "dd.f.remaining", false);
      this.clPriority = this.createLine(var2, "dd.f.priority", false);
      this.clRate = this.createLine(var2, "dd.f.rate", false);
      this.clETA = this.createLine(var2, "dd.f.eta", false);
      this.clLast = this.createLine(var2, "dd.f.last", false);
      if (SWT.getPlatform().equals("win32") && this.file.getChunkAges().length < 1000) {
         this.createChunkAgesGroup(var2);
      }
   }

   private void createChunkGroup(Composite var1, String var2, Network var3) {
      super.createChunkGroup(var1, var2, null, this.file, var3);
   }

   private void createChunkAgesGroup(Composite var1) {
      Label var2 = new Label(var1, 0);
      var2.setText(SResources.getString("dd.f.chunkAges"));
      GridData var3 = new GridData();
      var3.widthHint = 100;
      var2.setLayoutData(var3);
      Combo var4 = new Combo(var1, 8);
      GridData var5 = new GridData(768);
      var5.widthHint = 1;
      var4.setLayoutData(var5);
      int[] var6 = this.file.getChunkAges();

      for (int var7 = 0; var7 < var6.length; var7++) {
         var4.add(var7 + 1 + ": " + (var6[var7] > 75000000 ? "-" : SwissArmy.calcStringOfSeconds((long)var6[var7])));
      }

      if (var6.length > 0) {
         var4.select(0);
      }
   }

   public void addComment(String var1) {
      if (var1 != null) {
         this.file.setComment(var1);
      }
   }

   private void createMirrorGroup(Composite var1) {
      Label var2 = new Label(var1, 258);
      var2.setLayoutData(new GridData(768));
      Composite var3 = new Composite(var1, 0);
      var3.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 4, 0, false));
      var3.setLayoutData(new GridData(768));
      Text var4 = new Text(var3, 2048);
      var4.setFont(PreferenceLoader.loadFont("consoleFontData"));
      GridData var5 = new GridData(768);
      var5.widthHint = 1;
      var4.setLayoutData(var5);
      var4.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            if (var1.character == '\r') {
               FileDetailDialog.this.addMirror(var4.getText());
               var4.setText("");
            }
         }
      });
      Button var6 = new Button(var3, 0);
      var6.setText(SResources.getString("dd.f.addMirror"));
      var6.setLayoutData(new GridData(128));
      var6.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            FileDetailDialog.this.addMirror(var4.getText());
            var4.setText("");
         }
      });
   }

   public void addMirror(String var1) {
      if (var1 != null && var1.length() > 3) {
         Sancho.send((short)29, "mirror " + this.file.getId() + " " + var1);
      }
   }

   private void createRenameGroup(Composite var1) {
      Group var2 = new Group(var1, 64);
      var2.setText(SResources.getString("dd.f.alternativeFilenames"));
      var2.setLayout(WidgetFactory.createGridLayout(1, 1, 1, 0, 0, false));
      var2.setLayoutData(new GridData(768));
      Arrays.sort(this.file.getNames(), String.CASE_INSENSITIVE_ORDER);
      this.renameList = new List(var2, 2820);

      for (int var3 = 0; var3 < this.file.getNames().length; var3++) {
         this.renameList.add(this.file.getNames()[var3]);
      }

      GridData var4 = new GridData(768);
      var4.heightHint = 80;
      var4.widthHint = 1;
      this.renameList.setLayoutData(var4);
      this.renameList.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            String var2 = FileDetailDialog.this.renameList.getSelection()[0];
            FileDetailDialog.this.renameText.setText(var2);
         }
      });
      Composite var5 = new Composite(var1, 0);
      var5.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 4, 0, false));
      var5.setLayoutData(new GridData(768));
      this.renameText = new Text(var5, 2048);
      this.renameText.setText(this.file.getName());
      this.renameText.setFont(PreferenceLoader.loadFont("consoleFontData"));
      GridData var6 = new GridData(768);
      var6.widthHint = 1;
      this.renameText.setLayoutData(var6);
      this.renameText.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            if (var1.character == '\r') {
               FileDetailDialog.this.renameFile();
               FileDetailDialog.this.renameText.setText("");
            }
         }
      });
      Button var7 = new Button(var5, 0);
      var7.setText(SResources.getString("dd.f.renameFile"));
      var7.setLayoutData(new GridData(128));
      var7.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            FileDetailDialog.this.renameFile();
         }
      });
   }

   protected Control createButtonBar(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayoutData(new GridData(768));
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 0, 0, false));
      Composite var3 = new Composite(var2, 0);
      GridData var4 = new GridData(768);
      var4.heightHint = 5;
      var3.setLayoutData(var4);
      Composite var5 = new Composite(var2, 0);
      var5.setLayoutData(new GridData(128));
      var5.setLayout(WidgetFactory.createRowLayout(false, false, false, 256, 0, 0, 0, 0, 5));
      if (this.file.getFileStateEnum() == EnumFileState.PAUSED
         || this.file.getFileStateEnum() == EnumFileState.DOWNLOADING
         || this.file.getFileStateEnum() == EnumFileState.QUEUED) {
         this.fileCancelButton = new Button(var5, 0);
         this.fileCancelButton.setLayoutData(new RowData());
         this.fileCancelButton.setText(SResources.getString("dd.f.cancelFile"));
         this.fileCancelButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               MessageBox var2 = new MessageBox(FileDetailDialog.this.fileCancelButton.getShell(), 196);
               var2.setMessage(SResources.getString("dd.f.reallyCancel"));
               if (var2.open() == 64) {
                  FileDetailDialog.this.file.setState(EnumFileState.CANCELLED);
                  FileDetailDialog.this.fileCancelButton.setEnabled(false);
                  FileDetailDialog.this.fileActionButton.setEnabled(false);
               }
            }
         });
      }

      this.fileActionButton = new Button(var5, 0);
      this.fileActionButton.setLayoutData(new RowData());
      if (this.file.getFileStateEnum() == EnumFileState.PAUSED || this.file.getFileStateEnum() == EnumFileState.QUEUED) {
         this.fileActionButton.setText(SResources.getString("dd.f.resumeFile"));
      } else if (this.file.getFileStateEnum() == EnumFileState.DOWNLOADING) {
         this.fileActionButton.setText("  " + SResources.getString("dd.f.pauseFile"));
      } else if (this.file.getFileStateEnum() == EnumFileState.DOWNLOADED) {
         this.fileActionButton.setText(SResources.getString("dd.f.commitFile"));
      }

      if (this.file.getFileStateEnum() == EnumFileState.QUEUED) {
         this.fileActionButton.setEnabled(false);
      }

      this.fileActionButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            if (FileDetailDialog.this.file.getFileStateEnum() == EnumFileState.PAUSED) {
               FileDetailDialog.this.file.setState(EnumFileState.DOWNLOADING);
               FileDetailDialog.this.fileActionButton.setText(SResources.getString("dd.f.pauseFile"));
            } else if (FileDetailDialog.this.file.getFileStateEnum() == EnumFileState.DOWNLOADING) {
               FileDetailDialog.this.file.setState(EnumFileState.PAUSED);
               FileDetailDialog.this.fileActionButton.setText(SResources.getString("dd.f.resumeFile"));
            } else if (FileDetailDialog.this.file.getFileStateEnum() == EnumFileState.DOWNLOADED) {
               if (FileDetailDialog.this.renameText.getText().equals("")) {
                  FileDetailDialog.this.file.saveFileAs(FileDetailDialog.this.file.getName());
               } else {
                  FileDetailDialog.this.file.saveFileAs(FileDetailDialog.this.renameText.getText());
               }

               FileDetailDialog.this.fileActionButton.setText(SResources.getString("b.ok"));
               FileDetailDialog.this.fileActionButton.setEnabled(false);
            }
         }
      });
      Button var6 = new Button(var5, 0);
      var6.setFocus();
      var6.setLayoutData(new RowData());
      var6.setText(SResources.getString("b.close"));
      var6.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            FileDetailDialog.this.close();
         }
      });
      return var2;
   }

   public void updateLabels() {
      this.updateLabel(this.clFileName, this.file.getName());
      this.updateLabel(this.clHash, this.file.getHash().toUpperCase());
      this.updateLabel(this.clSize, this.file.getSizeString());
      this.updateLabel(this.clAge, this.file.getAgeString());
      this.updateLabel(this.clSources, Integer.toString(this.file.getSources()));
      this.updateLabel(this.clChunks, Integer.toString(this.file.getNumChunks()) + " / " + Integer.toString(this.file.getChunks().length()));
      this.updateLabel(this.clTransferred, this.file.getDownloadedString());
      this.updateLabel(this.clRelativeAvail, this.file.getRelativeAvail() + "%");
      this.updateLabel(this.clLast, this.file.getLastSeenString());
      this.updateLabel(this.clPriority, this.file.getPriorityString());
      this.updateLabel(this.clComment, this.file.getComment());
      this.updateLabel(this.clMagic, this.file.getMagic());
      this.updateLabel(this.clRemaining, this.file.getRemainingString());
      this.updateLabel(this.clUser, this.file.getUser());
      this.updateLabel(this.clGroup, this.file.getGroup());
      if (this.file.getFileStateEnum() != EnumFileState.PAUSED && this.file.getFileStateEnum() != EnumFileState.QUEUED) {
         this.updateLabel(this.clRate, (double)Math.round(100.0 * (double)(this.file.getRate() / 1000.0F)) / 100.0 + " KB/s");
      } else {
         this.updateLabel(this.clRate, this.file.getFileStateEnum().getName());
      }

      this.updateLabel(this.clETA, this.file.getEtaString());
   }

   public void updateViews(int var1) {
      if (this.fileCommentsViewFrame != null) {
         this.fileCommentsViewFrame.getGView().refresh();
      }
   }

   public boolean close() {
      this.file.deleteObserver(this);
      return super.close();
   }
}
