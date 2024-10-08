package piece;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.Board;
import main.GamePanel;

public class Piece {

        private final int LOWER_BOUND = 0;
        private final int UPPER_BOUND = 7;
        public BufferedImage bufferedImage;
        public int x;
        public int y;
        public int currentRank;
        public int currentFile;
        public int nextRank;
        public int nextFile;
        public PieceColor color;
        public PieceType pieceType;
        public Piece collidingPiece;
        public boolean hasMoved;
        public boolean hasTwoStepped;

        public Piece(PieceColor color, int file, int rank) {
                this.color = color;
                this.currentFile = file;
                this.currentRank = rank;
                x = getX(file);
                y = getY(rank);
                nextFile = file;
                nextRank = rank;
        }

        public Piece getCollidingPiece(int targetFile, int targetRank) {
                for (Piece piece : GamePanel.simPieces) {
                        if (piece.currentFile == targetFile && piece.currentRank == targetRank && piece != this) {
                                return piece;
                        }
                }
                return null;
        }

        public final BufferedImage getImage(String pieceName, String pieceColorName) {
                BufferedImage bImage = null;
                try {
                        bImage = ImageIO.read(
                                        new FileInputStream("res/piece/" + pieceName + "_" + pieceColorName + ".png"));
                } catch (IOException exception) {
                }
                return bImage;
        }

        public final int getX(int file) {
                return file * Board.TILE_SIZE;
        }

        public final int getY(int rank) {
                return rank * Board.TILE_SIZE;
        }

        public int getCol(int x) {
                return (x + Board.TILE_SIZE_HALF) / Board.TILE_SIZE;
        }

        public int getRank(int y) {
                return (y + Board.TILE_SIZE_HALF) / Board.TILE_SIZE;
        }

        public int getIndex() {
                for (int i = 0; i < GamePanel.simPieces.size(); i++) {
                        if (GamePanel.simPieces.get(i) == this) {
                                return i;
                        }
                }
                return 0;
        }

        public boolean isMovable(int targetFile, int targetRank) {
                return false;
        }

        public boolean isWithinBounds(int targetFile, int targetRank) {
                return !(targetFile > UPPER_BOUND || targetFile < LOWER_BOUND || targetRank > UPPER_BOUND
                                || targetRank < LOWER_BOUND);
        }

        public boolean isSameSquare(int targetFile, int targetRank) {
                return targetFile == nextFile && targetRank == nextRank;
        }

        public boolean isValidTile(int targetFile, int targetRank) {

                collidingPiece = getCollidingPiece(targetFile, targetRank);

                if (collidingPiece != null) {
                        // This tile is not occupied
                        return false;
                }
                if (collidingPiece.color != this.color) {
                        return true;
                }
                collidingPiece = null;
                return false;
        }        

        public boolean isPieceInSightNorth(int targetFile, int targetRank) {
                for (int i = nextRank - 1; i > targetRank; i--) {
                        for (Piece piece : GamePanel.simPieces) {
                                if (piece.currentRank == i && piece.currentFile == targetFile) {
                                        collidingPiece = piece;
                                        return true;
                                }
                        }
                }
                return false;
        }

        public boolean isPieceInSightEast(int targetFile, int targetRank) {
                for (int i = nextFile + 1; i < targetFile; i++) {
                        for (Piece piece : GamePanel.simPieces) {
                                if (piece.currentFile == i && piece.currentRank == targetRank) {
                                        collidingPiece = piece;
                                        return true;
                                }
                        }
                }
                return false;
        }

        public boolean isPieceInSightSouth(int targetFile, int targetRank) {
                for (int i = nextRank + 1; i < targetRank; i++) {
                        for (Piece piece : GamePanel.simPieces) {
                                if (piece.currentRank == i && piece.currentFile == targetFile) {
                                        collidingPiece = piece;
                                        return true;
                                }
                        }
                }
                return false;
        }

        public boolean isPieceInSightWest(int targetFile, int targetRank) {
                for (int i = nextFile - 1; i > targetFile; i--) {
                        for (Piece piece : GamePanel.simPieces) {
                                if (piece.currentFile == i && piece.currentRank == targetRank) {
                                        collidingPiece = piece;
                                        return true;
                                }
                        }
                }
                return false;
        }

        public boolean isPieceInSightNorthEast(int targetFile, int targetRank) {
                if (targetRank < nextRank) {
                        for (int i = nextFile - 1; i > targetFile; i--) {
                                int difference = Math.abs(i - nextFile);
                                for (Piece piece : GamePanel.simPieces) {
                                        if (piece.currentFile == i && piece.currentRank == nextRank - difference) {
                                                collidingPiece = piece;
                                                return true;
                                        }
                                }
                        }
                }
                return false;
        }

        public boolean isPieceInSightNorthWest(int targetFile, int targetRank) {
                if (targetRank < nextRank) {
                        for (int i = nextFile + 1; i < targetFile; i++) {
                                int difference = Math.abs(i - nextFile);
                                for (Piece piece : GamePanel.simPieces) {
                                        if (piece.currentFile == i && piece.currentRank == nextRank - difference) {
                                                collidingPiece = piece;
                                                return true;
                                        }
                                }
                        }
                }
                return false;
        }

        public boolean isPieceInSightSouthEast(int targetFile, int targetRank) {
                if (targetRank > nextRank) {
                        for (int i = nextFile + 1; i < targetFile; i++) {
                                int difference = Math.abs(i - nextFile);
                                for (Piece piece : GamePanel.simPieces) {
                                        if (piece.currentFile == i && piece.currentRank == nextRank + difference) {
                                                collidingPiece = piece;
                                                return true;
                                        }
                                }
                        }
                }
                return false;
        }

        public boolean isPieceInSightSouthWest(int targetFile, int targetRank) {
                if (targetRank > nextRank) {
                        for (int i = nextFile - 1; i > targetFile; i--) {
                                int difference = Math.abs(i - nextFile);
                                for (Piece piece : GamePanel.simPieces) {
                                        if (piece.currentFile == i && piece.currentRank == nextRank + difference) {
                                                collidingPiece = piece;
                                                return true;
                                        }
                                }
                        }
                }
                return false;
        }

        public boolean isPieceInVerticalAndHorizontalSight(int targetFile, int targetRank) {
                return isPieceInSightNorth(targetFile, targetRank) || isPieceInSightSouth(targetFile, targetRank)
                                || isPieceInSightEast(targetFile, targetRank)
                                || isPieceInSightWest(targetFile, targetRank);

        }

        public boolean isPieceInDiagonalSight(int targetFile, int targetRank) {
                return isPieceInSightNorthEast(targetFile, targetRank)
                                || isPieceInSightNorthWest(targetFile, targetRank)
                                || isPieceInSightSouthEast(targetFile, targetRank)
                                || isPieceInSightSouthWest(targetFile, targetRank);
        }

        public void resetPosition() {
                currentFile = nextFile;
                currentRank = nextRank;
                x = getX(currentFile);
                y = getY(currentRank);
        }

        public void updatePosition() {
                // En Passant
                if (pieceType == PieceType.PAWN) {
                        if (Math.abs(currentRank - nextRank) == 2) {
                                hasTwoStepped = true;
                        }
                }
                x = getX(currentFile);
                y = getY(currentRank);
                nextFile = getCol(x);
                nextRank = getRank(y);
                hasMoved = true;
        }

        public void draw(Graphics2D graphics2D) {
                graphics2D.drawImage(bufferedImage, x, y, Board.TILE_SIZE, Board.TILE_SIZE, null);
        }

}
