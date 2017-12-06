import java.util.ArrayList;
import java.util.List;


public class SudokuSolver
{
    private Board board;

    public SudokuSolver(Board board)
    {
        this.board = board;
        board.printBoard();
    }

    void solve()
    {
        boolean changesMadeToPossibles = true;

        while (!board.solved() && changesMadeToPossibles)
        {
            Integer originalPossiblesCount = board.countPossibles();

            board.setPossibles();
            board.scanForUniqueSetElement();
            board.scanForEqualSetsOfTwo();
            board.setOnePossibles();
            changesMadeToPossibles = originalPossiblesCount > board.countPossibles();
        }

        if (!board.solved())
        {
            board.printBoard();
            guessFromElementSets();
        }
        else
        {
            board.printPossibles();
        }
    }

    private void guessFromElementSets()
    {
        int setSize = board.findSmallestSetSize();
        boolean keepSearching = true;

        for (int i = 0; i < 9 && keepSearching; i++)
        {
            for (int j = 0; j < 9 && keepSearching; j++)
            {
                if (board.getSudokuBoard()[i][j].isDynamic() && board.getSudokuBoard()[i][j].getPossibles().size() == setSize)
                {
                    keepSearching = false;
                    List<Integer> list = new ArrayList<>(board.getSudokuBoard()[i][j].getPossibles());

                    for (int number : list)
                    {
                        Board boardClone = board.copyBoard();
                        boardClone.getSudokuBoard()[i][j].getPossibles().remove(number);
                        solveFurther(boardClone);

                        if (boardClone.solved() || boardClone.isSolvable())
                        {
                            board.getSudokuBoard()[i][j].getPossibles().remove(number);
                            solve();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void solveFurther(Board board)
    {
        boolean changesMadeToPossibles = true;

        while (!board.solved() && changesMadeToPossibles)
        {
            Integer originalPossiblesCount = board.countPossibles();

            board.setPossibles();
            board.scanForUniqueSetElement();
            board.scanForEqualSetsOfTwo();
            board.setOnePossibles();
            changesMadeToPossibles = originalPossiblesCount > board.countPossibles();
        }
    }
}
