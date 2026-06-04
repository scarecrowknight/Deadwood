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

      this.height = parseAttribute(areaMap, "h", "height");
      this.width = parseAttribute(areaMap, "w", "width");
      this.xPos = parseAttribute(areaMap, "x", "xPos");
      this.yPos =  parseAttribute(areaMap, "y", "yPos");

   }
    private int parseAttribute(NamedNodeMap areaMap, String shortName, String longName) {
        Node attributeNode = areaMap.getNamedItem(shortName);
        if (attributeNode == null) {
            attributeNode = areaMap.getNamedItem(longName);
        }
        if (attributeNode != null) {
            return Integer.parseInt(attributeNode.getNodeValue());
        } else {
            throw new IllegalArgumentException("Missing attribute: " + shortName + "/" + longName);
        }
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
    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }
    public int getXPos() {
        return this.xPos;
    }
    public int getYPos() {
        return this.yPos;
    }
}