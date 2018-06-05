package it.biagio.esc.test.web.security;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 * 
 * @author faonio
 * 
 *         Classe dummy che funge da identity store volatile degli user
 *
 */
@RequestScoped
public class AuthenticationIdentityStore implements IdentityStore {

    private Map<String, String> callerToPassword;

    @PostConstruct
    public void init() {
        callerToPassword = new HashMap<String, String>();
        callerToPassword.put("faonio", "biagio");

    }

    public CredentialValidationResult validate(Credential credential) {
        
        CredentialValidationResult result;

        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;
            String expectedPW = callerToPassword.get(usernamePassword.getCaller());
            if (expectedPW != null && expectedPW.equals(usernamePassword.getPasswordAsString())) {
                result = new CredentialValidationResult(usernamePassword.getCaller());
            } else {
                result = INVALID_RESULT;
            }
        } else {
            result = NOT_VALIDATED_RESULT;
        }
        return result;
    }

    public Set<ValidationType> validationTypes() {
        
        return Collections.singleton(VALIDATE);
    }

}
