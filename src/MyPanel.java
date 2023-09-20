import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class MyPanel extends JPanel implements ActionListener, KeyListener {

    //Tile class to store the coordinates of something
    private class Tile{
        int x,y;
        public Tile(int xi,int yi){
            x=xi;
            y=yi;
        }
    }
    ImageIcon ic,ic1;
    Timer timer;
    Tile apple;

    Tile head;
    ArrayList<Tile>Snake_body; // to store the snake body parts

    int apples_eaten=0;
    int velocityx;
    int velocityy;
    boolean gameover;
    Random random;
    public MyPanel(){
        ic=new ImageIcon("apple1.png");
        ic1=new ImageIcon("snakehead.png");

        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(600,600));
        this.addKeyListener(this);
        this.setFocusable(true);
        start_game();
    }
    public void start_game(){
        gameover=false;
        velocityx=0;
        velocityy=0;
        Snake_body=new ArrayList<>();
        head=new Tile(0,0);
        Snake_body.add(head); // adding the head to the snake body
        random=new Random();
        apple=new Tile(0,0);
        generate_apple(); //randomly gives the coordinates for apple
        timer=new Timer(100,this);
        timer.start();
    }
    public void generate_apple(){
        //generating the apple coordinates
        apple.x=random.nextInt((600/25)) ;
        apple.y= random.nextInt((600/25)) ;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g); // paints the JPanel
        draw(g); //then draws the graphics 'g' on the panel
    }
    public void draw(Graphics g){
        if(gameover==false){
            g.setFont(new Font("Ink free",Font.BOLD,30));
            g.setColor(Color.cyan);
            g.drawString("Score : "+apples_eaten,250,50);
            //draws the apple
            g.setColor(Color.red);
            g.drawImage(ic.getImage(),apple.x*25,apple.y*25,25,25,null);
            //draws the snake head
            g.setColor(Color.BLUE);
            g.fill3DRect(head.x*25,head.y*25,25,25,true);

            //draws the body
            for(int i=1;i<Snake_body.size();i++){
                int x_co=Snake_body.get(i).x;
                int y_co=Snake_body.get(i).y;
                g.setColor(Color.CYAN);
                g.fill3DRect(x_co*25,y_co*25,25,25,true);
            }
        }
        else{
            game_over(g);
        }
    }
    public boolean CheckIfOwnBody(){
        //check if own body
        for(int i=1;i<Snake_body.size();i++){
            if(Snake_body.get(i).x==head.x && Snake_body.get(i).y==head.y){
                return true;
            }
        }
        return false;
    }
    public void move(){
        //check for own body collision
        if(CheckIfOwnBody()){
            gameover=true;
        }
        //check if wall
        if(head.x*25>=600 || head.y*25>=600 || head.x*25<0 || head.y*25<0){
            gameover=true;
        }
        //check if apple
        if(head.x==apple.x && head.y==apple.y){
            Snake_body.add(new Tile(apple.x,apple.y));
            apples_eaten++;
            generate_apple();
        }


        //move every body part by one place, except the head
        for(int i=Snake_body.size()-1;i>0;i--){
            //move the head
                Snake_body.get(i).x=Snake_body.get(i-1).x;
                Snake_body.get(i).y=Snake_body.get(i-1).y;
        }
        //move the head
        head.x+=velocityx;
        head.y+=velocityy;

    }
    public void game_over(Graphics g){
        g.setFont(new Font("Ink free",Font.BOLD,30));
        g.setColor(Color.cyan);
        g.drawString("Score : "+apples_eaten,250,50);
        g.setColor(Color.CYAN);
        g.setFont(new Font("INK FREE",Font.BOLD,50));
        g.drawString("OYE PAPA JI! ",150,250);
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // move the snake to its new coordinates
        repaint(); // calls the draw method repeatedly as soon as the timer reaches its 'delay'
    }

    //defining control keys, KEY LISTENER
    @Override
    public void keyPressed(KeyEvent e) {

        //if i am moving down, i should not be able to move up
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityy!=1){
                velocityy=-1;
                velocityx=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityy!=-1){
            velocityy=1;
            velocityx=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityx!=1){
            velocityx=-1;
            velocityy=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityx!=-1){
            velocityx=1;
            velocityy=0;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
