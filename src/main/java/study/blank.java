package study;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class blank {

    List<String> enlist = new ArrayList<>();

    public static void main(String[] args) {
        blank b = new blank(1);
    }
    public blank(int i){
        try {
            InputStream modelIn = new FileInputStream("./en-sent.bin");
            SentenceModel model = new SentenceModel(modelIn);                        // 문장 나누기 준비
            SentenceDetectorME sdetector = new SentenceDetectorME(model);

            BufferedReader enbr = new BufferedReader(new FileReader("./sentence\\sentence" + i + ".txt"));     // 문장 파일 얻기

            List<String> sentencelist = new ArrayList<>(enbr.lines().toList()); // 문장 얻기

            List<String> lines = Files.readAllLines(Path.of("./word\\sentenceword" + i + ".txt"));   //파일로부터 얻어오기
            for(int j = 1; j < lines.size(); j+=2){
                enlist.add(lines.get(i - 1));
            }

            for(String str : sentencelist){
                for(String enstr : enlist){
                    if(str.contains(enstr)){
                        int length = enstr.length();
                        StringBuilder blank = new StringBuilder();
                        for(int j = 0; j < length; j++){
                            blank.append("_");
                        }
                        str = str.replace(enstr, blank);
                    }
                }
            }

        }catch( Exception e ) {
                throw new RuntimeException(e);
        }
    }
}
