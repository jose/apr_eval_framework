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

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Uninterruptibles;
import com.linkedin.pinot.common.data.DataManager;
import com.linkedin.pinot.common.exception.QueryException;
import com.linkedin.pinot.common.metrics.ServerMetrics;
import com.linkedin.pinot.common.query.QueryExecutor;
import com.linkedin.pinot.common.query.ServerQueryRequest;
import com.linkedin.pinot.common.utils.DataTable;
import com.linkedin.pinot.core.common.datatable.DataTableFactory;
import com.linkedin.pinot.core.common.datatable.DataTableImplV2;
import com.linkedin.pinot.core.query.scheduler.resources.PolicyBasedResourceManager;
import com.linkedin.pinot.core.query.scheduler.resources.ResourceLimitPolicy;
import com.linkedin.pinot.core.query.scheduler.resources.ResourceManager;
import com.yammer.metrics.core.MetricsRegistry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.linkedin.pinot.core.query.scheduler.TestHelper.*;
import static org.testng.Assert.*;


public class PrioritySchedulerTest {
  private static final ServerMetrics metrics = new ServerMetrics(new MetricsRegistry());
  private static boolean useBarrier = false;
  private static CyclicBarrier startupBarrier;
  private static CyclicBarrier validationBarrier;
  private static CountDownLatch numQueries = new CountDownLatch(1);

  @AfterMethod
  public void afterMethod() {
    useBarrier = false;
    startupBarrier = null;
    validationBarrier = null;
    numQueries = new CountDownLatch(1);
  }

  // Tests that there is no "hang" on stop
  @Test
  public void testStartStop() throws InterruptedException {
  }

  @Test
  public void testStartStopQueries() throws ExecutionException, InterruptedException, IOException {
  }

  @Test
  public void testOneQuery() throws InterruptedException, ExecutionException, IOException, BrokenBarrierException {
  }

  @Test
  public void testMultiThreaded() throws InterruptedException {
  }

  /*
   * Disabled because of race condition
   */
  @Test (enabled = false)
  public void testOutOfCapacityResponse()
      throws Exception{
  }

  @Test
  public void testSubmitBeforeRunning() throws ExecutionException, InterruptedException, IOException {
  }

  static class TestPriorityScheduler extends PriorityScheduler {
    static TestSchedulerGroupFactory groupFactory;

    public static TestPriorityScheduler create(Configuration conf) {
      ResourceManager rm = new PolicyBasedResourceManager(conf);
      QueryExecutor qe = new TestQueryExecutor();
      groupFactory = new TestSchedulerGroupFactory();
      MultiLevelPriorityQueue queue = new MultiLevelPriorityQueue(conf, rm,
          groupFactory, new TableBasedGroupMapper());
      return new TestPriorityScheduler(rm, qe, queue, metrics);
    }

    public static TestPriorityScheduler create() {
      PropertiesConfiguration conf = new PropertiesConfiguration();
      return create(conf);
    }

    // store locally for easy access
    public TestPriorityScheduler(@Nonnull ResourceManager resourceManager, @Nonnull QueryExecutor queryExecutor,
        @Nonnull SchedulerPriorityQueue queue, @Nonnull ServerMetrics metrics) {
      super(resourceManager, queryExecutor, queue, metrics);
    }

    ResourceManager getResourceManager() {
      return this.resourceManager;
    }

    @Override
    public String name() {
      return "TestScheduler";
    }

    public Semaphore getRunningQueriesSemaphore() {
      return runningQueriesSemaphore;
    }

    Thread getSchedulerThread() {
      return scheduler;
    }

    SchedulerPriorityQueue getQueue() {
      return queryQueue;
    }
  }

  static class TestQueryExecutor implements QueryExecutor {

    @Override
    public void init(Configuration queryExecutorConfig, DataManager dataManager, ServerMetrics serverMetrics)
        throws ConfigurationException {

    }

    @Override
    public void start() {

    }

    @Override
    public DataTable processQuery(ServerQueryRequest queryRequest,
        ExecutorService executorService) {
      if (useBarrier) {
        try {
          startupBarrier.await();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      DataTableImplV2 result = new DataTableImplV2();
      result.getMetadata().put("table", queryRequest.getTableName());
      if (useBarrier) {
        try {
          validationBarrier.await();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      numQueries.countDown();
      return result;
    }

    @Override
    public void shutDown() {

    }

    @Override
    public boolean isStarted() {
      return true;
    }

    @Override
    public void updateResourceTimeOutInMs(String resource, long timeOutMs) {

    }
  }
}
