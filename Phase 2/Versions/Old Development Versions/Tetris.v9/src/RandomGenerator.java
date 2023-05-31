
public class RandomGenerator {
    private int numberOfShapes;

    RandomGenerator(int numberOfShapes){
        this.numberOfShapes = numberOfShapes;
    }

    public int Generator(){
        int randomNumber = (int)(Math.random()*numberOfShapes);
        System.out.println("Random number generated: " + randomNumber);
        return randomNumber;
    }
}
