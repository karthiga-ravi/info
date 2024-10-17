import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.SecureRandom;

public class Cman {
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
            Socket s=new Socket("localhost",5001);
            System.out.println("client connected attacker");

            DataInputStream i=new DataInputStream(s.getInputStream());
            DataOutputStream o=new DataOutputStream(s.getOutputStream());

            BigInteger p=new BigInteger(i.readUTF());
            BigInteger g=new BigInteger(i.readUTF());
            if(!isprimeroot(g,p)){
                System.out.println("g is not primitive root of p");
            }
            else{
                System.out.println("g is a primitive root of p");   
            }

            BigInteger spub=new BigInteger(i.readUTF());

            SecureRandom sec=new SecureRandom();
            BigInteger cpri=new BigInteger(len,sec);
            System.out.println("clients private key:"+cpri);

            BigInteger cpub=g.modPow(cpri,p);
            System.out.println("clients public key:"+cpub);

            o.writeUTF(cpub.toString());
            BigInteger ss=spub.modPow(cpri,p);
            System.out.println("shared key with server:"+ss);
            s.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
