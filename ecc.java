import java.util.*;
import java.math.*;
public class ecc {
    public static BigInteger mulinv(BigInteger n, BigInteger modulus) 
    {
        try
        {
            return n.modInverse(modulus);  
        }
        catch (ArithmeticException e) 
        {
            System.out.println("NOT POSSIBLE: Modular inverse does not exist for " + n + " modulo " + modulus);
            return BigInteger.ZERO;
        }
    }
    public static BigInteger[] addition(BigInteger[] arr1,BigInteger[] arr2,BigInteger p,BigInteger a){
        BigInteger[] result = new BigInteger[2];
        if(arr1==null){
            return arr2;
        }
        if(arr2==null){
            return arr1;
        }
        else{
            if((arr1[0].equals(arr2[0]))&& (arr1[1].equals(arr2[1])) ){
                BigInteger x=arr1[0];
                BigInteger y=arr1[1];
                BigInteger lambda=(((BigInteger.valueOf(3).multiply((x).pow(2))).add(a)).multiply((BigInteger.valueOf(2).multiply(y)).modInverse(p))).mod(p);
                result[0]=(lambda.pow(2).subtract(x).subtract(x)).mod(p);
                result[1]=(lambda.multiply(x.subtract(result[0])).subtract(y)).mod(p);
            }
            else{
                BigInteger x1=arr1[0];
                BigInteger y1=arr1[1];
                BigInteger x2=arr2[0];
                BigInteger y2=arr2[1];
                BigInteger lambda=(y2.subtract(y1).multiply(mulinv(x2.subtract(x1), p))).mod(p);
                if (lambda.compareTo(BigInteger.ZERO) < 0) lambda = lambda.add(p);
                result[0]=(lambda.pow(2).subtract(x1).subtract(x2)).mod(p);
                if (result[0].compareTo(BigInteger.ZERO) < 0) result[0] = result[0].add(p);
                result[1]=(lambda.multiply(x1.subtract(result[0])).subtract(y1)).mod(p);
                if (result[1].compareTo(BigInteger.ZERO) < 0) result[1] = result[1].add(p);
            }
        return result;
        
        }
    }
    public static BigInteger[] scalarmul(BigInteger n,BigInteger[] arr,BigInteger p,BigInteger a){
        if(n.equals(BigInteger.ZERO)){
            return null;
        }
        if(n.equals(BigInteger.ONE)){
            return arr.clone();
        }
        BigInteger[] result=null;
        BigInteger[] current=arr.clone();
        while(n.compareTo(BigInteger.ZERO)>0){
            if(n.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)){
                result=addition(result,current,p,a);
            }
            current=addition(current,current,p,a);
            n=n.divide(BigInteger.valueOf(2));
        }
        return result;
    }
    public static Boolean pointExists(List<BigInteger[]> points,BigInteger[] point){
        for(BigInteger[] arr:points){
            if(arr[0].equals(point[0]) && arr[1].equals(point[1])){
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args){
            Scanner input=new Scanner(System.in);
            System.out.println("Enter p");
            BigInteger p=input.nextBigInteger();
            System.out.println("Enter a");
            BigInteger a=input.nextBigInteger();
            System.out.println("Enter b");
            ArrayList<BigInteger[]> afpoints=new ArrayList<>();
            BigInteger b=input.nextBigInteger();
            BigInteger lhs=BigInteger.ONE;
            BigInteger rhs=BigInteger.ONE;
            for(int i=0;i<p.intValue()-1;i++){
                BigInteger x=BigInteger.valueOf(i);
                rhs=(x.pow(3).add(a.multiply(x)).add(b)).mod(p);
                for(int j=0;j<p.intValue()-1;j++){
                    BigInteger y=BigInteger.valueOf(j);
                    lhs=(y.pow(2)).mod(p);
                    if(lhs.equals(rhs) && !(pointExists(afpoints,new BigInteger[]{x,y}))){
                        afpoints.add(new BigInteger[]{x,y});
                        if( !(pointExists(afpoints,new BigInteger[]{x,p.subtract(y)}))){
                            afpoints.add(new BigInteger[]{x,p.subtract(y)});
                        }
                    }
                }

            }
            System.out.println("The affine points are");
            for(BigInteger[] point:afpoints){
                System.out.println(point[0]+","+point[1]);
            }
            input.nextLine();
            System.out.println("Enter global affine points");
            String g=input.nextLine();
            String[] garr=g.split(",");
            BigInteger[] gp=new BigInteger[garr.length];
            for(int i=0;i<garr.length;i++){
                gp[i]=new BigInteger(garr[i].trim());
            }
            System.out.println("Enter the prvt key of A");
            BigInteger na=input.nextBigInteger();
            BigInteger[] pA=scalarmul(na,gp,p,a);
            System.out.println("Enter the prvt key of B");
            BigInteger nb=input.nextBigInteger();
            BigInteger[] pb=scalarmul(nb,gp,p,a);
            System.out.println("The public key of A"+pA[0]+","+pA[1]);
            System.out.println("The public key of b"+pb[0]+","+pb[1]);
            BigInteger[] sa=scalarmul(na,pb,p,a);
            BigInteger[] sb=scalarmul(nb,pA,p,a);
            System.out.println("The sec key of A"+sa[0]+","+sa[1]);
            System.out.println("The sec key of b"+sb[0]+","+sb[1]);
            System.out.println("Enter the random integer k");
            BigInteger k=input.nextBigInteger();
            input.nextLine();
            System.out.println("Enter the msg to be encrypted");
            String m=input.nextLine();
            String[] marr=m.split(",");
            BigInteger[] msg=new BigInteger[marr.length];
            for(int i=0;i<marr.length;i++){
                msg[i]=new BigInteger(marr[i].trim());
            }
            BigInteger[] c1=scalarmul(k,gp,p,a);
            BigInteger[] c2=addition(msg,scalarmul(k,pA,p,a),p,a);
            System.out.println("ans "+ scalarmul(k,pA,p,a)[0]+","+scalarmul(k,pA,p,a)[1]);
            System.out.println("c1 is "+c1[0]+","+c1[1]);
            System.out.println("c2 is "+c2[0]+","+c2[1]);

    }
}