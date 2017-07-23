package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class MoveValidatingForEachPiece extends BoardManager {
	private boolean isMoveAllowed = false;
	private boolean isPathFree = false;

	MoveValidatingForEachPiece(Board board) throws InvalidMoveException{
		this.board = board;
	}

	static private Coordinate from;
	static private Coordinate to;
	private MoveType type;
	private boolean isThisMoveAlloved = false;

	/**
	 * Validate move performing by the chess piece from one place to other
	 * 
	 * @param from
	 * @param to
	 * @return Move
	 * @throws InvalidMoveException
	 */
	Move moveValidatingForEachPiece(Coordinate from, Coordinate to) throws InvalidMoveException {
		this.board = getBoard();
		Piece myPiece = getMyPiece(from);
		Move thisMove = null;

		if (from.getX() > Board.SIZE || from.getY() > Board.SIZE || to.getX() > Board.SIZE || to.getY() > Board.SIZE
				|| from.getX() < 0 || from.getY() < 0 || to.getX() < 0 || to.getY() < 0) {
			throw new InvalidMoveException("Wybrane pole jest poza plansz¹!");
		} else if (myPiece.getType() == PieceType.BISHOP && this.board.getPieceAt(from) != null) {
			this.isThisMoveAlloved = bishopValidating(from, to);
		} else if (myPiece.getType() == PieceType.QUEEN && this.board.getPieceAt(from) != null) {
			this.isThisMoveAlloved = queenValidating(from, to);
		} else if (myPiece.getType() == PieceType.KING && this.board.getPieceAt(from) != null) {
			this.isThisMoveAlloved = kingValidating(from, to);
		} else if (myPiece.getType() == PieceType.ROOK && this.board.getPieceAt(from) != null) {
			this.isThisMoveAlloved = rookValidating(from, to);
		} else if (myPiece.getType() == PieceType.KNIGHT && this.board.getPieceAt(from) != null) {
			this.isThisMoveAlloved = knightValidating(from, to);
		} else if (myPiece.getType() == PieceType.PAWN && this.board.getPieceAt(from) != null) {
			isThisMoveAlloved = pawnValidating(from, to);
		} else
			throw new InvalidMoveException("B³¹d! Nie rozpoznano figury.");

		if (isThisMoveAlloved == true) {
			Move someMove = new Move(from, to, type, myPiece);
			thisMove = someMove;
		} else if (isThisMoveAlloved != true)
			throw new InvalidMoveException("Nie mozna wykonaæ ruchu.");

		if (thisMove == null)
			throw new InvalidMoveException("Pusty obiekt ruchu.");
		return thisMove;
	}

	/**
	 * Checks piece type on last field of move and sets move type to CAPTURE or
	 * ATTACK
	 * 
	 * @param isPathFreeCounter
	 * @return isPathFreeCounter
	 * @throws InvalidMoveException
	 */
	private int whoInLastCellAndMoveType(int isPathFreeCounter, Coordinate from, Coordinate to) throws InvalidMoveException {
		if (!board.getPieces()[to.getX()][to.getY()].equals(null) && !board.getPieces()[from.getX()][from.getY()].equals(null) && board.getPieceAt(from).getColor() != board.getPieceAt(to).getColor()) {
			isPathFreeCounter++;
			this.type = MoveType.CAPTURE;
		} else if (!board.getPieces()[to.getX()][to.getY()].equals(null) && !board.getPieces()[from.getX()][from.getY()].equals(null) && board.getPieceAt(from).getColor() == board.getPieceAt(to).getColor()) {
			isPathFreeCounter--;
		} else if (!board.getPieces()[from.getX()][from.getY()].equals(null) && board.getPieces()[to.getX()][to.getY()].equals(null)) {
			isPathFreeCounter++;
			this.type = MoveType.ATTACK;
		} else
			throw new InvalidMoveException("Nie mo¿na wykonaæ ruchu! Nieznany kolor figury na polu docelowym.");
		return isPathFreeCounter;
	}

	private int deltaX(Coordinate from, Coordinate to) {
		int deltaX;
		deltaX = to.getX() - from.getX();
		return deltaX;
	}

	private int deltaY(Coordinate from, Coordinate to) {
		int deltaY = to.getY() - from.getY();
		return deltaY;
	}

	/**
	 * Checks fields of move diegonally, horizontally, vertically, for one-field
	 * move and for knight move type.
	 * 
	 * @param from
	 * @param to
	 * @return isPathFree
	 * @throws InvalidMoveException
	 */
	private boolean isItPathFree(Coordinate from, Coordinate to) throws InvalidMoveException {
		isPathFree = false;
		int xIncrementSign = 0;
		int yIncrementSign = 0;
		int isPathFreeCounter = 0;
		int xIncrement = xIncrementSign;
		int yIncrement = yIncrementSign;

		// czy ruch na ukos
		if (Math.abs(deltaX(from, to)) != 0 && Math.abs(deltaY(from, to)) != 0) {
			xIncrementSign = (deltaX(from, to)) / (Math.abs(deltaX(from, to)));
			yIncrementSign = (deltaY(from, to)) / (Math.abs(deltaY(from, to)));
			for (int iteratorThroughBoard = 0; iteratorThroughBoard < Math
					.abs(deltaX(from, to) - 1); iteratorThroughBoard++) {
				if (board.getPieceAt(from.getX() + xIncrement, from.getY() + yIncrement) != null) {
				}else{
					isPathFreeCounter++;
					xIncrement += xIncrementSign;
					yIncrement += yIncrementSign;
				}
			}
			if (whoInLastCellAndMoveType(isPathFreeCounter, from, to) == Math.abs(deltaX(from, to))
					|| board.getPieceAt(to) != null) {
				isPathFree = true;
			} else
				throw new InvalidMoveException("Bl¹d! Nie mozna wykonaæ ruchu na ukos.");
		}
		// czy ruch w poziomie
		else if (Math.abs(deltaX(from, to)) != 0 && Math.abs(deltaY(from, to)) == 0) {
			xIncrementSign = (deltaX(from, to)) / (Math.abs(deltaX(from, to)));
			for (int iteratorThroughBoard = 0; iteratorThroughBoard < Math
					.abs(deltaX(from, to) - 1); iteratorThroughBoard++) {
				if (board.getPieceAt(from.getX() + xIncrement, from.getY()) == null) {
					isPathFreeCounter++;
					xIncrement += xIncrementSign;
				}
			}

			if (whoInLastCellAndMoveType(isPathFreeCounter, from, to) == Math.abs(deltaX(from, to))
					|| board.getPieceAt(to) != null) {
				isPathFree = true;
			} else
				throw new InvalidMoveException("Bl¹d! Nie mozna wykonaæ ruchu w poziomie.");
		}
		// czy ruch w pionie
		else if (Math.abs(deltaX(from, to)) == 0 && Math.abs(deltaY(from, to)) != 0) {
			yIncrementSign = (deltaY(from, to)) / (Math.abs(deltaY(from, to)));
			for (int iteratorThroughBoard = 0; iteratorThroughBoard < Math
					.abs(deltaY(from, to) - 1); iteratorThroughBoard++) {
				if (board.getPieceAt(from.getX(), from.getY() + yIncrement) == null) {
					isPathFreeCounter++;
					yIncrement += yIncrementSign;
				}
			}
			if (whoInLastCellAndMoveType(isPathFreeCounter, from, to) == Math.abs(deltaY(from, to))
					|| board.getPieceAt(to) != null) {
				isPathFree = true;
			} else
				throw new InvalidMoveException("Bl¹d! Nie mozna wykonaæ ruchu w pionie.");

		}
		// czy teraz rusza siê skoczek
		else if (getMyPiece(from) == Piece.BLACK_KNIGHT || getMyPiece(from) == Piece.WHITE_KNIGHT) {
			whoInLastCellAndMoveType(isPathFreeCounter, from, to);
			if (isPathFreeCounter == 1) {
				isPathFree = true;
			} else if (isPathFreeCounter == -1) {
				isPathFree = false;
				throw new InvalidMoveException("Bl¹d! Na miejsu docelowym znajduje siê figura Twojego koloru.");
			} else {
			}
		} else
			throw new InvalidMoveException("B³¹d! Ruch nimo¿liwy do wykonania.");
		return isPathFree;
	}

	private boolean bishopValidating(Coordinate from, Coordinate to) throws InvalidMoveException {
		if (Math.abs(deltaX(from, to)) == Math.abs(deltaY(from, to)) && isItPathFree(from, to) == true) {
			isMoveAllowed = true;
		} else {
			throw new InvalidMoveException("Ruch goñca nimo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}

	private boolean queenValidating(Coordinate from, Coordinate to) throws InvalidMoveException {
		if (Math.abs(deltaX(from, to)) == Math.abs(deltaY(from, to)) && isItPathFree(from, to) == true
				&& Math.abs(deltaX(from, to)) != 0) {
			isMoveAllowed = true;
		} else if (Math.abs(deltaX(from, to)) == 0 && Math.abs(deltaY(from, to)) != 0
				&& isItPathFree(from, to) == true) {
			isMoveAllowed = true;
		} else if (Math.abs(deltaX(from, to)) != 0 && Math.abs(deltaX(from, to)) == 0
				&& isItPathFree(from, to) == true) {
			isMoveAllowed = true;
		} else {
			throw new InvalidMoveException("B³¹d! Ruch damy niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}

	private boolean kingValidating(Coordinate from, Coordinate to) throws InvalidMoveException {
//		boolean b = board.getPieces()[from.getX()+1][from.getY()] != null;
		if (Math.abs(deltaX(from, to)) <= 1 && Math.abs(deltaY(from, to)) <= 1 && isItPathFree(from, to) == true
				&& isKingInCheck(this.board.getPieceAt(from).getColor()) == false) {
			isMoveAllowed = true;
		} else if (getMyPiece(from).getColor() == Color.WHITE && from.getX() == 4 && from.getY() == 0 && getMyPiece(from).getColor() == Color.WHITE && board.getPieces()[from.getX()+1][from.getY()] != null){
		} else {
			throw new InvalidMoveException("B³¹d! Ruch króla niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}

	private boolean rookValidating(Coordinate from, Coordinate to) throws InvalidMoveException {
		if (Math.abs(deltaX(from, to)) == 0 && Math.abs(deltaY(from, to)) != 0 && isItPathFree(from, to) == true) {
			isMoveAllowed = true;
		} else if (Math.abs(deltaX(from, to)) != 0 && Math.abs(deltaX(from, to)) == 0
				&& isItPathFree(from, to) == true) {
			isMoveAllowed = true;
		} else {
			throw new InvalidMoveException("B³¹d! Ruch wie¿y niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}

	private boolean knightValidating(Coordinate from, Coordinate to) throws InvalidMoveException {
		if ((Math.abs(deltaX(from, to)) == 2 && Math.abs(deltaY(from, to)) == 1 && isItPathFree(from, to) == true)
				|| (Math.abs(deltaX(from, to)) == 1 && Math.abs(deltaY(from, to)) == 2
						&& isItPathFree(from, to) == true)) {
			isMoveAllowed = true;
		} else {
			throw new InvalidMoveException("B³¹d! Ruch skoczka niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}

	private boolean pawnValidating(Coordinate from, Coordinate to) throws InvalidMoveException {
		isMoveAllowed = false;

		if (this.board.getPieceAt(to) == null && deltaY(from, to) == 1 && deltaX(from, to) == 0) {
			isMoveAllowed = true;
		} else if (this.board.getPieceAt(from).getColor() != calculateNextMoveColor()
				&& Math.abs(deltaX(from, to)) == Math.abs(deltaY(from, to)) && deltaY(from, to) == 1) {
			isMoveAllowed = true;
		} else if (from.getY() == 2 && to.getY() == 4 && to.getX() == from.getX()
				&& this.board.getPieceAt(to) == null) {
			isMoveAllowed = true;
		} else if (isMoveAllowed = false) {
			throw new InvalidMoveException("B³¹d! Ruch pionka niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}

	private Piece getMyPiece(Coordinate point) throws InvalidMoveException {
		if (this.board.getPieceAt(point) == null) {
			throw new InvalidMoveException("Pole pocz¹tkowe jest puste!");
		} else {
			Piece myPiece = this.board.getPieceAt(point);
			return myPiece;
		}
	}

	private Color calculateNextMoveColor() {
		if (this.board.getMoveHistory().size() % 2 == 0) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}
}
