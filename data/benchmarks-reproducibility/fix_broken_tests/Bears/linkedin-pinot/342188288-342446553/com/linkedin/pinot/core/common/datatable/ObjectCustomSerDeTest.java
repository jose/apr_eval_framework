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
package com.linkedin.pinot.core.common.datatable;

import com.linkedin.pinot.core.query.aggregation.function.customobject.AvgPair;
import com.linkedin.pinot.core.query.aggregation.function.customobject.MinMaxRangePair;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Unit test for {@link ObjectCustomSerDe} class.
 */
public class ObjectCustomSerDeTest {
  private static final long RANDOM_SEED = System.currentTimeMillis();
  private static final Random RANDOM = new Random(RANDOM_SEED);
  private static final String ERROR_MESSAGE = "Random seed: " + RANDOM_SEED;

  private static final int NUM_ITERATIONS = 100;

  /**
   * Test for ser/de of {@link Long}.
   */
  @Test
  public void testLong()
      throws IOException {
  }

  /**
   * Test for ser/de of {@link Double}.
   */
  @Test
  public void testDouble()
      throws IOException {
  }

  /**
   * Test for ser/de of {@link DoubleArrayList}.
   */
  @Test
  public void testDoubleArrayList()
      throws IOException {
  }

  /**
   * Test for ser/de of {@link AvgPair}.
   */
  @Test
  public void testAvgPair()
      throws IOException {
  }

  /**
   * Test for ser/de of {@link MinMaxRangePair}.
   */
  @Test
  public void testMinMaxRangePair()
      throws IOException {
  }

  /**
   * Test for ser/de of {@link HashMap} from {@link String} to {@link Double}.
   */
  @Test
  public void testStringDoubleHashMap()
      throws IOException {
  }

  /**
   * Test for ser/de of {@link IntOpenHashSet}.
   */
  @Test
  public void testIntOpenHashSet()
      throws IOException {
  }
}
