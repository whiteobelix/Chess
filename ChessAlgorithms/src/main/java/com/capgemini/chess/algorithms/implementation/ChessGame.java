package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

public class ChessGame {

	public static void main(String[] args) throws InvalidMoveException, KingInCheckException {
		Coordinate sk¹dœ = new Coordinate(0, 1);
		Coordinate dok¹dœ = new Coordinate(0, 2);
		try {
			BoardManager newGeame = new BoardManager();
			newGeame.performMove(sk¹dœ, dok¹dœ);
		} catch (InvalidMoveException e) {
			e.printStackTrace();
		}
	}
}
