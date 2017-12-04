public class SudokuSolver
{
    private Board board;

    public SudokuSolver(Board board)
    {
        this.board = board;
    }

    void solve()
    {
        board.setOnePossibles();

        while(!board.solved())
        {
            board.printPossibles();
            board.setPossibles();
            board.scanForUniqueSetElement();
            board.scanForEqualSets();
            board.setOnePossibles();
        }
        board.printBoard();
    }
}
