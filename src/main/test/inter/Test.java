package inter;

/**
 * Created by hadoop on 2017/9/25.
 */
public class Test {
    public static void main(String[] args) {
        Man a = new Man() {
            @Override
            public void fun1() {
                System.out.println("1");
            }
        };
    }
}
