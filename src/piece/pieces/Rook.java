package piece.pieces;

import piece.*;
public class Rook extends Piece {

        public Rook(PieceColor pieceColor, int file, int rank) {
                super(pieceColor, file, rank);
                pieceType = PieceType.ROOK;
                bufferedImage = getImage(pieceType.getPieceName(), color.getPieceColorName());
        }

        @Override
        public boolean isMovable(int targetFile, int targetRank) {
                if (!isWithinBounds(targetFile, targetRank)) {
                        return false;
                }
                if (isSameSquare(targetFile, targetRank)) {
                        return false;
                }
                if (targetFile == nextFile || targetRank == nextRank) {
                        return isValidTile(targetFile, targetRank)
                                        && !isPieceInVerticalAndHorizontalSight(targetFile, targetRank);
                }
                return false;
        }
}