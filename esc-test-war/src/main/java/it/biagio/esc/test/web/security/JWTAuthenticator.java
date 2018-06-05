package it.biagio.esc.test.web.security;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;

@RequestScoped
public class JWTAuthenticator implements HttpAuthenticationMechanism {

    private static final Logger LOGGER = Logger.getLogger(JWTAuthenticator.class.getName());

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER = "Bearer ";

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private TokenProvider tokenProvider;

    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
            HttpMessageContext httpMessageContext) throws AuthenticationException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String token = extractToken(httpMessageContext);

        // valida le credenziali

        if (name != null && password != null) {
            CredentialValidationResult result = identityStoreHandler
                    .validate(new UsernamePasswordCredential(name, password));

            if (result.getStatus().equals(CredentialValidationResult.Status.VALID)) {
                return createToken(result, httpMessageContext);
            }
            // se fallisce l'autorizzazione si ritorna un unauthorized status
            return httpMessageContext.responseUnauthorized();
        } else if (token != null) {
            // caso in cui si è già autenticati by credentials e bisogna
            // validare il token
            return validateToken(token, httpMessageContext);
        }

        return null;
    }

    private AuthenticationStatus validateToken(String token, HttpMessageContext httpMessageContext) {
        try {
            if (tokenProvider.validateToken(token)) {
                JWTCredentials credential = tokenProvider.getCredential(token);
                return httpMessageContext.notifyContainerAboutLogin(credential.getPrincipal(),
                        credential.getAuthorities());
            }
            // se il token è invalido, si risponde con unauthorized status
            return httpMessageContext.responseUnauthorized();
        } catch (ExpiredJwtException eje) {
            LOGGER.log(Level.INFO, "Security exception for user {0} - {1}",
                    new String[] { eje.getClaims().getSubject(), eje.getMessage() });
            return httpMessageContext.responseUnauthorized();
        }

    }

    private AuthenticationStatus createToken(CredentialValidationResult result, HttpMessageContext httpMessageContext) {

        String jwt = tokenProvider.createToken(result.getCallerPrincipal().getName(), result.getCallerGroups());
        httpMessageContext.getResponse().setHeader(AUTHORIZATION_HEADER, BEARER + jwt);
        return httpMessageContext.notifyContainerAboutLogin(result);
    }

    /**
     * Estrae il JWT token dall'HTTP Header
     * 
     * @param httpMessageContext
     * @return
     */
    private String extractToken(HttpMessageContext httpMessageContext) {

        String authorizationHeader = httpMessageContext.getRequest().getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            String token = authorizationHeader.substring(BEARER.length(), authorizationHeader.length());
            return token;
        }
        return null;
    }

}
