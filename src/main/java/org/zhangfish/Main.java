package org.zhangfish;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.lowagie.text.pdf.PdfReader;

public class Main {
    static MainFrame mf;

    public static void main(String[] args) {
        mf = new MainFrame(new StartActionListener());

        mf.addText("正在初始化...");
        String inFolder = "./in";
        String outFolder = "./out";
        if (createDirectory(inFolder)) {
            mf.addText("输入文件夹创建成功, 名称为in, 请将要转为图片的pdf文件放入in文件夹");
        }
        if (createDirectory(outFolder)) {
            mf.addText("输出文件夹创建成功, 名称为out");
        }
    }

    /**
     * 开始转换按钮事件
     */
    static class StartActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pdf2png();
        }
    }

    /**
     * 转换in文件夹中的文件
     */
    static void pdf2png() {
        String inFolder = "./in";
        String outFolder = "./out";
        try {
            if (createDirectory(inFolder) && createDirectory(outFolder)) {
                File f = new File(inFolder);
                File[] fs = f.listFiles();
                if (fs == null) {
                    mf.addText(f.getCanonicalPath() + "文件夹中无文件");
                } else {
                    for (File i : fs) {
                        pdf2png(i.getCanonicalPath(), outFolder, 200);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * PDF转PNG
     *
     * @param pdfPath PDF路径
     * @param outFolderPath 输出文件夹
     * @param dpi 清晰度
     */
    static void pdf2png(String pdfPath, String outFolderPath, int dpi) {
        File pdfFile = new File(pdfPath);
        PDDocument pdDocument = null;
        try {
            String imgName = pdfFile.getName().substring(0, pdfFile.getName().lastIndexOf('.'));
            String extName = pdfFile.getName().substring(pdfFile.getName().lastIndexOf('.') + 1);
            String imgFolderPath = outFolderPath;

            if (!extName.equals("pdf")) {
                mf.addText("<" + imgName + ">" + " 不是pdf文件");
                return;
            }

            pdDocument = PDDocument.load(pdfFile);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            PdfReader reader = new PdfReader(pdfPath);
            int pages = reader.getNumberOfPages();

            boolean ok = true;  //单页文件直接输出单张png, 多页新建文件夹输出多张png
            if (pages > 1) {
                imgFolderPath = outFolderPath + File.separator + imgName;
                ok = createDirectory(imgFolderPath);
            }

            if (ok) {
                StringBuilder imgFilePath = null;
                for (int i = 0; i < pages; i++) {
                    String imgFilePathPrefix = imgFolderPath + File.separator + imgName;
                    imgFilePath = new StringBuilder();
                    imgFilePath.append(imgFilePathPrefix).append("_").append(String.valueOf(i + 1)).append(".png");
                    File outFile = new File(imgFilePath.toString());
                    BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                    ImageIO.write(image, "png", outFile);
                }
                mf.addText("<" + imgName + ">" + " 转换成功");
            } else {
                mf.addText(imgFolderPath + "文件夹创建失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pdDocument != null) {
                COSDocument cos = pdDocument.getDocument();
                try {
                    cos.close();
                    pdDocument.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建文件夹
     *
     * @param folder 需要创建的文件夹路径
     * @return 是否成功创建文件夹
     */
    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }
}