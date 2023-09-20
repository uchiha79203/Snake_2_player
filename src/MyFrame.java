import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame implements ActionListener {
    MyPanel panel;
    JFrame frame;
    ImageIcon ic;
    JButton start_button;

    public MyFrame(){
        ic=new ImageIcon("snake.png");
        frame=new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Snake");
        frame.setResizable(false);
        frame.setIconImage(ic.getImage());
        frame.setBounds(0,0,600,600);

        start_button=new JButton();
        start_button.setText("S T A R T");
        start_button.setSize(100,50);
        start_button.setBackground(Color.black);
        start_button.setForeground(Color.GREEN);
        start_button.setFont(new Font("Arial",Font.BOLD,30));
        start_button.setFocusable(false);
        start_button.addActionListener(this);

        frame.add(start_button);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==start_button){
            start_button.setVisible(false);
            start_button.setEnabled(false);
            panel=new MyPanel();
            frame.add(panel);
            frame.pack();
            panel.requestFocus();
        }
    }

}
