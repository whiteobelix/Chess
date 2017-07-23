package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

public class ChessGame {

	public static void main(String[] args) throws InvalidMoveException, KingInCheckException {
		Coordinate sk�d� = new Coordinate(0, 1);
		Coordinate dok�d� = new Coordinate(0, 2);
		try {
			BoardManager newGeame = new BoardManager();
			newGeame.performMove(sk�d�, dok�d�);
		} catch (InvalidMoveException e) {
			e.printStackTrace();
		}
	}
}
