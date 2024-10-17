import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

public class Man {
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
    public static void main(String[] args) throws IOException{
        ServerSocket s=new ServerSocket(5001);
        Socket cs=s.accept();
        System.out.println("client connected to attacker");
        Socket ss=new Socket("localhost",5000);
        System.out.println("attacker connected to server");

        DataInputStream cin=new DataInputStream(cs.getInputStream());
        DataOutputStream cout=new DataOutputStream(cs.getOutputStream());
        DataInputStream sin=new DataInputStream(ss.getInputStream());
        DataOutputStream sout=new DataOutputStream(ss.getOutputStream());

        BigInteger p=new BigInteger(sin.readUTF());
        BigInteger g=new BigInteger(sin.readUTF());
        BigInteger spub=new BigInteger(sin.readUTF());

        if(!isprimeroot(g,p)){
            System.out.println("g is not primitive root of p");
        }
        else{
            System.out.println("g is a primitive root of p");   
        }

        cout.writeUTF(p.toString());
        cout.writeUTF(g.toString());

        SecureRandom ran=new SecureRandom();
        BigInteger atpri=new BigInteger(len,ran);
        BigInteger atpub=g.modPow(atpri,p);

        cout.writeUTF(atpub.toString());
        BigInteger cpub=new BigInteger(cin.readUTF());

        sout.writeUTF(cpub.toString());

        BigInteger sharedwithserver=spub.modPow(atpri,p);
        BigInteger sharedwithclient=cpub.modPow(atpri,p);
        System.out.println("shared key with client:"+sharedwithclient);
        System.out.println("shared key with server:"+sharedwithserver);

        cs.close();
        ss.close();
    }
}
