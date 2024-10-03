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
package com.linkedin.pinot.queries;

import com.linkedin.pinot.common.response.broker.BrokerResponseNative;
import org.testng.annotations.Test;


public class InterSegmentAggregationMultiValueQueriesTest extends BaseMultiValueQueriesTest {
  private static String SV_GROUP_BY = " group by column8";
  private static String MV_GROUP_BY = " group by column7";

  @Test
  public void testCountMV() {
  }

  @Test
  public void testMaxMV() {
  }

  @Test
  public void testMinMV() {
  }

  @Test
  public void testSumMV() {
  }

  @Test
  public void testAvgMV() {
  }

  @Test
  public void testMinMaxRangeMV() {
  }

  @Test
  public void testDistinctCountMV() {
  }

  @Test
  public void testDistinctCountHLLMV() {
  }

  @Test
  public void testPercentile50MV() {
  }

  @Test
  public void testPercentile90MV() {
  }

  @Test
  public void testPercentile95MV() {
  }

  @Test
  public void testPercentile99MV() {
  }

  @Test
  public void testPercentileEst50MV() {
  }

  @Test
  public void testPercentileEst90MV() {
  }

  @Test
  public void testPercentileEst95MV() {
  }

  @Test
  public void testPercentileEst99MV() {
  }
}
