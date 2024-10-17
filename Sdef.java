import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Scanner;

public class Sdef{
    private static final int len=512;
    public static boolean isprimeroot(BigInteger g,BigInteger p){
        BigInteger phi=p.subtract(BigInteger.ONE);
        BigInteger temp = phi;
        for(BigInteger i=BigInteger.valueOf(2);i.compareTo(phi)<=0;i=i.add(BigInteger.ONE)){
            if (temp.mod(i).equals(BigInteger.ZERO)) {
                // If g^(phi/i) % p == 1, g is not a primitive root
                if (g.modPow(phi.divide(i), p).equals(BigInteger.ONE)) {
                    return false;
                }
                // Remove all factors of i
                while (temp.mod(i).equals(BigInteger.ZERO)) {
                    temp = temp.divide(i);
                }
            }
        }
    
        // Check the last factor if temp > 1
        return temp.equals(BigInteger.ONE) || !g.modPow(phi.divide(temp), p).equals(BigInteger.ONE);
    }
    public static void main(String[] args) {
        try{
            ServerSocket ss=new ServerSocket(5000);
            Socket s=ss.accept();
            System.out.println("client connected to server");

            Scanner sc=new Scanner(System.in);
            DataInputStream input=new DataInputStream(s.getInputStream());
            DataOutputStream output=new DataOutputStream(s.getOutputStream());

            System.out.println("enter a prime number:");
            BigInteger p=new BigInteger(sc.nextLine());
            BigInteger g;
            do{
            System.out.println("enter base g:");
            g=new BigInteger(sc.nextLine());
            if(!isprimeroot(g,p)){
                System.out.println("the g is not the primitive of p");
            }
            }while(!isprimeroot(g,p));

            SecureRandom ran=new SecureRandom();
            BigInteger pri=new BigInteger(len,ran);
            System.out.println("server private key:"+pri);

            BigInteger pub=g.modPow(pri, p);
            System.out.println("server public key:"+pub);

            output.writeUTF(p.toString());
            output.writeUTF(g.toString());
            output.writeUTF(pub.toString());
            
            BigInteger cpub=new BigInteger(input.readUTF());
            BigInteger sharedkey=cpub.modPow(pri,p);
            System.out.println("shared key with client:"+sharedkey);
            ss.close();
            sc.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
