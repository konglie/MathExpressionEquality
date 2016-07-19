package kkurawal;

/**
 * Created by Konglie on 7/18/2016.
 */
public class TheElement implements Comparable<TheElement> {
    /**
     * the orginal expression
     */
    private String expr;

    /**
     * the variable
     * ex: +a, the variable is a
     */
    private char variable;

    /**
     * the sign
     * ex: +a, the sign is +
     */
    private char sign;


    /**
     * instantiate new object
     * @param s should be just 2 letter string
     *          ex: +a, -b, etc
     */
    public TheElement(String s){
        this.expr       = s;
        this.sign       = s.charAt(0);
        this.variable   = s.charAt(1);
    }

    /**
     * check if an object is the opposite of another
     * @param o another TheElement object
     * @return is o is opposite of current
     *      ex: +a is opposite of -a
     *          -b is opposite of +b
     */
    public boolean isOppositeOf(TheElement o){
        // if the variable is different, it surely not an opposite
        if(this.variable != o.getVariable()){
            return false;
        }

        // TODO: this valid on 2 type of sign only
        // simply check if the sign is not equal
        return this.sign != o.sign;
    }

    /**
     * get the variable
     * @return the variable
     */
    public char getVariable(){
        return this.variable;
    }

    /**
     * object comparison
     * @param o the other object
     * @return the value of o.variable comparison
     */
    @Override
    public int compareTo(TheElement o) {
        return this.getVariable() - o.getVariable();
    }

    /**
     * get the object as String
     * @return the String representasion
     */
    @Override
    public String toString(){
        return expr;
    }
}
