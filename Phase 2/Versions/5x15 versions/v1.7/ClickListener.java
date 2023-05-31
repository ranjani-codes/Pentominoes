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
        Window.start();
      }
    }
    else{
      if(Window.check == 0){
        JLabel error = new JLabel("GAME HASN'T BEEN STARTED!");
        error.setFont(new Font("Arial", Font.BOLD, 25));
        Window.window.add(error);
        error.setBounds(20, 600, 760, 200);
      }
      else{
        if(Window.check%2 == 1){
          Window.grid.timer.stop();
        }
        else{
          Window.grid.timer.start();
        }
        ++Window.check;
      }
    }
  }
}
