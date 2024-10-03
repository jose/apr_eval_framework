/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.core.query.scheduler;

import com.google.common.base.Preconditions;
import com.linkedin.pinot.common.metrics.ServerMetrics;
import com.linkedin.pinot.core.query.scheduler.resources.PolicyBasedResourceManager;
import com.linkedin.pinot.core.query.scheduler.resources.ResourceLimitPolicy;
import com.linkedin.pinot.core.query.scheduler.resources.ResourceManager;
import com.linkedin.pinot.core.query.scheduler.resources.UnboundedResourceManager;
import com.yammer.metrics.core.MetricsRegistry;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.linkedin.pinot.core.query.scheduler.TestHelper.*;
import static org.testng.Assert.*;


public class MultiLevelPriorityQueueTest {
  SchedulerGroup group;
  final ServerMetrics metrics = new ServerMetrics(new MetricsRegistry());

  final SchedulerGroupMapper groupMapper = new TableBasedGroupMapper();
  final TestSchedulerGroupFactory groupFactory = new TestSchedulerGroupFactory();
  final String groupOne = "1";
  final String groupTwo = "2";

  @BeforeMethod
  public void beforeMethod() {
    groupFactory.reset();
  }

  @Test
  public void testSimplePutTake() throws OutOfCapacityError {
  }

  @Test
  public void testPutOutOfCapacity() throws OutOfCapacityError {
  }

  @Test
  public void testPutForBlockedReader() throws Exception {
  }

  @Test
  public void testTakeWithLimits() throws OutOfCapacityError, BrokenBarrierException, InterruptedException {
  }

  private void sleepForQueueWakeup(MultiLevelPriorityQueue queue) throws InterruptedException {
    // sleep is okay since we sleep for short time
    // add 10 millis to avoid any race condition around time boundary
    Thread.sleep(queue.getWakeupTimeMicros() / 1000 + 10);
  }

  @Test
  public void testNoPendingAfterTrim() throws OutOfCapacityError, BrokenBarrierException, InterruptedException {
  }

  private MultiLevelPriorityQueue createQueue() {
    PropertiesConfiguration conf = new PropertiesConfiguration();
    conf.setProperty(MultiLevelPriorityQueue.QUERY_DEADLINE_SECONDS_KEY, 100000);
    return createQueue(conf, new UnboundedResourceManager(conf));
  }

  private MultiLevelPriorityQueue createQueue(Configuration config, ResourceManager rm) {
    return new MultiLevelPriorityQueue(config, rm, groupFactory, groupMapper);
  }

  // caller needs to start the thread
  class QueueReader {
    private final MultiLevelPriorityQueue queue;
    CyclicBarrier startBarrier = new CyclicBarrier(2);
    CountDownLatch readDoneSignal = new CountDownLatch(1);
    ConcurrentLinkedQueue<SchedulerQueryContext> readQueries = new ConcurrentLinkedQueue<>();
    Thread reader;

    QueueReader(final MultiLevelPriorityQueue queue) {
      Preconditions.checkNotNull(queue);
      this.queue = queue;
      reader = new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            startBarrier.await();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
          readQueries.add(queue.take());
          try {
            readDoneSignal.countDown();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      });
    }

    // this is for main thread that creates reader. Pattern is odd
    // it keeps calling code concise
    // Use this when test expects to read something from queue. This blocks
    // till an entry is read from the queue
    void startAndWaitForRead() throws BrokenBarrierException, InterruptedException {
      reader.start();
      startBarrier.await();
      readDoneSignal.await();
    }

    // Use this if the reader is not expected to complete read after queue wakeup duration
    void startAndWaitForQueueWakeup() throws InterruptedException, BrokenBarrierException {
      reader.start();
      startBarrier.await();
      readDoneSignal.await(queue.getWakeupTimeMicros() +
          TimeUnit.MICROSECONDS.convert(10, TimeUnit.MILLISECONDS), TimeUnit.MICROSECONDS);
    }
  }
}
