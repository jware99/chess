package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myTeamColor = board.getPiece(myPosition).getTeamColor();

        // Possible move directions for a rook: {row offset, column offset}
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] direction : directions) {
            int rowOffset = direction[0], colOffset = direction[1];
            int newRow = myPosition.getRow() + rowOffset;
            int newCol = myPosition.getColumn() + colOffset;

            while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtNewPos = board.getPiece(newPosition);

                if (pieceAtNewPos != null) {
                    if (pieceAtNewPos.getTeamColor() != myTeamColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }

                moves.add(new ChessMove(myPosition, newPosition, null));

                newRow += rowOffset;
                newCol += colOffset;
            }
        }

        return moves;
    }
}
