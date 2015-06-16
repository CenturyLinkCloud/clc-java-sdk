package com.centurylink.cloud.sdk.servers.services.domain.remote;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.servers.services.domain.remote.domain.ShellResponse;

import java.io.File;

/**
 * @author Anton Karavayeu
 */
public interface SshClient {

    SshClient run(String command);

    SshClient run(File script);

    SshClient sudo(String command);

    OperationFuture<ShellResponse> execute() throws SshException;
}
