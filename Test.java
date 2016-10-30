/**
 * Created by zhenia on 02.10.16.
 */

public class Test {
    public static void main(String[] args){

		MyBigInteger A = new MyBigInteger("2F0FC0E2DDD0E864B410EABDD3C1F35F5879B73C6E4D1C1E47440DE7224EB5CB47DF24588409B856");
        MyBigInteger B = new MyBigInteger("AF9AE343063EFD3A7B0FF882BA0C109003F47CA84D2FCC9822C6A876A56F");
       /* System.out.println("Addition:");
		System.out.println(A.add(B).toHexString());
        System.out.println("Subtraction:");
        System.out.println(A.sub(B).toHexString());
        System.out.println("Multiplication:");
        System.out.println(A.multiply(B).toHexString());
        System.out.println("Power:");
        System.out.println(A.pow(B).toHexString());
        System.out.println("Division:");
        MyBigInteger[] res = A.divMod(B);
        System.out.println(res[1].toHexString());*/
        System.out.println(A.mod(B).toHexString());
    }
}
