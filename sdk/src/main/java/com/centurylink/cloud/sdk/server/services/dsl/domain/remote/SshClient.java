package com.centurylink.cloud.sdk.server.services.dsl.domain.remote;

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.server.services.dsl.domain.remote.domain.ShellResponse;

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
