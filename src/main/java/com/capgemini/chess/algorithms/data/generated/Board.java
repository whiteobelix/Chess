package com.capgemini.chess.algorithms.data.generated;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Piece;

/**
 * Board representation.
 * Board objects are generated based on move history.
 * 
 * @author Michal Bejm
 *
 */
public class Board {
	
	public static final int SIZE = 8;
	
	private Piece[][] pieces = new Piece[SIZE][SIZE]; //tablica o rozmiarze 8x8 z obiektami-bierkami
	private List<Move> moveHistory = new ArrayList<>(); //historia ruchów
	private BoardState state; //stan gry (szach, pat, normal, szach-mat)
	
	public Board() {
	}

	public List<Move> getMoveHistory() {
		return moveHistory;
	}

	public Piece[][] getPieces() {//metoda pobieraj¹ca pionki z ca³ej planszy w tablicy obiektów
		return pieces; //tablicowa reprezentacja planszy z pionkami
	}

	public BoardState getState() {
		return state;
	}

	public void setState(BoardState state) {
		this.state = state;
	}
	
	/**
	 * Sets chess piece on board based on given coordinates
	 * 
	 * @param piece chess piece
	 * @param board chess board
	 * @param coordinate given coordinates
	 */
	public void setPieceAt(Piece piece, Coordinate coordinate) { //stawianie bierki
		pieces[coordinate.getX()][coordinate.getY()] = piece;
	}
	
	/**
	 * Gets chess piece from board based on given coordinates
	 * 
	 * @param coordinate given coordinates
	 * @return chess piece
	 */
	public Piece getPieceAt(Coordinate coordinate) {
		return pieces[coordinate.getX()][coordinate.getY()];
	}
	//@Override
	public Piece getPieceAt(int x, int y) {
		return pieces[x][y];
	}
}
