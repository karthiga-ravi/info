import java.nio.ByteBuffer;
import java.util.*;
public class shaalgo {
    public static int leftRotate(int value,int shift){
        System.out.printf("before shift:%s\n",String.format("%32s",Integer.toBinaryString(value)).replace(' ','0'));
        int rotatedVal=(value<<shift|value>>>(32-shift));
        System.out.printf("after shift:%s\n",String.format("%32s",Integer.toBinaryString(rotatedVal)).replace(' ','0'));
        return rotatedVal;
    }
    public static void printHex(byte[] arr){
        for (byte b:arr){
            System.out.printf("%02x",b);
        }
        System.out.println();
    }
    public static byte[] padMsg (byte[] msg){
        int originalLength=msg.length;
        long originalLengthBits=(long)originalLength*8;
        int paddingLength=(56-(originalLength+1)%64 +64)%64;
        byte[] paddedMsg=new byte[paddingLength+originalLength+9];
        System.arraycopy(msg,0,paddedMsg,0,originalLength);
        paddedMsg[originalLength]=0x01;
        ByteBuffer buffer=ByteBuffer.allocate(8);
        buffer.putLong(originalLengthBits);
        byte[] lengthBytes=buffer.array();
        System.arraycopy(lengthBytes,0,paddedMsg,paddedMsg.length-8,8);
        return paddedMsg;
    }
    public static void main(String[] args){
        System.out.println("Enter the msg");
        Scanner input=new Scanner(System.in);
        String msg=input.nextLine();
        byte[] paddedmsg=padMsg(msg.getBytes());
        printHex(paddedmsg);
        int[] w=new int[80];
        for(int i=0;i<16;i++){
            w[i]=(((paddedmsg[i*4] & 0xFF)<<24)|((paddedmsg[i*4+1] & 0xFF)<< 16)|((paddedmsg[i*4+2] & 0xFF)<<8 )|((paddedmsg[i*4+3] & 0xFF)));
        }
        for(int i=16;i<80;i++){
            w[i]=leftRotate(w[i-16]^w[i-14]^w[i-8]^w[i-3],1);
        }
        System.out.println("Enter a");
        int a=Integer.parseUnsignedInt(input.next(),16);
        System.out.println("Enter b");
        int b=Integer.parseUnsignedInt(input.next(),16);
        System.out.println("Enter c");
        int c=Integer.parseUnsignedInt(input.next(),16);
        System.out.println("Enter d");
        int d=Integer.parseUnsignedInt(input.next(),16);
        System.out.println("Enter e");
        int e=Integer.parseUnsignedInt(input.next(),16);
        System.out.println("Enter k");
        int k=Integer.parseUnsignedInt(input.next(),16);
        System.out.println("Enter round");
        int round=input.nextInt();
        System.out.println("Enter step");
        int step=input.nextInt();
        if(step<0||step>79){
            return;
        }
        int f;
        if(step<20){
            f=(b&c) | (~b&d);
        }
        else if(step<40){
            f=b^c^d;
        }
        else if(step<60){
            f=(b&c)|(c&d)|(b&d);
        }
        else{
            f=b^c^d;
        }
        int temp=f+e+k+w[step]+leftRotate(a,5);
        e=d;
        d=c;
        c=leftRotate(b,30);
        b=a;
        a=temp;
        System.out.printf("the values after one step\n a=%08x\n b=%08x\n c=%08x\n d=%08x\n e=%08x\n",a,b,c,d,e);

    }
}