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
package com.linkedin.pinot.query.aggregation;

import com.linkedin.pinot.core.query.aggregation.AggregationResultHolder;
import com.linkedin.pinot.core.query.aggregation.DoubleAggregationResultHolder;
import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Test for DoubleAggregationResultHolder class.
 */
@Test
public class DoubleAggregationResultHolderTest {
  private static final long RANDOM_SEED = System.nanoTime();
  private static final Random _random = new Random(RANDOM_SEED);
  private static final double DEFAULT_VALUE = _random.nextDouble();

  @Test
  void testDefaultValue() {
  }

  /**
   * This test is for the AggregationResultHolder.SetValue() api.
   * - Sets a random value in the result holder.
   * - Asserts that the value returned by the result holder is as expected.
   */
  @Test
  void testSetValue() {
  }
}
