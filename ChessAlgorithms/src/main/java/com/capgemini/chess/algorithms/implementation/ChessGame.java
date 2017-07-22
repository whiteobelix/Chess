package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

public class ChessGame {

	public static void main(String[] args) throws InvalidMoveException, KingInCheckException {
		// TODO Auto-generated method stub
		Coordinate sk¹dœ = new Coordinate(0,1);
		Coordinate dok¹dœ = new Coordinate(0,2);
		try{
		BoardManager newGeame = new BoardManager();
		newGeame.performMove(sk¹dœ, dok¹dœ);
		}catch (InvalidMoveException e){
		e.printStackTrace();
		}
	}

}
