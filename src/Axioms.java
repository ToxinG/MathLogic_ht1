import java.util.ArrayList;
import static java.util.Arrays.asList;

/**
 * Created by Антон on 31.10.2016.
 */
public class Axioms {
    public static ArrayList<String> axioms = new ArrayList<>(asList(
            "A->B->A",
            "(A->B)->(A->B->C)->(A->C)",
            "A->B->A&B",
            "A&B->A",
            "A&B->B",
            "A->A|B",
            "B->A|B",
            "(A->C)->(B->C)->(A|B->C)",
            "(A->B)->(A->!B)->!A",
            "!!A->A"
    ));
}