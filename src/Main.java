import java.io.*;
import java.util.ArrayList;

/**
 * Created by Антон on 31.10.2016.
 */
public class Main {

    public static ArrayList<Entity> hypList = new ArrayList<>();
    public static ArrayList<Entity> axList = new ArrayList<>();
    public static ArrayList<Entity> proofList = new ArrayList<>();
    public static ArrayList<String> proofs = new ArrayList<>();
    public static Entity target;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        PrintWriter writer = new PrintWriter(new File("output.txt"));

        for (String ax: Axioms.axioms) axList.add(ExpressionParser.expression(ax));

        String inference = "";
        while (inference.equals(""))
            inference = reader.readLine();
        inference = inference.replace(" ", "");
        String formula;
        if (inference.charAt(0) == '|') {
            formula = inference.substring(2);
        } else {
            String[] hypStrings = inference.split(",");
            String[] ending = hypStrings[hypStrings.length - 1].split("\\|\\-");
            formula = ending[1];
            hypStrings[hypStrings.length - 1] = ending[0];
            for (String hypString : hypStrings) hypList.add(ExpressionParser.expression(hypString));
        }
        target = ExpressionParser.expression(formula);

        String l;
        while (reader.ready()) {
            l = reader.readLine();
            if (l.equals(""))
                continue;
            l = l.replace(" ", "");
            proofs.add(l);
            proofList.add(ExpressionParser.expression(l));
        }

        ProofChecker pc = new ProofChecker();
        writer.println(inference);
        for (int i = 1; i <= proofList.size(); i++)
            writer.println(new StringBuilder().append("(").append(i).append(") ").append(proofs.get(i - 1))
                    .append(" ").append(pc.check(proofList.get(i - 1), i)));

        writer.close();
    }
}
