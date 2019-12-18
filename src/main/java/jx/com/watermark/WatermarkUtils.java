package jx.com.watermark;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;

import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WatermarkUtils {
    public static void main(String[] args) throws DocumentException, IOException {
        // 要输出的pdf文件
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("E:/abc.pdf")));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // 将pdf文件先加水印然后输出
        setWatermark(bos, "D:/a.pdf", format.format(cal.getTime()) + "  下载使用人：" + "测试user", 16);
    }

    /**
     *
     * @param input
     *            原PDF位置
     * @param waterMarkName
     *            页脚添加水印
     * @param permission
     *            权限码
     * @throws DocumentException
     * @throws IOException
     */
    public static void setWatermark(BufferedOutputStream bos, String input, String waterMarkName, int permission)
            throws DocumentException, IOException {
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, bos);
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        BaseFont base = BaseFont.createFont(BaseFont.HELVETICA,
                BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i);// 在内容上方加水印
            // content = stamper.getUnderContent(i);//在内容下方加水印
            gs.setFillOpacity(0.2f);
            // content.setGState(gs);
            content.beginText();
            content.setColorFill(Color.LIGHT_GRAY);
            content.setFontAndSize(base, 50);
            System.out.println("");
            content.setTextMatrix(70, 200);
            content.showTextAligned(Element.ALIGN_CENTER, "公司内部文件，请注意保密！", 300, 350, 55);
            Image image = Image.getInstance("D:/1.jpg");
            /*
              img.setAlignment(Image.LEFT | Image.TEXTWRAP);
              img.setBorder(Image.BOX); img.setBorderWidth(10);
              img.setBorderColor(BaseColor.WHITE); img.scaleToFit(100072);//大小
              img.setRotationDegrees(-30);//旋转
             */
            image.setAbsolutePosition(200, 206); // set the first background
            // image of the absolute
            image.scaleToFit(200, 200);
            content.addImage(image);
            content.setColorFill(Color.BLACK);
            content.setFontAndSize(base, 8);
            content.showTextAligned(Element.ALIGN_CENTER, "下载时间：" + waterMarkName + "", 300, 10, 0);
            content.endText();

        }
        stamper.close();
    }
}
