package study;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;


public class choice extends JFrame implements ActionListener {
    private int selnumber;
    private final int width;
    private final int height;
    private HashMap<String,String> word = new HashMap<>();
    private final List<String> enlist = new ArrayList<>();
    private List<String> krlist = new ArrayList<>();
    private final HashMap<String,String> wrong = new HashMap<>();
    JButton[] jbs = new JButton[4];
    JPanel alist = new JPanel();
    JPanel qlist = new JPanel();
    JLabel q = new JLabel("",JLabel.CENTER);
    JLabel count = new JLabel("",JLabel.CENTER);
    private final Sound correct;
    private final Sound incorrect;
    public choice(String str, Sound correct, Sound incorrect) {
        this.width = Main.width;
        this.height = Main.height;
        this.correct = correct;
        this.incorrect = incorrect;
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        if(!str.equals("reexam")){
            init(str);
        }
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_1 -> actionList(0);
                    case KeyEvent.VK_2 -> actionList(1);
                    case KeyEvent.VK_3 -> actionList(2);                    //버튼 단축키 설정
                    case KeyEvent.VK_4 -> actionList(3);
                }
            }
        });
    }
    public void init(String str){
        this.setSize(width,height);
        this.setLocationRelativeTo(null);                                           //기본 설정
        this.setLayout(new GridLayout(3,1));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        alist.setSize(width,height / 2);
        alist.setLayout(new FlowLayout(FlowLayout.CENTER));                         // 보기 패널

        qlist.setSize(width,height / 3);
        qlist.setLayout(new BorderLayout());                                        // 위쪽 패널

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

        font = new Font("Arial",Font.BOLD,100);
        q.setFont(font);                                                           //문제 라벨
        qlist.add(q,BorderLayout.CENTER);

        font = new Font("Arial",Font.BOLD,30);
        count.setFont(font);                                                       //문제 갯수 라벨
        count.setLocation(width / 10,height);
        qlist.add(count,BorderLayout.EAST);

        font = new Font("한컴 고딕",Font.BOLD,18);
        for (int i = 0; i < jbs.length; i++) {
            jbs[i] = new JButton();
            jbs[i].setFont(font);
            jbs[i].setPreferredSize(new Dimension(width / 3,height / 10));  //문제 버튼
            jbs[i].setActionCommand(""+i);
            jbs[i].addActionListener(this);
            alist.add(jbs[i]);
        }

        this.add(qlist);this.add(new JLabel(""));this.add(alist);

        if(word.isEmpty()){
            word = xlsxRead.readWord(Integer.valueOf(str));

            enlist.addAll(word.keySet());
            krlist = xlsxRead.readKorean();
            Collections.shuffle(enlist);
            newword(enlist.get(0));

            this.setVisible(true);
        }else{
            for(String index : word.keySet()){
                enlist.add(index);
                krlist = xlsxRead.readKorean();
            }
            Collections.shuffle(enlist);
            newword(enlist.get(0));
            this.setVisible(true);
        }
    }
    public void newword(String encor){                                         //단어 바꾸기
        List<String> answers = new ArrayList<>(krlist);
        answers.remove(word.get(encor));
        Collections.shuffle(answers);
        q.setText(encor);
        count.setText(enlist.size() + "/" + word.size() + "   ");
        for (int i = 0; i < jbs.length; i++) {
            jbs[i].setBackground(Color.white);
            jbs[i].setText(answers.get(i));
        }
        jbs[selnumber].setText(word.get(encor));
        this.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {                               //버튼 클릭 이벤트
        if(e.getActionCommand().equals("quit")){
            new wrongbook("객관식 오답들", wrong, correct, incorrect);
            this.setVisible(false);
        }else{
            int command=Integer.parseInt(e.getActionCommand());
            actionList(command);
        }
    }
    public void actionList(int command){                                       //정답 확인
        if(command!=selnumber){
            wrong.put(enlist.get(0), word.get(enlist.get(0)));
            incorrect.play();

            try {
                HashMap<String, HashMap<String, String>> wrongword = wrongAnswer.readWrong();
                if(wrongword.containsKey(enlist.get(0))){
                    wrongword.get(enlist.get(0)).put("choice", String.valueOf(Integer.parseInt(wrongword.get(enlist.get(0)).get("choice")) + 1));
                }else{
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("answer", word.get(enlist.get(0)));
                    hashMap.put("choice", "1");
                    hashMap.put("subjective", "0");
                    wrongword.put(enlist.get(0), hashMap);
                }
                wrongAnswer.writeWrong(wrongword);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            correct.play();
        }
        for (JButton jb : jbs) {
            jb.setBackground(new Color(236,55,55));
        }
        jbs[selnumber].setBackground(new Color(6,255,69));
        selnumber = new Random().nextInt(4);
        new Thread(() -> {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            enlist.remove(enlist.get(0));
            if(enlist.isEmpty()){
                new wrongbook("객관식 오답들",wrong, correct, incorrect);
                this.setVisible(false);
                return;
            }
            newword(enlist.get(0));
        }).start();
    }
    public void ChangeWord(HashMap<String,String> hashMap){
        word = hashMap;
        init("reexam");
    }
}