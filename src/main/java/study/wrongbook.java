package study;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class wrongbook extends JFrame implements MouseListener,KeyListener,ListSelectionListener{

    int width = Main.width;
    int height = Main.height;
    private JList list;				//리스트
    private JTextField inputField;	//테스트 입력 Field
    private JButton addBtn;		//추가 버튼
    private JButton delBtn;		//삭제 버튼
    private JButton examBtn;

    private DefaultListModel model;	//JList에 보이는 실제 데이터
    private JScrollPane scrolled;

    private final Sound correct;
    private final Sound incorrect;

    public wrongbook(String title,HashMap<String,String> wrong, Sound correct, Sound incorrect) {
        super(title);
        init(wrong);
        this.correct = correct;
        this.incorrect = incorrect;
    }

    public void init(HashMap<String,String> wrong) {
        Font font = new Font("굴림체",Font.PLAIN,30);
        model=new DefaultListModel();
        list=new JList(model);
        list.setFont(font);
        inputField=new JTextField((width * 7) / 230);
        inputField.setFont(font);
        addBtn=new JButton("추가");
        addBtn.setFont(font);
        delBtn=new JButton("삭제");
        delBtn.setFont(font);
        examBtn=new JButton("시험보기");
        examBtn.setFont(font);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//하나만 선택 될 수 있도록

        inputField.addKeyListener(this);	//엔터 처리
        addBtn.addMouseListener(this);		//아이템 추가
        delBtn.addMouseListener(this);		//아이템 삭제
        examBtn.addMouseListener(this);
        list.addListSelectionListener(this);	//항목 선택시

        this.setLayout(new BorderLayout());


        JPanel topPanel=new JPanel(new FlowLayout(10,10,FlowLayout.LEFT));
        topPanel.add(inputField);
        topPanel.add(addBtn);
        topPanel.add(delBtn);		//위쪽 패널 [textfield]  [add] [del]
        topPanel.add(examBtn);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));	//상, 좌, 하, 우 공백(Padding)

        scrolled=new JScrollPane(list);
        scrolled.setBorder(BorderFactory.createEmptyBorder(20,10,10,10));

        this.add(topPanel,"North");
        this.add(scrolled,"Center");	//가운데 list


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);	//창 가운데 위치
        this.setVisible(true);
        for(String str : wrong.keySet()){
            model.addElement(str + ":" + wrong.get(str));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == addBtn) {
            addItem();
        }
        if(e.getSource() == delBtn) {
            int selected=list.getSelectedIndex();
            removeItem(selected);
        }
        if(e.getSource() == examBtn){
            if(model.size()>4){
                HashMap<String,String> hashMap = new HashMap<>();
                if(getTitle().equals("객관식 오답들")){
                    for(Object str : model.toArray()){
                        String[] strs = str.toString().split(":");
                        hashMap.put(strs[0],strs[1]);
                    }
                    choice q = new choice("reexam",correct,incorrect);
                    q.ChangeWord(hashMap);
                }
                if(getTitle().equals("주관식 오답들")){
                    for(Object str : model.toArray()){
                        String[] strs = str.toString().split(":");
                        hashMap.put(strs[1],strs[0]);
                    }
                    subjective q = new subjective("reexam",correct,incorrect);
                    q.ChangeWord(hashMap);
                }
                this.setVisible(false);
            }
        }
    }

    public void removeItem(int index) {
        if(index<0) {
            if(model.isEmpty()) return;	//아무것도 저장되어 있지 않으면 return
            index=0;	//그 이상이면 가장 상위 list index
        }

        model.remove(index);
    }

    public void addItem() {
        String inputText=inputField.getText();
        if(inputText==null|| inputText.isEmpty()) return;
        model.addElement(inputText);
        inputField.setText("");		//내용 지우기
        inputField.requestFocus();	//다음 입력을 편하게 받기 위해서 TextField에 포커스 요청
        //가장 마지막으로 list 위치 이동
        scrolled.getVerticalScrollBar().setValue(scrolled.getVerticalScrollBar().getMaximum());
    }
    //MouseListener
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    //KeyListener
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode==KeyEvent.VK_ENTER) {
            addItem();
        }
    }


    //ListSelectionListener
    @Override
    public void valueChanged(ListSelectionEvent e) {
    }

}