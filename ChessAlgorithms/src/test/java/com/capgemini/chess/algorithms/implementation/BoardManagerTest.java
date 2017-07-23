package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

/**
 * Test class for testing {@link BoardManager}
 * 
 * @author Michal Bejm
 *
 */
public class BoardManagerTest {

	@Test
	public void shouldReturnTrueIfAllPiecesAreCorrectlyInitialized() {
		// given
		List<Move> moves = new ArrayList<>();
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		for (int x = 0; x < Board.SIZE; x++) {
			for (int y = 0; y < Board.SIZE; y++) {
				if (y > 1 && y < 6) {
					assertNull(boardManager.getBoard().getPieceAt(new Coordinate(x, y)));
				}
				else {
					assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(x, y)));
				}
			}
		}
		assertEquals(Piece.WHITE_PAWN, boardManager.getBoard().getPieceAt(new Coordinate(5, 1)));
		assertEquals(Piece.WHITE_KING, boardManager.getBoard().getPieceAt(new Coordinate(4, 0)));
		assertEquals(Piece.WHITE_BISHOP, boardManager.getBoard().getPieceAt(new Coordinate(5, 0)));
		assertEquals(Piece.BLACK_ROOK, boardManager.getBoard().getPieceAt(new Coordinate(0, 7)));
		assertEquals(Piece.BLACK_KNIGHT, boardManager.getBoard().getPieceAt(new Coordinate(1, 7)));
		assertEquals(Piece.BLACK_QUEEN, boardManager.getBoard().getPieceAt(new Coordinate(3, 7)));
		assertEquals(32, calculateNumberOfPieces(boardManager.getBoard()));
	}
	
	@Test
	public void shouldReturnTrueIfGeneratedBoardAttack() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new Move();
		move.setFrom(new Coordinate(5, 1));
		move.setTo(new Coordinate(5, 3));
		move.setType(MoveType.ATTACK);
		moves.add(move);
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(5, 1)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(5, 3)));
		assertEquals(32, calculateNumberOfPieces(boardManager.getBoard()));
	}
	
	@Test
	public void shouldReturnTrueIfGeneratedBoardCapture() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new Move();
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 6));
		move.setType(MoveType.CAPTURE);
		moves.add(move);
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(0, 0)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(0, 6)));
		assertEquals(31, calculateNumberOfPieces(boardManager.getBoard()));
	}
	
	@Test
	public void shouldReturnTrueIfGeneratedBoardCastling() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new Move();
		move.setFrom(new Coordinate(4, 0));
		move.setTo(new Coordinate(2, 0));
		move.setType(MoveType.CASTLING);
		moves.add(move);
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(4, 0)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 0)));
		assertEquals(Piece.WHITE_KING, boardManager.getBoard().getPieceAt(new Coordinate(2, 0)));
		assertEquals(Piece.WHITE_ROOK, boardManager.getBoard().getPieceAt(new Coordinate(3, 0)));
	}
	
	@Test
	public void shouldReturnTrueIfPerformedGeneratingBoardEnPassant() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move1 = new Move();
		move1.setFrom(new Coordinate(1, 1));
		move1.setTo(new Coordinate(1, 4));
		move1.setType(MoveType.ATTACK);
		moves.add(move1);
		Move move2 = new Move();
		move2.setFrom(new Coordinate(2, 6));
		move2.setTo(new Coordinate(2, 4));
		move2.setType(MoveType.ATTACK);
		moves.add(move2);
		Move move3 = new Move();
		move3.setFrom(new Coordinate(1, 4));
		move3.setTo(new Coordinate(2, 5));
		move3.setType(MoveType.EN_PASSANT);
		moves.add(move3);
		
		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 4)));
		assertNull(boardManager.getBoard().getPieceAt(new Coordinate(1, 4)));
		assertNotNull(boardManager.getBoard().getPieceAt(new Coordinate(2, 5)));
		assertEquals(Piece.WHITE_PAWN, boardManager.getBoard().getPieceAt(new Coordinate(2, 5)));
		assertEquals(31, calculateNumberOfPieces(boardManager.getBoard()));
	}
	
	@Test
	public void shouldReturnTrueIfPerformedMoveWithBoardPromotion() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move = new Move();
		move.setFrom(new Coordinate(1, 6));
		move.setTo(new Coordinate(1, 0));
		move.setType(MoveType.CAPTURE);
		moves.add(move);

		// when
		BoardManager boardManager = new BoardManager(moves);
		
		// then
		assertEquals(Piece.BLACK_QUEEN, boardManager.getBoard().getPieceAt(new Coordinate(1, 0)));
	}
	
	@Test
	public void shouldReturnTrueIfPerformedBishopAttack() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(0, 6));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(0, 6), new Coordinate(6, 0));
		
		// then
		assertEquals(MoveType.ATTACK, move.getType());
		assertEquals(Piece.WHITE_BISHOP, move.getMovedPiece());
	}
	
	@Test
	public void shouldReturnTrueIfPerformedPawnAttack() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(4, 6));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 6), new Coordinate(4, 4));
		
		// then
		assertEquals(MoveType.ATTACK, move.getType());
		assertEquals(Piece.BLACK_PAWN, move.getMovedPiece());
	}

	@Test
	public void shouldReturnTrueIfPerformedKingAttack() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 0), new Coordinate(4, 1));
		
		// then
		assertEquals(MoveType.ATTACK, move.getType());
		assertEquals(Piece.WHITE_KING, move.getMovedPiece());
	}
	
	@Test
	public void shouldReturnTrueIfPerformedKnightCapture() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 4));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(2, 6));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(3, 4), new Coordinate(2, 6));
		
		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.BLACK_KNIGHT, move.getMovedPiece());
	}
	
	@Test
	public void shouldReturnTrueIfPerformedQueenCapture() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(5, 0));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(7, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(5, 0), new Coordinate(7, 2));
		
		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.WHITE_QUEEN, move.getMovedPiece());
	}
	
	@Test
	public void shouldReturnTrueIfPerformedRookCapture() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(1, 4));
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(5, 4));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(1, 4), new Coordinate(5, 4));
		
		// then
		assertEquals(MoveType.CAPTURE, move.getType());
		assertEquals(Piece.BLACK_ROOK, move.getMovedPiece());
	}
	
	@Test
	public void shouldReturnTrueIfPerformedCastling() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(7, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		Move move = boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		
		// then
		assertEquals(MoveType.CASTLING, move.getType());
		assertEquals(Piece.WHITE_KING, move.getMovedPiece());
	}
	
	@Test
	public void shouldReturnTrueIfPerformedMoveEnPassant() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(1, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(2, 6));
		boardManager.performMove(new Coordinate(2, 6), new Coordinate(2, 4));
		
		// when
		Move move = boardManager.performMove(new Coordinate(1, 4), new Coordinate(2, 5));
		
		// then
		assertEquals(MoveType.EN_PASSANT, move.getType());
		assertEquals(Piece.WHITE_PAWN, move.getMovedPiece());
	}
	
	@Test
	public void shouldReturnTrueIfPerformedMoveWithInvalidIndexOutOfBound() {
		// given
		BoardManager boardManager = new BoardManager();
		
		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(8, 6), new Coordinate(7, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedMoveWithInvalidMoveOrder() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(0, 7));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(0, 7), new Coordinate(1, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedMoveWithInvalidEmptySpot() {
		// given
		Board board = new Board();
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(0, 7), new Coordinate(1, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedMoveWithInvalidSameSpot() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(0, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(0, 0), new Coordinate(0, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	
	@Test
	public void shouldReturnTrueIfPerformMoveWithInvalidPawnBackwardMove() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(1, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(1, 1));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformMoveWithInvalidPawnAttackDestination() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(1, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(0, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformMoveWithInvalidPawnAttackDistance() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(1, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(1, 4));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformMoveWithInvalidPawnCaptureDestination() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(1, 2));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(1, 3));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 2), new Coordinate(1, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformMoveWithInvalidKingDistance() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(4, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformMoveWithInvalidKnightDestination() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(1, 1));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(3, 3));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformMoveWithInvalidBishopDestination() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(1, 1));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(1, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedInvalidQueenLeapsOver() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(1, 1));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(4, 4));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(1, 1), new Coordinate(6, 6));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedInvalidRookLeapsOver() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(3, 0));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(3, 0), new Coordinate(3, 7));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedInvalidOwnPieceCapture() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KNIGHT, new Coordinate(5, 6));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 5));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(5, 6), new Coordinate(3, 5));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedInvalidCastlingPiecesMove() throws InvalidMoveException {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(7, 0));
		boardManager.performMove(new Coordinate(4, 0), new Coordinate(3, 0));
		board.getMoveHistory().add(createDummyMove(board));
		boardManager.performMove(new Coordinate(3, 0), new Coordinate(4, 0));
		board.getMoveHistory().add(createDummyMove(board));
		
		// when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedMoveInvalidCastlingWithPiecesBetween() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(7, 0));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(5, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedInvalidCastlingKingUnderCheck() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(7, 0));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(5, 7));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 0), new Coordinate(6, 0));
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedInvalidKingWouldChecking() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(4, 5));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(4, 7));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(new Coordinate(4, 5), new Coordinate(7, 2));
		} catch (InvalidMoveException e) {
			exceptionThrown = e instanceof KingInCheckException;
		}
		
		// then 
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedUpdateBoardStateRegular() throws InvalidMoveException {
		// given
		BoardManager boardManager = new BoardManager();
		
		// when
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.REGULAR, boardState);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedUpdateBoardStateCheck() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_BISHOP, new Coordinate(1, 3));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(4, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.CHECK, boardState);
	}
	
	@Test
	public void shouldReturnTrueIfBoardStateIsChangedToCheckMate() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 1));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(1, 0));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(4, 0));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.CHECK_MATE, boardState);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedUpdateBoardStateStaleMate() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.getMoveHistory().add(createDummyMove(board));
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(7, 0));
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(5, 1));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(6, 2));
		
		// when
		BoardManager boardManager = new BoardManager(board);
		BoardState boardState = boardManager.updateBoardState();
		
		// then
		assertEquals(BoardState.STALE_MATE, boardState);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedThreefoldRepetitionRuleSuccessful() {
		// given
		List<Move> moves = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Move move1 = new Move();
			move1.setFrom(new Coordinate(5, 1));
			move1.setTo(new Coordinate(5, 3));
			move1.setType(MoveType.ATTACK);
			moves.add(move1);
			
			Move move2 = new Move();
			move2.setFrom(new Coordinate(5, 6));
			move2.setTo(new Coordinate(5, 4));
			move2.setType(MoveType.ATTACK);
			moves.add(move2);
			
			Move move3 = new Move();
			move3.setFrom(new Coordinate(5, 3));
			move3.setTo(new Coordinate(5, 1));
			move3.setType(MoveType.ATTACK);
			moves.add(move3);
			
			Move move4 = new Move();
			move4.setFrom(new Coordinate(5, 4));
			move4.setTo(new Coordinate(5, 6));
			move4.setType(MoveType.ATTACK);
			moves.add(move4);
		}
		BoardManager boardManager = new BoardManager(moves);
		
		// when
		boolean isThreefoldRepetition = boardManager.checkThreefoldRepetitionRule();
		
		// then
		assertTrue(isThreefoldRepetition);
	}
	
	@Test
	public void shouldReturnFalseIfPerformedThreefoldRepetitionRuleUnsuccessful() {
		// given
		List<Move> moves = new ArrayList<>();
		Move move1 = new Move();
		move1.setFrom(new Coordinate(5, 1));
		move1.setTo(new Coordinate(5, 3));
		move1.setType(MoveType.ATTACK);
		moves.add(move1);
		
		Move move2 = new Move();
		move2.setFrom(new Coordinate(5, 6));
		move2.setTo(new Coordinate(5, 4));
		move2.setType(MoveType.ATTACK);
		moves.add(move2);
		
		Move move3 = new Move();
		move3.setFrom(new Coordinate(5, 3));
		move3.setTo(new Coordinate(5, 1));
		move3.setType(MoveType.ATTACK);
		moves.add(move3);
		
		Move move4 = new Move();
		move4.setFrom(new Coordinate(5, 4));
		move4.setTo(new Coordinate(5, 6));
		move4.setType(MoveType.ATTACK);
		moves.add(move4);
		BoardManager boardManager = new BoardManager(moves);
		
		// when
		boolean isThreefoldRepetition = boardManager.checkThreefoldRepetitionRule();
		
		// then
		assertFalse(isThreefoldRepetition);
	}
	
	@Test
	public void shouldReturnTrueIfPerformedCheckFiftyMoveRuleSuccessful() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		for (int i = 0; i < 100; i++) {
			board.getMoveHistory().add(createDummyMove(board));
		}
			
		// when
		boolean areFiftyMoves = boardManager.checkFiftyMoveRule();
		
		// then
		assertTrue(areFiftyMoves);
	}
	
	@Test
	public void shouldReturnFalseIfPerformedCheckFiftyMoveRuleUnsuccessfulNotEnoughMoves() {
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		for (int i = 0; i < 99; i++) {
			board.getMoveHistory().add(createDummyMove(board));
		}
			
		// when
		boolean areFiftyMoves = boardManager.checkFiftyMoveRule();
		
		// then
		assertFalse(areFiftyMoves);
	}
	
	@Test
	public void shouldReturnFalseIfPerformedCheckFiftyMoveRuleUnsuccessfulPawnMoved() {
		// given
		BoardManager boardManager = new BoardManager(new Board());
		
		Move move = new Move();
		boardManager.getBoard().setPieceAt(Piece.WHITE_PAWN, new Coordinate(0, 0));
		move.setMovedPiece(Piece.WHITE_PAWN);
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 0));
		move.setType(MoveType.ATTACK);
		boardManager.getBoard().setPieceAt(null, new Coordinate(0, 0));
		boardManager.getBoard().getMoveHistory().add(move);
		
		for (int i = 0; i < 99; i++) {
			boardManager.getBoard().getMoveHistory().add(createDummyMove(boardManager.getBoard()));
		}
			
		// when
		boolean areFiftyMoves = boardManager.checkFiftyMoveRule();
		
		// then
		assertFalse(areFiftyMoves);
	}
	
	private Move createDummyMove(Board board) {
		
		Move move = new Move();
		
		if (board.getMoveHistory().size() % 2 == 0) {
			board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
			move.setMovedPiece(Piece.WHITE_ROOK);
		}
		else {
			board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 0));
			move.setMovedPiece(Piece.BLACK_ROOK);
		}
		move.setFrom(new Coordinate(0, 0));
		move.setTo(new Coordinate(0, 0));
		move.setType(MoveType.ATTACK);
		board.setPieceAt(null, new Coordinate(0, 0));
		return move;
	}

	private int calculateNumberOfPieces(Board board) {
		int counter = 0;
		for (int x = 0; x < Board.SIZE; x++) {
			for (int y = 0; y < Board.SIZE; y++) {
				if (board.getPieceAt(new Coordinate(x, y)) != null) {
					counter++;
				}
			}
		}
		return counter;
	}
}
