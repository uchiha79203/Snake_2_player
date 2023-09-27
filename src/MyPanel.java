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
    ImageIcon ic;
    Timer timer;
    Tile apple;

    Tile head1,head2;
    ArrayList<Tile> Snake_body1,Snake_body2; // to store the snake body parts
    boolean player1_wins=false;
    int apples_eaten1=0,apples_eaten2;
    int velocityx1;
    int velocityy1;
    int velocityx2,velocityy2;
    boolean gameover;
    Random random;
    public MyPanel(){
        ic=new ImageIcon("apple1.png");

        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(600,600));
        this.addKeyListener(this);
        this.setFocusable(true);
        start_game();
    }
    public void start_game(){
        gameover=false;

        velocityx1 =0;
        velocityy1 =0;

        velocityx2 =0;
        velocityy2 =0;

        Snake_body1 =new ArrayList<>();
        Snake_body2 =new ArrayList<>();

        head1=new Tile(0,0);
        head2=new Tile(23,23);

        Snake_body1.add(head1); // adding the head to the snake body
        Snake_body2.add(head2); // adding the head to the snake body

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
            g.setColor(Color.WHITE);
            g.drawString("Scores : "+apples_eaten1+" : "+apples_eaten2,200,50);
            //draws the apple
            g.setColor(Color.red);
            g.drawImage(ic.getImage(),apple.x*25,apple.y*25,25,25,null);
            //draws the snake head

            g.setColor(Color.BLUE);
            g.fill3DRect(head1.x*25,head1.y*25,25,25,true);

            g.setColor(new Color(0x194F06));
            g.fill3DRect(head2.x*25,head2.y*25,25,25,true);

            //draws the body
            for(int i = 1; i< Snake_body1.size(); i++){
                int x_co= Snake_body1.get(i).x;
                int y_co= Snake_body1.get(i).y;
                g.setColor(Color.CYAN);
                g.fill3DRect(x_co*25,y_co*25,25,25,true);
            }
            for(int i = 1; i< Snake_body2.size(); i++){
                int x_co= Snake_body2.get(i).x;
                int y_co= Snake_body2.get(i).y;
                g.setColor(Color.green);
                g.fill3DRect(x_co*25,y_co*25,25,25,true);
            }
        }
        else{
            game_over(g);
        }
    }
    public boolean CheckIfOwnBody(){
        for(int i = 1; i< Snake_body1.size(); i++){
            if(Snake_body1.get(i).x==head1.x && Snake_body1.get(i).y==head1.y){
                player1_wins=false;
                gameover=true;
                return true;
            }
        }
        for(int i = 1; i< Snake_body2.size(); i++){
            if(Snake_body2.get(i).x==head2.x && Snake_body2.get(i).y==head2.y){
                player1_wins=true;
                gameover=true;
                return true;
            }
        }
        return false;
    }
    public boolean CheckCollissionBetweenSnakes(){
        //check if own body
        for(int i = 0; i< Snake_body1.size(); i++){
                if(head2.x==Snake_body1.get(i).x && head2.y==Snake_body1.get(i).y)
                {
                    return true;
                }
        }
        for(int i = 0; i< Snake_body2.size(); i++){
            if(head1.x==Snake_body2.get(i).x && head1.y==Snake_body2.get(i).y)
            {
                return true;
            }
        }

        return false;
    }

    public void move(){
        CheckIfOwnBody();

        //check for own body collision
        if(CheckCollissionBetweenSnakes()){
            if(apples_eaten1>apples_eaten2){
                player1_wins=true;
            }
            else if(apples_eaten1<apples_eaten2){
                player1_wins=false;
            }
            gameover=true;
        }

        //check if wall
        if(head1.x*25>=600 || head1.y*25>=600 || head1.x*25<0 || head1.y*25<0){
            player1_wins=false;
            gameover=true;
        }
        if(head2.x*25>=600 || head2.y*25>=600 || head2.x*25<0 || head2.y*25<0){
            player1_wins=true;
            gameover=true;
        }

        //check if apple
        if(head1.x==apple.x && head1.y==apple.y){
            Snake_body1.add(new Tile(apple.x,apple.y));
            apples_eaten1++;
            generate_apple();
        }



        //move every body part by one place, except the head
        for(int i = Snake_body1.size()-1; i>0; i--){
            //move the head
                Snake_body1.get(i).x= Snake_body1.get(i-1).x;
                Snake_body1.get(i).y= Snake_body1.get(i-1).y;
        }
        //move the head
        head1.x+= velocityx1;
        head1.y+= velocityy1;


        if(head2.x==apple.x && head2.y==apple.y){
            Snake_body2.add(new Tile(apple.x,apple.y));
            apples_eaten2++;
            generate_apple();
        }
        for(int i = Snake_body2.size()-1; i>0; i--){
            //move the head
            Snake_body2.get(i).x= Snake_body2.get(i-1).x;
            Snake_body2.get(i).y= Snake_body2.get(i-1).y;
        }
        //move the head
        head2.x+= velocityx2;
        head2.y+= velocityy2;

    }
    public void game_over(Graphics g){
        g.setFont(new Font("Ink free",Font.BOLD,30));
        g.setColor(Color.cyan);
        g.drawString("Score : "+apples_eaten1+" : "+apples_eaten2,200,50);
        if(player1_wins){
            g.setColor(Color.CYAN);
            g.setFont(new Font("INK FREE",Font.BOLD,50));
            g.drawString("Blue Snake Wins! ",150,250);
        }
        else if(apples_eaten1==apples_eaten2){
            g.setColor(Color.RED);
            g.setFont(new Font("INK FREE",Font.BOLD,50));
            g.drawString("Draw! ",200,250);
        }
        else{
            g.setColor(Color.green);
            g.setFont(new Font("INK FREE",Font.BOLD,50));
            g.drawString("Green Snake Wins! ",150,250);
        }
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
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityy1 !=1){
                velocityy1 =-1;
                velocityx1 =0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityy1 !=-1){
            velocityy1 =1;
            velocityx1 =0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityx1 !=1){
            velocityx1 =-1;
            velocityy1 =0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityx1 !=-1){
            velocityx1 =1;
            velocityy1 =0;
        }

        //Snake 2
        if(e.getKeyCode()==KeyEvent.VK_W && velocityy2 !=1){
            velocityy2 =-1;
            velocityx2 =0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_S && velocityy2 !=-1){
            velocityy2 =1;
            velocityx2 =0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_A && velocityx2 !=1){
            velocityx2 =-1;
            velocityy2 =0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_D && velocityx2 !=-1){
            velocityx2 =1;
            velocityy2 =0;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
