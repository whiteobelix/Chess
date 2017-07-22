package com.capgemini.chess.algorithms.data.enums;

/**
 * Chess piece types
 * 
 * @author Michal Bejm
 *
 */
public enum PieceType {
	KING, //król (o 1 pole)
    QUEEN, //królowa (tak jak wieza i goniec)
    BISHOP, //goniec (po skosie)
    KNIGHT, //skoczek (koñ)
    ROOK, //wieza (do przodu lub w bok
    PAWN; //pion (do przodu, atakuje po skosie)
}
