import java.util.*;

class Board
{
    private Square[][] sudokuBoard = new Square[9][9];

    Board()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                sudokuBoard[i][j] = new Square();
            }
        }
    }

    Board copyBoard()
    {
        Board clone = new Board();

        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                clone.sudokuBoard[i][j].setDynamic(this.sudokuBoard[i][j].isDynamic());
                clone.sudokuBoard[i][j].setNumber(this.sudokuBoard[i][j].getNumber());
                clone.sudokuBoard[i][j].setPossibles(new HashSet<>(this.sudokuBoard[i][j].getPossibles()));
            }
        }

        return clone;
    }

    Square[][] getSudokuBoard()
    {
        return sudokuBoard;
    }

    void addNumbersToBoard(int row, int column, int number)
    {
        sudokuBoard[row][column].setDynamic(false);
        sudokuBoard[row][column].setNumber(number);

        for (int i = 0; i < 9; i++)
        {
            sudokuBoard[row][i].getPossibles().remove(number);
        }
        for (int i = 0; i < 9; i++)
        {
            sudokuBoard[i][column].getPossibles().remove(number);
        }
        sudokuBoard[row][column].setPossibles(new HashSet<>(number));

        sudokuBoard[row][column].setPossiblesToFinalNumber(number);
    }

    void setOnePossibles()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 1)
                {
                    sudokuBoard[i][j].setDynamic(false);
                    List<Integer> list = new ArrayList<>(sudokuBoard[i][j].getPossibles());
                    sudokuBoard[i][j].setNumber(list.get(0));
                }

            }
        }
    }

    void scanForUniqueSetElement() throws InterruptedException
    {
        scanForUniqueSetElementByRow();
        scanForUniqueSetElementByColumn();
        scanForUniqueSetElementBySquares();
    }

    private void scanForUniqueSetElementByRow()
    {
        for (int i = 0; i < 9; i++)
        {
            Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

            for (int j = 0; j < 9; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }

            if (mapOfNumberCount.size() > 0)
            {
                for (int key : mapOfNumberCount.keySet())
                {
                    if (mapOfNumberCount.get(key) == 1)
                    {
                        for (int j = 0; j < 9; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementByColumn()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int j = 0; j < 9; j++)
        {
            for (int i = 0; i < 9; i++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
            if (mapOfNumberCount.size() > 0)
            {
                for (int key : mapOfNumberCount.keySet())
                {
                    if (mapOfNumberCount.get(key) == 1)
                    {
                        for (int i = 0; i < 9; i++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementBySquares() throws InterruptedException
    {
        Thread t1 = new Thread(() ->
        {
            scanForUniqueSetElementSquare1();
            scanForUniqueSetElementSquare2();
            scanForUniqueSetElementSquare3();
        });
        Thread t2 = new Thread(() ->
        {
            scanForUniqueSetElementSquare4();
            scanForUniqueSetElementSquare5();
            scanForUniqueSetElementSquare6();
        });
        Thread t3 = new Thread(() ->
        {
            scanForUniqueSetElementSquare7();
            scanForUniqueSetElementSquare8();
            scanForUniqueSetElementSquare9();
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }

    void scanForEqualSetsOfTwo() throws InterruptedException
    {
        scanForEqualSetsOfTwoByRow();
        scanForEqualSetsOfTwoByColumn();
        scanForEqualSetsOfTwoBySquares();
    }

    private void scanForEqualSetsOfTwoByRow()
    {
        for (int i = 0; i < 9; i++)
        {
            List<Set<Integer>> setsOfEqual = new ArrayList<>();

            for (int j = 0; j < 8; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = j + 1; k < 9; k++)
                    {
                        if (sudokuBoard[i][k].isDynamic() && checkedSet.equals(sudokuBoard[i][k].getPossibles()))
                        {
                            setsOfEqual.add(checkedSet);
                        }
                    }
                }
            }

            if (setsOfEqual.size() > 0)
            {
                for (int j = 0; j < 9; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : setsOfEqual)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForEqualSetsOfTwoByColumn()
    {
        for (int i = 0; i < 9; i++)
        {
            List<Set<Integer>> setsOfEqual = new ArrayList<>();

            for (int j = 0; j < 8; j++)
            {
                if (sudokuBoard[j][i].isDynamic() && sudokuBoard[j][i].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[j][i].getPossibles();

                    for (int k = j + 1; k < 9; k++)
                    {
                        if (sudokuBoard[k][i].isDynamic() && checkedSet.equals(sudokuBoard[k][i].getPossibles()))
                        {
                            setsOfEqual.add(checkedSet);
                        }
                    }
                }
            }

            if (setsOfEqual.size() > 0)
            {
                for (int j = 0; j < 9; j++)
                {
                    if (sudokuBoard[j][i].isDynamic())
                    {
                        for (Set<Integer> usedSet : setsOfEqual)
                        {
                            if (!usedSet.equals(sudokuBoard[j][i].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[j][i].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForEqualSetsOfTwoBySquares() throws InterruptedException
    {
        Thread t1 = new Thread(() ->
        {
            scanSquare1ForEqualSets();
            scanSquare2ForEqualSets();
            scanSquare3ForEqualSets();
        });
        Thread t2 = new Thread(() ->
        {
            scanSquare4ForEqualSets();
            scanSquare5ForEqualSets();
            scanSquare6ForEqualSets();
        });
        Thread t3 = new Thread(() ->
        {
            scanSquare7ForEqualSets();
            scanSquare8ForEqualSets();
            scanSquare9ForEqualSets();
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }

    void setPossibles() throws InterruptedException
    {
        for (int i = 0; i < 9; i++)
        {
            Set<Integer> setOfNumbersInUse = new HashSet<>();

            for (int j = 0; j < 9; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
                else if (sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
            if (setOfNumbersInUse.size() > 0)
            {
                for (int j = 0; j < 9; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 9; i++)
        {
            Set<Integer> setOfNumbersInUse = new HashSet<>();

            for (int j = 0; j < 9; j++)
            {
                if (!sudokuBoard[j][i].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[j][i].getNumber());
                }
            }
            if (setOfNumbersInUse.size() > 0)
            {
                for (int j = 0; j < 9; j++)
                {
                    if (sudokuBoard[j][i].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[j][i].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }

        setPossiblesBySquares();
    }

    private void setPossiblesBySquares() throws InterruptedException
    {
        Thread t1 = new Thread(() ->
        {
            setSquare1();
            setSquare2();
            setSquare3();
        });
        Thread t2 = new Thread(() ->
        {
            setSquare4();
            setSquare5();
            setSquare6();
        });
        Thread t3 = new Thread(() ->
        {
            setSquare7();
            setSquare8();
            setSquare9();
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

    }

    private void setSquare1()
    {
        Set<Integer> setOfNumbersInUse = new HashSet<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
        }

        if (setOfNumbersInUse.size() > 0)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }
    }

    private void setSquare2()
    {
        Set<Integer> setOfNumbersInUse = new HashSet<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 3; j < 6; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
        }

        if (setOfNumbersInUse.size() > 0)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 3; j < 6; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }
    }

    private void setSquare3()
    {
        Set<Integer> setOfNumbersInUse = new HashSet<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 6; j < 9; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
        }

        if (setOfNumbersInUse.size() > 0)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 6; j < 9; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }
    }

    private void setSquare4()
    {
        Set<Integer> setOfNumbersInUse = new HashSet<>();

        for (int i = 3; i < 6; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
        }

        if (setOfNumbersInUse.size() > 0)
        {
            for (int i = 3; i < 6; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }
    }

    private void setSquare5()
    {
        Set<Integer> setOfNumbersInUse = new HashSet<>();

        for (int i = 3; i < 6; i++)
        {
            for (int j = 3; j < 6; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
        }

        if (setOfNumbersInUse.size() > 0)
        {
            for (int i = 3; i < 6; i++)
            {
                for (int j = 3; j < 6; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }
    }

    private void setSquare6()
    {
        Set<Integer> setOfNumbersInUse = new HashSet<>();

        for (int i = 3; i < 6; i++)
        {
            for (int j = 6; j < 9; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
        }

        if (setOfNumbersInUse.size() > 0)
        {
            for (int i = 3; i < 6; i++)
            {
                for (int j = 6; j < 9; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }
    }

    private void setSquare7()
    {
        Set<Integer> setOfNumbersInUse = new HashSet<>();

        for (int i = 6; i < 9; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
        }

        if (setOfNumbersInUse.size() > 0)
        {
            for (int i = 6; i < 9; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }
    }

    private void setSquare8()
    {
        Set<Integer> setOfNumbersInUse = new HashSet<>();

        for (int i = 6; i < 9; i++)
        {
            for (int j = 3; j < 6; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
        }

        if (setOfNumbersInUse.size() > 0)
        {
            for (int i = 6; i < 9; i++)
            {
                for (int j = 3; j < 6; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }
    }

    private void setSquare9()
    {
        Set<Integer> setOfNumbersInUse = new HashSet<>();

        for (int i = 6; i < 9; i++)
        {
            for (int j = 6; j < 9; j++)
            {
                if (!sudokuBoard[i][j].isDynamic())
                {
                    setOfNumbersInUse.add(sudokuBoard[i][j].getNumber());
                }
            }
        }

        if (setOfNumbersInUse.size() > 0)
        {
            for (int i = 6; i < 9; i++)
            {
                for (int j = 6; j < 9; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (int number : setOfNumbersInUse)
                        {
                            sudokuBoard[i][j].getPossibles().removeIf(n -> n == number);
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementSquare1()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
        }

        if (mapOfNumberCount.size() > 0)
        {
            for (int key : mapOfNumberCount.keySet())
            {
                if (mapOfNumberCount.get(key) == 1)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        for (int j = 0; j < 3; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementSquare2()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 3; j < 6; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
        }

        if (mapOfNumberCount.size() > 0)
        {
            for (int key : mapOfNumberCount.keySet())
            {
                if (mapOfNumberCount.get(key) == 1)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        for (int j = 3; j < 6; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementSquare3()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 6; j < 9; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
        }

        if (mapOfNumberCount.size() > 0)
        {
            for (int key : mapOfNumberCount.keySet())
            {
                if (mapOfNumberCount.get(key) == 1)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        for (int j = 6; j < 9; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementSquare4()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int i = 3; i < 6; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
        }

        if (mapOfNumberCount.size() > 0)
        {
            for (int key : mapOfNumberCount.keySet())
            {
                if (mapOfNumberCount.get(key) == 1)
                {
                    for (int i = 3; i < 6; i++)
                    {
                        for (int j = 0; j < 3; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementSquare5()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int i = 3; i < 6; i++)
        {
            for (int j = 3; j < 6; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
        }

        if (mapOfNumberCount.size() > 0)
        {
            for (int key : mapOfNumberCount.keySet())
            {
                if (mapOfNumberCount.get(key) == 1)
                {
                    for (int i = 3; i < 6; i++)
                    {
                        for (int j = 3; j < 6; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementSquare6()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int i = 3; i < 6; i++)
        {
            for (int j = 6; j < 9; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
        }

        if (mapOfNumberCount.size() > 0)
        {
            for (int key : mapOfNumberCount.keySet())
            {
                if (mapOfNumberCount.get(key) == 1)
                {
                    for (int i = 3; i < 6; i++)
                    {
                        for (int j = 6; j < 9; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementSquare7()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int i = 6; i < 9; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
        }

        if (mapOfNumberCount.size() > 0)
        {
            for (int key : mapOfNumberCount.keySet())
            {
                if (mapOfNumberCount.get(key) == 1)
                {
                    for (int i = 6; i < 9; i++)
                    {
                        for (int j = 0; j < 3; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementSquare8()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int i = 6; i < 9; i++)
        {
            for (int j = 3; j < 6; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
        }

        if (mapOfNumberCount.size() > 0)
        {
            for (int key : mapOfNumberCount.keySet())
            {
                if (mapOfNumberCount.get(key) == 1)
                {
                    for (int i = 6; i < 9; i++)
                    {
                        for (int j = 3; j < 6; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanForUniqueSetElementSquare9()
    {
        Map<Integer, Integer> mapOfNumberCount = new HashMap<>();

        for (int i = 6; i < 9; i++)
        {
            for (int j = 6; j < 9; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    for (Integer number : sudokuBoard[i][j].getPossibles())
                    {
                        mapOfNumberCount.putIfAbsent(number, 0);
                        int newCount = mapOfNumberCount.get(number) + 1;
                        mapOfNumberCount.put(number, newCount);
                    }
                }
            }
        }

        if (mapOfNumberCount.size() > 0)
        {
            for (int key : mapOfNumberCount.keySet())
            {
                if (mapOfNumberCount.get(key) == 1)
                {
                    for (int i = 6; i < 9; i++)
                    {
                        for (int j = 6; j < 9; j++)
                        {
                            if (sudokuBoard[i][j].getPossibles().contains(key))
                            {
                                sudokuBoard[i][j].getPossibles().removeIf(n -> n != key);
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanSquare1ForEqualSets()
    {
        List<Set<Integer>> usedSets = new ArrayList<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = i; k < 3; k++)
                    {
                        for (int l = j + 1; l < 3; l++)
                        {
                            if (sudokuBoard[k][l].isDynamic() && checkedSet.equals(sudokuBoard[k][l].getPossibles()))
                            {
                                usedSets.add(checkedSet);
                            }
                        }
                    }
                }
            }
        }

        if (usedSets.size() > 0)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : usedSets)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanSquare2ForEqualSets()
    {
        List<Set<Integer>> usedSets = new ArrayList<>();

        for (int i = 3; i < 6; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = i; k < 6; k++)
                    {
                        for (int l = j + 1; l < 3; l++)
                        {
                            if (sudokuBoard[k][l].isDynamic() && checkedSet.equals(sudokuBoard[k][l].getPossibles()))
                            {
                                usedSets.add(checkedSet);
                            }
                        }
                    }
                }
            }
        }

        if (usedSets.size() > 0)
        {
            for (int i = 3; i < 6; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : usedSets)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanSquare3ForEqualSets()
    {
        List<Set<Integer>> usedSets = new ArrayList<>();

        for (int i = 6; i < 9; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = i; k < 9; k++)
                    {
                        for (int l = j + 1; l < 3; l++)
                        {
                            if (sudokuBoard[k][l].isDynamic() && checkedSet.equals(sudokuBoard[k][l].getPossibles()))
                            {
                                usedSets.add(checkedSet);
                            }
                        }
                    }
                }
            }
        }

        if (usedSets.size() > 0)
        {
            for (int i = 6; i < 9; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : usedSets)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanSquare4ForEqualSets()
    {
        List<Set<Integer>> usedSets = new ArrayList<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 3; j < 5; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = i; k < 3; k++)
                    {
                        for (int l = j + 1; l < 6; l++)
                        {
                            if (sudokuBoard[k][l].isDynamic() && checkedSet.equals(sudokuBoard[k][l].getPossibles()))
                            {
                                usedSets.add(checkedSet);
                            }
                        }
                    }
                }
            }
        }

        if (usedSets.size() > 0)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 3; j < 6; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : usedSets)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanSquare5ForEqualSets()
    {
        List<Set<Integer>> usedSets = new ArrayList<>();

        for (int i = 3; i < 6; i++)
        {
            for (int j = 3; j < 5; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = i; k < 6; k++)
                    {
                        for (int l = j + 1; l < 6; l++)
                        {
                            if (sudokuBoard[k][l].isDynamic() && checkedSet.equals(sudokuBoard[k][l].getPossibles()))
                            {
                                usedSets.add(checkedSet);
                            }
                        }
                    }
                }
            }
        }

        if (usedSets.size() > 0)
        {
            for (int i = 3; i < 6; i++)
            {
                for (int j = 3; j < 6; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : usedSets)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanSquare6ForEqualSets()
    {
        List<Set<Integer>> usedSets = new ArrayList<>();

        for (int i = 6; i < 9; i++)
        {
            for (int j = 3; j < 5; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = i; k < 9; k++)
                    {
                        for (int l = j + 1; l < 6; l++)
                        {
                            if (sudokuBoard[k][l].isDynamic() && checkedSet.equals(sudokuBoard[k][l].getPossibles()))
                            {
                                usedSets.add(checkedSet);
                            }
                        }
                    }
                }
            }
        }

        if (usedSets.size() > 0)
        {
            for (int i = 6; i < 9; i++)
            {
                for (int j = 3; j < 6; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : usedSets)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanSquare7ForEqualSets()
    {
        List<Set<Integer>> usedSets = new ArrayList<>();

        for (int i = 0; i < 3; i++)
        {
            for (int j = 6; j < 8; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = i; k < 3; k++)
                    {
                        for (int l = j + 1; l < 9; l++)
                        {
                            if (sudokuBoard[k][l].isDynamic() && checkedSet.equals(sudokuBoard[k][l].getPossibles()))
                            {
                                usedSets.add(checkedSet);
                            }
                        }
                    }
                }
            }
        }

        if (usedSets.size() > 0)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 6; j < 9; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : usedSets)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanSquare8ForEqualSets()
    {
        List<Set<Integer>> usedSets = new ArrayList<>();

        for (int i = 3; i < 6; i++)
        {
            for (int j = 6; j < 8; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = i; k < 6; k++)
                    {
                        for (int l = j + 1; l < 9; l++)
                        {
                            if (sudokuBoard[k][l].isDynamic() && checkedSet.equals(sudokuBoard[k][l].getPossibles()))
                            {
                                usedSets.add(checkedSet);
                            }
                        }
                    }
                }
            }
        }

        if (usedSets.size() > 0)
        {
            for (int i = 3; i < 6; i++)
            {
                for (int j = 6; j < 9; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : usedSets)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scanSquare9ForEqualSets()
    {
        List<Set<Integer>> usedSets = new ArrayList<>();

        for (int i = 6; i < 9; i++)
        {
            for (int j = 6; j < 8; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() == 2)
                {
                    Set<Integer> checkedSet = sudokuBoard[i][j].getPossibles();

                    for (int k = i; k < 9; k++)
                    {
                        for (int l = j + 1; l < 9; l++)
                        {
                            if (sudokuBoard[k][l].isDynamic() && checkedSet.equals(sudokuBoard[k][l].getPossibles()))
                            {
                                usedSets.add(checkedSet);
                            }
                        }
                    }
                }
            }
        }

        if (usedSets.size() > 0)
        {
            for (int i = 6; i < 9; i++)
            {
                for (int j = 6; j < 9; j++)
                {
                    if (sudokuBoard[i][j].isDynamic())
                    {
                        for (Set<Integer> usedSet : usedSets)
                        {
                            if (!usedSet.equals(sudokuBoard[i][j].getPossibles()))
                            {
                                for (Integer usedNumber : usedSet)
                                {
                                    sudokuBoard[i][j].getPossibles().removeIf(n -> n == usedNumber);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    boolean solved()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    return false;
                }
            }
        }

        return true;
    }

    boolean isSolvable()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                List<Integer> list = new ArrayList<>(sudokuBoard[i][j].getPossibles());

                if (list.isEmpty())
                {
                    return false;
                }
            }
        }

        return true;
    }

    int countPossibles()
    {
        int possiblesCount = 0;

        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    possiblesCount += sudokuBoard[i][j].getPossibles().size();
                }
            }
        }
        return possiblesCount;
    }

    int findSmallestSetSize()
    {
        int smallestPossiblesSize = 10;

        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (sudokuBoard[i][j].isDynamic() && sudokuBoard[i][j].getPossibles().size() < smallestPossiblesSize)
                {
                    smallestPossiblesSize = sudokuBoard[i][j].getPossibles().size();
                }
            }
        }

        return smallestPossiblesSize;
    }

    synchronized void printBoard()
    {
        System.out.println("***************************");

        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (sudokuBoard[i][j].isDynamic())
                {
                    System.out.print("[ ]");
                }
                else
                {
                    System.out.print("[" + sudokuBoard[i][j].getNumber() + "]");
                }
            }
            System.out.println();
        }
    }

    void printPossibles()
    {
        System.out.println("***************************");
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                //if (sudokuBoard[i][j].isDynamic())
                {
                    System.out.print(sudokuBoard[i][j].getPossibles());
                }
            }
            System.out.println();
        }
        System.out.println("***************************");
    }
}
