/**
 * Created by zhenia on 02.10.16.
 */

public class Test {
    public static void main(String[] args){

		MyBigInteger A = new MyBigInteger("10000000000000000000000000000000000001");
        MyBigInteger B = new MyBigInteger("2");
        System.out.println("Addition:");
		System.out.println(A.add(B).toHexString());
        System.out.println("Subtraction:");
        System.out.println(A.sub(B).toHexString());
        System.out.println("Multiplication:");
        System.out.println(A.multiply(B).toHexString());
        System.out.println("Power:");
        System.out.println(A.pow(B).toHexString());
        System.out.println("Division:");
        MyBigInteger[] res = A.divMod(B);
        System.out.println(res[1].toHexString());
    }
}
