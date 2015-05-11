package com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future;

import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.exceptions.JobFailedException;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.waiting.WaitingLoop;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletionException;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static org.testng.Assert.*;

public class ParallelJobsFutureTest {

    /**
     * Stub for failed job future
     */
    class FailedJobFuture extends AbstractSingleJobFuture {
        @Override
        protected String operationInfo() {
            return "Mocked Operation";
        }

        @Override
        public WaitingLoop waitingLoop() {
            return new SingleWaitingLoop(() -> {
                throw new ClcException("Testing Error");
            });
        }
    }

    private List<JobFuture> failedJobFutures(int count) {
        return rangeClosed(1, count)
            .mapToObj(i -> new FailedJobFuture())
            .collect(toList());
    }

    /**
     * Stub for succeeded job future
     */
    class SuccessJobFuture extends AbstractSingleJobFuture {

        @Override
        protected String operationInfo() {
            return "Mocked Operation";
        }

        @Override
        public WaitingLoop waitingLoop() {
            return new SingleWaitingLoop(() -> true);
        }

    }

    private List<JobFuture> successJobFutures(int count) {
        return rangeClosed(1, count)
            .mapToObj(i -> new SuccessJobFuture())
            .collect(toList());
    }

    @Test(timeOut = 1000L)
    public void testWaitUntilComplete() throws Exception {
        new ParallelJobsFuture(successJobFutures(4)).waitUntilComplete();
    }

    @Test
    public void testWaitUntilComplete_failed() throws Exception {
        try {
            new ParallelJobsFuture(failedJobFutures(2))
                .waitUntilComplete();

            fail("Parallel future must throw exception");
        } catch (Exception ex) {
            assert ex instanceof JobFailedException;
            assertEquals(((JobFailedException) ex).getSubExceptions().size(), 2);
        }
    }

    @Test
    public void testWaitUntilCompleteWithDuration_failed() throws Exception {
        try {
            new ParallelJobsFuture(failedJobFutures(3))
                .waitUntilComplete(Duration.of(10, MINUTES));

            fail("Parallel future must throw exception");
        } catch (Exception ex) {
            assert ex instanceof JobFailedException;
            assertEquals(((JobFailedException) ex).getSubExceptions().size(), 3);
        }
    }

    @Test
    public void testWaitUntilCompleteWithDuration_timedOut() throws Exception {
        try {
            new ParallelJobsFuture(failedJobFutures(3))
                .waitUntilComplete(Duration.of(10, MINUTES));

            fail("Parallel future must throw exception");
        } catch (Exception ex) {
            assert ex instanceof JobFailedException;
            assertEquals(((JobFailedException) ex).getSubExceptions().size(), 3);
        }
    }

    @Test(timeOut = 1000L)
    public void testWaitAsync() throws Exception {
        new ParallelJobsFuture(successJobFutures(3))
            .waitAsync()
            .join();
    }

    @Test
    public void testWaitAsync_failed() throws Exception {
        try {
            new ParallelJobsFuture(failedJobFutures(3))
                .waitAsync()
                .join();

            fail("Parallel future must throw exception");
        } catch (CompletionException ex) {
            Throwable cause = ex.getCause();

            assert cause instanceof JobFailedException;
            assertEquals(((JobFailedException) cause).getSubExceptions().size(), 3);
        }
    }

}