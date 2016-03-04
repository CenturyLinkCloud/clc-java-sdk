package com.centurylink.cloud.sdk.server.services.dsl.domain.remote;

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.core.preconditions.Preconditions;
import com.centurylink.cloud.sdk.core.util.Strings;
import com.centurylink.cloud.sdk.server.services.client.domain.server.ServerCredentials;
import com.centurylink.cloud.sdk.server.services.dsl.domain.remote.domain.ShellResponse;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.centurylink.cloud.sdk.core.preconditions.ArgumentPreconditions.notNull;
import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkArgument;
import static java.time.Duration.ofMinutes;
import static net.schmizz.sshj.common.IOUtils.closeQuietly;

/**
 * @author Anton Karavayeu
 */
public class SshjClient implements SshClient {
    public static final int CONNECTION_TIMEOUT = 15;

    private final SSHClient ssh;
    private final ServerCredentials credentials;
    private final String host;
    private List<String> commandList = new ArrayList<>();

    SshjClient(SSHClient sshClient, String host, ServerCredentials credentials) {
        this.ssh = sshClient;
        this.host = host;
        this.credentials = credentials;
    }

    private SshjClient(String host, ServerCredentials credentials) {
        this(new SSHClient(), host, credentials);
    }

    public static class Builder {
        private String username;
        private String host;
        private Optional<String> password = Optional.empty();
        private Optional<String> privateKey = Optional.empty();

        public Builder username(String username) {
            Preconditions.checkNotNull(username);
            this.username = username;
            return this;
        }

        public Builder privateKey(String privateKey) {
            this.privateKey = Optional.ofNullable(privateKey);
            return this;
        }

        public Builder password(String password) {
            this.password = Optional.ofNullable(password);
            return this;
        }

        public SshjClient build() {
            ServerCredentials serverCredentials = new ServerCredentials();
            serverCredentials.setUserName(username);

            if (password.isPresent()) {
                serverCredentials.setPassword(password.get());
            }

            if (privateKey.isPresent()) {
                serverCredentials.setPassword(privateKey.get());
            }

            return new SshjClient(host, serverCredentials);
        }

        public Builder host(String publicIp) {
            Preconditions.checkNotNull(publicIp);
            this.host = publicIp;
            return this;
        }
    }

    @Override
    public SshjClient run(String command) {
        commandList.add(command);
        return this;
    }

    @Override
    public SshClient runScript(String pathToFile) {
        try {
            commandList.addAll(getCommandsFromScript(pathToFile));
        } catch (Exception e) {
            throw new SshException(e);
        }
        return this;
    }

    @FunctionalInterface
    public interface SshConnectFunc {
        void connect() throws IOException;
    }

    private void withTimeout(Duration timeout, SshConnectFunc func) {
        notNull(func);
        notNull(timeout);

        long startTime = System.currentTimeMillis();

        for (; ; ) {
            try {
                func.connect();
                return;
            } catch (IOException ex) {
                long curTime = System.currentTimeMillis();
                if (timeout.toMillis() < curTime - startTime) {
                    throw new SshException(ex);
                }
            }
        }
    }

    @Override
    public OperationFuture<ShellResponse> execute() throws SshException {
        ShellResponse response = null;
        Session session = null;
        try {
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            withTimeout(ofMinutes(CONNECTION_TIMEOUT), () -> ssh.connect(host));
            ssh.authPassword(credentials.getUserName(), credentials.getPassword());

            for (String command : commandList) {
                session = ssh.startSession();
                response = execCommand(session, command);
            }
        } catch (IOException e) {
            throw new SshException(e);
        } finally {
            closeQuietly(ssh, session);
        }

        return new OperationFuture<>(response, new NoWaitingJobFuture());
    }

    ShellResponse execCommand(Session session, String command) throws IOException {
        ShellResponse response;
        Session.Command cmd = session.exec(command);
        String output = IOUtils.readFully(cmd.getInputStream()).toString();
        response = new ShellResponse(exitStatus(cmd), output);
        return response;
    }

    private Integer exitStatus(Session.Command cmd) {
        if (cmd != null && cmd.getExitStatus() != null) {
            return cmd.getExitStatus();
        } else {
            return 0;
        }
    }

    List<String> getCommandsFromScript(String pathToFile) throws Exception {
        checkArgument(!Strings.isNullOrEmpty(pathToFile), "Path to file must NOT be Null or Empty");

        Path path = pathToFile.startsWith("classpath") ?
            Paths.get(getClass().getClassLoader().getResource(pathToFile.split(":")[1]).toURI())
            : Paths.get(pathToFile);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            return br.lines().collect(Collectors.toList());
        }
    }

    String getHost() {
        return host;
    }

    SSHClient getSsh() {
        return ssh;
    }
}
