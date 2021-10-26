import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TryCatchFinallyDemo {

    public static void main(String[] args){
        System.out.println("return value = " + tryReturn());
        System.out.println("return value = " + tryReturn2());
        System.out.println("return value = " + tryReturn3());
//        tryFinallySp();
//        tryFinallySp2();
        List<Integer> list = new ArrayList<>();
        list.addAll()
        System.out.println("-7 %10 = " + (-17 % 10));
    }

    // try有return，仍然会执行finally，但返回值已经确定了
    public static int tryReturn(){
        int i = 0;
        try {
            i = 10;
            System.out.println("try");
            return i;
        }catch (Exception e){
            System.out.println("catch");
        }finally {
            i = 100;
            System.out.println("finally");
        }
        return -1;
    }

    // catch有return，也会执行finally
    public static int tryReturn2(){
        int i = 0;
        try {
            i = 10;
            int [] arr = new int[i];
            i = arr[i];
            System.out.println("try");
            return i;
        }catch (Exception e){
            System.out.println("catch");
            i = 20;
            return i;
        }finally {
            i = 100;
            System.out.println("finally");
        }
    }

    // finally有return，以finally return为准
    public static int tryReturn3(){
        int i = 0;
        try {
            i = 10;
            int [] arr = new int[i];
            i = arr[i];
            System.out.println("try");
            return i;
        }catch (Exception e){
            System.out.println("catch");
            i = 20;
            return i;
        }finally {
            i = 100;
            System.out.println("finally");
            return i;
        }
    }

    // finally不会执行的情况，在try-catch里执行System.exit(1)
    public static void tryFinallySp(){
        int i = 0;
        try {
            i = 10;
            System.out.println("try");
            System.exit(0);
        }catch (Exception e){
            System.out.println("catch");
            System.exit(0);
        }finally {
            System.out.println("finally");
        }
    }

    // try里抛出异常，仍然会执行finally然后才抛出
    public static void tryFinallySp2(){
        int i = 0;
        try {
            i = 10;
            System.out.println("try");
            int[]arr = new int[2];
            i = arr[2];
        }finally {
            System.out.println("finally");
        }
    }
}
