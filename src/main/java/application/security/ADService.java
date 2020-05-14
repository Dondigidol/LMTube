package application.security;

import application.Role;
import application.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

public class ADService {

    private static DirContext ldapContext;
    private String username;
    private String password;

    public ADService(String username, String password){
        this.username = username;
        this.password = password;
    }

    public ADService(){}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void init() throws NamingException {
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
        String[] returnedAttrs = {"sAMAccountName","givenName","sn","title"};
        searchControls.setReturningAttributes(returnedAttrs);
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String searchFilter = "(&(sAMAccountName=" + username +"))";
        String searchBase = "ou=Shops,ou=Leroy Merlin Vostok,dc=hq,dc=ru,dc=corp,dc=leroymerlin,dc=com";
        NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchControls);

        if (answer.hasMoreElements()){
            User user = new User();
            SearchResult sr = answer.next();
            user.setLdap(sr.getAttributes().get("sAMAccountName").get(0).toString());
            user.setName(sr.getAttributes().get("givenName").get(0).toString());
            user.setSurname(sr.getAttributes().get("sn").get(0).toString());
            user.setPosition(sr.getAttributes().get("title").get(0).toString());
            user.setRole(Role.CREATOR.getValue());
            return user;
        }
        return null;
    }




}