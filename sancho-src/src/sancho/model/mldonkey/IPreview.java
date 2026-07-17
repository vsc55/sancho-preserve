package sancho.model.mldonkey;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.program.Program;

public interface IPreview {
   String getName();

   String getPreviewURL();

   Program getOSPreviewApp();

   ImageDescriptor getFileTypeImageDescriptor();

   String preview(String var1, int var2);

   String preview(Program var1, int var2);

   String preview(int var1);

   String getContentRange(int var1);

   String[] getSubFileNames();

   long[] getSubFileSizes();

   String[] getSubFileMagics();
}
