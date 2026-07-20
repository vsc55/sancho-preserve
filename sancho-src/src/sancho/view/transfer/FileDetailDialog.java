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

   public FileDetailDialog(Shell shell, File file) {
      this(shell, file, false);
   }

   public FileDetailDialog(Shell shell, File file, boolean openComments) {
      super(shell);
      this.file = file;
      this.openComments = openComments;
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText(SResources.getString("l.file") + " " + this.file.getId() + " " + SResources.getString("l.details").toLowerCase());
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      boolean hasSubFiles = this.file.getSubFileNames() != null;
      boolean hasTabs = hasSubFiles || this.file.hasFileComments();
      CTabFolder tabFolder = null;
      Composite generalComposite;
      if (hasTabs) {
         tabFolder = WidgetFactory.createCTabFolder(composite);
         tabFolder.setLayoutData(new GridData(1808));
         CTabItem generalTab = new CTabItem(tabFolder, 0);
         tabFolder.setSelection(generalTab);
         tabFolder.setBorderVisible(false);
         generalTab.setText(SResources.getString("l.general"));
         generalComposite = new Composite(tabFolder, 0);
         generalTab.setControl(generalComposite);
      } else {
         generalComposite = new Composite(composite, 0);
      }

      generalComposite.setLayout(WidgetFactory.createGridLayout(1, 0, 5, 0, 5, false));
      this.createFileGeneralGroup(generalComposite);
      this.createFileTransferGroup(generalComposite);
      this.createChunkGroup(generalComposite, SResources.getString("dd.f.chunksInformation"), null);
      if (this.file.hasAvails()) {
         Network[] networks = this.file.getAllAvailNetworks();
         if (networks.length > 1) {
            for (int i = 0; i < networks.length; i++) {
               Network network = networks[i];
               if (network.isEnabled()) {
                  this.createChunkGroup(generalComposite, network.getName(), network);
               }
            }
         }
      }

      this.createRenameGroup(generalComposite);
      if (this.file.getEnumNetwork() == EnumNetwork.FILETP) {
         this.createMirrorGroup(generalComposite);
      }

      Label separator = new Label(generalComposite, 258);
      separator.setLayoutData(new GridData(768));
      this.updateLabels();
      this.file.addObserver(this);
      if (hasTabs) {
         if (this.file.hasFileComments()) {
            CTabItem commentsTab = new CTabItem(tabFolder, 0);
            Composite commentsComposite = new Composite(tabFolder, 0) {
               public Point computeSize(int wHint, int hHint, boolean changed) {
                  return new Point(-1, -1);
               }
            };
            commentsComposite.setLayout(new FillLayout());
            this.fileCommentsViewFrame = new FileCommentsViewFrame(commentsComposite, "l.fileComments", "tab.transfers.buttonSmall", null, this.file);
            commentsTab.setControl(commentsComposite);
            commentsTab.setText(SResources.getString("l.fileComments"));
            if (this.openComments) {
               tabFolder.setSelection(commentsTab);
            }
         }

         if (hasSubFiles) {
            CTabItem subFilesTab = new CTabItem(tabFolder, 0);
            Composite subFilesComposite = new Composite(tabFolder, 0) {
               public Point computeSize(int wHint, int hHint, boolean changed) {
                  return new Point(-1, -1);
               }
            };
            subFilesComposite.setLayout(new FillLayout());
            new SubfilesViewFrame(subFilesComposite, "l.subFiles", "tab.transfers.buttonSmall", null, this.file);
            subFilesTab.setControl(subFilesComposite);
            subFilesTab.setText(SResources.getString("l.subFiles"));
         }
      }

      return composite;
   }

   private void renameFile() {
      String newName = "";
      if (!this.renameText.getText().equals("") && !this.renameText.getText().equals(this.file.getName())) {
         newName = this.renameText.getText();
      } else if (this.renameList.getSelection().length > 0 && !this.renameList.getSelection()[0].equals(this.file.getName())) {
         newName = this.renameList.getSelection()[0];
      }

      if (!newName.equals("")) {
         this.file.rename(newName);
      }
   }

   private void createFileGeneralGroup(Composite composite) {
      Group group = new Group(composite, 64);
      group.setText(SResources.getString("dd.f.fileInformation"));
      group.setLayout(WidgetFactory.createGridLayout(4, 5, 0, 0, 0, false));
      group.setLayoutData(new GridData(768));
      this.clFileName = this.createLine(group, "dd.f.fileName", true);
      this.clHash = this.createLine(group, "dd.f.hash", true);
      this.clSize = this.createLine(group, "dd.f.size", false);
      this.clAge = this.createLine(group, "dd.f.age", false);
      this.clUser = this.createLine(group, "dd.f.user", false);
      this.clGroup = this.createLine(group, "dd.f.group", false);
      this.clMagic = this.createLine(group, "dd.f.magic", true);
      this.clComment = this.createLine(group, "dd.f.comment", true);
   }

   private void createFileTransferGroup(Composite composite) {
      Group group = new Group(composite, 64);
      group.setText(SResources.getString("dd.f.transferInformation"));
      group.setLayout(WidgetFactory.createGridLayout(4, 5, 0, 0, 0, false));
      group.setLayoutData(new GridData(768));
      this.clSources = this.createLine(group, "dd.f.sources", false);
      this.clChunks = this.createLine(group, "dd.f.chunks", false);
      this.clTransferred = this.createLine(group, "dd.f.transferred", false);
      this.clRelativeAvail = this.createLine(group, "dd.f.availability", false);
      this.clRemaining = this.createLine(group, "dd.f.remaining", false);
      this.clPriority = this.createLine(group, "dd.f.priority", false);
      this.clRate = this.createLine(group, "dd.f.rate", false);
      this.clETA = this.createLine(group, "dd.f.eta", false);
      this.clLast = this.createLine(group, "dd.f.last", false);
      if (SWT.getPlatform().equals("win32") && this.file.getChunkAges().length < 1000) {
         this.createChunkAgesGroup(group);
      }
   }

   private void createChunkGroup(Composite composite, String title, Network network) {
      super.createChunkGroup(composite, title, null, this.file, network);
   }

   private void createChunkAgesGroup(Composite composite) {
      Label label = new Label(composite, 0);
      label.setText(SResources.getString("dd.f.chunkAges"));
      // Size to the label text (was a fixed 100px that clipped longer translations).
      label.setLayoutData(new GridData());
      Combo combo = new Combo(composite, 8);
      GridData comboGridData = new GridData(768);
      comboGridData.widthHint = 1;
      combo.setLayoutData(comboGridData);
      int[] ages = this.file.getChunkAges();

      for (int i = 0; i < ages.length; i++) {
         combo.add(i + 1 + ": " + (ages[i] > 75000000 ? "-" : SwissArmy.calcStringOfSeconds((long)ages[i])));
      }

      if (ages.length > 0) {
         combo.select(0);
      }
   }

   public void addComment(String comment) {
      if (comment != null) {
         this.file.setComment(comment);
      }
   }

   private void createMirrorGroup(Composite composite) {
      Label separator = new Label(composite, 258);
      separator.setLayoutData(new GridData(768));
      Composite mirrorComposite = new Composite(composite, 0);
      mirrorComposite.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 4, 0, false));
      mirrorComposite.setLayoutData(new GridData(768));
      Text mirrorText = new Text(mirrorComposite, 2048);
      mirrorText.setFont(PreferenceLoader.loadFont("consoleFontData"));
      GridData gridData = new GridData(768);
      gridData.widthHint = 1;
      mirrorText.setLayoutData(gridData);
      mirrorText.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent event) {
            if (event.character == '\r') {
               FileDetailDialog.this.addMirror(mirrorText.getText());
               mirrorText.setText("");
            }
         }
      });
      Button addButton = new Button(mirrorComposite, 0);
      addButton.setText(SResources.getString("dd.f.addMirror"));
      addButton.setLayoutData(new GridData(128));
      addButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FileDetailDialog.this.addMirror(mirrorText.getText());
            mirrorText.setText("");
         }
      });
   }

   public void addMirror(String mirror) {
      if (mirror != null && mirror.length() > 3) {
         Sancho.send((short)29, "mirror " + this.file.getId() + " " + mirror);
      }
   }

   private void createRenameGroup(Composite composite) {
      Group group = new Group(composite, 64);
      group.setText(SResources.getString("dd.f.alternativeFilenames"));
      group.setLayout(WidgetFactory.createGridLayout(1, 1, 1, 0, 0, false));
      group.setLayoutData(new GridData(768));
      Arrays.sort(this.file.getNames(), String.CASE_INSENSITIVE_ORDER);
      this.renameList = new List(group, 2820);

      for (int i = 0; i < this.file.getNames().length; i++) {
         this.renameList.add(this.file.getNames()[i]);
      }

      GridData listGridData = new GridData(768);
      listGridData.heightHint = 80;
      listGridData.widthHint = 1;
      this.renameList.setLayoutData(listGridData);
      this.renameList.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            String name = FileDetailDialog.this.renameList.getSelection()[0];
            FileDetailDialog.this.renameText.setText(name);
         }
      });
      Composite renameComposite = new Composite(composite, 0);
      renameComposite.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 4, 0, false));
      renameComposite.setLayoutData(new GridData(768));
      this.renameText = new Text(renameComposite, 2048);
      this.renameText.setText(this.file.getName());
      this.renameText.setFont(PreferenceLoader.loadFont("consoleFontData"));
      GridData textGridData = new GridData(768);
      textGridData.widthHint = 1;
      this.renameText.setLayoutData(textGridData);
      this.renameText.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent event) {
            if (event.character == '\r') {
               FileDetailDialog.this.renameFile();
               FileDetailDialog.this.renameText.setText("");
            }
         }
      });
      Button renameButton = new Button(renameComposite, 0);
      renameButton.setText(SResources.getString("dd.f.renameFile"));
      renameButton.setLayoutData(new GridData(128));
      renameButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FileDetailDialog.this.renameFile();
         }
      });
   }

   protected Control createButtonBar(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayoutData(new GridData(768));
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 0, 0, false));
      Composite spacer = new Composite(composite, 0);
      GridData spacerGridData = new GridData(768);
      spacerGridData.heightHint = 5;
      spacer.setLayoutData(spacerGridData);
      Composite buttonComposite = new Composite(composite, 0);
      buttonComposite.setLayoutData(new GridData(128));
      buttonComposite.setLayout(WidgetFactory.createRowLayout(false, false, false, 256, 0, 0, 0, 0, 5));
      if (this.file.getFileStateEnum() == EnumFileState.PAUSED
         || this.file.getFileStateEnum() == EnumFileState.DOWNLOADING
         || this.file.getFileStateEnum() == EnumFileState.QUEUED) {
         this.fileCancelButton = new Button(buttonComposite, 0);
         this.fileCancelButton.setLayoutData(new RowData());
         this.fileCancelButton.setText(SResources.getString("dd.f.cancelFile"));
         this.fileCancelButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               MessageBox messageBox = new MessageBox(FileDetailDialog.this.fileCancelButton.getShell(), 196);
               messageBox.setMessage(SResources.getString("dd.f.reallyCancel"));
               if (messageBox.open() == 64) {
                  FileDetailDialog.this.file.setState(EnumFileState.CANCELLED);
                  FileDetailDialog.this.fileCancelButton.setEnabled(false);
                  FileDetailDialog.this.fileActionButton.setEnabled(false);
               }
            }
         });
      }

      this.fileActionButton = new Button(buttonComposite, 0);
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
         public void widgetSelected(SelectionEvent event) {
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
      Button closeButton = new Button(buttonComposite, 0);
      closeButton.setFocus();
      closeButton.setLayoutData(new RowData());
      closeButton.setText(SResources.getString("b.close"));
      closeButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FileDetailDialog.this.close();
         }
      });
      return composite;
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

   public void updateViews(int id) {
      if (this.fileCommentsViewFrame != null) {
         this.fileCommentsViewFrame.getGView().refresh();
      }
   }

   public boolean close() {
      this.file.deleteObserver(this);
      return super.close();
   }
}
