package common.security.model;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author asd <br>
 * @create 2021-06-29 5:52 PM <br>
 * @project custom-upms-grpc <br>
 */
public class WebAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 110L;
    protected Object principal;
    protected Object credentials;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal represented
     *     by this authentication object.
     */
    public WebAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public WebAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(true);
    }

    public WebAuthenticationToken(
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
