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

    private List<JobFuture> singleJobFutures(int count) {
        return rangeClosed(1, count)
            .mapToObj(i -> new TestJobFuture())
            .collect(toList());
    }

    class TestJobFuture extends AbstractSingleJobFuture {
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

    @Test
    public void testWaitUntilComplete() throws Exception {
        try {
            new ParallelJobsFuture(singleJobFutures(2))
                .waitUntilComplete();

            fail("Parallel future must throw exception");
        } catch (Exception ex) {
            assert ex instanceof JobFailedException;
            assertEquals(ex.getSuppressed().length, 2);
        }
    }

    @Test
    public void testWaitUntilCompleteWithDuration() throws Exception {
        try {
            new ParallelJobsFuture(singleJobFutures(3))
                .waitUntilComplete(Duration.of(10, MINUTES));

            fail("Parallel future must throw exception");
        } catch (Exception ex) {
            assert ex instanceof JobFailedException;
            assertEquals(ex.getSuppressed().length, 3);
        }
    }

    @Test
    public void testWaitAsync() throws Exception {
        try {
            new ParallelJobsFuture(singleJobFutures(3))
                .waitAsync()
                .join();

            fail("Parallel future must throw exception");
        } catch (CompletionException ex) {
            Throwable cause = ex.getCause();

            assert cause instanceof JobFailedException;
            assertEquals(cause.getSuppressed().length, 3);
        }
    }

}