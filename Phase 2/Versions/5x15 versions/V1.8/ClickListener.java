import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
public class ClickListener implements ActionListener{

  public void actionPerformed(ActionEvent event) {

    if(event.getActionCommand().equals("START")){
      if(Window.check == 0){
        Window.check=1;
      }
      Window.start();
    }
    else if(event.getActionCommand().equals("EXIT")){
      System.exit(0);
      }
    else if(event.getActionCommand().equals("RESET")){
      if(Window.check == 0){
        JLabel error = new JLabel("GAME HASN'T BEEN STARTED!");
        error.setFont(new Font("Arial", Font.BOLD, 25));
        Window.window.add(error);
        error.setBounds(20, 600, 760, 200);
      }
      else{
        Window.score.setText("Score: 0");
        Window.lCleared.setText("Lines cleared: 0");
        Window.start();
      }
    }
    else{
      if(Window.check == 0){
        JLabel error = new JLabel("GAME HASN'T BEEN STARTED!");
      }
      else{
        if(Window.check%2 == 1){
          Window.grid.timer.stop();
          JPanel panel = new JPanel();
          JLabel pauseL = new JLabel("GAME PAUSED");
          pauseL.setFont(new Font("Arial", Font.BOLD, 25));
          panel.add(pauseL);
          Window.window.add(panel);
          pauseL.setVisible(true);
          pauseL.setBounds(20, 400, 760, 200);
        }
        else{
          Window.grid.timer.start();
        }
        ++Window.check;
      }
    }
  }
}
