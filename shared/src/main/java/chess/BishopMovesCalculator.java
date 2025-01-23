package chess;

import jdk.jshell.EvalException;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        var myTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();
        boolean sameTeam;
        var occupied = false;
        int i = 1;

        //check direction {1,1]
        if (myPosition.getRow() != 8 && myPosition.getColumn() != 8) {
            while (myPosition.getRow() + i <= 8 && myPosition.getColumn() + i <= 8 && !occupied) {
                if(board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i)) != null) {
                    var colorToCapture = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i)).getTeamColor();
                    sameTeam = colorToCapture == myTeamColor;
                    if (sameTeam) {
                        break;
                    }
                }
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), null));
                i++;
                if (myPosition.getRow() + i !=8 && myPosition.getColumn() + i != 8) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i)) != null) {
                        var colorToCapture = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i)).getTeamColor();
                        sameTeam = colorToCapture == myTeamColor;
                        if (sameTeam) {
                            occupied = true;
                        }
                        else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), null));
                            occupied = true;
                        }
                    }
                }
            }
        }

        //check direction {-1,1]
        i = 1;
        int j = 1;
        occupied = false;
        if (myPosition.getRow() != 1 && myPosition.getColumn() != 8) {
            while (myPosition.getRow() - i >= 1 && myPosition.getColumn() + j  <= 8 && !occupied) {
                if(board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j)) != null) {
                    var colorToCapture = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j)).getTeamColor();
                    sameTeam = colorToCapture == myTeamColor;
                    if (sameTeam) {
                        break;
                    }
                }
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j), null));
                i++;
                j++;
                if (myPosition.getRow() - i !=0 && myPosition.getColumn() + j != 8) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j)) != null) {
                        var colorToCapture = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j)).getTeamColor();
                        sameTeam = colorToCapture == myTeamColor;
                        if (sameTeam) {
                            occupied = true;
                        }
                        else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + j), null));
                            occupied = true;
                        }
                    }
                }
            }
        }

        //check direction {-1,-1]
        i = 1;
        j = 1;
        occupied = false;
        if (myPosition.getRow() != 1 && myPosition.getColumn() != 1) {
            while (myPosition.getRow() - i >= 1 && myPosition.getColumn() - j  >= 1 && !occupied) {
                if(board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j)) != null) {
                    var colorToCapture = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j)).getTeamColor();
                    sameTeam = colorToCapture == myTeamColor;
                    if (sameTeam) {
                        break;
                    }
                }
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j), null));
                i++;
                j++;
                if (myPosition.getRow() - i !=0 && myPosition.getColumn() - j != 0) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j)) != null) {
                        var colorToCapture = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j)).getTeamColor();
                        sameTeam = colorToCapture == myTeamColor;
                        if (sameTeam) {
                            occupied = true;
                        }
                        else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - j), null));
                            occupied = true;
                        }
                    }
                }
            }
        }

        //check direction {1,-1]
        i = 1;
        j = 1;
        occupied = false;
        if (myPosition.getRow() != 8 && myPosition.getColumn() != 1) {
            while (myPosition.getRow() + i <= 8 && myPosition.getColumn() - j >= 1 && !occupied) {
                if(board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j)) != null) {
                    var colorToCapture = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j)).getTeamColor();
                    sameTeam = colorToCapture == myTeamColor;
                    if (sameTeam) {
                        break;
                    }
                }
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j), null));
                i++;
                j++;
                if (myPosition.getRow() + i !=8 && myPosition.getColumn() - j != 0) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j)) != null) {
                        var colorToCapture = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j)).getTeamColor();
                        sameTeam = colorToCapture == myTeamColor;
                        if (sameTeam) {
                            occupied = true;
                        }
                        else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - j), null));
                            occupied = true;
                        }
                    }
                }
            }
        }


        return moves;
    }

}
