package chess;

import jdk.jshell.EvalException;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();

        // direction: {1,1}
        int i = 1;
        int j = 1;
        while(true) {
            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() + j <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)) != null) {
                    ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)).getTeamColor();
                    if (myTeamColor != otherPieceColor) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                        System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() + j));
                    }
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                    System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() + j));
                    i++;
                    j++;
                }
            } else {
                break;
            }
        }

        // direction: {1,-1}
        i = 1;
        j = 1;
        while(true) {
            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() - j >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j)) != null) {
                    ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j)).getTeamColor();
                    if (myTeamColor != otherPieceColor) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j), null));
                        System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() - j));
                    }
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j), null));
                    System.out.println((myPosition.getRow() + i) + " " + (myPosition.getColumn() - j));
                    i++;
                    j++;
                }
            } else {
                break;
            }
        }

        // direction: {-1,1}
        i = 1;
        j = 1;
        while(true) {
            if (myPosition.getRow() - i >= 1 && myPosition.getColumn() + j <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j)) != null) {
                    ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j)).getTeamColor();
                    if (myTeamColor != otherPieceColor) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j), null));
                        System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() + j));
                    }
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j), null));
                    System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() + j));
                    i++;
                    j++;
                }
            } else {
                break;
            }
        }

        // direction: {-1,-1}
        i = 1;
        j = 1;
        while(true) {
            if (myPosition.getRow() - i >= 1 && myPosition.getColumn() - j >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j)) != null) {
                    ChessGame.TeamColor otherPieceColor = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j)).getTeamColor();
                    if (myTeamColor != otherPieceColor) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j), null));
                        System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() - j));
                    }
                    break;
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j), null));
                    System.out.println((myPosition.getRow() - i) + " " + (myPosition.getColumn() - j));
                    i++;
                    j++;
                }
            } else {
                break;
            }
        }
        return moves;
    }
}
