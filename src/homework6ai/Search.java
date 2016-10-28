/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework6ai;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Mike
 */
public class Search {
    
    
    public void runAlgorithm(StateSpace startStateSpace, AnchorPane gridContainer){
        
        //Set workingStateSpace to startStateSpace
        StateSpace workingStateSpace = startStateSpace;
        
        //Create our Fringe
        List<StateSpace> fringe = new ArrayList<StateSpace>();        
        fringe.add(workingStateSpace);
        
        int nodesSearched = 0;
        long startTime = System.nanoTime();
        
        //Here's where the Magic Happens
        while(!fringe.isEmpty() && !workingStateSpace.isCompleted()){
            nodesSearched++;
            //Because of Backtracking, we get and add from the Back
            workingStateSpace = fringe.get(fringe.size()-1);
            fringe.remove(fringe.size()-1);
            
            //Print the Working StateSpace
            workingStateSpace.printBoard();
            
            //Generate the Children and Loop Through Them. Add them to them to the Fringe
            //Note, the Children a Sorted via MRV, H-Degree, and Foward Checking within the Generate Children Algorithm!
            workingStateSpace.generateChildrenStateSpaces();
            for(int i = workingStateSpace.children.size()-1; i >= 0; i--){
                fringe.add(workingStateSpace.children.get(i));
            }        
        }
        
        //Output Time it Took
        long endTime = System.nanoTime();
        System.out.println("Nodes Searched: " + nodesSearched);
        System.out.println("Algorithm Duration: " + (endTime-startTime)/1000000);
        
        //Print Final Board and Update the Display!
        workingStateSpace.printBoard();
        gridContainer.getChildren().clear();
        GridPane newBoard = new GridPane();
        newBoard.setStyle("-fx-background-color: white; -fx-padding: 10 ;");
        workingStateSpace.updateDisplay(newBoard);
        gridContainer.getChildren().add(newBoard);
    }
    
}





