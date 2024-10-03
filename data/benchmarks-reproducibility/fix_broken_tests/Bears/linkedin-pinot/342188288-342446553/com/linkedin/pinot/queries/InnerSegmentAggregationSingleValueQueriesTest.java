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

import com.linkedin.pinot.core.operator.blocks.IntermediateResultsBlock;
import com.linkedin.pinot.core.operator.query.AggregationGroupByOperator;
import com.linkedin.pinot.core.operator.query.AggregationOperator;
import org.testng.annotations.Test;


@SuppressWarnings("ConstantConditions")
public class InnerSegmentAggregationSingleValueQueriesTest extends BaseSingleValueQueriesTest {
  private static final String AGGREGATION = " COUNT(*), SUM(column1), MAX(column3), MIN(column6), AVG(column7)";

  // ARRAY_BASED
  private static final String SMALL_GROUP_BY = " GROUP BY column9";
  // INT_MAP_BASED
  private static final String MEDIUM_GROUP_BY = " GROUP BY column9, column11, column12";
  // LONG_MAP_BASED
  private static final String LARGE_GROUP_BY = " GROUP BY column1, column6, column9, column11, column12";
  // ARRAY_MAP_BASED
  private static final String VERY_LARGE_GROUP_BY =
      " GROUP BY column1, column3, column6, column7, column9, column11, column12, column17, column18";

  @Test
  public void testAggregationOnly() {
  }

  @Test
  public void testSmallAggregationGroupBy() {
  }

  @Test
  public void testMediumAggregationGroupBy() {
  }

  @Test
  public void testLargeAggregationGroupBy() {
  }

  @Test
  public void testVeryLargeAggregationGroupBy() {
  }
}
