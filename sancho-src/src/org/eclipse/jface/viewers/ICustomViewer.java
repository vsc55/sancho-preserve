package org.eclipse.jface.viewers;

public interface ICustomViewer extends IInputProvider {
   int[] getColumnIDs();

   void setColumnIDs(String var1);

   void setContentProvider(IContentProvider var1);

   void setLabelProvider(IBaseLabelProvider var1);

   void setSorter(ViewerSorter var1);

   void setInput(Object var1);

   void setEditors(boolean var1);

   boolean getEditors();

   void updateDisplay();

   void updateSelection(ISelection var1);

   void clearAll();
}
