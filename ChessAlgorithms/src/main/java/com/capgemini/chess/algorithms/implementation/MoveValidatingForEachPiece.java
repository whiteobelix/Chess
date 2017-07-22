package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class MoveValidatingForEachPiece extends BoardManager{
    private boolean isMoveAllowed = false;
//	static private BoardManager tempBoard;
	private boolean isPathFree = false;
	
	
//	Metoda validateMove zwraca obiekt klasy Move, który ma zawieraæ:
	static private Coordinate from;
	static private Coordinate to;
	private MoveType type;	
	private boolean isThisMoveAlloved = false;	
	
	Move moveValidatingForEachPiece(Coordinate from, Coordinate to) throws InvalidMoveException{
//		getBoard();
		Piece myPiece = getMyPiece(from);		
		Move thisMove = null;	
		
//		boolean b33 =  this.board.getPieceAt(to) == null;
//		boolean	b333 = deltaY(from,to) == 1;
//		boolean	b3333 = deltaX(from,to) == 0;
		


//		PieceType bb = myPiece.getType();
//		boolean b = this.board.getPieceAt(to) == null;
//		Color k = this.board.getPieceAt(from).getColor();
//		//(board.getPieceAt(to) == null && deltaY(from,to) == 1 && deltaX(from,to) == 0)    ||    (this.board.getPieceAt(from).getColor() != calculateNextMoveColor()  && Math.abs(deltaX(from,to)) == Math.abs(deltaY(from,to)) && deltaY(from,to) == 1)   ||   (from.getY() == 2 && to.getY() == 4 && to.getX() == from.getX() && isItPathFree(from, to) == true)
//		Piece p = board.getPieceAt(to);
//		int d = deltaY(from,to);
//		int d2 = deltaX(from,to);
//		Color k1 = this.board.getPieceAt(from).getColor();
//		Color cc = calculateNextMoveColor();
//		int d3 = Math.abs(deltaX(from,to));
//		int d4 = Math.abs(deltaY(from,to));
//		int d5 = deltaY(from,to);
//		int d6 = from.getY();
//		int d7 = to.getY();
//		int d8 = to.getX();
//		int d9 = from.getX();
//		//boolean d10 = isItPathFree(from, to);
//		int gg = 2;
//		int gj = to.getX();
		
		// boolean mówi¹cy wprost, czy pole "to" jest zajête: board.getPieceAt(from).equals(board.getPieceAt(to));
		try{
			if (from.getX() > Board.SIZE || from.getY() > Board.SIZE || to.getX() > Board.SIZE || to.getY() > Board.SIZE || from.getX() < 0 || from.getY() < 0 || to.getX() < 0 || to.getY() < 0){
				throw new InvalidMoveException("Wybrane pole jest poza plansz¹!");
			}else if(myPiece.getType() == PieceType.BISHOP && this.board.getPieceAt(from)!=null){
				this.isThisMoveAlloved = bishopValidating(from,to);
			}else if(myPiece.getType() == PieceType.QUEEN){
				this.isThisMoveAlloved = queenValidating(from,to);
			}else if(myPiece.getType() == PieceType.KING){
				this.isThisMoveAlloved = kingValidating(from,to);
			}else if(myPiece.getType() == PieceType.ROOK){
				this.isThisMoveAlloved = rookValidating(from,to);
			}else if(myPiece.getType() == PieceType.KNIGHT){
				this.isThisMoveAlloved = knightValidating(from,to);
			}else if(myPiece.getType() == PieceType.PAWN){
				this.isThisMoveAlloved = pawnValidating(from,to);
			}else throw new InvalidMoveException("B³¹d! Nie rozpoznano figury.");
	
			if(isThisMoveAlloved == true){
				Move someMove = new Move(from, to, type, myPiece);
				thisMove = someMove;
			}else System.out.println("Nie mozna wykonaæ ruchu.");
		}
		catch(InvalidMoveException e){
			e.printStackTrace();
		}
		return thisMove;
	}
	
//	try {
//		   // Protected code
//		}catch(ExceptionName e1) {
//		   // Catch block
//		}
	
	int whoInLastCellAndMoveType(int isPathFreeCounter) throws InvalidMoveException{//sprawdza kto jest na ostatnim polu (swój czy przeciwnik)
		if (board.getPieceAt(from).getColor() != board.getPieceAt(to).getColor()){				
			isPathFreeCounter++;
			this.type = MoveType.CAPTURE;
		}else if(board.getPieceAt(from).getColor() == board.getPieceAt(to).getColor()){
			isPathFreeCounter--;
		}else if(board.getPieceAt(from).equals(board.getPieceAt(to)) == false){
			isPathFreeCounter++;
			this.type = MoveType.ATTACK;
		}
		else throw new InvalidMoveException("Nie mo¿na wykonaæ ruchu! Nieznany kolor figury na polu docelowym.");
		return isPathFreeCounter;
	}
		
	int deltaX(Coordinate from, Coordinate to){
		int deltaX = 15;
//		if (to.getX()-from.getX() != 0){
			deltaX = to.getX()-from.getX();
//			}
		
		return deltaX;	
	}
	
	int deltaY(Coordinate from, Coordinate to){
		int deltaY = to.getY()-from.getY();
		return deltaY;	
	}
	
	//IS IT PATH FREE      Ruchy w rózne strony
	private boolean isItPathFree (Coordinate from, Coordinate to) throws InvalidMoveException{
		isPathFree = false;
		int xIncrementSign = (deltaX(from,to))/(Math.abs(deltaX(from,to))); //= +-1
		int yIncrementSign = (deltaY(from,to))/(Math.abs(deltaY(from,to))); //= +-1
		int isPathFreeCounter = 0;
		int xIncrement = xIncrementSign;
		int yIncrement = yIncrementSign;
		
		//czy ruch na skos
		if(Math.abs(deltaX(from,to)) != 0 && Math.abs(deltaY(from,to)) != 0){		
			for (int iteratorThroughBoard = 0; iteratorThroughBoard < Math.abs(deltaX(from,to)-1); iteratorThroughBoard++){		
				if(board.getPieceAt(from.getX()+xIncrement ,from.getY()+yIncrement) == null){
					isPathFreeCounter++;
					xIncrement += xIncrementSign;
					yIncrement += yIncrementSign;
				}
			}
			if(whoInLastCellAndMoveType(isPathFreeCounter) == Math.abs(deltaX(from, to)) || board.getPieceAt(to) != null){
				isPathFree = true;
			}else throw new InvalidMoveException("Bl¹d! Nie mozna wykonaæ ruchu na ukos.");
		}
		//czy ruch w poziomie
		else if(Math.abs(deltaX(from,to)) != 0 && Math.abs(deltaY(from,to)) == 0){
			for (int iteratorThroughBoard = 0; iteratorThroughBoard < Math.abs(deltaX(from,to)-1); iteratorThroughBoard++){		
				if(board.getPieceAt(from.getX()+xIncrement,from.getY()) == null){
					isPathFreeCounter++;
					xIncrement += xIncrementSign;
					yIncrement += yIncrementSign;
				}
			}		
		
			 if(whoInLastCellAndMoveType(isPathFreeCounter) == Math.abs(deltaX(from, to)) || board.getPieceAt(to) != null){
					isPathFree = true;
				}
			 else throw new InvalidMoveException("Bl¹d! Nie mozna wykonaæ ruchu w poziomie.");
		}
		//czy ruch w pionie
		else if(Math.abs(deltaX(from,to)) == 0 && Math.abs(deltaY(from,to)) != 0){
			for (int iteratorThroughBoard = 0; iteratorThroughBoard < Math.abs(deltaY(from,to)-1); iteratorThroughBoard++){	
				if(board.getPieceAt(from.getX(),from.getY()+yIncrement) == null){
					isPathFreeCounter++;
					xIncrement += xIncrementSign;
					yIncrement += yIncrementSign;
				}
			}
			if(whoInLastCellAndMoveType(isPathFreeCounter) == Math.abs(deltaY(from, to)) || board.getPieceAt(to) != null){
				isPathFree = true;
				}else throw new InvalidMoveException("Bl¹d! Nie mozna wykonaæ ruchu w pionie.");
			
		}
		//czy teraz rusza siê skoczek
		else if (getMyPiece(from) == Piece.BLACK_KNIGHT || getMyPiece(from) == Piece.WHITE_KNIGHT){
			whoInLastCellAndMoveType(isPathFreeCounter);
			if(isPathFreeCounter == 1){
				isPathFree = true;
			}			
			else if(isPathFreeCounter == -1){
				isPathFree = false;
				throw new InvalidMoveException("Bl¹d! Na miejsu docelowym znajduje siê figura Twojego koloru.");				
			}
		}
		//inny nieznany ruch
		else throw new InvalidMoveException("B³¹d! Ruch nimo¿liwy do wykonania.");
		
		return isPathFree;
	}
	
	
//	boolean blackBishopValidating(Piece piece, Coordinate from, Coordinate to) {//czy figura to czarny goniec
//		if(piece.equals("BLACK_BISHOP") == true && Math.abs(from.getX()-to.getX()) == Math.abs(from.getY()-to.getY()) && board.getPieceAt(to) == null && isPathFree(from, to) == true){
//			isMoveAllowed = true;}
//		else if (piece.equals("BLACK_BISHOP") == true && Math.abs(from.getX()-to.getX()) == 0 && Math.abs(from.getY()-to.getY()) != 0){
//			return isMoveAllowed;}
//		else if (piece.equals("BLACK_BISHOP") == true && Math.abs(from.getX()-to.getX()) != 0 && Math.abs(from.getY()-to.getY()) == 0){
//		}
//		else{		
//		}	
//	}
	
//	boolean queenValidating(Coordinate from, Coordinate to) {//ruchy w ka¿d¹ stronê
//		if(Math.abs(deltaX(from,to)) == Math.abs(deltaY(from,to)) && isItPathFree(from, to) == true){//czy figura to królowa && czy porusza sie po skosie && czy miejsce jest zajête, dodaæ jeszcze: czy droga wolna
//			isMoveAllowed = true;
//		}else if (Math.abs(deltaX(from,to)) == 0 && Math.abs(deltaY(from,to)) != 0 && isItPathFree(from, to) == true){//czy figura to królowa, czy porusza sie wzd³u¿ osi y,
//			isMoveAllowed = true;
//		}else if (Math.abs(deltaX(from,to)) != 0 && Math.abs(deltaX(from,to)) == 0){//czy figura to królowa, czy porusza sie wzd³u¿ osi x
//			isMoveAllowed = true;
//		}else{
//			System.out.println("Ruch goñca nimo¿liwy.");
//		}
//		return isMoveAllowed;
//	}

	boolean bishopValidating(Coordinate from, Coordinate to) throws InvalidMoveException {//ruchy tylko po skosie
		if(Math.abs(deltaX(from,to)) == Math.abs(deltaY(from,to)) && isItPathFree(from, to) == true){//czy figura to goniec && czy porusza sie po skosie && czy droga wolna
			isMoveAllowed = true;
		}
		else{
			System.out.println("Ruch goñca nimo¿liwy do wykonania.");			
		}
		return isMoveAllowed;
	}	
	
	boolean queenValidating(Coordinate from, Coordinate to) throws InvalidMoveException {//ruchy w ka¿d¹ stronê
		if(Math.abs(deltaX(from,to)) == Math.abs(deltaY(from,to)) && isItPathFree(from, to) == true && Math.abs(deltaX(from,to)) != 0){//czy figura to królowa && czy porusza sie po skosie && czy miejsce jest zajête, dodaæ jeszcze: czy droga wolna
			isMoveAllowed = true;
		}else if (Math.abs(deltaX(from,to)) == 0 && Math.abs(deltaY(from,to)) != 0 && isItPathFree(from, to) == true){//czy figura to królowa, czy porusza sie wzd³u¿ osi y,
			isMoveAllowed = true;
		}else if (Math.abs(deltaX(from,to)) != 0 && Math.abs(deltaX(from,to)) == 0 && isItPathFree(from, to) == true){//czy figura to królowa, czy porusza sie wzd³u¿ osi x
			isMoveAllowed = true;
		}else{
			System.out.println("B³¹d! Ruch damy niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}
	
	boolean kingValidating(Coordinate from, Coordinate to) throws InvalidMoveException{//ruchy w ka¿d¹ stronê o 1 pole
		if(Math.abs(deltaX(from,to)) <= 1 && Math.abs(deltaY(from,to)) <=1 && isItPathFree(from, to) == true && isKingInCheck(this.board.getPieceAt(from).getColor()) == false){
			isMoveAllowed = true;
		}else{
			System.out.println("B³¹d! Ruch króla niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;		
	}
	
	boolean rookValidating(Coordinate from, Coordinate to) throws InvalidMoveException {
		if (Math.abs(deltaX(from,to)) == 0 && Math.abs(deltaY(from,to)) != 0 && isItPathFree(from, to) == true){//czy figura to wie¿a, czy porusza sie wzd³u¿ osi y, czy droga wolna
			isMoveAllowed = true;
		}else if (Math.abs(deltaX(from,to)) != 0 && Math.abs(deltaX(from,to)) == 0 && isItPathFree(from, to) == true){//czy figura to wie¿a, czy porusza sie wzd³u¿ osi x, czy droga wolna
			isMoveAllowed = true;
		}else{
			System.out.println("B³¹d! Ruch wie¿y niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}
	
	boolean knightValidating(Coordinate from, Coordinate to) throws InvalidMoveException {
		if ((Math.abs(deltaX(from,to)) == 2 && Math.abs(deltaY(from,to)) == 1 && isItPathFree(from, to) == true) || (Math.abs(deltaX(from,to)) == 1 && Math.abs(deltaY(from,to)) == 2 && isItPathFree(from, to) == true)){//czy figura to skoczek, czy porusza sie jak skoczek, czy droga wolna
			isMoveAllowed = true;
		}else{
			System.out.println("B³¹d! Ruch skoczka niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}
	
	
	boolean pawnValidating(Coordinate from, Coordinate to) throws InvalidMoveException {
//		Coordinate fromPlus = from;
//		fromPlus.setXAndYPlusOne(from);	
		//by³o: if ((this.board.getPieceAt(to) == null && deltaY(from,to) == 1 && deltaX(from,to) == 0) || (this.board.getPieceAt(from).getColor() != calculateNextMoveColor()  && Math.abs(deltaX(from,to)) == Math.abs(deltaY(from,to)) && deltaY(from,to) == 1) || (from.getY() == 2 && to.getY() == 4 && to.getX() == from.getX() && this.board.getPieceAt(to) == null)){//        ||        ||        ||        czy figura to pion, capture po skosie lub attack na wprost
		//jest:
		if (this.board.getPieceAt(to) == null && deltaY(from,to) == 1 && deltaX(from,to) == 0){//        ||        ||        ||        czy figura to pion, capture po skosie lub attack na wprost
			isMoveAllowed = true;
		}
		
		Piece b444 = this.board.getPieceAt(from);
		Color ccc2 = calculateNextMoveColor();
//		Board ccc1 = this.board;
		Coordinate f = from;
		Color b44 = this.board.getPieceAt(from).getColor()  ;
		int b4444 = Math.abs(deltaX(from,to));
		int g = 3;
		
		if (this.board.getPieceAt(from).getColor() != calculateNextMoveColor()  && Math.abs(deltaX(from,to)) == Math.abs(deltaY(from,to)) && deltaY(from,to) == 1){
			isMoveAllowed = true;
		}
		if (from.getY() == 2 && to.getY() == 4 && to.getX() == from.getX() && this.board.getPieceAt(to) == null){
			isMoveAllowed = true;
		}
		if (isMoveAllowed = false){
			System.out.println("B³¹d! Ruch pionka niemo¿liwy do wykonania.");
		}
		return isMoveAllowed;
	}
	
	public boolean getIsThisMoveAlloved(){
		return isThisMoveAlloved;
	}
	
	Piece getMyPiece(Coordinate point){
		Piece myPiece = this.board.getPieceAt(point);
		return myPiece;
	}
	
	private Color calculateNextMoveColor() {
		if (this.board.getMoveHistory().size() % 2 == 0) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}
}
