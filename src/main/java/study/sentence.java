package study;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.writer.HWPWriter;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class sentence {
    public static void main(String[] arg) {
        new sentence(1);
    }
    public sentence(int i){
        try
        {
            InputStream modelIn = new FileInputStream("./en-sent.bin");
            SentenceModel model = new SentenceModel(modelIn);                        // 문장 나누기 준비
            SentenceDetectorME sdetector = new SentenceDetectorME(model);

            BufferedReader br = new BufferedReader(new FileReader("./sentence\\sentence" + i +".txt"));     // 문장 파일 얻기
            StringBuilder sentencelist = new StringBuilder();
            for(String str : br.lines().toList()) {
                sentencelist.append(" ").append(str);            // 문장 얻기
            }

            String[] sentences = sdetector.sentDetect(sentencelist.toString());      //문장 나누기
            List<String> mixedSentences = new ArrayList<>();
            for(String str : sentences){
                List<String> token = getStrings(str);
                Collections.shuffle(token);
                StringBuilder mix = new StringBuilder("(");
                for(String str2 : token){
                    if(str2.length()==1){
                        mix.append(str2).append(" ");
                    }else{
                        mix.append(str2).append(" / ");
                    }
                }
                mix.append(")");
                mix = new StringBuilder(mix.toString().replace("/ )", ")"));
                mixedSentences.add(mix.toString());
            }


            HWPFile hwpFile = HWPReader.fromFile("./files\\1.hwp");
            Section s = hwpFile.getBodyText( ).getSectionList( ).get( 0 );
            Paragraph firstParagraph = s.getParagraph( 0 );
            List<String> translate = Files.readAllLines(Path.of("./translate\\translate" + i +".txt"));   //파일로부터 얻어오기
            for(i = 0; i < mixedSentences.size(); i++){
                firstParagraph.getText().addString(translate.get(i) + "\n");
                firstParagraph.getText().addString(mixedSentences.get(i)+"\n\n\n\n\n");
            }
            HWPWriter.toFile(hwpFile, "./files\\2.hwp" );
        }
        catch( Exception e ) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getStrings(String str) {
        List<String> token = new ArrayList<>(Arrays.asList(str.split(" ")));
        List<String> token2 = new ArrayList<>();
        String istr = "";
        for(String str2 : token){
            if(str2.length()==1 || str2.equals("the")){
                istr = str2 + " ";
            }else{
                if(istr.isEmpty()){
                    token2.add(str2);
                }else{
                    token2.add(istr + str2);
                    istr = "";
                }
            }
        }
        return token2;
    }
}
