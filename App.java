import javax.swing.*;

public class App {

    public static void main(String[] args) throws Exception {

        int boardwith = 360;
        int boardheight = 640; 
        JFrame frame = new JFrame("Flappy Bird");
        frame.setVisible(true);
        frame.setSize(boardwith, boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //program closes on x clicking

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); //keep the title bar
        flappyBird.requestFocus();
        frame.setVisible(true);    
    }

}