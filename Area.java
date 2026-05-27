import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class Area {
    int height;
    int width;
    int xPos;
    int yPos;
    
    
    public Area(Node areaNode){
      NamedNodeMap areaMap = areaNode.getAttributes();
      int[] areaArray = new int[4];
      for (int i = 0; i < 4; i++) {
         areaArray[i] = Integer.parseInt( areaMap.item(i).getNodeValue() );
      }
      
      this.height = areaArray[0];
      this.width = areaArray[1];
      this.xPos = areaArray[2];
      this.yPos = areaArray[3];

   }

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