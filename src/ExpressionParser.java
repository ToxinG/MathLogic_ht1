/**
 * Created by Антон on 29.10.2016.
 */

public class ExpressionParser {

    static Entity expression(String s) {
        Entity res = disjunction(s);
        while (!res.unparsed.isEmpty()) {
            if (res.unparsed.charAt(0) != '-')
                return res;
            Entity left = res;
            Entity right = expression(res.unparsed.substring(2));
            res = new Entity();
            res.type = Entity.EntityType.impl;
            res.left = left;
            res.right = right;
            res.stringRepresentation = "(" + left.stringRepresentation + "->" + right.stringRepresentation + ")";
            res.unparsed = right.unparsed;
            res.left.unparsed = "";
            res.right.unparsed = "";
        }
        return res;
    }

    private static Entity disjunction(String s) {
        Entity res = conjunction(s);
        while (!res.unparsed.isEmpty()) {
            if (res.unparsed.charAt(0) != '|')
                return res;
            Entity left = res;
            Entity right = conjunction(res.unparsed.substring(1));
            res = new Entity();
            res.type = Entity.EntityType.dis;
            res.left = left;
            res.right = right;
            res.stringRepresentation = "(" + left.stringRepresentation + "|" + right.stringRepresentation + ")";
            res.unparsed = right.unparsed;
            res.left.unparsed = "";
            res.right.unparsed = "";
        }
        return res;
    }

    private static Entity conjunction(String s) {
        Entity res = negation(s);
        while (!res.unparsed.isEmpty()) {
            if (res.unparsed.charAt(0) != '&')
                return res;
            Entity left = res;
            Entity right = negation(res.unparsed.substring(1));
            res = new Entity();
            res.type = Entity.EntityType.con;
            res.left = left;
            res.right = right;
            res.stringRepresentation = "(" + left.stringRepresentation + "&" + right.stringRepresentation + ")";
            res.unparsed = right.unparsed;
            res.left.unparsed = "";
            res.right.unparsed = "";
        }
        return res;
    }

    private static Entity negation(String s) {
        if (s.charAt(0) == '!') {
            Entity res = new Entity();
            res.type = Entity.EntityType.neg;
            res.left = negation(s.substring(1));
            res.stringRepresentation = "!" + res.left.stringRepresentation;
            res.unparsed = res.left.unparsed;
            res.left.unparsed = "";
            return res;
        }
        if (s.charAt(0) == '(') {
            return parentheses(s);
        }
        return singleVar(s);
    }

    private static Entity parentheses(String s) {
        int i = 0, balance = 1;
        while (balance != 0) {
            i++;
            if (s.charAt(i) == '(') {
                balance++;
            } else if (s.charAt(i) == ')') {
                balance--;
            }
        }
        Entity res = expression(s.substring(1, i));
        res.unparsed = s.substring(i + 1);
        return res;
    }

    private static Entity singleVar(String s) {
        Entity res = new Entity();
        int i = 1;
        while (i < s.length() && (Character.isDigit(s.charAt(i)) || Character.isUpperCase(s.charAt(i))))
            i++;
        res.stringRepresentation = s.substring(0, i);
        res.unparsed = s.substring(i);
        return res;
    }
}
