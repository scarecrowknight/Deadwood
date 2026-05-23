import java.util.ArrayList;

public class Area {
    int height;
    int width;
    int xPos;
    int yPos;


    public Area(int[] area){
        this.height = area[0];
        this.width = area[1];
        this.xPos = area[2];
        this.yPos = area[3];
    }
    public int[] getArea(){

        int[] area = new int[4];

        area[0] = this.height;
        area[1] = this.width;
        area[2] = this.xPos;
        area[3] = this.yPos;

        return area;
    }
}