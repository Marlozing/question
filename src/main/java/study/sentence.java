package study;

import kr.bydelta.koala.hnn.SentenceSplitter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class sentence {
    public static void main(String[] arg) {
        for(int i = 1; i <= 9; i++){
            new sentence(i);
        }
    }
    public sentence(int i){
        try
        {
            InputStream modelIn = new FileInputStream("./en-sent.bin");
            SentenceModel model = new SentenceModel(modelIn);                        // 문장 나누기 준비
            SentenceDetectorME sdetector = new SentenceDetectorME(model);

            BufferedReader enbr = new BufferedReader(new FileReader("./sentence\\sentence" + i +".txt"));     // 문장 파일 얻기
            StringBuilder sentencelist = new StringBuilder();
            for(String str : enbr.lines().toList()) {
                sentencelist.append(" ").append(str);            // 문장 얻기
            }

            String[] sentences = sdetector.sentDetect(sentencelist.toString());      //문장 나누기
            List<String> mixedSentences = new ArrayList<>();
            String instant_sentence = "";
            for(String str : sentences){
                str = str.replace(" .", "");
                if(!instant_sentence.isEmpty()){
                    str = instant_sentence + " " + str;
                    instant_sentence = "";
                }
                if(str.contains("\"")){
                    int count = str.length() - str.replace("\"", "").length();
                    if(count == 1){
                        instant_sentence = str;
                        continue;
                    }
                }
                List<String> token = getStrings(str);
                Collections.shuffle(token);
                StringBuilder mix = new StringBuilder("(");
                for(String str2 : token){
                    mix.append(str2).append(" / ");
                }
                mix.append(")");
                mix = new StringBuilder(mix.toString().replace("/ )", ")"));
                mixedSentences.add(mix.toString());
            }

            HWPFile hwpFile = HWPReader.fromFile("./files\\1.hwp");
            Section s = hwpFile.getBodyText( ).getSectionList( ).get( 0 );
            Paragraph firstParagraph = s.getParagraph( 0 );
            BufferedReader krbr = new BufferedReader(new FileReader("./translate\\translate" + i +".txt"));     //파일로부터 얻어오기
            StringBuilder translatelist = new StringBuilder();
            for(String str : krbr.lines().toList()) {
                translatelist.append(" ").append(str);            // 문장 얻기
            }
            instant_sentence = "";
            List<String> translate = new ArrayList<>();
            SentenceSplitter splitter = new SentenceSplitter();
            List<String> translate_sentences = splitter.invoke(String.valueOf(translatelist));
            for(String str : translate_sentences){
                str = str.replace(" .", ".");
                str = str.replace("  ", "");
                str = str.replace("‘", "'");
                str = str.replace("’", "'");
                str = str.replace("“", "\"");
                str = str.replace("”", "\"");
                if(!instant_sentence.isEmpty()){
                    for(String condition : Arrays.asList("'","\"")){
                        if(instant_sentence.contains(condition)) {
                            if(str.contains(condition)){
                                str = instant_sentence + " " + str;
                                instant_sentence = "";
                                translate.add(str);
                            }
                            else{
                                instant_sentence = instant_sentence + " " + str;
                            }
                        }
                    }
                    continue;
                }
                if (str.contains("'")) {
                    int count = str.length() - str.replace("'", "").length();
                    if (count % 2 == 1) {
                        instant_sentence = str;
                        continue;
                    }
                }
                if (str.contains("\"")) {
                    int count = str.length() - str.replace("\"", "").length();
                    if (count % 2 == 1) {
                        instant_sentence = str;
                        continue;
                    }
                }
                translate.add(str);
            }
            System.out.println(translate.size());
            System.out.println(mixedSentences.size());
            for(int j = 0; j < mixedSentences.size(); j++){
                System.out.println(translate.get(j));
                System.out.println(mixedSentences.get(j));
                firstParagraph.getText().addString(translate.get(j) + "\n");
                firstParagraph.getText().addString(mixedSentences.get(j)+"\n\n\n\n\n");
            }
            HWPWriter.toFile(hwpFile, "./files\\file-" + i + ".hwp" );
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
