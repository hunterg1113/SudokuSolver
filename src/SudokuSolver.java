import java.util.ArrayList;
import java.util.List;


public class SudokuSolver
{
    int count = 0;

    public SudokuSolver()
    {}

    void solve(Board board) throws InterruptedException
    {
        board.printBoard();
        boolean changesMadeToPossibles = true;

        while (!board.solved() && changesMadeToPossibles)
        {
            int originalPossiblesCount = board.countPossibles();
            board.setPossibles();
            board.scanForUniqueSetElement();
            board.scanForEqualSetsOfTwo();
            board.setOnePossibles();
            changesMadeToPossibles = originalPossiblesCount > board.countPossibles();
        }

        if (board.solved())
        {
            incrementCount();
            board.printBoard();
        }
        else
        {
            guessFromElementSets(board);
        }

        System.out.println("***************************");
        System.out.println("# of solutions : " + count);
        System.out.println("***************************");
    }

    private void guessFromElementSets(Board board) throws InterruptedException
    {
        int setSize = board.findSmallestSetSize();
        boolean keepSearching = true;

        for (int i = 0; i < 9 && keepSearching; i++)
        {
            for (int j = 0; j < 9 && keepSearching; j++)
            {
                if (board.getSudokuBoard()[i][j].isDynamic() && board.getSudokuBoard()[i][j].getPossibles().size() == setSize)
                {
                    final int row = i;
                    final int column = j;
                    keepSearching = false;
                    List<Integer> list = new ArrayList<>(board.getSudokuBoard()[i][j].getPossibles());
                    List<Thread> threads = new ArrayList<>();
                    for (int number : list)
                    {
                        Thread thread = new Thread(() ->
                        {
                            Board boardClone = board.copyBoard();
                            boardClone.getSudokuBoard()[row][column].getPossibles().remove(number);
                            try{solveFurther(boardClone);} catch(InterruptedException ie){}

                            if (boardClone.solved())
                            {
                                incrementCount();
                                boardClone.printBoard();
                            }
                            else if (boardClone.isSolvable())
                            {
                                try{guessFromElementSets(boardClone);} catch(InterruptedException ie){}
                            }
                        });

                        threads.add(thread);
                        thread.start();
                    }
                    for(Thread thread : threads)
                    {
                        thread.join();
                    }
                }
            }
        }
    }

    private void solveFurther(Board board) throws InterruptedException
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

    private synchronized void incrementCount()
    {
        count++;
    }
}