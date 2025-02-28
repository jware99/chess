package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        var myTeamColor = board.getPiece(myPosition).getTeamColor();

        // Possible directions for the king (row offset, column offset)
        int[][] directions = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        for (int[] direction : directions) {
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];

            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                var pieceAtNewPos = board.getPiece(newPosition);

                if (pieceAtNewPos == null || pieceAtNewPos.getTeamColor() != myTeamColor) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                    System.out.println(newRow + " " + newCol);
                }
            }
        }

        return moves;
    }
}
