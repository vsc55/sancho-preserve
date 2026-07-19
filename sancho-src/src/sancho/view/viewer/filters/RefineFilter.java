package sancho.view.viewer.filters;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Result;
import sancho.model.mldonkey.Server;
import sancho.model.mldonkey.SharedFile;
import sancho.model.mldonkey.utility.FileComment;
import sancho.utility.SwissArmy;
import sancho.view.downloadComplete.DownloadCompleteItem;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.fileDialog.SubfileItem;
import sancho.view.viewer.GView;

public class RefineFilter extends ViewerFilter {
   private static String SEPARATOR = " ";
   private GView gView;
   private Pattern regex;
   private boolean refine;
   private boolean returnValue;
   private boolean searchAlternates;

   public RefineFilter(GView gView) {
      this.gView = gView;
      this.update();
   }

   public void update() {
      if (this.refine = !this.gView.getRefineString().equals("") && !this.gView.getRefineString().equals("-")) {
         try {
            String refineString = this.gView.getRefineString();
            if (refineString.length() > 1000) {
               refineString = refineString.substring(0, 1000);
            }

            if (refineString.startsWith("-")) {
               refineString = refineString.substring(1);
            }

            this.regex = Pattern.compile(refineString, Pattern.CASE_INSENSITIVE);
         } catch (PatternSyntaxException exception) {
            this.refine = false;
         }
      }

      this.returnValue = !PreferenceLoader.loadBoolean("refineFilterNegation");
      if (this.returnValue && this.gView.getRefineString().startsWith("-")) {
         this.returnValue = false;
      }

      this.searchAlternates = PreferenceLoader.loadBoolean("refineFilterAlternates");
   }

   public boolean select(Viewer viewer, Object parentElement, Object element) {
      if (!this.refine) {
         return true;
      } else {
         String text;
         if (element instanceof Server) {
            Server server = (Server)element;
            StringBuffer buffer = new StringBuffer(64);
            buffer.append(server.getName());
            buffer.append(SEPARATOR);
            buffer.append(server.getDescription());
            buffer.append(SEPARATOR);
            buffer.append(server.getAddr());
            text = buffer.toString();
         } else if (element instanceof Result) {
            Result result = (Result)element;
            StringBuffer buffer = new StringBuffer();
            buffer.append(result.getName());
            if (this.searchAlternates) {
               this.appendAlternates(buffer, result.getNames());
            }

            text = buffer.toString();
         } else if (element instanceof SharedFile) {
            text = ((SharedFile)element).getName();
         } else if (element instanceof Client) {
            Client client = (Client)element;
            StringBuffer buffer = new StringBuffer();
            buffer.append(client.getName());
            buffer.append(SEPARATOR);
            buffer.append(client.getUploadFilename());
            text = buffer.toString();
         } else if (element instanceof File) {
            File file = (File)element;
            StringBuffer buffer = new StringBuffer();
            buffer.append(file.getName());
            buffer.append(SEPARATOR);
            buffer.append(file.getUser());
            buffer.append(SEPARATOR);
            buffer.append(file.getGroup());
            if (this.searchAlternates) {
               this.appendAlternates(buffer, file.getNames());
            }

            text = buffer.toString();
         } else if (element instanceof DownloadCompleteItem) {
            text = ((DownloadCompleteItem)element).getName();
         } else if (element instanceof SubfileItem) {
            text = ((SubfileItem)element).getName();
         } else {
            if (!(element instanceof FileComment)) {
               return true;
            }

            FileComment fileComment = (FileComment)element;
            StringBuffer buffer = new StringBuffer();
            buffer.append(fileComment.getName());
            buffer.append("");
            buffer.append(fileComment.getComment());
            text = buffer.toString();
         }

         return this.regex.matcher(text).find() ? this.returnValue : !this.returnValue;
      }
   }

   public void appendAlternates(StringBuffer buffer, String[] names) {
      for (int i = 0; i < names.length; i++) {
         buffer.append(SEPARATOR);
         buffer.append(names[i]);
      }
   }

   public String toString() {
      return "5," + SwissArmy.replaceAll(this.gView.getRefineString(), ",", "") + ",";
   }
}
