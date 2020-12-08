package VM;

import DATATYPES.InstructionData;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VirtualMachine {
    private static final VirtualMachine INSTANCE = new VirtualMachine();
    public static VirtualMachine getInstance() { return INSTANCE; }
    public List<InstructionData> I = new ArrayList<InstructionData>();  //Memoria de Programa(instrução)
    public ArrayList<Integer> M = new ArrayList<Integer>();          //Memoria de Dados
    public int i=0,s=0;
    public boolean running=false;

    public VirtualMachine() {

    }


    //LOAD FUNCTIONS TODO: rewrite() & validate --> Allow Comments!??
    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
    private String rewrite(String in){
        String ret=in.toUpperCase();
        ret=ret.replaceAll(" +|\\t+"," ");                 //spaces = tabs
        ret = ret.replaceAll("(?m)\\/{2}.*$", "");          //comments
        ret = ret.replaceAll("(?m)^( +)|( +)$", "");    //empty start or end
        ret = ret.replaceAll("(?m)^[ \t]*\r?\n", "");       //empty lines
        return ret;
    }
    private boolean validate(String in){
        Pattern pat = Pattern.compile("^(\\s*([A-Z]{2,}|L[0-9]+)(\\s|$))(NULL|(L*([0-9]+))(,([0-9]+))*)*", Pattern.MULTILINE);

        Matcher n, m = pat.matcher(in);
        int i;
        boolean v=false;
        for(i=0;m.find();i++) System.out.println("FOUND: ["+m.start()+","+m.end()+"]"+ m.group(0));
        System.out.println("VALIDATE: {i="+i+", countLines="+countLines(in)+"}");
        if(i==countLines(in)){
            //agora checa se todos os labels existem
            v = true;
        }
        System.out.println("VALIDATE: " + v + " value returned.");
        return v;
    }
    private String transLabels(String in){
        String ret = in;
        Pattern label_pos = Pattern.compile("^(L[0-9]+) *", Pattern.MULTILINE);
        Pattern label_pointer = Pattern.compile(" +(L[0-9]+)$", Pattern.MULTILINE);
        Matcher a=label_pos.matcher(ret);
        int ln=0;
        while(a.find()){
            ln=countLines(ret.substring(0,a.end()));
            System.out.println("FOUND>" + a.group(0) + "<-- ln=" + ln);
            ret = ret.replaceAll("(?m) +"+a.group(1)+"$", " "+Integer.toString(ln-1));
        }
        a=label_pos.matcher(ret);
        ret = a.replaceAll("");
        return ret;
    }
    public boolean load(File asm){
        try (BufferedReader br = new BufferedReader(new FileReader(asm))) {
            String tmp,  str = br.readLine()+"\r\n";
            while ((tmp = br.readLine()) != null)   { str+=tmp+"\r\n"; }

            System.out.println("****************************************");
            System.out.println("ORIGINAL: \n"+str);
            System.out.println("=======================================");
            str = rewrite(str);
            if(!validate(str)) { return false; }
            str = transLabels(str);
            System.out.println("FIM: \n"+ str);
            System.out.println("****************************************");

            Pattern pat = Pattern.compile("^(\\s*([A-Z]{2,})( +|$))(([0-9]+)(,([0-9]+))*)*", Pattern.MULTILINE);
            Matcher m = pat.matcher(str);
            Integer x1,x2;
            while(m.find()){
                x1=null;
                x2=null;
                if(m.group(5) != null)
                    x1=Integer.parseInt(m.group(5));
                if(m.group(7) != null)
                    x2=Integer.parseInt(m.group(7));
                InstructionData ins = new InstructionData(m.group(2),x1,x2,"");
                I.add(ins);
            }

        } catch(IOException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void run(){
        running=true;
            try {
                do{
                    exec_instruction(I.get(i));

                }while(running);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public void exec_instruction(InstructionData ins) throws Exception {
        int aux=0;
        switch(ins.get_mnemonic()){
            case "LDC"://OK
                if(M.size() <= s+1)
                    M.add(0);
                s++;
                M.set(s,ins.get_att1());
                i++;
                break;
            case "LDV"://OK
                if(M.size() <= s+1)
                    M.add(0);
                s++;
                M.set(s,M.get(ins.get_att1()));
                i++;
                break;
            case "ADD"://OK
                aux = M.get(s-1) + M.get(s);
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "SUB"://OK
                aux = M.get(s-1) - M.get(s);
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "MULT"://OK
                aux = M.get(s-1) * M.get(s);
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "DIVI"://OK
                aux = M.get(s-1) / M.get(s);
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "INV"://OK
                aux = M.get(s);
                aux = -1 * aux;
                M.set(s,aux);
                i++;
                break;
            case "AND"://OK
                if(M.get(s-1) == 1 && M.get(s) == 1)
                    aux=1;
                else
                    aux=0;
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "OR"://Ok
                if(M.get(s-1) == 1 || M.get(s) == 1)
                    aux=1;
                else
                    aux=0;
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "NEG"://OK
                aux=M.get(s);
                aux=1-aux;
                M.set(s,aux);
                i++;
                break;
            case "CME"://OK
                if(M.get(s-1)<M.get(s))
                    aux=1;
                else
                    aux=0;
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "CMA"://OK
                if(M.get(s-1)>M.get(s))
                    aux=1;
                else
                    aux=0;
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "CEQ"://OK
                if(M.get(s-1)==M.get(s))
                    aux=1;
                else
                    aux=0;
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "CDIF"://OK
                if(M.get(s-1)!=M.get(s))
                    aux=1;
                else
                    aux=0;
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "CMEQ"://OK
                if(M.get(s-1)<=M.get(s))
                    aux=1;
                else
                    aux=0;
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "CMAQ"://OK
                if(M.get(s-1)>=M.get(s))
                    aux=1;
                else
                    aux=0;
                M.set(s-1,aux);
                s--;
                i++;
                break;
            case "START"://OK
                running=true;
                s=-1;
                i++;
                break;
            case "HLT"://OK
                running = false;
                i++;
                JOptionPane.showMessageDialog(null, "HALT");
                break;
            case "STR"://Ok
                while(M.size() <= ins.get_att1()+1)
                    M.add(0);
                aux = M.get(s);
                M.set(ins.get_att1(),aux);
                s--;
                i++;
                break;
            //----------------
            case "JMP"://OK
                i=ins.get_att1();
                break;
            case "JMPF"://OK
                if(M.get(s) == 0)
                    i=ins.get_att1();
                else
                    i++;
                s--;
                break;
            //----------------
            case "NULL"://OK
                i++;
                break;
            case "RD"://OK
                if(M.size() <= s+1)
                    M.add(0);
                s++;
                M.set(s,Integer.parseInt(JOptionPane.showInputDialog(null, "Input integer value:")));
                i++;
                break;
            case "PRN":
                System.out.println("OUTPUT: "+M.get(s));//TODO PRINT M.get(s);
                s--;
                i++;
                break;
            case "ALLOC":
                for (int k=0; k<ins.get_att2();k++){
                    while (M.size() < s + ins.get_att2() + 1 || M.size() < ins.get_att1() + ins.get_att2() + 1)
                            M.add(0);
                    s++;
                    M.set(s,M.get(ins.get_att1()+k));
                }
                i++;
                break;
            case "DALLOC":
                for (int k=ins.get_att2()-1; k>=0;k--){
                    System.out.println("k="+k);
                    //while (M.size() < s + ins.get_att2() + 1 || M.size() < ins.get_att1() + ins.get_att2() + 1)
                    //    M.add(0);
                    M.set(ins.get_att1()+k,M.get(s));
                    s--;
                }
                System.out.println("_-_-_-_-");
                i++;
                break;
            case "CALL":
                if(M.size() <= s+1)
                    M.add(0);
                s++;
                aux=i+1;
                M.set(s,aux);
                i=ins.get_att1();
                break;
            case "RETURN":
                i=M.get(s);
                s--;
                break;
            default:
                break;
        }
    }

   

}
