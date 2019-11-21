package com.huoniao.oc.util;

import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
/**
 * 验证手机号、邮箱、身份证、用户名的规范类
 * 
 * @author 雷鹏杰
 *
 */
 
public class RegexUtils { 
	
	/**
	 * 验证手机号码
	 * @param mobiles
	 * @return
	 */
    public static boolean isMobileNO(String mobiles){     
        Pattern p = Pattern.compile("^[1][2,3,4,5,6,7,8,9][0-9]{9}$");
        Matcher m = p.matcher(mobiles);     

        return m.matches();     
    } 
    /**
     * 验证邮箱
     * 
     * @param email
     * @return
     */
    public static boolean isEmail(String email){     
     String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);     
        Matcher m = p.matcher(email);     
        return m.matches();     
    } 
   
    /**  
     * 验证身份证号码  
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母  
     * @return 验证成功返回true，验证失败返回false  
     */ 
    public static boolean checkIdCard(String idCard) {  
    	if (idCard != null) {
            int correct = new IdCardUtil(idCard).isCorrect();
            if (0 == correct) {// 符合规范
                return true;
            }
        }
        return false;
    }

    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            return IDCard.matches(IDCardRegex);
        }
        return false;
    }
    
    /**
     * 判断是否全是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(str);
    if (!isNum.matches()) {
    return false;
    	}
    return true;
    }
    
    /** 
     * 验证用户名 
     * @param username 用户名 
     * @return boolean 
     */  
    public static boolean checkUsername(String username){  
        String regex = "([a-zA-Z0-9]{6,12})";  
        Pattern p = Pattern.compile(regex);  
        Matcher m = p.matcher(username);  
        return m.matches();  
    }

    /**



     * 匹配Luhn算法：可用于检测银行卡卡号
     * @param cardNo
     * @return
     */
    public static boolean checkBankID(String cardNo) {
        int[] cardNoArr = new int[cardNo.length()];
        for (int i=0; i<cardNo.length(); i++) {
            cardNoArr[i] = Integer.valueOf(String.valueOf(cardNo.charAt(i)));
        }
        for(int i=cardNoArr.length-2;i>=0;i-=2) {

            cardNoArr[i] <<= 1;
            cardNoArr[i] = cardNoArr[i]/10 + cardNoArr[i]%10;
        }
        int sum = 0;
        for(int i=0;i<cardNoArr.length;i++) {
            sum += cardNoArr[i];
        }
        return sum % 10 == 0;
    }
} 