public class Main
{
    public static void main(String[] args)
    {
        Board board = new Board();
        board.addNumbersToBoard(0, 1, 1);
        board.addNumbersToBoard(0, 4, 8);
        board.addNumbersToBoard(0, 5, 4);
        board.addNumbersToBoard(0, 8, 2);

        board.addNumbersToBoard(1, 0, 9);
        board.addNumbersToBoard(1, 3, 7);
        board.addNumbersToBoard(1, 6, 4);
        board.addNumbersToBoard(1, 7, 1);
        board.addNumbersToBoard(1, 8, 3);

        board.addNumbersToBoard(2, 0, 7);
        board.addNumbersToBoard(2, 6, 6);

        board.addNumbersToBoard(3, 4, 2);
        board.addNumbersToBoard(3, 5, 5);
        board.addNumbersToBoard(3, 8, 7);

        board.addNumbersToBoard(4, 1, 6);
        board.addNumbersToBoard(4, 2, 2);
        board.addNumbersToBoard(4, 3, 9);
        board.addNumbersToBoard(4, 5, 7);
        board.addNumbersToBoard(4, 6, 8);
        board.addNumbersToBoard(4, 7, 4);

        board.addNumbersToBoard(5, 0, 5);
        board.addNumbersToBoard(5, 3, 4);
        board.addNumbersToBoard(5, 4, 6);

        board.addNumbersToBoard(6, 2, 6);
        board.addNumbersToBoard(6, 8, 9);

        board.addNumbersToBoard(7, 0, 1);
        board.addNumbersToBoard(7, 1, 3);
        board.addNumbersToBoard(7, 2, 9);
        board.addNumbersToBoard(7, 5, 2);
        board.addNumbersToBoard(7, 8, 4);

        board.addNumbersToBoard(8, 0, 8);
        board.addNumbersToBoard(8, 3, 5);
        board.addNumbersToBoard(8, 4, 3);
        board.addNumbersToBoard(8, 7, 2);

        SudokuSolver sudokuSolver = new SudokuSolver(board);
        sudokuSolver.solve();
    }
}
