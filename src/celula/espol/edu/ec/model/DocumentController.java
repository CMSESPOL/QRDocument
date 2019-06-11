/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celula.espol.edu.ec.model;

//import java.awt.Desktop;
//import java.io.File;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
//import java.math.BigInteger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

/**
 *
 * @author Kenny Camba
 */
public class DocumentController {
    private final String title;
    //private String content;
    private final XWPFDocument doc;
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
    
    public Document generateDocument()throws FileNotFoundException, IOException{
        try(FileOutputStream word = new FileOutputStream(title+".docx")){
            doc.write(word);
            word.close();
            return new Document(title, new File(title+".docx"));
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
        try (FileInputStream is = new FileInputStream(file)) {
            run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, file, Units.toEMU(100), Units.toEMU(100)); //200x200 pixeles
        } //200x200 pixeles
        return true;
    }
    
    public String getName(){
        return title + ".docx";
    }
    
    @Override
    public String toString(){
        return this.getName();
    }
    
    public void generatePDF(Document doc){
        try {
            POIFSFileSystem word = new POIFSFileSystem(doc.getDirectory());
            HWPFDocument hwd = new HWPFDocument(word);
            WordExtractor we = new WordExtractor(hwd);
            OutputStream file = new FileOutputStream(new File(doc.getName()+".pdf"));
            com.lowagie.text.Document docu = new com.lowagie.text.Document();
            PdfWriter writer = PdfWriter.getInstance(docu, file);
            Range range = hwd.getRange();
            docu.open();
            writer.setPageEmpty(true);  
            docu.newPage();  
            writer.setPageEmpty(true);
            String[] paragraphs = we.getParagraphText();  
            for (int i = 0; i < paragraphs.length; i++) {  
                org.apache.poi.hwpf.usermodel.Paragraph pr = range.getParagraph(i);
                paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");  
                docu.add(new Paragraph(paragraphs[i]));
            }  
            //PdfWriter writer
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(DocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
