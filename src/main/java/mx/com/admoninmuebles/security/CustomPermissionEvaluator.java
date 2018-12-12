package mx.com.admoninmuebles.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(final Authentication authentication, final Object targetDomainObject, final Object permission) {
        if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(authentication, permission.toString());
    }

    @Override
    public boolean hasPermission(final Authentication authentication, final Serializable targetId, final String targetType, final Object permission) {
        if (authentication == null || targetType == null || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(authentication, permission.toString());
    }

    private boolean hasPrivilege(final Authentication authentication, final String permission) {
        return authentication.getAuthorities().stream().anyMatch(t -> t.getAuthority().equalsIgnoreCase(permission));
    }

}
