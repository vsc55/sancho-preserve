package sancho.view.search.result;

import java.util.Arrays;
import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import sancho.model.mldonkey.Result;

class ResultTableMenuListener$9 extends MouseTrackAdapter {
   // $VF: synthetic field
   private final Control val$control;
   // $VF: synthetic field
   private final ICustomViewer val$cViewer;
   // $VF: synthetic field
   private final ResultTableMenuListener$ToolTipHandler this$1;

   ResultTableMenuListener$9(ResultTableMenuListener$ToolTipHandler var1, Control var2, ICustomViewer var3) {
      this.this$1 = var1;
      this.val$control = var2;
      this.val$cViewer = var3;
   }

   public void mouseExit(MouseEvent var1) {
      if (ResultTableMenuListener$ToolTipHandler.access$900(this.this$1).isVisible()
         && ResultTableMenuListener$ToolTipHandler.access$1800(this.this$1).x != var1.x
         && ResultTableMenuListener$ToolTipHandler.access$1800(this.this$1).y != var1.y) {
         ResultTableMenuListener$ToolTipHandler.access$1700(this.this$1, false);
      }

      ResultTableMenuListener$ToolTipHandler.access$1902(this.this$1, null);
   }

   public void mouseHover(MouseEvent var1) {
      if (var1.widget != null && !var1.widget.isDisposed()) {
         ResultTableMenuListener$ToolTipHandler.access$1800(this.this$1).x = var1.x;
         ResultTableMenuListener$ToolTipHandler.access$1800(this.this$1).y = var1.y;
         ResultTableMenuListener$ToolTipHandler.access$2002(
            this.this$1, this.val$control.toDisplay(ResultTableMenuListener$ToolTipHandler.access$1800(this.this$1))
         );
         Rectangle var2 = ResultTableMenuListener$ToolTipHandler.access$900(this.this$1).getBounds();
         var2.x -= 10;
         var2.y -= 10;
         var2.width += 20;
         var2.height += 20;
         if (!ResultTableMenuListener$ToolTipHandler.access$900(this.this$1).isVisible()
            || !var2.contains(ResultTableMenuListener$ToolTipHandler.access$2000(this.this$1))) {
            Widget var3 = var1.widget;
            Table var4 = (Table)var3;
            int var5 = 0;
            int var6 = 0;
            boolean var7 = false;

            for (int var8 = 0; var8 < var4.getColumns().length; var8++) {
               if (this.val$cViewer.getColumnIDs()[var8] == 1) {
                  var6 = var5 + var4.getColumns()[var8].getWidth();
                  var7 = true;
                  break;
               }

               var5 += var4.getColumns()[var8].getWidth();
            }

            ScrollBar var9 = var4.getHorizontalBar();
            int var10 = var9 != null ? var9.getSelection() : 0;
            var5 -= var10;
            var6 -= var10;
            TableItem var17 = var4.getItem(ResultTableMenuListener$ToolTipHandler.access$1800(this.this$1));
            if (var17 == null) {
               ResultTableMenuListener$ToolTipHandler.access$1700(this.this$1, false);
               ResultTableMenuListener$ToolTipHandler.access$1902(this.this$1, null);
            }

            if (var17 != ResultTableMenuListener$ToolTipHandler.access$1900(this.this$1)) {
               ResultTableMenuListener$ToolTipHandler.access$1902(this.this$1, var17);
               if (!var7
                  || var5 < ResultTableMenuListener$ToolTipHandler.access$1800(this.this$1).x
                     && ResultTableMenuListener$ToolTipHandler.access$1800(this.this$1).x < var6) {
                  TableItem var11 = var17;
                  ResultTableMenuListener$ToolTipHandler.access$1202(this.this$1, (Result)var11.getData());
                  if (ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1) == null) {
                     ResultTableMenuListener$ToolTipHandler.access$1700(this.this$1, false);
                     ResultTableMenuListener$ToolTipHandler.access$1902(this.this$1, null);
                  } else {
                     ResultTableMenuListener$ToolTipHandler.access$2100(this.this$1)
                        .setImage(ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1).getFileTypeImage());
                     ResultTableMenuListener$ToolTipHandler.access$2200(this.this$1)
                        .setText(ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1).getName());
                     ResultTableMenuListener$ToolTipHandler.access$1402(this.this$1, "");
                     ResultTableMenuListener$ToolTipHandler.access$1502(this.this$1, "");
                     ResultTableMenuListener$ToolTipHandler.access$1402(
                        this.this$1, ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1).getMD4().toUpperCase()
                     );
                     ResultTableMenuListener$ToolTipHandler.access$1502(this.this$1, ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1).getED2K());
                     ResultTableMenuListener$ToolTipHandler.access$1602(this.this$1, ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1).getSize());
                     ResultTableMenuListener$ToolTipHandler.access$2300(this.this$1)
                        .setText(ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1).getToolTipContent());
                     ResultTableMenuListener$ToolTipHandler.access$2400(this.this$1).removeAll();
                     ResultTableMenuListener$ToolTipHandler.access$2400(this.this$1).pack();
                     GridData var12 = new GridData();
                     var12.heightHint = 0;
                     var12.widthHint = 0;
                     ResultTableMenuListener$ToolTipHandler.access$2500(this.this$1).setLayoutData(var12);
                     var12 = new GridData();
                     var12.heightHint = 0;
                     var12.widthHint = 0;
                     ResultTableMenuListener$ToolTipHandler.access$2400(this.this$1).setLayoutData(var12);
                     var12 = new GridData(768);
                     var12.heightHint = 0;
                     var12.widthHint = 0;
                     ResultTableMenuListener$ToolTipHandler.access$2600(this.this$1).setLayoutData(var12);
                     ResultTableMenuListener$ToolTipHandler.access$900(this.this$1).pack();
                     String[] var13 = ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1).getNames();
                     if (var13 != null && var13.length > 1) {
                        int var14 = ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1).getNames().length;
                        var14 = var14 > 6 ? 6 : var14;
                        String[] var15 = new String[var13.length];
                        System.arraycopy(var13, 0, var15, 0, var13.length);
                        Arrays.sort(var15, String.CASE_INSENSITIVE_ORDER);
                        var12 = new GridData(768);
                        ResultTableMenuListener$ToolTipHandler.access$2500(this.this$1).setLayoutData(new GridData(768));
                        ResultTableMenuListener$ToolTipHandler.access$2600(this.this$1).setLayoutData(var12);
                        var12 = new GridData();
                        var12.heightHint = var14 * ResultTableMenuListener$ToolTipHandler.access$2700(this.this$1);
                        var12.widthHint = ResultTableMenuListener$ToolTipHandler.access$900(this.this$1).getBounds().width;
                        ResultTableMenuListener$ToolTipHandler.access$2400(this.this$1).setLayoutData(var12);

                        for (int var16 = 0; var16 < var15.length; var16++) {
                           ResultTableMenuListener$ToolTipHandler.access$2400(this.this$1).add(var15[var16]);
                        }
                     } else {
                        var12 = new GridData();
                        var12.heightHint = 0;
                        var12.widthHint = 0;
                        ResultTableMenuListener$ToolTipHandler.access$2500(this.this$1).setLayoutData(var12);
                        var12 = new GridData();
                        var12.heightHint = 0;
                        var12.widthHint = 0;
                        ResultTableMenuListener$ToolTipHandler.access$2400(this.this$1).setLayoutData(var12);
                        var12 = new GridData(768);
                        var12.heightHint = 0;
                        ResultTableMenuListener$ToolTipHandler.access$2600(this.this$1).setLayoutData(var12);
                     }

                     ResultTableMenuListener$ToolTipHandler.access$2800(
                        this.this$1,
                        ResultTableMenuListener$ToolTipHandler.access$900(this.this$1),
                        ResultTableMenuListener$ToolTipHandler.access$2000(this.this$1)
                     );
                     ResultTableMenuListener$ToolTipHandler.access$1700(this.this$1, true);
                  }
               } else {
                  ResultTableMenuListener$ToolTipHandler.access$1700(this.this$1, false);
               }
            }
         }
      }
   }
}
