package ProductService.configuration;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String REAL_ACCESS = "realm_access";

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccessMap = source.getClaimAsMap(REAL_ACCESS);
        Object roles = realmAccessMap.get("roles");
        if (roles instanceof List stringRoles) {
            return ((List<String>) stringRoles).stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());

        }
        return List.of();
    }

}
