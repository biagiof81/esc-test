package it.biagio.esc.test.web.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
/**
 * 
 * @author faonio
 * 
 * classe dummy per la relazione user-groups
 */
@RequestScoped
public class AuthorizationIdentityStore implements IdentityStore{
    
    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";
    
    private Map<String, Set<String>> groupsPerCaller;

    @PostConstruct
    public void init() {
        groupsPerCaller = new HashMap<String, Set<String>>();
        groupsPerCaller.put("faonio",new HashSet<String>(Arrays.asList(USER, ADMIN)));
        groupsPerCaller.put("emanuele", Collections.singleton(USER));
    }

    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        Set<String> result = groupsPerCaller.get(validationResult.getCallerPrincipal().getName());
        if (result == null) {
            result = Collections.emptySet();
        }
        return result;
    }

    public Set<ValidationType> validationTypes() {
       
        return Collections.singleton(PROVIDE_GROUPS);
    }

    
}
