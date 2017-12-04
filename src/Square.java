import java.util.HashSet;
import java.util.Set;

public class Square
{
    private boolean dynamic;
    private int number;
    private Set<Integer> possibles = new HashSet<>();

    Square()
    {
        dynamic = true;
        for (int i = 1; i < 10; i++)
        {
            possibles.add(i);
        }
    }

    boolean isDynamic()
    {
        return dynamic;
    }

    void setDynamic(boolean dynamic)
    {
        this.dynamic = dynamic;
    }

    int getNumber()
    {
         return number;
    }

    void setNumber(int number)
    {
        this.number = number;
    }

    public Set<Integer> getPossibles()
    {
        return possibles;
    }

    public void setPossibles(Set<Integer> possibles)
    {
        this.possibles = possibles;
    }
}