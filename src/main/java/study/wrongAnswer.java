package study;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class wrongAnswer implements ActionListener {

    static HashMap<String, String> word = new HashMap<>();
    static HashMap<String, HashMap<String, String>> wrong;
    JFrame frame;
    public static void main(String[] args) throws Exception {
        new wrongAnswer();
    }
    public wrongAnswer() throws Exception {
        wrong = readWrong();
        for(String str : wrong.keySet()){
            word.put(str, wrong.get(str).get("answer"));
        }
        wrongTable();
    }

    public void wrongTable(){
        frame = new JFrame("오답노트");
        //버튼들
        JButton minbtn = new JButton("<");
        JButton numbtn = new JButton("0-0");
        JButton maxbtn = new JButton(">");
        JButton testbtn = new JButton("시험보기");

        minbtn.addActionListener(this);
        numbtn.addActionListener(this);
        maxbtn.addActionListener(this);
        testbtn.addActionListener(this);
        //JTable
        List<String> datalist = word.keySet().stream().toList();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("영어");
        model.addColumn("한국어");
        model.addColumn("객관식 오댭수");
        model.addColumn("주관식 오댭수");
        for(String str : datalist){
            model.addRow(new Object[]{str,word.get(str),
                                      wrong.get(str).get("choice")+"개",
                                      wrong.get(str).get("subjective")+"개"});
        }
        JTable table = new JTable(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane pane = new JScrollPane(table);

        frame.add(pane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static HashMap<String, HashMap<String, String>> readWrong() throws Exception {
        HashMap<String, HashMap<String, String>> wrongword = new HashMap<>();
        List<String> lines = Files.readAllLines(Path.of("C:\\Users\\user\\OneDrive\\바탕 화면\\문제내기\\wrong\\wrong.txt"));      //파일로부터 얻어오기
        for(String str : lines){
            String[] split = str.split("/");
            HashMap<String, String> count = new HashMap<>();
            count.put("answer", split[1]);
            count.put("choice", split[2]);
            count.put("subjective", split[3]);
            wrongword.put(split[0], count);
        }
        return wrongword;
    }

    public static void writeWrong(HashMap<String, HashMap<String, String>> wrong) throws Exception {
        String path = "C:\\Users\\user\\OneDrive\\바탕 화면\\문제내기\\wrong\\wrong.txt";
        FileOutputStream fos = new FileOutputStream(path, false);

        for(String str : wrong.keySet()){
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("/");
            sb.append(wrong.get(str).get("answer"));
            sb.append("/");
            sb.append(wrong.get(str).get("choice"));
            sb.append("/");
            sb.append(wrong.get(str).get("subjective"));

            fos.write(sb.toString().getBytes());
            fos.write("\n".getBytes());
        }

        fos.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
