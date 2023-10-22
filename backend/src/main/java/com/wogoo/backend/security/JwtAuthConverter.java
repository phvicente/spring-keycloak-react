package com.wogoo.backend.security;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final RestTemplate restTemplate;
    private static final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtAuthConverterProperties properties;


    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities =
                Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        String claimName = properties.getPrincipalAttribute() == null ? JwtClaimNames.SUB : properties.getPrincipalAttribute();
        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(claimName));
    }


    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (resourceAccess == null
                || (resource = (Map<String, Object>) resourceAccess.get(properties.getResourceId())) == null
                || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
            return Set.of();
        }
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }


    public UUID convertTokenToUserInfo(String accessToken) {
        // Crie um objeto HttpEntity para conter os cabeçalhos da solicitação
        HttpHeaders headers = createHeaders(accessToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Faça a chamada HTTP
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:9080/realms/loan-services/protocol/openid-connect/userinfo",
                HttpMethod.GET,
                requestEntity,
                String.class  // Especificando que a resposta deve ser mapeada para uma String
        );

        String responseBody = response.getBody();
        if(responseBody != null){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                JsonNode subNode = jsonNode.get("sub");
                if (subNode != null) {
                    return UUID.fromString(subNode.asText());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private HttpHeaders createHeaders(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        return headers;
    }



}
