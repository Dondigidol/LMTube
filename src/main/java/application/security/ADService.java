package application.security;

import application.entities.User;
import application.services.LoggerService;
import org.apache.logging.log4j.Level;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.rmi.Naming;
import java.util.Hashtable;
import java.util.logging.Logger;

public class ADService {

    private static DirContext ldapContext;
    private String username;
    private String password;

    public ADService(String username, String password){
        this.username = username;
        this.password = password;
        try {
            init();
        } catch (NamingException e){
            LoggerService.log(Level.ERROR, e.getMessage());
        }

    }

    public ADService(){}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void init() throws NamingException {
        Hashtable<String, String> ldapEnv = new Hashtable<>();
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, "ldap://ad.lmru.tech:389");
        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, "ru1000\\" + username);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, password);
        ldapContext = new InitialDirContext(ldapEnv);
    }

    public User getUser() throws NamingException{

        SearchControls searchControls = new SearchControls();
        String[] returnedAttrs = {"sAMAccountName","displayName","title"};
        searchControls.setReturningAttributes(returnedAttrs);
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String searchFilter = "(&(sAMAccountName=" + username +"))";
        String searchBase = "ou=Shops,ou=Leroy Merlin Vostok,dc=hq,dc=ru,dc=corp,dc=leroymerlin,dc=com";
        NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchControls);

        if (answer.hasMoreElements()){
            User user = new User();
            SearchResult sr = answer.next();
            user.setUsername(sr.getAttributes().get("sAMAccountName").get(0).toString());
            user.setFullName(sr.getAttributes().get("displayName").get(0).toString());
            user.setPosition(sr.getAttributes().get("title").get(0).toString());
            return user;
        }
        return null;
    }




}
