package DATATYPES;

public class InstructionData {
    private String mnemonic;
    private Integer att1, att2;
    private String comment;

    public InstructionData(){
        mnemonic = "";
        att1=null;
        att2=null;
        comment = "";
    }

    public InstructionData(String instruction, Integer a1, Integer a2, String comentario){
        mnemonic = instruction;
        att1=a1;
        att2=a2;
        comment = comentario;
    }

    public Integer get_att1(){
        return att1;
    }

    public Integer get_att2(){
        return att2;
    }

    public String get_mnemonic() {
        return mnemonic;
    }

    public String get_comment() { return comment; }


    public void set_att1(Integer val) { att1=val; }

    public void set_att2(Integer val) { att2=val; }

    public void set_mnemonic(String val) { mnemonic=val; }

    public void set_comment(String val) { comment=val; }

}