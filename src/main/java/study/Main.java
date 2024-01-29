package study;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends JFrame implements ActionListener{
    static Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
    static int width = res.width / 2;
    static int height = res.height / 2;
    static Sound correct;
    static Sound incorrect;
    JFrame list = new JFrame();
    public static void main(String[] args) {
        new Main();
    }
    public Main() {
        init();
    }

    public void init(){
        list.setSize(width, height);
        list.setLocationRelativeTo(null);

        Font font = new Font("한컴 고딕",Font.BOLD,70);
        for(String str : Arrays.asList("객관식", "주관식","문장")){
            JButton b = new JButton(str);
            b.setActionCommand(str);
            b.addActionListener(this);
            b.setFont(font);
            add(b);
        }
        setSize(width,height);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1,2));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        correct = new Sound("./sound/correct.wav");
        incorrect = new Sound("./sound/incorrect.wav");
    }
    @Override
    public void actionPerformed(ActionEvent e){
        String command=e.getActionCommand();
        if(command.equals("객관식")||command.equals("주관식")) {
            setVisible(false);
            list.setTitle(command);
            Font font = new Font("한컴 고딕", Font.BOLD, 20);
            File folder = new File("./word");
            ArrayList<String> files = new ArrayList<>();
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    files.add(file.getName().replace(".txt",""));
                }
            }
            for (int i = 0; i < 50; i++) {
                JButton b = new JButton("word " + (i + 1));
                b.setFont(font);
                b.addActionListener(this);
                b.setActionCommand("word" + (i + 1));
                list.add(b);
            }
            list.setLayout(new GridLayout(5, 10));
            list.setVisible(true);
            return;
        }
        if(command.equals("문장")){
            setVisible(false);
            list.setTitle(command);
            Font font = new Font("한컴 고딕", Font.BOLD, 100);
            for(String str : Arrays.asList("문장 시험","문장 출력")){
                JButton b = new JButton(str);
                b.setActionCommand(str);
                b.addActionListener(this);
                b.setFont(font);
                list.add(b);
            }
            list.setLayout(new GridLayout(1,2));
            list.setVisible(true);
            return;
        }
        if(command.contains("문장")){
            list.setTitle(command);

        }
        if(command.contains("word")) {
            list.setVisible(false);
            if (list.getTitle().equals("객관식")) {
                new choice(command, correct, incorrect);
            }
            if (list.getTitle().equals("주관식")) {
                new subjective(command, correct, incorrect);
            }
        }
    }
}