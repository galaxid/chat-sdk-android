package co.chatsdk.ui.search;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.ui.threads.UserList;

public class NameInterpreter {
    String str;
    public NameInterpreter(String str){
        this.str = str;
    }
    public String returnName(){
        if(illegal(1)){
            return str;
        }
        else return str.substring(0,str.indexOf('\r'));
    }

    public String returnLoc(){
        if(illegal(2)){
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
        if(illegal(3)){
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
        if(illegal(4)){
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
        if(illegal(5)){
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
    public String returnLink(){
        if(illegal(6)){
            return " ";
        }
        else {
            String s = str + "";
            for(int i =0;i<5;i++) {
                int a = s.indexOf('\r');
                s = s.substring(a+1);
            }
            return s.substring(0,s.indexOf('\r'));
        }
    }
    public String returnIntro(){
        if(illegal(7)){
            return "No Introduction";
        }
        else {
            String s = str + "";
            for(int i =0;i<6;i++) {
                int a = s.indexOf('\r');
                s = s.substring(a+1);
            }
            return s.substring(0,s.indexOf('\r'));
        }
    }

    public boolean illegal(int lim){
        int count=0;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='\r'){
                count++;
            }
        }
        return count<lim;
    }


    public String check(String s){
        if(s==null||s.length()==0||s.equals("null")){
            return " ";
        }
        return s.replace("\r"," ");
    }

    public static boolean isURL2(String str) {
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return match(regex, str);
    }
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isAdmin(){
        if(ChatSDK.currentUser().getEntityID().equals(UserList.ljr))
            return true;
        if(ChatSDK.currentUser().getEntityID().equals(UserList.xuyang1))
            return true;
        if(ChatSDK.currentUser().getEntityID().equals(UserList.jueruil))
            return true;
        if(ChatSDK.currentUser().getEntityID().equals(UserList.jueruilics))
            return true;
        if(ChatSDK.currentUser().getEntityID().equals(UserList.lucas))
            return true;

        return false;
    }


}
