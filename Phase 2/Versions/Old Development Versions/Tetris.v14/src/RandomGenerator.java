
public class RandomGenerator {
    private int numberOfPentominos;

    RandomGenerator(int numberOfPentominos){
        this.numberOfPentominos = numberOfPentominos;
    }

    public int Generator(){
        int randomNumber = (int)(Math.random()*numberOfPentominos);
        System.out.println("Random number generated: " + randomNumber);
        return randomNumber;
    }
}
