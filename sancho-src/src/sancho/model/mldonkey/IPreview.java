package sancho.model.mldonkey;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.program.Program;

public interface IPreview {
   String getName();

   String getPreviewURL();

   Program getOSPreviewApp();

   ImageDescriptor getFileTypeImageDescriptor();

   String preview(String app, int index);

   String preview(Program program, int index);

   String preview(int index);

   String getContentRange(int index);

   String[] getSubFileNames();

   long[] getSubFileSizes();

   String[] getSubFileMagics();
}
