package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        var myTeamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();
        boolean occupied = false;
        boolean sameTeam = false;

        //direction: {1,0} (White team)
        if (myTeamColor == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() + 1 <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())).getTeamColor();
                    occupied = true;
                    if (myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    if (myPosition.getRow() + 1 == 8) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));

                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));

                    } System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn()));
                }
            }
        }

        //direction: {-1,0} (Black team)
        occupied = false;
        sameTeam = false;
        if (myTeamColor == ChessGame.TeamColor.BLACK) {
            if (myPosition.getRow() - 1 >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())).getTeamColor();
                    occupied = true;
                    if (myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                }
                if (!occupied) {
                    if (myPosition.getRow() - 1 == 1) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));

                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));

                    } System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn()));
                }
            }
        }

        //initial move white
        if (myTeamColor == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() == 2) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null && board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn())) == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()), null));
                }
            }
        }

        //initial move black
        if (myTeamColor == ChessGame.TeamColor.BLACK) {
            if (myPosition.getRow() == 7) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null && board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn())) == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()), null));
                }
            }
        }

        //pawn capture white
        sameTeam = false;
        if (myTeamColor == ChessGame.TeamColor.WHITE) {
            //capture right side
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 1 <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor();
                    if (myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                    if (!sameTeam) {
                        if (myPosition.getRow() + 1 == 8) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));

                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));

                        } System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() + 1));
                    }
                }
            }

            //capture left side
            sameTeam = false;
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 1 >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1)).getTeamColor();
                    if (myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                    if (!sameTeam) {
                        if (myPosition.getRow() + 1 == 8) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));

                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));

                        } System.out.println((myPosition.getRow() + 1) + " " + (myPosition.getColumn() - 1));
                    }
                }
            }
        }

        //pawn capture black
        sameTeam = false;
        if (myTeamColor == ChessGame.TeamColor.BLACK) {
            //capture right side
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 1 <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1)).getTeamColor();
                    if (myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                    if (!sameTeam) {
                        if (myPosition.getRow() - 1 == 1) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));

                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));

                        } System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() + 1));
                    }
                }
            }

            //capture right side
            sameTeam = false;
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 1 >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)) != null) {
                    var otherTeamColor = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1)).getTeamColor();
                    if (myTeamColor == otherTeamColor) {
                        sameTeam = true;
                    }
                    if (!sameTeam) {
                        if (myPosition.getRow() - 1 == 1) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));

                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));

                        } System.out.println((myPosition.getRow() - 1) + " " + (myPosition.getColumn() - 1));
                    }
                }
            }
        }


        return moves;

    }
}
