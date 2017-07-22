package com.capgemini.chess.algorithms.implementation;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

/**
 * Class for managing of basic operations on the Chess Board.
 *
 * @author Michal Bejm
 *
 */
public class BoardManager {

	protected Board board = new Board();

	public BoardManager() {
		initBoard();
	}

	public BoardManager(List<Move> moves) {
		initBoard();
		for (Move move : moves) {
			addMove(move);
		}
	}

	public BoardManager(Board board) {
		this.board = board;
	}

	/**
	 * Getter for generated board
	 *
	 * @return board
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Performs move of the chess piece on the chess board from one field to
	 * another.
	 *
	 * @param from
	 *            coordinates of 'from' field
	 * @param to
	 *            coordinates of 'to' field
	 * @return move object which includes moved piece and move type
	 * @throws InvalidMoveException
	 *             in case move is not valid
	 */
	public Move performMove(Coordinate from, Coordinate to) throws InvalidMoveException {
		Move move = validateMove(from, to);// Zwraca koordynaty from i to, rodzaj ruchu(attack, capture, roszada, bicie w przelocie) oraz rodzaj poruszanej bierki razem z kolorem.

		addMove(move);// ostatecznie wykonuje ruch

		return move;
	}

	/**
	 * Calculates state of the chess board.
	 *
	 * @return state of the chess board
	 */
	public BoardState updateBoardState() throws InvalidMoveException, KingInCheckException{
//TODO tu s¹ metody wykorzystuj¹ce isKingInCheck
		Color nextMoveColor = calculateNextMoveColor();

		boolean isKingInCheck = isKingInCheck(nextMoveColor);
		boolean isAnyMoveValid = isAnyMoveValid(nextMoveColor);

		BoardState boardState;
		if (isKingInCheck) {
			if (isAnyMoveValid) {
				boardState = BoardState.CHECK;
			} else {
				boardState = BoardState.CHECK_MATE; // koñcem bêdzie brak mo¿liwoœci wyjœcia z mata													 
			}
		} else {
			if (isAnyMoveValid) {
				boardState = BoardState.REGULAR;
			} else {
				boardState = BoardState.STALE_MATE;
			}
		}
		this.board.setState(boardState);
		return boardState;
	}

	/**
	 * Checks threefold repetition rule (one of the conditions to end the chess
	 * game with a draw).
	 *
	 * @return true if current state repeated at list two times, false otherwise
	 */
	public boolean checkThreefoldRepetitionRule() {

		// there is no need to check moves that where before last capture/en
		// passant/castling
		int lastNonAttackMoveIndex = findLastNonAttackMoveIndex();
		List<Move> omittedMoves = this.board.getMoveHistory().subList(0, lastNonAttackMoveIndex);
		BoardManager simulatedBoardManager = new BoardManager(omittedMoves);

		int counter = 0;
		for (int i = lastNonAttackMoveIndex; i < this.board.getMoveHistory().size(); i++) {
			Move moveToAdd = this.board.getMoveHistory().get(i);
			simulatedBoardManager.addMove(moveToAdd);
			boolean areBoardsEqual = Arrays.deepEquals(this.board.getPieces(),
					simulatedBoardManager.getBoard().getPieces());
			if (areBoardsEqual) {
				counter++;
			}
		}

		return counter >= 2;
	}

	/**
	 * Checks 50-move rule (one of the conditions to end the chess game with a
	 * draw).
	 *
	 * @return true if no pawn was moved or not capture was performed during
	 *         last 50 moves, false otherwise
	 */
	public boolean checkFiftyMoveRule() {

		// for this purpose a "move" consists of a player completing his turn
		// followed by his opponent completing his turn
		if (this.board.getMoveHistory().size() < 100) {
			return false;
		}

		for (int i = this.board.getMoveHistory().size() - 1; i >= this.board.getMoveHistory().size() - 100; i--) {
			Move currentMove = this.board.getMoveHistory().get(i);
			PieceType currentPieceType = currentMove.getMovedPiece().getType();
			if (currentMove.getType() != MoveType.ATTACK || currentPieceType == PieceType.PAWN) {
				return false;
			}
		}

		return true;
	}

	// PRIVATE

	private void initBoard() {

		this.board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 7));
		this.board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(1, 7));
		this.board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(2, 7));
		this.board.setPieceAt(Piece.BLACK_QUEEN, new Coordinate(3, 7));
		this.board.setPieceAt(Piece.BLACK_KING, new Coordinate(4, 7));
		this.board.setPieceAt(Piece.BLACK_BISHOP, new Coordinate(5, 7));
		this.board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(6, 7));
		this.board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(7, 7));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(x, 6));
		}

		this.board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		this.board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(1, 0));
		this.board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(2, 0));
		this.board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(3, 0));
		this.board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		this.board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(5, 0));
		this.board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(6, 0));
		this.board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(7, 0));

		for (int x = 0; x < Board.SIZE; x++) {
			this.board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(x, 1));
		}
	}

	private void addMove(Move move) {

		addRegularMove(move);

		if (move.getType() == MoveType.CASTLING) {
			addCastling(move);
		} else if (move.getType() == MoveType.EN_PASSANT) {
			addEnPassant(move);
		}

		this.board.getMoveHistory().add(move);
	}

	private void addRegularMove(Move move) { //wykonuje normalny ruch (poza roszad¹ i biciem piona w locie)
		Piece movedPiece = this.board.getPieceAt(move.getFrom());
		this.board.setPieceAt(null, move.getFrom()); //(piece, Coord.)
		this.board.setPieceAt(movedPiece, move.getTo());

		performPromotion(move, movedPiece);
	}

	private void performPromotion(Move move, Piece movedPiece) {
		if (movedPiece == Piece.WHITE_PAWN && move.getTo().getY() == (Board.SIZE - 1)) {
			this.board.setPieceAt(Piece.WHITE_QUEEN, move.getTo());
		}
		if (movedPiece == Piece.BLACK_PAWN && move.getTo().getY() == 0) {
			this.board.setPieceAt(Piece.BLACK_QUEEN, move.getTo());
		}
	}

	private void addCastling(Move move) {
		if (move.getFrom().getX() > move.getTo().getX()) {
			Piece rook = this.board.getPieceAt(new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(0, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() + 1, move.getTo().getY()));
		} else {
			Piece rook = this.board.getPieceAt(new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(null, new Coordinate(Board.SIZE - 1, move.getFrom().getY()));
			this.board.setPieceAt(rook, new Coordinate(move.getTo().getX() - 1, move.getTo().getY()));
		}
	}

	private void addEnPassant(Move move) { // cofa ostatni ruch? Why?
		Move lastMove = this.board.getMoveHistory().get(this.board.getMoveHistory().size() - 1);
		this.board.setPieceAt(null, lastMove.getTo());
	}

	/**
	 * Metoda zwraca typ Move (koordynaty, rodzaj ruchu i rodzaj bierki) i
	 * oblicza czy dany ruch dan¹ bierk¹ mo¿e byæ wykonany.
	 */
	@SuppressWarnings("null")
	private Move validateMove(Coordinate from, Coordinate to) throws InvalidMoveException, KingInCheckException {
		MoveValidatingForEachPiece oneMove = new MoveValidatingForEachPiece(board);
		oneMove.moveValidatingForEachPiece(from, to);
			
				oneMove.moveValidatingForEachPiece(from, to);
			
		return oneMove.moveValidatingForEachPiece(from, to);
					
	}

	
	Coordinate kingSearching(Color kingColor){
		Coordinate thisKingCoordinate = null;
		for(int x = 0; x < Board.SIZE - 1; x++){
			for(int y = 0; y < Board.SIZE - 1; y++){	
				if (this.board.getPieceAt(x, y) != null && this.board.getPieceAt(x, y).getType() == PieceType.KING && this.board.getPieceAt(x, y).getColor() == kingColor){
					thisKingCoordinate = new Coordinate(x, y);				
				}
			}
		}
		return thisKingCoordinate;
	}
	
	boolean isKingInCheckByKnight(Color kingColor, int kingX, int kingY){
		boolean isKingInCheckByKnight = false;
		int[][] knightFields = {{kingX+1, kingY+2},{kingX+1, kingY-2},{kingX+2, kingY+1},{kingX+2, kingY-1},{kingX-1, kingY+2},{kingX-1, kingY-2},{kingX-2, kingY+1},{kingX-2, kingY-1}};	
		for(int knightField[] : knightFields){
			Coordinate potentialKnightCoordinate = new Coordinate(knightField[0], knightField[1]);
			if (this.board.getPieceAt(kingX, kingY) != null && knightField[0] >= 0 && knightField[0] <= Board.SIZE && knightField[1] >= 0 && knightField[1] <= Board.SIZE && this.board.getPieceAt(potentialKnightCoordinate) != null && this.board.getPieceAt(potentialKnightCoordinate).getType() == PieceType.KNIGHT && this.board.getPieceAt(potentialKnightCoordinate).getColor() != kingColor){
				isKingInCheckByKnight = true;
			}			
		}
		return isKingInCheckByKnight;
	}
	
	boolean isKingInCheckByOtherFigureThanKnight(Color kingColor, int kingX, int kingY) throws KingInCheckException, InvalidMoveException{
		boolean isKingInCheckByOtherFigureThanKnight = false;
		for(int x = 0; x < Board.SIZE; x++){
			for(int y = 0; y < Board.SIZE; y++){				
//				this.board.getPieceAt(x, y);
				Coordinate from = new Coordinate(kingX, kingY);
				Coordinate to = new Coordinate(x, y);				
				if (validateMove(from, to).equals(null) == false){
					isKingInCheckByOtherFigureThanKnight = true;
				}				
			}
		}		
		return isKingInCheckByOtherFigureThanKnight;
	}
	
	 boolean isKingInCheck(Color kingColor) throws KingInCheckException, InvalidMoveException {

//		TODO please add implementation here: is king in check(). Najpierw wyszukaj króla konkretnego koloru
//		ruch po skosach ruch poziomo i pionowo ruch skoczka wszystko a¿ do koñca planszy, przerwij przy napotkaniu pierwszej figury, sprawdŸ czy figura na koñcu wolnej œcie¿ki to 
//		skoczek
		boolean isKingInCheck = false;
		int kingX = kingSearching(kingColor).getX();
		int kingY = kingSearching(kingColor).getY();
		
		if(isKingInCheckByKnight(kingColor, kingX, kingY) == true || isKingInCheckByOtherFigureThanKnight(kingColor, kingX, kingY) == true){
			isKingInCheck = true;
			throw new KingInCheckException();
			}
		return isKingInCheck;		
	}

	boolean isAnyMoveValid(Color nextMoveColor) throws InvalidMoveException, KingInCheckException{
		 //TODO isKingInCheck musi sprawdzaæ czy król teraz (przed wykonaniem ruchu jetst w szachu - niech wykona ruch ka¿dej figury i sprawdzi czy jest tam ktoœ przeciwnego koloru kto mo¿e siê ruszac w³aœnie w ten sposób)
		boolean isAnyMoveValid = false;
		int counter = 0;
//		nextMoveColor = Color.WHITE;
//		if (nextMoveColor == Color.WHITE){
//		List<Piece> piecesArray = new ArrayList<Piece>();
//		Board tempBoard = this.board;
//		BoardManager tempBM = new BoardManager(tempBoard);
//		List<Coordinate> coordinatesArray = new ArrayList<Coordinate>();
		for(int x = 0; x < 8; x++){
			for(int y = 0; y < 8; y++){	
				if (this.board.getPieceAt(x, y).getColor() == nextMoveColor){									
					Piece p = this.board.getPieceAt(x, y);
					Coordinate fromCoord = new Coordinate(x, y);
					for(int iAroundX = -1; iAroundX < 2; iAroundX++){
						if (iAroundX != 0){
							for(int iAroundY = -1; iAroundY < 2; iAroundY++){
								if (iAroundY != 0){
									int newX = x + iAroundX;
									int newY = y + iAroundY;
									Coordinate toCoord = new Coordinate(newX,newY);
									 // zwraca move, który zawiera koordynaty from i to, rodzaj ruchu oraz poruszan¹ bierkê.
									if (validateMove(fromCoord, toCoord) != null){
										counter++;
									}
								}
							}
						}
						
					}
//					coordinatesArray.add(c);
				}
			}
		}
//		while (isAnyMoveValid == false)
//		BoardManager tempBM = new BoardManager(tempBoard);//na tempBM mogê wywo³ywaæ metody waliduj¹ce ruch
//		for(int piecesCounter = 0; piecesCounter < 16; piecesCounter++){
////				piecesArray.get(piecesCounter).;;
//			Coordinate d = new Coordinate(x, y)
//				validateMove(coordinatesArray.get(piecesCounter), Coordinate(coordinatesArray.get(piecesCounter).getX()+1, coordinatesArray.get(piecesCounter).getY()+1));
//				//moveVal validateMove(from, to);
//			if(isAnyMoveValid == true){
//				
//			}
//		}
			
		
		boolean z=true;
		while(z == true){
			z=true;
		}
		
		return z;
		
				
//		List<Piece> piecesArray = new ArrayList<Piece>();
//		List<Coordinate> coordinatesArray = new ArrayList<Coordinate>();
//		int x=0;//wspó³rzêdna x bierki
//		for(Piece[] pieceInBoard:board.getPieces()){
//			x++;
//			int y=0;//wspó³rzêdna y bierki
//			for(Piece p : pieceInBoard){
//				y++;
//				if(p != null && p.getColor() == nextMoveColor){
//					p.getColor();
//				piecesArray.add(p);
//				Coordinate tempCoord = new Coordinate(x,y);
//				coordinatesArray.add(tempCoord);
////				dopisz wspó³rzêdne do listy;
//				}
//			}
////			System.out.println(board.getPieces().length);
//		}
//			Drukuje figury z planszy:
//		System.out.println(piecesArray);
//		while(true){
//			int z=1;
//		}
		
//		do			
//		for(d³ugoœæ listy){
//			zwaliduj ruch bierk¹ z listy (pobierz te¿ wspólrzêdne) a¿ uda siê czymœ poruszyæ (wtedy flaga1=true) 
//			isKingInCheck przed ruchem (mój kolor czyli nextMoveColor) (jeœli nie to flaga2=false, inaczej true)
//		}
//		while()
//		Jeœli flaga1==true a flaga2==false to isAnyMoveValid=true;
//}
		
		
		
			
//		return isAnyMoveValid;
}

	private Color calculateNextMoveColor() {
		if (this.board.getMoveHistory().size() % 2 == 0) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}

	private int findLastNonAttackMoveIndex() {
		int counter = 0;
		int lastNonAttackMoveIndex = 0;

		for (Move move : this.board.getMoveHistory()) {
			if (move.getType() != MoveType.ATTACK) {
				lastNonAttackMoveIndex = counter;
			}
			counter++;
		}
		return lastNonAttackMoveIndex;
	}
}


//TODO w moich metodach dodaæ obs³ugê klasy InvalidMoveExeption
//TODO pozmieniaæ metody walidacyjne tak ¿eby by³y dobre te¿ dla figur czarnych
//TODO Testy: poprawiæ try catch ¿eby inaczej rzuca³y wyj¹tki