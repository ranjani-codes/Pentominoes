import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.beans.property.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.transform.*;
import javafx.stage.Stage;

public class Container1 extends Application {

    public static final int size1Block = 20; //size by pixel per block
    public static int xAxis = parcels_GA.container_size[1]; //number of x boxes
    public static int yAxis = parcels_GA.container_size[0]; //number of y boxes
    public static int zAxis = parcels_GA.container_size[2]; //number of z boxes


    private static int WIDTH = size1Block*xAxis+size1Block*zAxis;  //total width grid dimensions
    private static int HEIGHT = size1Block*yAxis+size1Block*zAxis; //total height grid dimensions
    private static int DEPTH = size1Block*zAxis; //total height grid dimensions

    Window window;
    public static Box[] packages;
    public static int [][][] grid = new int[yAxis][xAxis][zAxis]; //array of the grid
    public static boolean go = false;
    static int a = 0;

    public Container1(){
      window = new Window();
      window.setVisible(true);
    }


    public static void main(String[] args) {
      if(go){
      launch(args);
      }
      else{
        new Container1();
      }
    }


    public static void createContainer(){
      packages = new Box[xAxis*yAxis*zAxis];
      int a = 0;
      PhongMaterial m = new PhongMaterial();
      Color temp = Color.BLUE;
      for(int z=0; z<yAxis; z++){
        for(int y=0; y<xAxis; y++){
          for(int x=0; x<zAxis; x++){
            if((grid[z][y][x]) != 0){
            packages[a] = new Box(size1Block/2, size1Block/2, size1Block/2);
            packages[a].getTransforms().addAll(new Translate((x*size1Block), (y*size1Block), (z*size1Block)));
              if((grid[z][y][x]) == 1){
              temp = Color.RED;
              }
              else if((grid[z][y][x]) == 2){
              temp = Color.GREEN;
              }
              else if((grid[z][y][x]) == 9){
              temp = Color.BLUE;
              }
              else if((grid[z][y][x]) == 4){
              temp = Color.YELLOW;
              }

              packages[a++].setMaterial(new PhongMaterial(temp));
            }
          }
        }
      }

    }

  @Override
    public void start(Stage primaryStage) throws Exception {

      createContainer();

      PerspectiveCamera camera = new PerspectiveCamera();
      camera.getTransforms().addAll(new Translate(-WIDTH, -HEIGHT/2 , -DEPTH*3));

      // Build the Scene Graph
      SmartGroup root = new SmartGroup();
      int a = 0;
      for(int z = 0; z < grid.length; z++){
        for(int y = 0; y < grid[z].length; y++){
          for(int x = 0; x < grid[z][y].length; x++){
            if((grid[z][y][x]) != 0){
              root.getChildren().addAll(packages[a++]);
            }
          }
    }
  }
      primaryStage.setTitle("Container");
      primaryStage.setResizable(true);
      primaryStage.setWidth(WIDTH*2);
      primaryStage.setHeight(HEIGHT*2);
      Scene scene = new Scene(root, WIDTH, HEIGHT);
      primaryStage.setScene(scene);
      scene.setFill(Color.TRANSPARENT);
      scene.setCamera(camera);

      primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
      switch (event.getCode()) {
        case UP:
          root.rotateByX(-10);
          break;
        case DOWN:
          root.rotateByX(10);
          break;
        case LEFT:
          root.rotateByY(-10);
          break;
        case RIGHT:
          root.rotateByY(10);
          break;
        case W:
          camera.setTranslateZ(camera.getTranslateZ() + 10);
          break;
        case S:
          camera.setTranslateZ(camera.getTranslateZ() - 10);
          break;
        case A:
          camera.setTranslateX(camera.getTranslateX() + 10);
          break;
        case D:
          camera.setTranslateX(camera.getTranslateX() - 10);
          break;
          }
      });
        primaryStage.show();
    }


    class SmartGroup extends Group {

    Rotate r;
    Transform t = new Rotate();

    void rotateByX(int ang) {
      r = new Rotate(ang, Rotate.X_AXIS);
      t = t.createConcatenation(r);
      this.getTransforms().clear();
      this.getTransforms().addAll(t);
    }

    void rotateByY(int ang) {
      r = new Rotate(ang, Rotate.Y_AXIS);
      t = t.createConcatenation(r);
      this.getTransforms().clear();
      this.getTransforms().addAll(t);
    }
  }

}
