package kkurawal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Konglie on 7/18/2016.
 */
public class MEEProcessor {

    /**
     * the expression
     * ex: a + b = b + a
     */
    private String expr;

    /**
     * The left expression
     * ex: a + b
     */
    private String leftExpr;

    /**
     * The right expression
     * ex: b + a
     */
    private String rightExpr;

    /**
     * holds the leftExpression elements
     */
    private ArrayList<TheElement> leftElements;

    /**
     * holds the rightExpression elements
     */
    private ArrayList<TheElement> rightElements;

    /**
     * holds the result, if leftExpr is equal to rightExpr
     */
    private boolean isEqual = false;

    /**
     * normalized left expr
     */
    private String normalizedLeft;

    /**
     * normalized right expr
     */
    private String normalizedRight;

    /**
     * Instantiate new Object
     *
     * @param s the expression
     *          ex: a+b+c == a-d+b-(-c)+d
     */
    public MEEProcessor(String s){
        // uniform the String case
        this.expr = s.toLowerCase().trim();
        this.leftElements = new ArrayList<TheElement>();
        this.rightElements = new ArrayList<TheElement>();

        doParse();
    }

    /**
     * start working,
     */
    private void doParse(){
        if(!isValidExpr() || !splitExpression()){
            return;
        }

        // now we already have the left and right expressions
        // clean (normalize) them
        normalize();

        // so far, the expression should be normalized partially
        // ex: a-d+b-(-c)+d will become +a-d+b+c+d
        // split them
        tokenize();

        // now remove any element opposites
        // +a-d+b+c+d will become +a+b+c,
        // the -d is opposite of +d, then the two are removed
        cleanup();

        // now sort each side of the expression
        // so +a+c+b will become +a+b+c
        TheElement[] lefts  = leftElements.toArray(new TheElement[leftElements.size()]);
        TheElement[] rights = rightElements.toArray(new TheElement[rightElements.size()]);
        Arrays.sort(lefts);
        Arrays.sort(rights);

        normalizedLeft = join(lefts);
        normalizedRight = join(rights);

        // check if both string is identical
        // that is the final result
        isEqual = normalizedLeft.equals(normalizedRight);
    }

    /**
     * join as string
     * @param els the arrays of TheElements
     * @return the joined string
     */
    private String join(TheElement[] els){
        String res = "";
        for(TheElement el : els){
            res += el.toString();
        }

        return res;
    }

    /**
     * helper to cleanup both sides
     */
    private void cleanup(){
        cleanup(leftElements);
        cleanup(rightElements);
    }

    /**
     * remove any element opposites
     * ex:  remove +a if we found -a
     *      remove -b if we found +b
     */
    private void cleanup(ArrayList<TheElement> bucket){
        ArrayList<TheElement> opposites = new ArrayList<TheElement>();

        for(Iterator<TheElement> it = bucket.iterator(); it.hasNext(); ){
            TheElement el = it.next();
            // if an element already marked for removal
            // skip the test
            if(opposites.indexOf(el) >= 0){
                continue;
            }
            TheElement op = findOpposite(el, bucket, opposites);
            if(op != null){
                it.remove();
                opposites.add(op);
            }
        }

        for(TheElement op : opposites){
            bucket.remove(op);
        }
    }

    /**
     * check if el has its opposite inside bucket
     * @param el Thelement to be checked
     * @param bucket the haystack to be iterate
     * @return the opposite element
     */
    private TheElement findOpposite(TheElement el, ArrayList<TheElement> bucket, ArrayList<TheElement> trashes){
        for(Iterator<TheElement> it = bucket.iterator(); it.hasNext(); ){
            TheElement te = it.next();
            if(te == el){
                continue;
            }

            // if an element already marked for removal
            // skip the test
            if(trashes.indexOf(te) >= 0){
                continue;
            }

            if(el.isOppositeOf(te)){
                // remove the opposite
                // we don't need it
                return te;
            }
        }

        return null;
    }

    /**
     * helper to tokenize both sides
     */
    private void tokenize(){
        tokenize(leftExpr, leftElements);
        tokenize(rightExpr, rightElements);
    }

    /**
     * collect the sylable or "TheElement"
     * ex: +a-d+b+c+d become +a, -d, +b, +c, +d
     */
    private void tokenize(String s, ArrayList<TheElement> bucket){
        // nothing to do on empty list
        if(s.isEmpty()){
            return;
        }

        for(int i = 0; i < s.length(); i += 2 ){
            // collect 2 characters from the string
            String vars = s.substring(i, i+2);
            if(vars.trim().isEmpty()){
                continue;
            }
            bucket.add(new TheElement(vars));
        }
    }

    /**
     * helper to normalize both sides of expression
     */
    private void normalize(){
        leftExpr    = normalize(leftExpr);
        rightExpr   = normalize(rightExpr);
    }

    /**
     * normalize the single side expression
     * @param s single side expression
     *          ex: a-d+b-(-c)+d
     */
    private String normalize(String s){
        // first step
        // if the expression is not began with a sign ( + or -)
        // then it is actually started by +
        // because a = +a
        if(!s.startsWith("+") && !s.startsWith("-")){
            s = "+" + s;
        }

        // remove any spaces
        s = s.replaceAll("\\s+", "");

        // next, remove any parenthesis
        // a-d+b-(-c)+d will become a-d+b--c+d
        s = s.replaceAll("\\(", "");
        s = s.replaceAll("\\)", "");

        // next, plusminus war
        // +- is negative
        s = s.replaceAll("\\+-", "-");

        // -+ is negative
        s = s.replaceAll("-\\+", "-");

        // -- is positive
        s = s.replaceAll("--", "+");

        // ++ is positive
        s = s.replaceAll("\\+\\+", "+");

        // the expression length, must be an even number
        // if not, the expression itself it not valid
        if(s.length() % 2 > 0){
            s = "";
            return s;
        }

        return s;
    }

    /**
     * get the left and right expression
     */
    private boolean splitExpression(){
        // split by == sign
        String[] s = this.expr.split("\\s+==\\s+");
        if(s.length != 2){
            // invalid
            return false;
        }

        leftExpr    = s[0].trim();
        rightExpr   = s[1].trim();

        return true;
    }

    private boolean isValidExpr(){
        // TODO: validate the complete expression

        // assumes it is valid
        return true;
    }

    public boolean isEqual(){
        return isEqual;
    }

    /**
     * get the object as String
     * @return the String representasion
     */
    @Override
    public String toString(){
        int len = 60;
        return String.format(
                "%s -> left: %s right: %s is equal? %s",
                Launcher.padRight(this.expr, len),
                Launcher.padRight(this.normalizedLeft, len / 3),
                Launcher.padRight(this.normalizedRight, len / 3),
                this.isEqual() ? "True" : "False"
        );
    }
}
