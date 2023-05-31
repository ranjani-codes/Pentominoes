public class ColorPentomino {
    private int currentShape;

    ColorPentomino(int currentShape){
        this.currentShape = currentShape;
    }

    public int [] GiveColor(){
        switch (currentShape){
            case 0:
                int [] Color0 = {25,190,155};
                return Color0;
            case 1:
                int [] Color1 ={45,205,115};
                return Color1;
            case 2:
                int [] Color2 = {50,150,220};
                return Color2;
            case 3:
                int [] Color3 = {95,90,180};
                return Color3;
            case 4:
                int [] Color4 = {50,75,95};
                return Color4;
            case 5:
                int [] Color5 = {240,195,15};
                return Color5;
            case 6:
                int [] Color6 = {230,125,35};
                return  Color6;
            case 7:
                int [] Color7 = {230,75,60};
                return Color7;
            case 8:
                int [] Color8 = {150,165,165};
                return Color8;
            case 9:
                int [] Color9 = {245,155,20};
                return Color9;
            case 10:
                int [] Color10 = {210,85,0};
                return Color10;
            case 11:
                int [] Color11 = {190,57,43};
                return Color11;

        }
        return null;
    }
}