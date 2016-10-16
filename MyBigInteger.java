import java.util.ArrayList;
import java.util.Collections;


public class MyBigInteger{

	private final long BASE = 0x10000L;

	private ArrayList<Long> number = new ArrayList<>();

	public MyBigInteger(String num){
        if (num.length() < 4) number.add(Long.parseLong(num, 16));
        else
            for (int i = num.length() - 4; i >= 0; i-=4){
			number.add(Long.parseLong(num.substring(i, i + 4), 16));
			if (i < 4 && i > 0) number.add(Long.parseLong(num.substring(0, i), 16));
		}
	}



	public MyBigInteger(){

	}



    private static void zeroFill(MyBigInteger num1, MyBigInteger num2){

        int size1 = num1.number.size();
        int size2 = num2.number.size();

        ArrayList<Long> zeros = new ArrayList<>(Collections.nCopies(Math.abs(size1 -  size2), 0L));

        if(size1 > size2){
            num2.number.addAll(zeros);
        }
        else if (size2 > size1){
            num1.number.addAll(zeros);
        }
    }


    private void zeroFill(int pos){

        ArrayList<Long> zeros = new ArrayList<>(Collections.nCopies(pos, 0L));

        this.number.addAll(zeros);

    }



    private void LongShiftToHigh(int pos){

        ArrayList<Long> zeros = new ArrayList<>(Collections.nCopies(pos, 0L));

        this.number.addAll(0, zeros);

    }


    private void cleanZeros(){
        int i = number.size() - 1;
        while (number.get(i) == 0 && i > 0){
            number.remove(i);
            i = number.size() - 1;
        }
    }



    public int compareTo(MyBigInteger num){

        this.cleanZeros();
        num.cleanZeros();

        int max_size = Math.max(num.number.size(), this.number.size());

        int i = max_size - 1;
        try{
            while (num.number.get(i).equals(this.number.get(i))){
                i--;
            }
        }catch (IndexOutOfBoundsException e){
            if (i == -1) return 0;
            else if (max_size == num.number.size()) return -1;
            else return 1;
        }

        if (num.number.get(i) > this.number.get(i)) return -1;
        else return 1;
    }


	public MyBigInteger add(MyBigInteger num){

		long carry = 0;
		long temp;
		MyBigInteger result = new MyBigInteger();

        zeroFill(this, num);

        for (int i = 0; i < num.number.size(); i++){

            temp = num.number.get(i) + this.number.get(i) + carry;
            result.number.add( temp & (BASE - 1) );
            carry = temp / BASE;
		}
        if (carry > 0) result.number.add(carry);
		return result;
	}



	public MyBigInteger sub(MyBigInteger num){

		long borrow = 0;
		long temp;

        if (this.compareTo(num) == -1) throw new ArithmeticException("Negative number!");

		MyBigInteger result = new MyBigInteger();

        zeroFill(this, num);

		for(int i = 0; i < num.number.size(); i++ ) {

            temp = this.number.get(i) - num.number.get(i) - borrow;

            if (temp >= 0) {
                result.number.add(temp);
                borrow = 0;
            } else {
                result.number.add(BASE + temp);
                borrow = 1;
            }
        }
		return result;
	}



	public String toHexString(){
		String result = "";
		String hex;
		for (int i = 0; i < number.size(); i++){
			hex = Long.toHexString(number.get(i));
			result =  ("0000" + hex).substring(hex.length()) + result;
		}

		int begin = 0;
		while (begin < result.length() - 1 && result.charAt(begin) == '0') begin++;
		result = result.substring(begin);
		return result;
	}




    private MyBigInteger mulOneDigit(long b){

        long carry = 0L;
        long temp;

        MyBigInteger result = new MyBigInteger();

        for (int i = 0; i < number.size(); i++){

            temp = number.get(i) * b + carry;
            result.number.add(temp & (BASE - 1));
            carry = temp >> 16;
        }

        result.number.add(carry);
        return result;
    }

    private int getBitPos(long i){
        int res = 0;
        while (i != 1){
            i >>= 1;
            res++;
        }
        return res;
    }


    private static long LeftShift(long num){
        long res = num << 1;
        num = res;
        long i = 0;
        while(num != 1){
            num >>= 1;
            i++;
        }
        return res ^ (1 << i);
    }


    private MyBigInteger LongShiftBit(){
        MyBigInteger result = new MyBigInteger();
        byte bit = 0;

        if ((Long.SIZE - Long.numberOfLeadingZeros(number.get(0)) == 16)){
            result.number.add((number.get(0) ^ (1 << 15)) << 1);
            bit = 1;
        }
        else{
            result.number.add(number.get(0) << 1);
        }
        long curr;

        for (int i = 1; i < number.size(); i++){
            curr = number.get(i);

            if ((Long.SIZE - Long.numberOfLeadingZeros(curr)) == 16){
                result.number.add((curr ^ (1 << 15)) << 1 | bit);
                bit = 1;
            }
            else{
                result.number.add(curr << 1 | bit);
                bit = 0;
            }
        }

        if (bit == 1){
            result.number.add(1L);
        }

        return result;
    }


    private MyBigInteger LongShiftBitsToHigh(long n){
        MyBigInteger result;
        if(n == 0){
            result = this;
        }
        else{
            result = this.LongShiftBit();
        }

        for (int i = 0; i < n - 1; i++){
            result = result.LongShiftBit();
        }
        return result;
    }


    private long bitLength(){
        return 16 * (number.size() - 1) + Long.toBinaryString(number.get(number.size()-1)).length();
    }



    public MyBigInteger multiply(MyBigInteger num){


        MyBigInteger min = num.number.size() < this.number.size() ? num : this;
        MyBigInteger a = (min == num) ? this: num;
        MyBigInteger result = new MyBigInteger("0");
        MyBigInteger temp;

        for (int i = 0; i < Math.min(num.number.size(), this.number.size()); i++){

            temp = a.mulOneDigit(min.number.get(i));
            temp.LongShiftToHigh(i);
            result = result.add(temp);
        }

        return result;
    }


    public MyBigInteger pow(MyBigInteger exponent){

        MyBigInteger result = new MyBigInteger("1");
        MyBigInteger[] pows = new MyBigInteger[15];

        pows[0] = new MyBigInteger("1");
        pows[1] = this;

        for (int i = 2; i < pows.length; i++){
            pows[i] = pows[i - 1].multiply(this);
        }

        String hex_exp = exponent.toHexString();

        for (int i = 0; i < hex_exp.length(); i++){

            result = result.multiply(pows[Integer.parseInt(String.valueOf(hex_exp.charAt(i)), 16)]);
            if (i != hex_exp.length() - 1){
                for (int j = 0; j < 4; j++){
                    result = result.multiply(result);
                }
            }
        }
        return result;
    }


   public MyBigInteger[] divMod(MyBigInteger num) {
       if (num.compareTo(new MyBigInteger("0")) == 0){
           throw new ArithmeticException("Division by zero!");
       }

       long k = num.bitLength();
       long t;
       int index;
       MyBigInteger R = new MyBigInteger(this.toHexString());
       MyBigInteger Q = new MyBigInteger("0");
       MyBigInteger C;

       while (R.compareTo(num) != -1) {
           t = R.bitLength();
           C = num.LongShiftBitsToHigh(t - k);
           if (R.compareTo(C) == -1) {
               --t;
               C = num.LongShiftBitsToHigh(t - k);
           }
           R = R.sub(C);
           index = (int) (t-k)/16;

           if (Q.number.size() <= index){
               int size = Q.number.size();
               for (int i = 0; i < index - size + 1; i++){
                   Q.number.add(0L);
               }
           }
           Q.number.set(index, Q.number.get(index) | 1 << ((t-k) % 16));
       }

       MyBigInteger[] res = {R, Q};
       return res;
   }


    public static void main(String[] args){

		/*MyBigInteger A = new MyBigInteger("10000000000000000000000000000000000001");
        MyBigInteger B = new MyBigInteger("2");
        System.out.println("Addition:");
		System.out.println(A.add(B).toHexString());
        System.out.println("Subtraction:");
        System.out.println(A.sub(B).toHexString());
        System.out.println("Multiplication:");
        System.out.println(A.multiply(B).toHexString());
        System.out.println("Power:");
        System.out.println(A.pow(B).toHexString());*/

        MyBigInteger A = new MyBigInteger("123456789");
        MyBigInteger B = new MyBigInteger("0");
        MyBigInteger[] res = A.divMod(B);
        System.out.println(res[0].toHexString());
	} 
}