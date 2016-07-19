package kkurawal;

/**
 * Created by Konglie on 7/18/2016.
 * konglie@kurungkurawal.com
 */
public class Launcher {
    public static void main(String[] args){
        String[] expr = new String[]{
                "a+b == b+a", "a+c == c-a", "a+a+b == a+b+a", "a+(-b) == a -b",
                "a+a+b == a+b+a", "a+a+b == a+b+a+b",
                "a+d+c == c-(-a)+d",
                "a+b+c == a-d+b-(-c)+d",
                "a+c-b-(-e)+d == -(-c)+a+d+e-b",
                "a+c-b-(-e)+d == -c+a+d+e-b",
                "b-c-b+c+a+b-c-b+c == a",
                "a == b-c-b+c+a+b-c-b+c",
                "a == b-c-b+c+a+b-c-b+c+b",
                "a == -b-c-b+c+a+b-c-b+c+b",
                "a == a+a-a+a-a-a+a-a+a",
                "-(-a)+a+(-a)-(-a)-a+(-a)+a-a+a == a+a-a+a-a-a+a-a+a",
                "-(-a)+a+(-a)-(-a)-a+(-a)+a-a+a == a"
        };
        for(String e : expr){
            out(new MEEProcessor(e).toString());
        }
    }

    public static void out(Object o){
        System.out.println(o);
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
}
