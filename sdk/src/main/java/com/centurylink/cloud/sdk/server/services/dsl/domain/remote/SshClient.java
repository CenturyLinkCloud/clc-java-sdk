package com.centurylink.cloud.sdk.server.services.dsl.domain.remote;

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.server.services.dsl.domain.remote.domain.ShellResponse;

/**
 * @author Anton Karavayeu
 */
public interface SshClient {

    SshClient run(String command);

    SshClient runScript(String pathToFile);

    OperationFuture<ShellResponse> execute() throws SshException;

}
