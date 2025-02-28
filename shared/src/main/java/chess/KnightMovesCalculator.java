package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myTeamColor = board.getPiece(myPosition).getTeamColor();

        // Possible moves for a knight (row, column)
        int[][] directions = {
                {1, 2}, {-1, 2}, {2, 1}, {2, -1},
                {1, -2}, {-2, -1}, {-2, 1}, {-1, -2}
        };

        for (int[] dir : directions) {
            int newRow = myPosition.getRow() + dir[0];
            int newCol = myPosition.getColumn() + dir[1];

            if (isValidPosition(newRow, newCol)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                if (pieceAtNewPosition == null || pieceAtNewPosition.getTeamColor() != myTeamColor) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        return moves;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
