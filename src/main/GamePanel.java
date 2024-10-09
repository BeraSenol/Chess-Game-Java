package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

import piece.*;
import piece.pieces.*;

public class GamePanel extends JPanel implements Runnable {

        // VARIABLES - CONSTANTS
        private final int FPS = 120;
        private final int PIECE_RANK_BLACK = 0;
        private final int PAWN_RANK_BLACK = 1;
        private final int PAWN_RANK_WHITE = 6;
        private final int PIECE_RANK_WHITE = 7;
        private static final int WINDOW_WIDTH = 1280;
        private static final int WINDOW_HEIGHT = 800;
        private final double DRAW_INTERVAL = 1000000000 / FPS;
        private final float ALPHA_ONE = 1f;
        private final float ALPHA_LIGHT = 0.4f;
        private final Color BACKGROUND_COLOR = new Color(48, 46, 43);
        private final ArrayList<Piece> PROMOTION_PIECES = new ArrayList<>();
        private final static ArrayList<Piece> INITIAL_PIECES = new ArrayList<>();
        private final PieceColor WHITE = PieceColor.WHITE;
        private final PieceColor BLACK = PieceColor.BLACK;
        private final Board BOARD = new Board();
        private final Mouse MOUSE = new Mouse();

        // VARIABLES - BOOLEANS
        private boolean isPieceMovable;
        private boolean isPiecePromoted;
        private boolean isValidTile;
        private boolean isGameOver;

        // VARIABLES - PIECES
        private Piece selectedPiece;
        private Piece checkingPiece;
        public static Piece castlingPiece;
        public static ArrayList<Piece> simPieces = new ArrayList<>();
        private PieceColor currentTurnColor = WHITE;

        // VARIABLES - OTHER
        private Thread gThread;

        // CONSTRUCTOR
        public GamePanel() {
                setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
                setBackground(BACKGROUND_COLOR);
                addMouseListener(MOUSE);
                addMouseMotionListener(MOUSE);
                createPieces();
                copyPieces(INITIAL_PIECES, simPieces);
        }

        // METHODS - BOOLEAN
        private boolean isPromotable() {
                if (selectedPiece.pieceType != PieceType.PAWN) {
                        return false;
                }
                if (selectedPiece.currentRank == PIECE_RANK_WHITE || selectedPiece.currentRank == PIECE_RANK_BLACK) {
                        PROMOTION_PIECES.clear();
                        PROMOTION_PIECES.add(new Queen(currentTurnColor, 10, 2));
                        PROMOTION_PIECES.add(new Knight(currentTurnColor, 10, 3));
                        PROMOTION_PIECES.add(new Rook(currentTurnColor, 10, 4));
                        PROMOTION_PIECES.add(new Bishop(currentTurnColor, 10, 5));
                        return true;
                }
                return false;
        }

        private boolean isIllegalMove(Piece king) {
                if (king.pieceType != PieceType.KING) {
                        return false;
                }
                for (Piece piece : simPieces) {
                        if (piece != king && piece.color != king.color
                                        && piece.isMovable(king.currentFile, king.currentRank)) {
                                return true;
                        }
                }
                return false;
        }

        private boolean isKingInCheckNextTurn() {
                Piece king = getKing(false);
                for (Piece piece : simPieces) {
                        if (piece.color != king.color && piece.isMovable(king.currentFile, king.currentRank)) {
                                return true;
                        }
                }
                return false;
        }

        private boolean isKingInCheck() {
                Piece king = getKing(true);
                if (selectedPiece.isMovable(king.currentFile, king.currentRank)) {
                        checkingPiece = selectedPiece;
                        return true;
                }
                checkingPiece = null;
                return false;
        }

        private boolean isCheckMate() {
                Piece king = getKing(true);
                if (isKingMoveable(king)) {
                        return false;
                } else {
                        // Check if attack can be blocked by one of your own pieces
                        int colummDistance = Math.abs(checkingPiece.currentFile - king.currentFile);
                        int RankDistance = Math.abs(checkingPiece.currentRank - king.currentRank);
                        if (colummDistance == 0) {
                                // Checking piece attacks vertically
                                if (checkingPiece.currentRank < king.currentRank) {
                                        for (int i = checkingPiece.currentRank; i < king.currentRank; i++) {
                                                for (Piece piece : simPieces) {
                                                        if (piece != king && piece.color != currentTurnColor && piece
                                                                        .isMovable(checkingPiece.currentFile, i)) {
                                                                return false;
                                                        }
                                                }
                                        }
                                }
                                if (checkingPiece.currentRank < king.currentRank) {
                                        for (int i = checkingPiece.currentRank; i > king.currentRank; i--) {
                                                for (Piece piece : simPieces) {
                                                        if (piece != king && piece.color != currentTurnColor && piece
                                                                        .isMovable(checkingPiece.currentFile, i)) {
                                                                return false;
                                                        }
                                                }
                                        }
                                }
                        } else if (RankDistance == 0) {
                                // Checking piece attacks horizontally
                                if (checkingPiece.currentFile < king.currentFile) {
                                        for (int i = checkingPiece.currentFile; i < king.currentFile; i++) {
                                                for (Piece piece : simPieces) {
                                                        if (piece != king && piece.color != currentTurnColor && piece
                                                                        .isMovable(i, checkingPiece.currentFile)) {
                                                                return false;
                                                        }
                                                }
                                        }
                                }
                                if (checkingPiece.currentFile < king.currentFile) {
                                        for (int i = checkingPiece.currentFile; i > king.currentFile; i--) {
                                                for (Piece piece : simPieces) {
                                                        if (piece != king && piece.color != currentTurnColor && piece
                                                                        .isMovable(i, checkingPiece.currentFile)) {
                                                                return false;
                                                        }
                                                }
                                        }
                                }
                        } else if (colummDistance == RankDistance) {
                                // Checking piece attacks diagonally
                                if (checkingPiece.currentRank < king.currentRank) {
                                        if (checkingPiece.currentFile < king.currentFile) {
                                                for (int i = checkingPiece.currentFile,
                                                                j = checkingPiece.currentRank; i < king.currentFile; i++, j++) {
                                                        for (Piece piece : simPieces) {
                                                                if (piece != king && piece.color != currentTurnColor
                                                                                && piece.isMovable(i, j)) {
                                                                        return false;
                                                                }
                                                        }
                                                }
                                        }
                                        if (checkingPiece.currentFile > king.currentFile) {
                                                for (int i = checkingPiece.currentFile,
                                                                j = checkingPiece.currentRank; i > king.currentFile; i--, j++) {
                                                        for (Piece piece : simPieces) {
                                                                if (piece != king && piece.color != currentTurnColor
                                                                                && piece.isMovable(i, j)) {
                                                                        return false;
                                                                }
                                                        }
                                                }
                                        }
                                }
                                if (checkingPiece.currentRank > king.currentRank) {
                                        if (checkingPiece.currentFile < king.currentFile) {
                                                for (int i = checkingPiece.currentFile,
                                                                j = checkingPiece.currentRank; i < king.currentFile; i++, j--) {
                                                        for (Piece piece : simPieces) {
                                                                if (piece != king && piece.color != currentTurnColor
                                                                                && piece.isMovable(i, j)) {
                                                                        return false;
                                                                }
                                                        }
                                                }
                                        }
                                        if (checkingPiece.currentFile > king.currentFile) {
                                                for (int i = checkingPiece.currentFile,
                                                                j = checkingPiece.currentRank; i > king.currentFile; i--, j--) {
                                                        for (Piece piece : simPieces) {
                                                                if (piece != king && piece.color != currentTurnColor
                                                                                && piece.isMovable(i, j)) {
                                                                        return false;
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
                return true;
        }

        private boolean isKingMoveable(Piece king) {
                for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                                if (i != 0 || j != 0) {
                                        if (isValidMove(king, i, j)) {
                                                return true;
                                        }
                                }
                        }
                }
                return false;
        }

        private boolean isValidMove(Piece king, int colPlus, int RankPlus) {
                boolean isValidMove = false;
                int targetcurrentFile = king.currentFile + colPlus;
                int targetcurrentRank = king.currentRank + RankPlus;
                if (king.isMovable(targetcurrentFile, targetcurrentRank)) {
                        if (king.collidingPiece != null) {
                                simPieces.remove(king.collidingPiece.getIndex());
                        }
                        if (!isIllegalMove(king)) {
                                isValidMove = true;
                        }
                }
                copyPieces(INITIAL_PIECES, simPieces);
                return isValidMove;
        }

        // METHODS - VOID
        private void createPieces() {
                for (int i = PIECE_RANK_BLACK; i <= PAWN_RANK_WHITE; i++) {
                        INITIAL_PIECES.add(new Pawn(WHITE, i, PAWN_RANK_WHITE));
                        INITIAL_PIECES.add(new Pawn(BLACK, i, PAWN_RANK_BLACK));
                        switch (i) {
                                case 0, 7 -> {
                                        INITIAL_PIECES.add(new Rook(WHITE, i, PIECE_RANK_WHITE));
                                        INITIAL_PIECES.add(new Rook(BLACK, i, PIECE_RANK_BLACK));
                                }
                                case 1, 6 -> {
                                        INITIAL_PIECES.add(new Knight(WHITE, i, PIECE_RANK_WHITE));
                                        INITIAL_PIECES.add(new Knight(BLACK, i, PIECE_RANK_BLACK));
                                }
                                case 2, 5 -> {
                                        INITIAL_PIECES.add(new Bishop(WHITE, i, PIECE_RANK_WHITE));
                                        INITIAL_PIECES.add(new Bishop(BLACK, i, PIECE_RANK_BLACK));
                                }

                                case 3 -> {
                                        INITIAL_PIECES.add(new Queen(WHITE, i, PIECE_RANK_WHITE));
                                        INITIAL_PIECES.add(new Queen(BLACK, i, PIECE_RANK_BLACK));
                                }

                                case 4 -> {
                                        INITIAL_PIECES.add(new King(WHITE, i, PIECE_RANK_WHITE));
                                        INITIAL_PIECES.add(new King(BLACK, i, PIECE_RANK_BLACK));
                                }
                        }
                }
        }

        private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
                target.clear();
                for (int i = 0; i < source.size(); i++) {
                        target.add(source.get(i));
                }
        }

        private void checkCastling() {
                if (castlingPiece != null) {
                        if (castlingPiece.currentFile == 0) {
                                castlingPiece.currentFile += 3;
                        } else if (castlingPiece.currentFile == 7) {
                                castlingPiece.currentFile -= 2;
                        }
                        castlingPiece.x = castlingPiece.getX(castlingPiece.currentFile);
                }
        }

        private void PromotePiece() {
                if (MOUSE.pressed) {
                        for (Piece piece : PROMOTION_PIECES) {
                                if (piece.currentFile == MOUSE.x / Board.TILE_SIZE
                                                && piece.currentRank == MOUSE.y / Board.TILE_SIZE) {
                                        switch (piece.pieceType) {
                                                case QUEEN -> simPieces.add(new Queen(currentTurnColor,
                                                                selectedPiece.currentFile, selectedPiece.currentRank));
                                                case KNIGHT -> simPieces.add(new Knight(currentTurnColor,
                                                                selectedPiece.currentFile, selectedPiece.currentRank));
                                                case ROOK -> simPieces.add(new Rook(currentTurnColor,
                                                                selectedPiece.currentFile, selectedPiece.currentRank));
                                                case BISHOP -> simPieces.add(new Bishop(currentTurnColor,
                                                                selectedPiece.currentFile, selectedPiece.currentRank));
                                                default -> {
                                                }
                                        }
                                        simPieces.remove(selectedPiece.getIndex());
                                        copyPieces(simPieces, INITIAL_PIECES);
                                        selectedPiece = null;
                                        isPiecePromoted = false;
                                        endTurn();
                                }
                        }
                }
        }

        protected void startGame() {
                gThread = new Thread(this);
                gThread.start();
        }

        private void endTurn() {
                if (currentTurnColor == WHITE) {
                        currentTurnColor = BLACK;
                        // Reset hasTwoStepped for whiteColor
                        for (Piece piece : INITIAL_PIECES) {
                                if (piece.color == BLACK) {
                                        piece.hasTwoStepped = false;
                                }
                        }
                } else {
                        currentTurnColor = WHITE;
                        // Reset hasTwoStepped for Black
                        for (Piece piece : INITIAL_PIECES) {
                                if (piece.color == WHITE) {
                                        piece.hasTwoStepped = false;
                                }
                        }
                }
                selectedPiece = null;
        }

        private void setGraphics(Graphics2D graphics2D, Color color) {
                graphics2D.setColor(color);
                graphics2D.setComposite(
                                AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                ALPHA_LIGHT));
                graphics2D.fillRect(selectedPiece.currentFile * Board.TILE_SIZE,
                                selectedPiece.currentRank * Board.TILE_SIZE, Board.TILE_SIZE,
                                Board.TILE_SIZE);
                graphics2D.setComposite(
                                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_ONE));
        }

        // METHODS - PIECE
        private Piece getKing(boolean isOpponent) {
                Piece king = null;
                for (Piece piece : simPieces) {
                        if (isOpponent) {
                                if (piece.pieceType == PieceType.KING && piece.color != currentTurnColor) {
                                        king = piece;
                                }
                        } else if (piece.pieceType == PieceType.KING && piece.color == currentTurnColor) {
                                king = piece;
                        }
                }
                return king;
        }

        // METHODS - GAME LOOP
        @Override
        public void run() {
                double deltaTime = 0;
                long lastTime = System.nanoTime();
                long currentTime;
                while (gThread != null) {
                        currentTime = System.nanoTime();
                        deltaTime += (currentTime - lastTime) / DRAW_INTERVAL;
                        lastTime = currentTime;
                        if (deltaTime >= 1) {
                                update();
                                repaint();
                                deltaTime--;
                        }
                }
        }

        private void update() {
                if (isPiecePromoted) {
                        PromotePiece();
                } else if (!isGameOver) {
                        if (MOUSE.pressed) {
                                // if player isn't already holding a piece
                                if (selectedPiece == null) {
                                        for (Piece piece : simPieces) {
                                                // check if piece is current players's piece & mouse hovers over piece's
                                                // tile
                                                if (piece.color == currentTurnColor
                                                                && piece.currentFile == (MOUSE.x / Board.TILE_SIZE)
                                                                && piece.currentRank == (MOUSE.y / Board.TILE_SIZE)) {
                                                        selectedPiece = piece;
                                                }
                                        }
                                } else {
                                        simulate();
                                }
                        }
                        if (!MOUSE.pressed) {
                                if (selectedPiece != null) {
                                        if (isValidTile) {
                                                // MOVE CONFIRMED
                                                // In case a piece has been captured, update the list and get removed
                                                // from simulation
                                                copyPieces(simPieces, INITIAL_PIECES);
                                                selectedPiece.updatePosition();

                                                if (castlingPiece != null) {
                                                        castlingPiece.updatePosition();
                                                }
                                                if (isKingInCheck() && isCheckMate()) {
                                                        isGameOver = true;
                                                } else if (isPromotable()) {
                                                        isPiecePromoted = true;
                                                } else {
                                                        endTurn();
                                                }
                                        } else {
                                                // If move was invallid, restore original state
                                                copyPieces(INITIAL_PIECES, simPieces);
                                                selectedPiece.resetPosition();
                                                selectedPiece = null;
                                        }
                                }
                        }
                }
        }

        private void simulate() {
                isPieceMovable = false;
                isValidTile = false;
                // Reset the piece list every loop
                copyPieces(INITIAL_PIECES, simPieces);
                // Reset castling piece position
                if (castlingPiece != null) {
                        castlingPiece.currentFile = castlingPiece.nextFile;
                        castlingPiece.x = castlingPiece.getX(castlingPiece.currentFile);
                        castlingPiece = null;
                }
                selectedPiece.x = MOUSE.x - Board.TILE_SIZE_HALF;
                selectedPiece.y = MOUSE.y - Board.TILE_SIZE_HALF;
                selectedPiece.currentFile = selectedPiece.getFile(selectedPiece.x);
                selectedPiece.currentRank = selectedPiece.getRank(selectedPiece.y);
                if (selectedPiece.isMovable(selectedPiece.currentFile, selectedPiece.currentRank)) {
                        isPieceMovable = true;
                        if (selectedPiece.collidingPiece != null) {
                                simPieces.remove(selectedPiece.collidingPiece.getIndex());
                        }
                        checkCastling();
                        if (!isIllegalMove(selectedPiece) && !isKingInCheckNextTurn()) {
                                isValidTile = true;
                        }
                }
        }
        
        @Override
        public void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D graphics2D = (Graphics2D) graphics;
                BOARD.drawBoard(graphics2D);
                for (Piece piece : simPieces) {
                        piece.draw(graphics2D);
                }
                // Set selected piece's tile color at whiteColor at 50% transparancy
                if (selectedPiece != null && isPieceMovable) {
                        if (isIllegalMove(selectedPiece) || isKingInCheckNextTurn()) {
                                setGraphics(graphics2D, Color.red);

                        } else {
                                setGraphics(graphics2D, Color.white);
                        }
                        // Draw piece at the end to prevent getting drawed over by the board
                        selectedPiece.draw(graphics2D);
                }
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2D.setFont(new Font("Book Antiqua", Font.PLAIN, 40));
                graphics2D.setColor(Color.white);
                if (isPiecePromoted) {
                        graphics2D.drawString("Choose Piece:", 900, 150);
                        for (Piece piece : PROMOTION_PIECES) {
                                graphics2D.drawImage(piece.bufferedImage, piece.getX(piece.currentFile),
                                                piece.getY(piece.currentRank), Board.TILE_SIZE, Board.TILE_SIZE, null);
                        }
                } else {
                        if (currentTurnColor == WHITE) {
                                graphics2D.drawString("White's Turn", 850, 400);
                                if (checkingPiece != null && checkingPiece.color == BLACK) {
                                        setGraphics(graphics2D, Color.red);
                                }
                        } else {
                                graphics2D.drawString("Black's Turn", 850, 400);
                                if (checkingPiece != null && checkingPiece.color == WHITE) {
                                        setGraphics(graphics2D, Color.red);
                                }
                        }
                }
                if (isGameOver) {
                        graphics2D.drawString("Game Over", 850, 200);
                }
        }
}
