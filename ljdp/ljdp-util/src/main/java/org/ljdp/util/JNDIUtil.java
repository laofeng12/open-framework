package org.ljdp.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDIUtil {
    private static Context context;

    public static Context getContext() throws NamingException {
        if(context == null) {
            context = getInitialContext();
        }
        return context;
    }
    
    public static InitialContext getInitialContext() throws NamingException {
        return new InitialContext();
    }
    
    public static Object lookup(String name) throws NamingException{
        return getContext().lookup(name);
    }
    

}
