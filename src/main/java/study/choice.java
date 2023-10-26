package study;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
public class choice extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new choice("word1");
    }
    private int selnumber;
    private final int width;
    private final int height;
    private HashMap<String,String> word = new HashMap<>();
    private final List<String> enlist = new ArrayList<>();
    private final List<String> krlist = new ArrayList<>();
    private final HashMap<String,String> wrong = new HashMap<>();
    JButton[] jbs = new JButton[4];
    JPanel alist = new JPanel();
    JPanel qlist = new JPanel();
    JLabel q = new JLabel("",JLabel.CENTER);
    JLabel count = new JLabel("",JLabel.CENTER);
    public choice(String str) {
        this.width = Main.width;
        this.height = Main.height;
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
            try {
                List<String> lines = Files.readAllLines(Path.of("./word\\" + str + ".txt"));   //파일로부터 얻어오기
                for(int i = 1; i < lines.size(); i+=2){
                    word.put(lines.get(i - 1),lines.get(i));
                    enlist.add(lines.get(i - 1));
                    krlist.add(lines.get(i));
                }
                Collections.shuffle(enlist);
                newword(enlist.get(0));
                this.setVisible(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else{
            for(String index : word.keySet()){
                enlist.add(index);
                krlist.add(word.get(index));
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
        count.setText(enlist.size() + "/" + krlist.size() + "   ");
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
            new wrongbook("객관식 오답들",wrong);
            this.setVisible(false);
        }else{
            int command=Integer.parseInt(e.getActionCommand());
            actionList(command);
        }
    }
    public void actionList(int command){                                       //정답 확인
        if(command!=selnumber){
            wrong.put(enlist.get(0), word.get(enlist.get(0)));
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
                new wrongbook("객관식 오답들",wrong);
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