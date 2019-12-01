package co.chatsdk.ui.search;

public class NameInterpreter {
    String str;
    public NameInterpreter(String str){
        this.str = str;
    }
    public String returnName(){
        if(str.indexOf('\r')<0){
            return str;
        }
        else return str.substring(0,str.indexOf('\r'));
    }

    public String returnLoc(){
        if(str.indexOf('\r')<0){
            return "No Location Listed";
        }
        else {
            String s = str + "";
            for(int i =0;i<1;i++) {
                int a = s.indexOf('\r');
                s = s.substring(a+1);
            }
            return s.substring(0,s.indexOf('\r'));
        }
    }
    public String returnDate(){
        if(str.indexOf('\r')<0){
            return "No Time Listed";
        }
        else {
            String s = str + "";
            for(int i =0;i<2;i++) {
                int a = s.indexOf('\r');
                s = s.substring(a+1);
            }
            return s.substring(0,s.indexOf('\r'));
        }
    }
    public String returnDes(){
        if(str.indexOf('\r')<0){
            return "No Description";
        }
        else {
            String s = str + "";
            for(int i =0;i<3;i++) {
                int a = s.indexOf('\r');
                s = s.substring(a+1);
            }
            return s.substring(0,s.indexOf('\r'));
        }
    }
    public String returnCom(){
        if(str.indexOf('\r')<0){
            return "No Company Information";
        }
        else {
            String s = str + "";
            for(int i =0;i<4;i++) {
                int a = s.indexOf('\r');
                s = s.substring(a+1);
            }
            return s.substring(0,s.indexOf('\r'));
        }
    }

    public String check(String s){
        if(s==null||s.length()==0){
            return " ";
        }
        return s.replace("\r"," ");
    }


}
