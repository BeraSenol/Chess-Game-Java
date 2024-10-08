package piece.pieces;

import piece.Piece;
import piece.PieceType;
import piece.PieceColor;

public class Knight extends Piece {

        public Knight(PieceColor color, int column, int row) {
                super(color, column, row);
                pieceType = PieceType.KNIGHT;
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
                if (Math.abs(targetFile - nextFile) * Math.abs(targetRank - nextRank) != 2) {
                        return false;
                }
                return isValidTile(targetFile, targetRank);
        }
}