package com.huoniao.oc.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import com.huoniao.oc.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class EditFilterUtils {

  public static  InputFilter inputFilter=new InputFilter() {

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_,.?!:;…~_\\-\"\"/@*+'()<>{}/[/]()<>{}\\[\\]=%&$|\\/♀♂#¥£¢€\"^`\n ，。？！：；……～“”、“（）”、（——）‘’＠‘·’＆＊＃《》￥《〈〉》〈＄〉［］￡［］｛｝｛｝￠【】【】％〖〗〖〗／〔〕〔〕＼『』『』＾「」「」｜﹁﹂｀．]");

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher=  pattern.matcher(charSequence);
            if(!matcher.find()){
                return null;
            }else{
                Toast.makeText(MyApplication.mContext, "非法字符！", Toast.LENGTH_SHORT).show();
                return "";
            }

        }
    };

    /**只允许输入中文、字母、数字
     *
     * @param str 传入的字符串
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str)throws PatternSyntaxException {
        // 只允许字母、数字和汉字
        String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }

}
