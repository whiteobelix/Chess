package com.capgemini.chess.algorithms.data.enums;

/**
 * Board state
 * 
 * @author Michal Bejm
 *
 */
public enum BoardState {
	REGULAR, //normalny stan gry
	CHECK, //szach
	CHECK_MATE, //szach-mat
	STALE_MATE; //pat (sytuacja bez wyjœcia)
}
