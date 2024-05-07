package study;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;


public class wrongAnswer extends JFrame implements ActionListener {

    static HashMap<String, String> word = new HashMap<>();
    static HashMap<String, HashMap<String, String>> wrong;
    int minValue = 0;
    int maxValue = 0;
    JButton numbtn;
    JButton modbtn;
    boolean scoreChange = true;
    boolean mod = true;
    Sound correct;
    Sound incorrect;
    public static void main(String[] args) throws Exception {
        new wrongAnswer();
    }
    public wrongAnswer() throws Exception {
        wrong = readWrong();
        for(String str : wrong.keySet()){
            word.put(str, wrong.get(str).get("answer"));
        }
        init();
    }

    public void init(){

        correct = new Sound("./sound/correct.wav");
        incorrect = new Sound("./sound/incorrect.wav");

        setTitle("오답노트");
        //버튼들
        JButton minbtn = new JButton("<");
        numbtn = new JButton("[" + minValue+" ~ "+maxValue + "]");
        JButton maxbtn = new JButton(">");
        modbtn = new JButton("       객관식       ");
        JButton testbtn = new JButton("시험보기");

        //ActionListener
        minbtn.addActionListener(this);
        numbtn.addActionListener(this);
        maxbtn.addActionListener(this);
        modbtn.addActionListener(this);
        testbtn.addActionListener(this);

        minbtn.setActionCommand("min");
        numbtn.setActionCommand("num");
        maxbtn.setActionCommand("max");
        modbtn.setActionCommand("mod");
        testbtn.setActionCommand("test");

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

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(minbtn);
        topPanel.add(numbtn);
        topPanel.add(maxbtn);
        topPanel.add(modbtn, BorderLayout.CENTER);
        topPanel.add(testbtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
            String sb = str +
                    "/" +
                    wrong.get(str).get("answer") +
                    "/" +
                    wrong.get(str).get("choice") +
                    "/" +
                    wrong.get(str).get("subjective");

            fos.write(sb.getBytes());
            fos.write("\n".getBytes());
        }

        fos.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command){
            case "min":
                if(scoreChange){
                    if(maxValue > minValue) maxValue--;
                }else{
                    if(minValue > 0)  minValue--;
                }
                numbtn.setText("[" + minValue+" ~ "+maxValue + "]");
                this.revalidate();
                break;
            case "num":
                scoreChange = !scoreChange;
                break;
            case "max":
                if(scoreChange){
                    maxValue++;
                    System.out.println(maxValue);
                }else{
                    if(minValue < maxValue) minValue++;
                }
                numbtn.setText("[" + minValue+" ~ "+maxValue + "]");
                this.revalidate();
                break;
            case "mod":
                mod = !mod;
                if(mod) {
                    modbtn.setText("       객관식       ");
                }else{
                    modbtn.setText("       주관식       ");
                }

                this.revalidate();
                break;
            case "test":
                setVisible(false);
                HashMap<String, String> hashMap = new HashMap<>();
                for(String str : word.keySet()){
                    if(mod){
                        if(Integer.parseInt(wrong.get(str).get("choice")) >= minValue && Integer.parseInt(wrong.get(str).get("choice")) <= maxValue) hashMap.put(str, word.get(str));
                    }else{
                        if(Integer.parseInt(wrong.get(str).get("subjective")) >= minValue && Integer.parseInt(wrong.get(str).get("subjective")) <= maxValue) hashMap.put(str, word.get(str));
                    }
                }
                if(mod){
                    choice c = new choice("reexam",correct,incorrect);
                    c.ChangeWord(hashMap);
                }else{
                    subjective s = new subjective("reexam",correct,incorrect);
                    s.ChangeWord(hashMap);
                }
                break;
        }
    }
}
