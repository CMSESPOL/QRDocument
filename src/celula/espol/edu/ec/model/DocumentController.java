/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celula.espol.edu.ec.model;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

/**
 *
 * @author Kenny Camba
 */
public class DocumentController {
    private String title;
    //private String content;
    private XWPFDocument doc;
    private XWPFParagraph titleDoc;
    private XWPFParagraph paragraph;
    
    public DocumentController(String title/*, String content*/){
        this.title = title;
        //this.content = content;
        doc = new XWPFDocument();
        //CTSectPr ct = doc.getDocument().getBody().addNewSectPr();
        //ct.addNewPgMar().setTop(new BigInteger("5000")); 
        //titleDoc = doc.createParagraph();
        //paragraph = doc.createParagraph();
        //titleDoc.setAlignment(ParagraphAlignment.CENTER);
        //titleDoc.setVerticalAlignment(TextAlignment.TOP);
        //paragraph.setAlignment(ParagraphAlignment.BOTH);
        //format();
    }
    
    private void format(){
        XWPFRun r = titleDoc.createRun();
        r.setBold(true);
        r.setText(title);
        r.setFontFamily("Arial");
        r.setFontSize(14);
        r.setTextPosition(10);
        r.setUnderline(UnderlinePatterns.SINGLE);
        
        XWPFRun r1 = paragraph.createRun();
        //r1.setText(content);
        r.setFontSize(12);
        r1.addCarriageReturn();
    }
    
    public void generateDocument()throws FileNotFoundException, IOException{
        try(FileOutputStream word = new FileOutputStream(title+".docx")){
            doc.write(word);
            word.close();
            //Desktop.getDesktop().open(new File(title+".docx "));
        }catch(Exception ex){
            throw ex;
        }
    }
    
    public boolean addImage(String file) throws FileNotFoundException, InvalidFormatException, IOException{
        XWPFParagraph img = doc.createParagraph();
        img.setAlignment(ParagraphAlignment.RIGHT);
        img.setVerticalAlignment(TextAlignment.TOP);
        XWPFRun run = img.createRun();
        FileInputStream is = new FileInputStream(file);
        run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, file, Units.toEMU(100), Units.toEMU(100)); //200x200 pixeles
        is.close();
        return true;
    }
    
    public String getName(){
        return title + ".docx";
    }
    
    @Override
    public String toString(){
        return this.getName();
    }
    
}
