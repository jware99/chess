package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 3;
    private static final String EMPTY = "   ";

    public static void displayBoard(chess.ChessGame chessGame, ChessGame.TeamColor teamColor, boolean highlight, ChessPosition position) throws InvalidMoveException {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawColumnHeaders(out, teamColor);

        Collection<ChessMove> movesToHighlight = getMovesToHighlight(chessGame, highlight, position);

        drawChessBoard(out, chessGame.getBoard(), teamColor, movesToHighlight);

        drawColumnHeaders(out, teamColor);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static Collection<ChessMove> getMovesToHighlight(chess.ChessGame chessGame, boolean highlight, ChessPosition position) throws InvalidMoveException {
        if (highlight && position != null) {
            ChessPiece piece = chessGame.getBoard().getPiece(position);
            if (piece != null) {
                return chessGame.validMoves(position);
            }
        }
        return null;
    }

    private static void drawColumnHeaders(PrintStream out, ChessGame.TeamColor teamColor) {
        setBlack(out);
        System.out.print("   ");
        String[] headers = getColumnHeaders(teamColor);
        for (String header : headers) {
            drawHeader(out, header);
        }
        System.out.println();
    }

    private static String[] getColumnHeaders(ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.WHITE || teamColor == null) {
            return new String[]{" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "};
        } else {
            return new String[]{" H ", " G ", " F ", " E ", " D ", " C ", " B ", " A "};
        }
    }

    private static void drawHeader(PrintStream out, String header) {
        int prefixLength = SQUARE_SIZE / 2;
        int suffixLength = SQUARE_SIZE - prefixLength - 1;
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, header);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String text) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(text);
        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out, chess.ChessBoard board, ChessGame.TeamColor teamColor, Collection<ChessMove> validMoves) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            drawRow(out, row, board, teamColor, validMoves);
        }
    }

    private static void drawRow(PrintStream out, int row, chess.ChessBoard board, ChessGame.TeamColor teamColor, Collection<ChessMove> validMoves) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE; squareRow++) {
            printRowStart(out, row, teamColor, squareRow);
            drawSquaresInRow(out, row, squareRow, board, teamColor, validMoves);
            printRowEnd(out, row, teamColor, squareRow);
            System.out.println();
        }
    }

    private static void printRowStart(PrintStream out, int row, ChessGame.TeamColor teamColor, int squareRow) {
        if (squareRow == SQUARE_SIZE / 2) {
            printRowNumber(out, row, teamColor);
        } else {
            System.out.print("   ");
        }
    }

    private static void printRowEnd(PrintStream out, int row, ChessGame.TeamColor teamColor, int squareRow) {
        setBlack(out);
        if (squareRow == SQUARE_SIZE / 2) {
            printRowNumber(out, row, teamColor);
        } else {
            System.out.print("   ");
        }
    }

    private static void printRowNumber(PrintStream out, int row, ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.WHITE || teamColor == null) {
            System.out.print(" " + (8 - row) + " ");
        } else {
            System.out.print(" " + (row + 1) + " ");
        }
    }

    private static void drawSquaresInRow(PrintStream out, int row, int squareRow, chess.ChessBoard board,
                                         ChessGame.TeamColor teamColor, Collection<ChessMove> validMoves) {
        for (int col = 0; col < BOARD_SIZE; col++) {
            ChessPosition position = calculatePosition(row, col, teamColor);
            boolean isWhiteSquare = (row + col) % 2 == 0;
            boolean isHighlighted = isPositionHighlighted(position, validMoves);

            drawSquare(out, isWhiteSquare, isHighlighted, squareRow, board, position);
        }
    }

    private static ChessPosition calculatePosition(int row, int col, ChessGame.TeamColor teamColor) {
        int actualRow, actualCol;
        if (teamColor == ChessGame.TeamColor.WHITE || teamColor == null) {
            actualRow = 8 - row;
            actualCol = col + 1;
        } else {
            actualRow = row + 1;
            actualCol = 8 - col;
        }
        return new ChessPosition(actualRow, actualCol);
    }

    private static boolean isPositionHighlighted(ChessPosition position, Collection<ChessMove> validMoves) {
        if (validMoves == null) {
            return false;
        }

        for (ChessMove move : validMoves) {
            if (move.getEndPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    private static void drawSquare(PrintStream out, boolean isWhiteSquare, boolean isHighlighted,
                                   int squareRow, chess.ChessBoard board, ChessPosition position) {
        setSquareColor(out, isWhiteSquare, isHighlighted);

        if (squareRow == SQUARE_SIZE / 2) {
            drawPieceOrEmpty(out, board, position);
        } else {
            out.print(EMPTY.repeat(SQUARE_SIZE));
        }
    }

    private static void drawPieceOrEmpty(PrintStream out, chess.ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        if (piece != null) {
            printPiece(out, getPieceSymbol(piece));
        } else {
            out.print(EMPTY.repeat(SQUARE_SIZE));
        }
    }

    private static String getPieceSymbol(ChessPiece piece) {
        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();

        if (color == ChessGame.TeamColor.WHITE) {
            return getWhitePieceSymbol(type);
        } else {
            return getBlackPieceSymbol(type);
        }
    }

    private static String getWhitePieceSymbol(ChessPiece.PieceType type) {
        return getString(type, WHITE_KING, WHITE_QUEEN, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK, WHITE_PAWN);
    }

    private static String getString(
            ChessPiece.PieceType type,
            String whiteKing,
            String whiteQueen,
            String whiteBishop,
            String whiteKnight,
            String whiteRook,
            String whitePawn) {
        switch (type) {
            case KING: return whiteKing;
            case QUEEN: return whiteQueen;
            case BISHOP: return whiteBishop;
            case KNIGHT: return whiteKnight;
            case ROOK: return whiteRook;
            case PAWN: return whitePawn;
            default: return " ? ";
        }
    }

    private static String getBlackPieceSymbol(ChessPiece.PieceType type) {
        return getString(type, BLACK_KING, BLACK_QUEEN, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK, BLACK_PAWN);
    }

    private static void printPiece(PrintStream out, String piece) {
        out.print(EMPTY.repeat(SQUARE_SIZE / 2));
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(piece);
        out.print(EMPTY.repeat(SQUARE_SIZE / 2));
    }

    private static void setSquareColor(PrintStream out, boolean isWhite, boolean highlight) {
        if (highlight) {
            out.print(SET_BG_COLOR_MAGENTA);
        } else if (isWhite) {
            out.print(SET_BG_COLOR_WHITE);
        } else {
            out.print(SET_BG_COLOR_LIGHT_GREY);
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
}