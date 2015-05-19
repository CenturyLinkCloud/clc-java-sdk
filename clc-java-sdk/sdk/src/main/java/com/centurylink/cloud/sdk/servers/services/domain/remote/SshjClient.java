package com.centurylink.cloud.sdk.servers.services.domain.remote;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.servers.client.domain.server.ServerCredentials;
import com.centurylink.cloud.sdk.servers.services.domain.remote.domain.ShellResponse;
import com.google.common.base.Preconditions;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.UserAuthException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Anton Karavayeu
 */
public class SshjClient implements SshClient {
    private final SSHClient ssh;
    private final ServerCredentials credentials;
    private final String host;
    private JobFuture job;

    private SshjClient(String host, ServerCredentials credentials) throws UserAuthException, TransportException {
        ssh = new SSHClient();
        this.host = host;
        this.credentials = credentials;
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
            try {
                return new SshjClient(host, serverCredentials);
            } catch (UserAuthException | TransportException e) {
                // TODO: add logger
                throw new ClcException(e);
            }
        }

        public Builder host(String publicIp) {
            Preconditions.checkNotNull(publicIp);
            this.host = publicIp;
            return this;
        }
    }

    @Override
    public SshjClient run(String command) {
        return this;
    }

    @Override
    public SshClient run(File script) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SshjClient sudo(String command) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OperationFuture<ShellResponse> execute() {
        ShellResponse response = null;
        Session session = null;
        try {
            ssh.connect(host);
            ssh.authPassword(credentials.getUserName(), credentials.getPassword());
            session = ssh.startSession();
            // TODO: think how to pass commands
            Session.Command cmd = session.exec("echo test");
            String output = IOUtils.readFully(cmd.getInputStream()).toString();
            response = new ShellResponse(cmd.getExitStatus(), output);
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (TransportException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                ssh.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new OperationFuture<>(response, new NoWaitingJobFuture());
    }
}
