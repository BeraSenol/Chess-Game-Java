package piece.pieces;

import piece.*;
public class Queen extends Piece {

        public Queen(PieceColor pieceColor, int file, int rank) {
                super(pieceColor, file, rank);
                pieceType = PieceType.QUEEN;
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
                if (Math.abs(targetFile - nextFile) == Math.abs(targetRank - nextRank)) {
                        return isValidTile(targetFile, targetRank) && !isPieceInDiagonalSight(targetFile, targetRank);
                }
                return false;
        }
}