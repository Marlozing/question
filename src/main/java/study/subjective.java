package study;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class subjective extends JFrame implements KeyListener, ActionListener {

    private final int width;
    private final int height;
    private HashMap<String,String> word = new HashMap<>();
    private final List<String> krlist = new ArrayList<>();
    private final HashMap<String,String> wrong = new HashMap<>();
    private final Sound correct;
    private final Sound incorrect;
    JPanel qlist = new JPanel();
    JLabel q = new JLabel("",JLabel.CENTER);
    JLabel count = new JLabel("",JLabel.CENTER);
    JTextField field = new JTextField("");
    public subjective(String str, Sound correct, Sound incorrect) {
        this.width = Main.width;
        this.height = Main.height;
        this.correct = correct;
        this.incorrect = incorrect;
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        if(!str.equals("reexam")){
            init(str);
        }
    }
    public void init(String str){
        this.setSize(width,height);
        this.setLocationRelativeTo(null);                                   //기초 설정
        this.setLayout(new GridLayout(3,1));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        qlist.setSize(width,height / 3);                             //질문 패널
        qlist.setLayout(new BorderLayout());

        Font font = new Font("한컴 고딕",Font.BOLD,18);
        JButton quit = new JButton("포기하기");
        quit.setFont(font);
        quit.setBackground(Color.GRAY);
        quit.setPreferredSize(new Dimension(width / 6,height / 14));   //포기 버튼
        quit.setActionCommand("quit");
        quit.addActionListener(this);
        JPanel gg = new JPanel();
        gg.setLayout(new BorderLayout());
        gg.add(quit,BorderLayout.NORTH);
        qlist.add(gg,BorderLayout.WEST);

        font = new Font("한컴 고딕",Font.BOLD,60);
        q.setFont(font);
        qlist.add(q,BorderLayout.CENTER);                                   //질문 추가

        font = new Font("Arial",Font.BOLD,30);
        count.setFont(font);
        count.setLocation(width / 10,height);                            //문제 갯수
        qlist.add(count,BorderLayout.EAST);

        font = new Font("한컴 고딕",Font.BOLD,50);
        field.setFont(font);
        field.setHorizontalAlignment(SwingConstants.CENTER);                //정답 필드
        field.setSize(width/2,height/3);
        field.addKeyListener(this);

        this.add(qlist);this.add(new JLabel(""));this.add(field);
        if(word.isEmpty()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get("./word\\" + str + ".txt")); //파일 읽기
                for (int i = 1; i < lines.size(); i += 2) {
                    word.put(lines.get(i), lines.get(i - 1));
                    krlist.add(lines.get(i));
                }
                Collections.shuffle(krlist);
                newword(krlist.get(0));
                this.setVisible(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else{
            krlist.addAll(word.keySet());
            Collections.shuffle(krlist);
            newword(krlist.get(0));
            this.setVisible(true);
        }
    }
    public void ChangeWord(HashMap<String,String> hashMap){
        word = hashMap;
        init("reexam");
    }
    public void newword(String encor){                                   //단어 바꾸기
        q.setText(encor);
        count.setText(krlist.size() + "/" + word.size() + "   ");
        this.revalidate();
        field.setText(word.get(encor).charAt(0)+"");
        field.requestFocus();
        field.setSelectionStart(1);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {                                   //정답 확인
        int keyCode = e.getKeyCode();
        if(keyCode==KeyEvent.VK_ENTER) {
            if(!(field.getText() + " ").equals(word.get(krlist.get(0)))){
                wrong.put(word.get(krlist.get(0)), krlist.get(0));
                field.setText(word.get(krlist.get(0)));
                field.setBackground(new Color(236,55,55));
                incorrect.play();

                try {
                    HashMap<String, HashMap<String, String>> wrongword = wrongAnswer.readWrong();
                    if(wrongword.containsKey(word.get(krlist.get(0)))){
                        wrongword.get(word.get(krlist.get(0))).put("subjective", String.valueOf(Integer.parseInt(wrongword.get(word.get(krlist.get(0))).get("choice")) + 1));
                    }else{
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("answer", krlist.get(0));
                        hashMap.put("choice", "0");
                        hashMap.put("subjective", "1");
                        wrongword.put(word.get(krlist.get(0)), hashMap);
                    }
                    wrongAnswer.writeWrong(wrongword);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }else{
                field.setBackground(new Color(6,255,69));
                correct.play();
            }
            new Thread(() -> {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException error) {
                    error.printStackTrace();
                }
                field.setBackground(Color.WHITE);
                krlist.remove(krlist.get(0));
                field.setText("");
                if(krlist.isEmpty()){
                    new wrongbook("주관식 오답들",wrong,correct,incorrect);
                    setVisible(false);
                    return;
                }
                newword(krlist.get(0));
            }).start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("quit")) {
            new wrongbook("주관식 오답들", wrong, correct, incorrect);
            this.setVisible(false);
        }
    }
}