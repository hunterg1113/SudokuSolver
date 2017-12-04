import java.util.ArrayList;
import java.util.List;

public class SudokuSolver
{
    private Board board;

    public SudokuSolver(Board board)
    {
        this.board = board;
    }

    void solve()
    {
        boolean changesMadeToPossibles = true;

        board.setOnePossibles();

        while (!board.solved() && changesMadeToPossibles)
        {
            Integer originalPossiblesCount = board.countPossibles();

            board.setPossibles();
            board.scanForUniqueSetElement();
            board.scanForEqualSets();
            board.setOnePossibles();
            changesMadeToPossibles = originalPossiblesCount > board.countPossibles();
        }

        if (!board.solved())
        {
            guessFromTwoElementSets();
        }
        else
        {
            board.printPossibles();
        }
    }

    private void guessFromTwoElementSets()
    {
        boolean keepSearching = true;

        for (int i = 0; i < 9 && keepSearching; i++)
        {
            for (int j = 0; j < 9 && keepSearching; j++)
            {
                if (board.getSudokuBoard()[i][j].isDynamic() && board.getSudokuBoard()[i][j].getPossibles().size() == 2)
                {
                    List<Integer> list = new ArrayList<>(board.getSudokuBoard()[i][j].getPossibles());

                    Board boardClone = board.clone();
                    boardClone.getSudokuBoard()[i][j].getPossibles().remove(list.get(0));
                    solveFurther(boardClone);

                    if (boardClone.solved())
                    {
                        boardClone.printPossibles();
                        keepSearching = false;
                    }
                    else if (boardClone.unsolvable())
                    {
                        board.getSudokuBoard()[i][j].getPossibles().remove(list.get(1));
                        solve();
                    }
                }
            }
        }
    }

    private void solveFurther(Board board)
    {
        boolean changesMadeToPossibles = true;

        board.setOnePossibles();

        while (!board.solved() && changesMadeToPossibles)
        {
            Integer originalPossiblesCount = board.countPossibles();

            board.setPossibles();
            board.scanForUniqueSetElement();
            board.scanForEqualSets();
            board.setOnePossibles();
            changesMadeToPossibles = originalPossiblesCount > board.countPossibles();
        }
    }
}
