/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework6ai;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;

/**
 *
 * @author Mike
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;

    @FXML
    private Button scene1;
    @FXML
    private Button scene2;
    @FXML
    private Button scene3;
    @FXML
    private Button scene4;
    @FXML
    private AnchorPane gridContainer;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        
        StateSpace currentStateSpace;
        private void setCurrentStateSpace(StateSpace newStateSpace){this.currentStateSpace = newStateSpace;}
        private StateSpace getCurrentStateSpace(){return this.currentStateSpace;}
        */
        
    }    

    @FXML
    private void opt1Clicked(ActionEvent event) {
        //Test Case 1 -- 5x5 grid
        StateSpace startingStateSpace = new StateSpace(5,5);
              
        startingStateSpace.board[4][0].setTileMark("0");
        startingStateSpace.board[4][0].available = false;
        startingStateSpace.board[3][2].setTileMark("1");
        startingStateSpace.board[3][2].available = false;
        startingStateSpace.findInitialZero();
        
        Search start = new Search();
        start.runAlgorithm(startingStateSpace, gridContainer);
        

    }

    @FXML
    private void opt2Clicked(ActionEvent event) {
        //Test Case 2 -- 6x6 grid
        StateSpace startingStateSpace = new StateSpace(6,6);
        startingStateSpace.board[5][1].setTileMark("0");
        startingStateSpace.board[5][1].available = false;
        startingStateSpace.board[4][3].setTileMark("2");
        startingStateSpace.board[4][3].available = false;
        startingStateSpace.findInitialZero();
        
        Search start = new Search();
        start.runAlgorithm(startingStateSpace, gridContainer);
    }

    @FXML
    private void opt3Clicked(ActionEvent event) {
        //Test Case 3 -- 6x6 grid
        StateSpace startingStateSpace = new StateSpace(6,6);
        startingStateSpace.board[2][3].setTileMark("1");
        startingStateSpace.board[2][3].available = false;
        startingStateSpace.board[5][0].setTileMark("1");
        startingStateSpace.board[5][0].available = false;
        startingStateSpace.board[3][5].setTileMark("2");
        startingStateSpace.board[3][5].available = false;
        startingStateSpace.findInitialZero();
        
        Search start = new Search();
        start.runAlgorithm(startingStateSpace, gridContainer);
        
    }

    @FXML
    private void opt4Clicked(ActionEvent event) {
        //Test Case 4 -- 7x7 grid
        StateSpace startingStateSpace = new StateSpace(7,7);
        startingStateSpace.board[0][4].setTileMark("2");
        startingStateSpace.board[0][4].available = false;
        startingStateSpace.board[6][0].setTileMark("1");
        startingStateSpace.board[6][0].available = false;
        startingStateSpace.board[5][3].setTileMark("0");
        startingStateSpace.board[5][3].available = false;
        startingStateSpace.findInitialZero();

        Search start = new Search();
        start.runAlgorithm(startingStateSpace, gridContainer);
        
    }
    
}
