 /*
 *L>; To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework6ai;

/**
 *
 * @author Mike
 */
public class Tile {
    
    int row;
    int column;
    String tileMark;
    boolean available = true;
    
    public Tile(){}
    public Tile(int row, int column, String mark){
        this.row = row;
        this.column = column;
        this.tileMark = mark;
    }
    
    public void setTileMark(String mark){this.tileMark = mark;}  
}