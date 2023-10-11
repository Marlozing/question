package study;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Main extends JFrame implements ActionListener{
    static Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
    static int width = res.width / 2;
    static int height = res.height / 2;
    JFrame list;
    public static void main(String[] args) {
        new Main();
    }
    public Main() {
        Font font = new Font("한컴 고딕",Font.BOLD,70);
        for(String str : Arrays.asList("객관식", "주관식","문장풀이")){
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
    }



    @Override
    public void actionPerformed(ActionEvent e){
        String command=e.getActionCommand();
        if(command.equals("객관식")||command.equals("주관식")) {
            setVisible(false);
            list = new JFrame();
            list.setTitle(command);
            Font font = new Font("궁서", Font.BOLD, 20);
            for (int i = 0; i < 50; i++) {
                JButton b = new JButton("word " + (i + 1));
                b.setFont(font);
                b.addActionListener(this);
                b.setActionCommand("word" + (i + 1));
                list.add(b);
            }
            list.setLayout(new GridLayout(5, 10));
            list.setSize(width, height);
            list.setLocationRelativeTo(null);
            list.setVisible(true);
        }else if(command.contains("word")){
            list.setVisible(false);
            if(list.getTitle().equals("객관식")){
                new choice(command);
            }
            if(list.getTitle().equals("주관식")){
                new subjective(command);
            }
        }

    }
}