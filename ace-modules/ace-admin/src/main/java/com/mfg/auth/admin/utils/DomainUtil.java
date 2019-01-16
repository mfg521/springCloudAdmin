package com.mfg.auth.admin.utils;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

public class DomainUtil {

    private static final String URL="ladp://172.30.1.49:389";
    private static final String BASEDN = "cn=mengfanguang,ou=IT,dc=cpe,dc=ae";
    private static final String FACTORY="com.sun.jndi.ldap.LdapCtxFactory";
    private static LdapContext context=null;
    private static final Control[] connCtls=null;

    public static void LDAP_connect(){
        Hashtable<String,String> env=new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,FACTORY);
        env.put(Context.PROVIDER_URL,URL+BASEDN);
        env.put(Context.SECURITY_AUTHENTICATION,"simple");
        env.put(Context.SECURITY_PRINCIPAL,BASEDN);
        env.put(Context.SECURITY_CREDENTIALS,"qw@1234");

        try{
            context=new InitialLdapContext(env,connCtls);
            System.out.println("认证成功");
            System.out.println(context.getAttributes("name"));

        } catch (NamingException e) {
            e.printStackTrace();
        }

        if(context!=null){
            try {
                context.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }


}
