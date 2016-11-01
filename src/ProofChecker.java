import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Антон on 30.10.2016.
 */
public class ProofChecker {

    private HashMap<String, Entity> axMap = new HashMap<>(); //sets a relation between axiom variables and parts of expressions
    private HashMap<String, Integer> checkedStatements = new HashMap<>(); //tells numbers of checked statements
    private HashMap<String, ArrayList<Entity>> checkedImpl = new HashMap<>(); //finds checked implications by their right parts

    //recursive comparing of hypothesis and expression parse trees
    private boolean checkForHyp(Entity e, Entity hyp) {
        if (hyp.left == null && hyp.right == null)
            return hyp.stringRepresentation.equals(e.stringRepresentation);
        if (hyp.type == e.type) {
            if (hyp.type == Entity.EntityType.neg)
                return checkForHyp(e.left, hyp.left);
            return checkForHyp(e.left, hyp.left) && checkForHyp(e.right, hyp.right);
        }
        return false;
    }

    //recursive comparing of an axiom parse tree to the top part of expression parse tree;
    //for each parse tree leaf (variable) of an axiom it is checked whether it is related to the same expression part
    //      all the times it is used in an axiom
    private boolean checkForAx(Entity e, Entity ax) {
        if (ax.left == null && ax.right == null) {
            if (axMap.containsKey(ax.stringRepresentation)) {
                return axMap.get(ax.stringRepresentation).stringRepresentation.equals(e.stringRepresentation);
            } else {
                axMap.put(ax.stringRepresentation, e);
                return true;
            }
        }
        if (ax.type == e.type) {
            if (ax.type == Entity.EntityType.neg)
                return checkForAx(e.left, ax.left);
            return checkForAx(e.left, ax.left) && checkForAx(e.right, ax.right);
        }
        return false;
    }

    //coordinates other check-functions, checks statements for Modus Ponens and creates annotations
    public String check(Entity e, int num) {
        String res = "";
        for (int i = 1; i <= Main.axList.size(); i++) {
            axMap.clear();
            if (checkForAx(e, Main.axList.get(i - 1))) {
                res = new StringBuilder().append("(Сх. акс. ").append(i).append(")").toString();
                break;
            }
        }
        for (int i = 1; i <= Main.hypList.size(); i++)
            if (checkForHyp(e, Main.hypList.get(i - 1))) {
                res = new StringBuilder().append("(Предп. ").append(i).append(")").toString();
                break;
            }
        if (checkedImpl.containsKey(e.stringRepresentation)) {
            for (Entity impl: checkedImpl.get(e.stringRepresentation))
                if (checkedStatements.containsKey(impl.left.stringRepresentation))
                    res = new StringBuilder().append("(M.P. ").append(checkedStatements.get(impl.left.stringRepresentation))
                    .append(", ").append(checkedStatements.get(impl.stringRepresentation)).append(")").toString();
        }

        checkedStatements.put(e.stringRepresentation, num);
        if (e.type == Entity.EntityType.impl) {
            if (!checkedImpl.containsKey(e.right.stringRepresentation))
                checkedImpl.put(e.right.stringRepresentation, new ArrayList<Entity>());
            checkedImpl.get(e.right.stringRepresentation).add(e);
        }

        if (res.equals(""))
            res = new StringBuilder().append("(Не доказано)").toString();
        return res;
    }
}
