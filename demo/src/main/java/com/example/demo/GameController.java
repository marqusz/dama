package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

public class GameController implements Initializable{


    @FXML
    private GridPane gameBoard;

    private final int ROWS = 6;
    private final int COLS = 7;
    private Boolean blueToMove = true;

    private int[] clickedPiecePos = new int[2];
    private boolean isPiececlicked = false;
    private String clickedPieceColor;
    private String[][] pieces = new String[COLS][ROWS];

    private String[][] initPieces(){
        String[][] res = new String[COLS][ROWS];
        res[0][0] = "RED";
        res[1][0] = "RED";
        res[2][0] = "RED";
        res[3][0] = "RED";
        res[4][0] = "RED";
        res[5][0] = "RED";
        res[6][0] = "RED";

        res[0][5] = "BLUE";
        res[1][5] = "BLUE";
        res[2][5] = "BLUE";
        res[3][5] = "BLUE";
        res[4][5] = "BLUE";
        res[5][5] = "BLUE";
        res[6][5] = "BLUE";

        res[4][2] = "BLACK";
        res[2][3] = "BLACK";
        return res;
    }

    private void drawPieces(){
        for(int col = 0; col < pieces.length; col++ ){
            for(int row = 0; row < pieces[col].length; row++ ){
                if (pieces[col][row] != null){
                    if (pieces[col][row].equals("RED")) gameBoard.add(new Circle(30,Color.RED),col,row);
                    if (pieces[col][row].equals("BLUE")) gameBoard.add(new Circle(30,Color.BLUE),col,row);
                    if (pieces[col][row].equals("BLACK")) gameBoard.add(new Rectangle(80,80,Color.BLACK),col,row);
                }
            }
        }

    }
    private void clearBoard(){
        gameBoard.getChildren().removeIf(Objects::nonNull);
        gameBoard.getStyleClass().add("grid");
    }
    private boolean isBlue(String color){
        return color != null && color.equals("BLUE");
    }
    private boolean isRed(String color){
        return color != null && color.equals("RED");
    }

    private boolean canMove(int fromCol,int fromRow,String color,int toCol,int toRow){
        List<List<Integer>> possibleMoves = new ArrayList<>();
        if (isBlue(color)){
            possibleMoves.add(List.of(fromCol-1,fromRow-1));
            possibleMoves.add(List.of(fromCol,fromRow-1));
            possibleMoves.add(List.of(fromCol+1,fromRow-1));
        } else if (isRed(color)){
            possibleMoves.add(List.of(fromCol-1,fromRow+1));
            possibleMoves.add(List.of(fromCol,fromRow+1));
            possibleMoves.add(List.of(fromCol+1,fromRow+1));
        }

        possibleMoves.removeIf(move -> move.contains(-1) || move.contains(7) || move.get(1) == 6);

        for (List<Integer> move: possibleMoves) {
            if (pieces[move.get(0)][move.get(1)]!=null && pieces[move.get(0)][move.get(1)].equals("BLACK")){
                possibleMoves.remove(move);
                break;
            }
        }
        for (List<Integer> move: possibleMoves) {
            if (pieces[move.get(0)][move.get(1)]!=null && pieces[move.get(0)][move.get(1)].equals(color)){
                possibleMoves.remove(move);
                break;
            }
        }

        System.out.println(Arrays.toString(possibleMoves.toArray()));

        return possibleMoves.contains(List.of(toCol,toRow));
    }

    private void checkForWin(){
        int redPieceCount = 0;
        int bluePieceCount = 0;
        //kék nyert
        for (String[] piece : pieces) {
            for (String s : piece) {
                if (s != null) {
                    if (s.equals("RED")) redPieceCount++;
                    if (s.equals("BLUE")) bluePieceCount++;
                }

            }
        }


        if (bluePieceCount == 0){
            System.out.println("kék");
            Alert allertWindow = new Alert(Alert.AlertType.INFORMATION);
            allertWindow.setTitle("Game is ended!");
            allertWindow.setContentText("Red won");
            allertWindow.showAndWait();
            System.exit(1);
        }
        if (redPieceCount == 0){
            System.out.println("pilios");

            Alert allertWindow = new Alert(Alert.AlertType.INFORMATION);
            allertWindow.setTitle("Game is ended!");
            allertWindow.setContentText("Blue won");
            allertWindow.showAndWait();
            System.exit(1);

        }


    }

    private void movePiece(int col,int row,String color){

        if (isPiececlicked && canMove(clickedPiecePos[0],clickedPiecePos[1],clickedPieceColor,col,row)){
            System.out.println("lépett :(");
            pieces[clickedPiecePos[0]][clickedPiecePos[1]] = "";
            pieces[col][row] =  clickedPieceColor;
            isPiececlicked = false;
            clickedPieceColor = "";
            System.out.println(clickedPiecePos[0]);
            System.out.println(clickedPiecePos[1]);
            blueToMove = !blueToMove;
        } else {

            if (blueToMove && isBlue(color)) {
                clickedPiecePos[0] = col;
                clickedPiecePos[1] = row;
                clickedPieceColor = "BLUE";
                isPiececlicked = true;
            } else if (!blueToMove && isRed(color)) {
                clickedPiecePos[0] = col;
                clickedPiecePos[1] = row;
                clickedPieceColor = "RED";
                isPiececlicked = true;
            }
        }


        clearBoard();
        drawPieces();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pieces = initPieces();
        drawPieces();
    }
    @FXML
    public void boardClicked(MouseEvent mouseEvent) {
        int squareSize = 80;
        int clickedCol = (int) mouseEvent.getX() / squareSize;
        int clickedRow = (int) mouseEvent.getY() / squareSize;

        movePiece(clickedCol,clickedRow,pieces[clickedCol][clickedRow]);
        checkForWin();
    }
}