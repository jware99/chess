package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 3;
    private static final String EMPTY = "   ";

    /**
     * Creates and displays a chess board with the current game state
     *
     * @param chessGame the current chess game containing the board state
     * @param teamColor the perspective to view the board from (WHITE or BLACK)
     */
    public static void displayBoard(chess.ChessGame chessGame, ChessGame.TeamColor teamColor, boolean highlight, ChessPosition position) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawColumnHeaders(out, teamColor);

        if (highlight) {
            Collection<ChessMove> movesToHighlight = chessGame.validMoves(position);
            drawChessBoard(out, chessGame.getBoard(), teamColor, movesToHighlight);
        } else {
            drawChessBoard(out, chessGame.getBoard(), teamColor, null);
        }

        drawColumnHeaders(out, teamColor);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawColumnHeaders(PrintStream out, ChessGame.TeamColor teamColor) {
        setBlack(out);
        System.out.print("   ");
        String[] headers;
        if (teamColor == ChessGame.TeamColor.WHITE || teamColor == null) {
            headers = new String[]{" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "};
        } else {
            headers = new String[]{" H ", " G ", " F ", " E ", " D ", " C ", " B ", " A "};
        }
        for (String header : headers) {
            drawHeader(out, header);
        }
        System.out.println();
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
            printTopBottomHeaders(row, teamColor, squareRow);
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean isWhiteSquare = (row + col) % 2 == 0;

                ChessPosition newPosition = new ChessPosition(squareRow, col);

                if (!validMoves.isEmpty()) {
                    for (ChessMove move : validMoves) {
                        if (move.getEndPosition() == newPosition) {
                            setSquareColor(out, isWhiteSquare, true);
                        }
                    }
                } else {
                    setSquareColor(out, isWhiteSquare, false);
                }


                if (squareRow == SQUARE_SIZE / 2) {
                    int actualRow, actualCol;

                    if (teamColor == ChessGame.TeamColor.WHITE || teamColor == null) {
                        actualRow = 8 - row;
                        actualCol = col + 1;
                    } else {
                        actualRow = row + 1;
                        actualCol = 8 - col;
                    }

                    ChessPosition position = new ChessPosition(actualRow, actualCol);
                    ChessPiece piece = board.getPiece(position);

                    if (piece != null) {
                        printPiece(out, getPieceSymbol(piece));
                    } else {
                        out.print(EMPTY.repeat(SQUARE_SIZE));
                    }
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE));
                }
            }

            setBlack(out);
            printTopBottomHeaders(row, teamColor, squareRow);
            System.out.println();
        }
    }

    private static String getPieceSymbol(ChessPiece piece) {
        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();

        if (color == ChessGame.TeamColor.WHITE) {
            switch (type) {
                case KING: return WHITE_KING;
                case QUEEN: return WHITE_QUEEN;
                case BISHOP: return WHITE_BISHOP;
                case KNIGHT: return WHITE_KNIGHT;
                case ROOK: return WHITE_ROOK;
                case PAWN: return WHITE_PAWN;
                default: return " ? ";
            }
        } else {
            switch (type) {
                case KING: return BLACK_KING;
                case QUEEN: return BLACK_QUEEN;
                case BISHOP: return BLACK_BISHOP;
                case KNIGHT: return BLACK_KNIGHT;
                case ROOK: return BLACK_ROOK;
                case PAWN: return BLACK_PAWN;
                default: return " ? ";
            }
        }
    }

    private static void printTopBottomHeaders(int row, ChessGame.TeamColor teamColor, int squareRow) {
        if (squareRow == SQUARE_SIZE / 2) {
            if (teamColor == ChessGame.TeamColor.WHITE || teamColor == null) {
                System.out.print(" " + (8 - row) + " ");
            } else {
                System.out.print(" " + (row + 1) + " ");
            }
        } else {
            System.out.print("   ");
        }
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