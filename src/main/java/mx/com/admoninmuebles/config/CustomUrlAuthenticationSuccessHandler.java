package mx.com.admoninmuebles.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import mx.com.admoninmuebles.constant.RolConst;

public class CustomUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	protected final Log logger = LogFactory.getLog(this.getClass());

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomUrlAuthenticationSuccessHandler() {
        super();
    }

    
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		handle(request, response, authentication);
        clearAuthenticationAttributes(request);
		
	}

    

    protected void handle(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        final String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(final Authentication authentication) {
        boolean isAdminCorp = false;
        boolean isAdminZona = false;
        boolean isAdminBi = false;
        boolean isSocioBi = false;
        boolean isProveedor = false;
        boolean isContador = false;
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
        	
        	
            if (RolConst.ROLE_ADMIN_CORP.equals(grantedAuthority.getAuthority())) {
            	isAdminCorp = true;
                break;
            } else if (RolConst.ROLE_ADMIN_ZONA.equals(grantedAuthority.getAuthority())) {
            	isAdminZona = true;
                break;
            }
            else if (RolConst.ROLE_ADMIN_BI.equals(grantedAuthority.getAuthority())) {
            	isAdminBi = true;
                break;
            }else if (RolConst.ROLE_SOCIO_BI.equals(grantedAuthority.getAuthority())) {
            	isSocioBi = true;
                break;
            }else if (RolConst.ROLE_PROVEEDOR.equals(grantedAuthority.getAuthority())) {
            	isProveedor = true;
                break;
            }else if (RolConst.ROLE_CONTADOR.equals(grantedAuthority.getAuthority())) {
            	isContador = true;
                break;
            }
        }

        if (isAdminCorp) {
            return "/admincorp";
        } else if (isAdminZona) {
            return "/adminzona";
        } else if (isAdminBi) {
            return "/adminbi";
        }else if (isSocioBi) {
            return "/condomino";
        }else if (isProveedor) {
            return "/proveedor";
        }else if (isContador) {
            return "/contador";
        }
        else {
            throw new IllegalStateException();
        }
    }

    /**
     * Removes temporary authentication-related data which may have been stored in the session
     * during the authentication process.
     */
    protected final void clearAuthenticationAttributes(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }



}
