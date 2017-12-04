public class Main
{
    public static void main(String[] args)
    {
        Board board = new Board();
        board.addNumbersToBoard(0, 0, 6);
        board.addNumbersToBoard(0, 4, 8);
        board.addNumbersToBoard(0, 8, 9);

        board.addNumbersToBoard(1, 2, 3);
        board.addNumbersToBoard(1, 6, 1);
        board.addNumbersToBoard(1, 7, 8);

        board.addNumbersToBoard(2, 1, 5);
        board.addNumbersToBoard(2, 4, 4);
        board.addNumbersToBoard(2, 6, 6);

        board.addNumbersToBoard(3, 0, 2);
        board.addNumbersToBoard(3, 5, 1);

        board.addNumbersToBoard(4, 2, 9);
        board.addNumbersToBoard(4, 3, 6);
        board.addNumbersToBoard(4, 5, 4);
        board.addNumbersToBoard(4, 6, 3);

        board.addNumbersToBoard(5, 3, 8);
        board.addNumbersToBoard(5, 8, 4);

        board.addNumbersToBoard(6, 2, 8);
        board.addNumbersToBoard(6, 4, 1);
        board.addNumbersToBoard(6, 7, 4);

        board.addNumbersToBoard(7, 1, 6);
        board.addNumbersToBoard(7, 2, 7);
        board.addNumbersToBoard(7, 6, 5);

        board.addNumbersToBoard(8, 0, 5);
        board.addNumbersToBoard(8, 4, 7);
        board.addNumbersToBoard(8, 8, 2);

        SudokuSolver sudokuSolver = new SudokuSolver(board);
        sudokuSolver.solve();
    }
}

/*
Hard level...
        board.addNumbersToBoard(0, 6, 1);
        board.addNumbersToBoard(0, 7, 9);

        board.addNumbersToBoard(1, 0, 4);
        board.addNumbersToBoard(1, 1, 9);
        board.addNumbersToBoard(1, 2, 5);
        board.addNumbersToBoard(1, 8, 6);

        board.addNumbersToBoard(2, 4, 9);
        board.addNumbersToBoard(2, 7, 3);
        board.addNumbersToBoard(2, 8, 8);

        board.addNumbersToBoard(3, 1, 2);
        board.addNumbersToBoard(3, 4, 3);
        board.addNumbersToBoard(3, 6, 8);

        board.addNumbersToBoard(4, 1, 4);
        board.addNumbersToBoard(4, 3, 9);
        board.addNumbersToBoard(4, 5, 8);
        board.addNumbersToBoard(4, 7, 6);

        board.addNumbersToBoard(5, 2, 8);
        board.addNumbersToBoard(5, 4, 2);
        board.addNumbersToBoard(5, 7, 7);

        board.addNumbersToBoard(6, 0, 1);
        board.addNumbersToBoard(6, 1, 5);
        board.addNumbersToBoard(6, 4, 6);

        board.addNumbersToBoard(7, 0, 7);
        board.addNumbersToBoard(7, 6, 5);
        board.addNumbersToBoard(7, 7, 4);
        board.addNumbersToBoard(7, 8, 9);

        board.addNumbersToBoard(8, 1, 3);
        board.addNumbersToBoard(8, 2, 2);














Very hard level...
        board.addNumbersToBoard(0, 2, 1);
        board.addNumbersToBoard(0, 3, 5);

        board.addNumbersToBoard(1, 2, 9);
        board.addNumbersToBoard(1, 7, 5);
        board.addNumbersToBoard(1, 8, 8);

        board.addNumbersToBoard(2, 4, 9);
        board.addNumbersToBoard(2, 6, 2);

        board.addNumbersToBoard(3, 0, 8);
        board.addNumbersToBoard(3, 5, 7);
        board.addNumbersToBoard(3, 6, 3);
        board.addNumbersToBoard(3, 7, 6);

        board.addNumbersToBoard(4, 1, 7);
        board.addNumbersToBoard(4, 4, 3);
        board.addNumbersToBoard(4, 7, 9);

        board.addNumbersToBoard(5, 1, 3);
        board.addNumbersToBoard(5, 2, 2);
        board.addNumbersToBoard(5, 3, 8);
        board.addNumbersToBoard(5, 8, 7);

        board.addNumbersToBoard(6, 2, 3);
        board.addNumbersToBoard(6, 4, 6);

        board.addNumbersToBoard(7, 0, 6);
        board.addNumbersToBoard(7, 1, 8);
        board.addNumbersToBoard(7, 6, 4);

        board.addNumbersToBoard(8, 5, 3);
        board.addNumbersToBoard(8, 6, 5);
        */
