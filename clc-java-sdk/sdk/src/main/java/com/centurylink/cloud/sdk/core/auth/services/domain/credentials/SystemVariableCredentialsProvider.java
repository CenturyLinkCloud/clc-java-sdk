package com.centurylink.cloud.sdk.core.auth.services.domain.credentials;

/**
 * Class that provide possibilities to receive credentials from Java VM variables.
 * Default property key for username is <b>clc.username</b> and for password is <b>clc.password</b>.
 *
 * <pre>
 *     <code>
 *         java -jar myapp.jar -Dclc.username=Chuck -Dclc.password=Norris
 *     </code>
 * </pre>
 *
 * @author Ilya Drabenia
 */
public class SystemVariableCredentialsProvider implements CredentialsProvider {
    private final String usernameKey;
    private final String passwordKey;

    private volatile Credentials credentials;

    public SystemVariableCredentialsProvider() {
        this("clc.username", "clc.password");
    }

    public SystemVariableCredentialsProvider(String usernameKey, String passwordKey) {
        this.usernameKey = usernameKey;
        this.passwordKey = passwordKey;

        refresh();
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public CredentialsProvider refresh() {
        credentials = new Credentials(
            System.getProperty(usernameKey),
            System.getProperty(passwordKey)
        );

        return this;
    }

}
