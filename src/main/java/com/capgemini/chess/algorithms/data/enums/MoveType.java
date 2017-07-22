package com.capgemini.chess.algorithms.data.enums;

/**
 * Types of moves
 * 
 * @author Michal Bejm
 *
 */
public enum MoveType {
	ATTACK, // normalny ruch bez bicia
	CAPTURE, // bicie
	CASTLING, // roszada (mog¹: pion i król-wie¿a)
	EN_PASSANT,
	CHECK; // bicie pionka przez pionka w przelocie
}
