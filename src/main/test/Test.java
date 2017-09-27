/**
 * Created by hadoop on 2017/9/22.
 */
public class Test {
    public static void main(String[] args) {
        StringBuffer s = new StringBuffer("good");
        StringBuffer s2 = new StringBuffer("bad");
        test(s, s2);
        System.out.println(s);//9
        System.out.println(s2);//10
//        A a = new A();
//        add(a);
//        System.out.println(a.i);
    }

    static void test(StringBuffer s, StringBuffer s2) {
        System.out.println(s);//1
        System.out.println(s2);//2
        s2 = s;//3
        s = new StringBuffer("new");//4
        System.out.println(s);//5
        System.out.println(s2);//6
        s.append("hah");//7
        s2.append("hah");//8
    }
}
