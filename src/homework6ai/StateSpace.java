/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework6ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Mike
 */
public class StateSpace {
    
    int rows;
    int columns;
    Tile[][] board;
    
    StateSpace parent;
    List<StateSpace> children = new ArrayList<StateSpace>();
    
    //Constructor
    public StateSpace(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        this.board = new Tile[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                this.board[i][j] = new Tile(i,j,"empty");
            }
        }
    }
    
    //Prints out the Board StateSpace
    public void printBoard(){
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                System.out.print(this.board[i][j].tileMark + " ");
            }
            System.out.print("\n");
        }  
        System.out.print("Blacklisted Coords are: ");
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                if(!this.board[i][j].available)
                    System.out.print("(" + i + "," + j + ")" + " ");
            }
        }       
        System.out.print("\n");
    }    
    
    //Check Board for the Initial Zeros to Blacklist its Borders!
    public void findInitialZero(){
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                if(this.board[i][j].tileMark.equals("0"))
                    blackListBorder(i,j);
            }
        }
    }
    
    //Blacklists Spaces in the Desired Row and Column
    public void blackListRowAndColumn(int row, int column){
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                if(i == row || j == column){
                    this.board[i][j].available = false;
                }
            }
        }    
    }
    
    //Blacklists all Spaces in the Surrounding Area
    public void blackListBorder(int row, int column){
        //1 Up
        if(row > 0)
            this.board[row-1][column].available = false;
        
        //1 Left
        if(column > 0)
            this.board[row][column-1].available = false; 
    
        //1 Down
        if(row < this.rows-1)
            this.board[row+1][column].available = false;
        
        //1 Right
        if(column < this.columns-1)
            this.board[row][column+1].available = false;      

        //1 Up 1 Left
        if(row > 0 && column > 0)
            this.board[row-1][column-1].available = false;
        
        
        //1 Up 1 Right
        if(row > 0 && column < this.columns-1)
            this.board[row-1][column+1].available = false;
        
        //1 Down 1 Left
        if(row < this.rows-1 && column > 0)
            this.board[row+1][column-1].available = false;
        
        
        //1 Down 1 Right
        if(row < this.rows-1 && column < this.columns-1)
            this.board[row+1][column+1].available = false;
  
    }
    
    //Clones a Board
    public void cloneBoard(Tile[][] boardToClone){
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                this.board[i][j].tileMark = new String(boardToClone[i][j].tileMark);
                this.board[i][j].available = boardToClone[i][j].available;
            }
        }
    }
    
    //Generates Current Board State Space's Children 
    public void generateChildrenStateSpaces(){
        
        //MRV Algorithm
        //Set Starting MRV out of Range
        int mrv = this.columns+1;
        int workingMrv = this.columns+1;;
        int mrvColumn = -1;
        
        //Search our Board to find Column with best MRV
        for(int i = 0; i < this.columns; i++){
            workingMrv = this.getMRV(i);
            if(mrv > workingMrv && workingMrv > 0){
                mrv = workingMrv;
                mrvColumn = i;
            }
            //If our MRV is Equal, Settle Tie via the Degree Heuristic Value!
            if(mrv == workingMrv && mrvColumn > -1 && workingMrv > 0){
                if(this.calculateDegreeHeuristicColumn(mrvColumn) < this.calculateDegreeHeuristicColumn(i)){
                     mrv = workingMrv;
                     mrvColumn = i;               
                 }
            }
        }
        System.out.println("Min Value is: " + mrv + " at Column: " + mrvColumn);
        
        //If No Columns Available, Return
        if(mrvColumn == -1){
            System.out.println("NO VALUES FOUND! QUIT");
            return;
        }
        
        //We Now have our Open Column, Let's Look at the Rows of our Columns now!
        //See how many  Available (Not BlackList) Children there are
        int numberOfChildren = 0;
        for(int i = 0; i < this.rows; i++){
            if(this.board[i][mrvColumn].available && this.board[i][mrvColumn].tileMark.equals("empty")){
                numberOfChildren++;
            }
        }        
        
        //This is where our Foward Checking Magic Happens! 
        //We will Base our Foward Checking on the Availibity of a Space
        //And how much this Space will Impact NumberMines!
        List<Integer> childrenBag = new ArrayList<Integer>();
        while(childrenBag.size() != numberOfChildren){
            
            //Set Starting out of Bounds
            int maxDegree = -1;
            int maxDegreeIndex = -1;
            
            //Sort
            for(int i = 0; i < this.rows; i++){
                int workingDegree = this.calculateDegreeHeuristicColumnSpace(i, mrvColumn);
                if(this.board[i][mrvColumn].available && this.board[i][mrvColumn].tileMark.equals("empty")){
                    if(maxDegree < workingDegree && !childrenBag.contains(i)){
                        maxDegree = workingDegree;
                        maxDegreeIndex = i;
                    }
                }
            }
            childrenBag.add(maxDegreeIndex);
        }
       
        //Now we have a Children of a Column, whose been sorted via MRV, Degree Hueristic, and Foward Checking!
        for(int childRowIndex : childrenBag){
            //Create new Child
            StateSpace child = new StateSpace(this.rows, this.columns);
            child.cloneBoard(this.board);
            child.board[childRowIndex][mrvColumn].tileMark = "shaded";
            child.board[childRowIndex][mrvColumn].available = false;
            child.blackListRowAndColumn(childRowIndex, mrvColumn);
            child.blackListBorder(childRowIndex, mrvColumn);
            child.updateMineBorders();

            this.children.add(child);
        }
        
    }    
    
    //Ensure Mines who Have been Satisfied has their Border Blacklisted
    public void updateMineBorders(){
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                if(!this.board[i][j].tileMark.equals("shaded") && !this.board[i][j].tileMark.equals("empty")){
                    if(this.isMineSatisfied(i, j)){
                        this.blackListBorder(i, j);
                    }
                }
            }
        }
    }
    
    //Calculates the Degree Heuristic for a Single Space
    public int calculateDegreeHeuristicColumnSpace(int row, int column){
        //Look At Current Column, Previous Column, and Next Column. If any of the Contain a Mine Give Add to Degree
        int degree = 0;
        
        //1 Up
        if(row > 0){
            if(!this.board[row-1][column].tileMark.equals("shaded") && !this.board[row-1][column].tileMark.equals("empty")){
                if(!this.isMineSatisfied(row-1, column)){
                    degree++;
                }
            }  
        }
  
        //1 Left
        if(column > 0){
            if(!this.board[row][column-1].tileMark.equals("shaded") && !this.board[row][column-1].tileMark.equals("empty")){
                if(!this.isMineSatisfied(row, column-1)){
                    degree++;
                }
            }
        }
            
        //1 Down
        if(row < this.rows-1){
            if(!this.board[row+1][column].tileMark.equals("shaded") && !this.board[row+1][column].tileMark.equals("empty")){
              if(!this.isMineSatisfied(row+1, column)){
                    degree++;
                }
            }
        }
            
        //1 Right
        if(column < this.columns-1){
            if(!this.board[row][column+1].tileMark.equals("shaded") && !this.board[row][column+1].tileMark.equals("empty")){
                if(!this.isMineSatisfied(row, column+1)){
                      degree++;
                }            
            }
        }
                
        //1 Up 1 Left
        if(row > 0 && column > 0){
           if(!this.board[row-1][column-1].tileMark.equals("shaded") && !this.board[row-1][column-1].tileMark.equals("empty")){
                if(!this.isMineSatisfied(row-1, column-1)){
                      degree++;
                } 
           } 
        }
            
        
        //1 Up 1 Right
        if(row > 0 && column < this.columns-1){
            if(!this.board[row-1][column+1].tileMark.equals("shaded") && !this.board[row-1][column+1].tileMark.equals("empty")){
                if(!this.isMineSatisfied(row-1, column+1)){
                      degree++;
                }
            }
        }
            
        //1 Down 1 Left
        if(row < this.rows-1 && column > 0){
            if(!this.board[row+1][column-1].tileMark.equals("shaded") && !this.board[row+1][column-1].tileMark.equals("empty")){
                if(!this.isMineSatisfied(row+1, column-1)){
                      degree++;
                }
            }
        }
            
        //1 Down 1 Right
        if(row < this.rows-1 && column < this.columns-1){
            if(!this.board[row+1][column+1].tileMark.equals("shaded") && !this.board[row+1][column+1].tileMark.equals("empty")){
                if(!this.isMineSatisfied(row+1, column+1)){
                      degree++;
                }
            }
        }
            
        
        return degree;    
    }
    
    //Calculates the Degree Heuristic for a Single Column
    public int calculateDegreeHeuristicColumn(int column){
        //Look At Current Column, Previous Column, and Next Column. If any of the Contain a Mine Give Add to Degree
        int degree = 0;
        
        //Check Current
        if(column > 0){
            for(int i = 0; i < this.rows; i++){
                if(!board[i][column].equals("empty") || !board[i][column-1].equals("shaded")){
                    if(!this.isMineSatisfied(i, column)){
                        degree++;
                    }
                }
            }
        }      
        
        //Check Previous
        if(column > 0){
            for(int i = 0; i < this.rows; i++){
                if(!board[i][column-1].equals("empty") || !board[i][column-1].equals("shaded")){
                    if(!this.isMineSatisfied(i, column-1)){
                        degree++;
                    }
                }
            }
        }
        
        //Check Next
        if(column < this.columns-1){
            for(int i = 0; i < this.rows; i++){
                if(!board[i][column+1].equals("empty") || !board[i][column-1].equals("shaded")){
                    if(!this.isMineSatisfied(i, column+1)){
                        degree++;
                    }
                }
            }
        }
        
        return degree;
        
    }
    
    //Check if a Number Mine's Contraints have been Satisfied!
    public boolean isMineSatisfied(int row, int column){
        //Initial Check
        if(this.board[row][column].tileMark.equals("empty") || this.board[row][column].tileMark.equals("shaded")){
            return false;
        }
        
        //Values
        int expectedSurrounding = Integer.parseInt(this.board[row][column].tileMark);
        int actualSurrounding = 0;
        
        //Check Surroundings
        //1 Up
        if(row > 0)
            if(this.board[row-1][column].tileMark.equals("shaded"))
                actualSurrounding++;    
        //1 Left
        if(column > 0)
            if(this.board[row][column-1].tileMark.equals("shaded"))
                actualSurrounding++; 
        //1 Down
        if(row < this.rows-1)
            if(this.board[row+1][column].tileMark.equals("shaded"))
                actualSurrounding++; 
        //1 Right
        if(column < this.columns-1)
            if(this.board[row][column+1].tileMark.equals("shaded"))      
                actualSurrounding++; 
        //1 Up 1 Left
        if(row > 0 && column > 0)
            if(this.board[row-1][column-1].tileMark.equals("shaded"))
                actualSurrounding++; 
        
        //1 Up 1 Right
        if(row > 0 && column < this.columns-1)
            if(this.board[row-1][column+1].tileMark.equals("shaded"))
                actualSurrounding++; 
        //1 Down 1 Left
        if(row < this.rows-1 && column > 0)
            if(this.board[row+1][column-1].tileMark.equals("shaded"))
                actualSurrounding++; 
        //1 Down 1 Right
        if(row < this.rows-1 && column < this.columns-1)
            if(this.board[row+1][column+1].tileMark.equals("shaded"))
                actualSurrounding++; 
        
        //If Expected = Actual, Return True!
        if(expectedSurrounding == actualSurrounding){
            return true;
        }
        else{
            return false;
        }
    }
    
    public int getMRV(int column){
        int mrv = 0;
        System.out.println("Column: " + column );
        for(int i = 0; i < this.rows; i++){
            
            if(this.board[i][column].available && this.board[i][column].tileMark.equals("empty")){
                mrv++;
            }
            System.out.println("MRV is: " + mrv);   
        }
        return mrv;
    }
    
    //Check if a Board is Completed!
    public boolean isCompleted(){
        //For a Board to be Complete, All Columns Must have a Shaded Space and All Number Mines must be Satisfied
        
        //Check Shaded Spaces
        int shadedSpaceInColumnFlag = 0;
        for(int i = 0; i < this.rows; i++){
            shadedSpaceInColumnFlag = 0;
            for(int j = 0; j < this.columns; j++){
                if(this.board[i][j].tileMark.equals("shaded")){
                    shadedSpaceInColumnFlag = 1;
                }
            }
            if(shadedSpaceInColumnFlag == 0){
                return false;
            }
        }  
        
        //Check Mines
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                if(!this.board[i][j].tileMark.equals("shaded") && !this.board[i][j].tileMark.equals("empty")){
                    if(!this.isMineSatisfied(i, j)){
                        return false;
                    }
                }
            }
            
        }        
        
        System.out.println("BOARD WAS COMPLETED!");
        return true;
    }
    
    
    
    //Updates the Display of our Grid using current StateSpace
    public void updateDisplay(GridPane boardDisplayGrid){  
        
       boardDisplayGrid.getChildren().clear();
       boardDisplayGrid.setStyle("-fx-background-color: gray; -fx-padding: 2;");
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                ImageView mark = new ImageView();
                if(this.board[i][j].equals("shaded")){
                    mark.setImage(new Image("shaded.png"));
                    mark.setStyle("-fx-background-color: black, white ;-fx-background-insets: 0, 0 1 1 0 ;");
                    GridPane.setConstraints(mark, j, i);
                    boardDisplayGrid.getChildren().add(mark);
                }
                else if(this.board[i][j].equals("empty")){
                    mark.setImage(new Image("empty.png"));
                    mark.setStyle("-fx-background-color: black, white ;-fx-background-insets: 0, 0 1 1 0 ;");
                    GridPane.setConstraints(mark, j, i);
                    boardDisplayGrid.getChildren().add(mark);
                }
                else{
                    mark.setImage(new Image(this.board[i][j].tileMark + ".png"));
                    mark.setStyle("-fx-background-color: black, white ;-fx-background-insets: 0, 0 1 1 0 ;");
                    GridPane.setConstraints(mark, j, i);
                    boardDisplayGrid.getChildren().add(mark);
                }
            }
        }
           
    }
    
    
    //Resets the Board!
    public void resetBoard(){
        for(int i= 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                this.board[i][j].setTileMark("empty");
            }
        }
    }
}
