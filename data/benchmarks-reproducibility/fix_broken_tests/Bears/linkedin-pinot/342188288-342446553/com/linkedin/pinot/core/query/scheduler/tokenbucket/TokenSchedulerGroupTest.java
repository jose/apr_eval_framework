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
package com.linkedin.pinot.core.query.scheduler.tokenbucket;

import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class TokenSchedulerGroupTest {

  int timeMillis = 100;
  class TestTokenSchedulerGroup extends TokenSchedulerGroup {
    static final int numTokensPerMs = 100;
    static final int tokenLifetimeMs = 100;
    TestTokenSchedulerGroup() {
      super("testGroup", numTokensPerMs, tokenLifetimeMs);
    }

    @Override
    public long currentTimeMillis() {
      return timeMillis;
    }
  }
  @Test
  public void testIncrementThreads() throws Exception {
  }

  @Test
  public void testStartStopQuery() {
  }

  private void incrementThreads(TokenSchedulerGroup group, int nThreads) {
    for (int i = 0; i < nThreads; i++) {
      group.incrementThreads();
    }
  }

  private void decrementThreads(TokenSchedulerGroup group, int nThreads) {
    for (int i = 0; i < nThreads; i++) {
      group.decrementThreads();
    }
  }
}
