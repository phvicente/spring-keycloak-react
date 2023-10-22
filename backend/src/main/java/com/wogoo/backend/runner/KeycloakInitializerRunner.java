package com.wogoo.backend.runner;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class KeycloakInitializerRunner implements CommandLineRunner {

    private final Keycloak keycloakAdmin;
    private final RestTemplate restTemplate;

    private static final String KEYCLOAK_SERVER_URL = "http://localhost:9080";
    private static final String COMPANY_SERVICE_REALM_NAME = "loan-services";
    private static final String LOANS_APP_CLIENT_ID = "loans-app";
    private static final String LOANS_APP_REDIRECT_URL = "http://localhost:3000/*";
    private static final List<UserPass> LOANS_USER_LIST = Arrays.asList(
            new UserPass("admin", "admin"),
            new UserPass("user", "user"));

    private record UserPass(String username, String password) {
    }

    @Override
    public void run(String... args) {
        log.info("Initializing '{}' realm in Keycloak ...", COMPANY_SERVICE_REALM_NAME);

        Optional<RealmRepresentation> representationOptional = keycloakAdmin.realms()
                .findAll()
                .stream()
                .filter(r -> r.getRealm().equals(COMPANY_SERVICE_REALM_NAME))
                .findAny();
        if (representationOptional.isPresent()) {
            log.info("Removing already pre-configured '{}' realm", COMPANY_SERVICE_REALM_NAME);
            keycloakAdmin.realm(COMPANY_SERVICE_REALM_NAME).remove();
        }

        // Realm
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(COMPANY_SERVICE_REALM_NAME);
        realmRepresentation.setEnabled(true);
        realmRepresentation.setRegistrationAllowed(true);


        // Client
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(LOANS_APP_CLIENT_ID);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setRedirectUris(List.of(LOANS_APP_REDIRECT_URL));
//        clientRepresentation.setDefaultRoles(new String[]{WebSecurityConfig.MOVIES_USER});
        realmRepresentation.setClients(List.of(clientRepresentation));

        // Users
        List<UserRepresentation> userRepresentations = LOANS_USER_LIST.stream()
                .map(userPass -> {
                    // User Credentials
                    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                    credentialRepresentation.setValue(userPass.password());



                    // User
                    UserRepresentation userRepresentation = new UserRepresentation();
                    userRepresentation.setUsername(userPass.username());
                    userRepresentation.setEnabled(true);
                    userRepresentation.setCredentials(List.of(credentialRepresentation));

//                    userRepresentation.setClientRoles(getClientRoles(userPass));

                    return userRepresentation;
                })
                .toList();

        realmRepresentation.setUsers(userRepresentations);
        // Create Realm
        ScopeMappingRepresentation scopeMappingRepresentation = new ScopeMappingRepresentation();

        ClientScopeRepresentation clientScopeRepresentation = new ClientScopeRepresentation();
        clientScopeRepresentation.setName("openid");
        clientScopeRepresentation.setProtocol("openid-connect");
        realmRepresentation.setClientScopes(List.of(clientScopeRepresentation));
        clientRepresentation.setDefaultClientScopes(List.of("openid"));



        keycloakAdmin.realms().create(realmRepresentation);
        List<ClientScopeRepresentation> defaultDefaultClientScopes = keycloakAdmin.realm(COMPANY_SERVICE_REALM_NAME).getDefaultDefaultClientScopes();

        // Testing
        UserPass admin = LOANS_USER_LIST.get(0);
        log.info("Testing getting token for '{}' ...", admin.username());

        Keycloak keycloakMovieApp = KeycloakBuilder.builder().serverUrl(KEYCLOAK_SERVER_URL)
                .realm(COMPANY_SERVICE_REALM_NAME).username(admin.username()).password(admin.password())
                .clientId(LOANS_APP_CLIENT_ID).build();


        log.info("'{}' token: {}", admin.username(), keycloakMovieApp.tokenManager().grantToken().getToken());

        log.info("'{}' initialization completed successfully!", COMPANY_SERVICE_REALM_NAME);

        log.info("Testing getting token for '{}' ...", defaultDefaultClientScopes);
    }

//    private Map<String, List<String>> getClientRoles(UserPass userPass) {
//        List<String> roles = new ArrayList<>();
//        roles.add(WebSecurityConfig.LOANS_USER);
//        if ("admin".equals(userPass.username())) {
//            roles.add(WebSecurityConfig.LOANS_MANAGER);
//        }
//        return Map.of(LOANS_APP_CLIENT_ID, roles);
//    }



}
