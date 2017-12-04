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
            board.printPossibles();
        }

        if (!board.solved())
        {
            guessFromTwoElementSets();
        }
    }

    private void guessFromTwoElementSets()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (board.getSudokuBoard()[i][j].isDynamic() && board.getSudokuBoard()[i][j].getPossibles().size() == 2)
                {
                    List<Integer> list = new ArrayList<>(board.getSudokuBoard()[i][j].getPossibles());
                    board.getSudokuBoard()[i][j].getPossibles().remove(list.get(0));
                    solve();
                }
            }
        }
    }
}
