import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{

    int boardwidth = 360;
    int boardheight = 640;

    //images

    Image BackgroundImg;
    Image birdImg;
    Image TopPipeImg;
    Image BottomPipeImg;

    //pipes

    int pipeX = boardwidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;
        Pipe(Image img){
            this.img = img;
        }
    }


    //bird

    int birdX = boardwidth/8;
    int birdY = boardheight/2;
    int birdwidth = 34;
    int birdheight = 24;

    class bird{
        int x = birdX;
        int y = birdY;
        int width = birdwidth;
        int height = birdheight;
        Image Img;

        bird(Image img){
            this.Img = img;
        }
    }
    //game logic
    bird bird;
    int velocityX = -4;
    int Velocity = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;

    

    FlappyBird(){
        setPreferredSize(new Dimension(boardwidth, boardheight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load images

    BackgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
    birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
    TopPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
    BottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
    bird = new bird(birdImg);
    pipes = new ArrayList<Pipe>();

    gameLoop = new Timer(1000/60,this); //ms 60 frames by second
    gameLoop.start();
    placePipesTimer = new Timer(1500, new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            placePipes();
        }
    });
    placePipesTimer.start();

    
    }

    public void placePipes(){

        //(0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardheight/4;
        Pipe topPipe = new Pipe(TopPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
        Pipe bottomPipe = new Pipe(BottomPipeImg);
        bottomPipe.y= topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //background
        g.drawImage(BackgroundImg, 0, 0, boardwidth, boardheight, null); //we always start from the top left corner
        g.drawImage(bird.Img, bird.x, bird.y,bird.width,bird.height ,null); //we always start from the top left corner
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);

        }
        //score
        g.setColor(Color.white);

        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
        
	}

    public void move(){
        Velocity += gravity;
        bird.y += Velocity;
        bird.y = Math.max(bird.y, 0);

        //pipes
        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                pipe.passed = true;
            }


            if(collision(bird, pipe)){
                gameOver = true;
            }
        }
        if(bird.y > boardheight){
            gameOver = true;
        }
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    public boolean collision(bird a, Pipe b){
        return a.x < b.x +b.width &&
        a.x + a.width > b.x &&
        a.y < b.y + b.height &&
    a.y + a.height > b.y; }

   

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            Velocity = -9;
            if (gameOver) {
                //restart game by resetting conditions
                bird.y = birdY;
                Velocity = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }


}