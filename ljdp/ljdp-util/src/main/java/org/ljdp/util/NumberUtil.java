package org.ljdp.util;

import java.util.Stack;

public class NumberUtil {

	private static char[] charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	/** 
     * 将10进制转化为62进制  
     * @param number  
     * @return 
     */  
    public static String _10_to_62(long number){  
         Long rest=number;  
         Stack<Character> stack=new Stack<Character>();  
         StringBuilder result=new StringBuilder(0);  
         while(rest!=0){  
             stack.add(charSet[new Long((rest-(rest/62)*62)).intValue()]);  
             rest=rest/62;  
         }  
         for(;!stack.isEmpty();){  
             result.append(stack.pop());  
         }  
         return result.toString();  
  
    }
    
    /** 
     * 将62进制转换成10进制数 
     *  
     * @param ident62 
     * @return 
     */  
    public static Long _62_to_10( String ident62 ) {  
        Long dst = 0L;
        for(int i=0; i<ident62.length(); i++)
        {
            char c = ident62.charAt(i);
            for(int j=0; j<charSet.length; j++)
            {
                if(c == charSet[j])
                {
                    dst = (dst * 62) + j;
                    break;
                }
            }
        }
        return dst;
    }
    
//    public static void main(String[] args) {
//    	System.out.println(NumberUtil._10_to_62(Math.abs(-312378234)));
//    }
}
