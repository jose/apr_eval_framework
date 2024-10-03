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
package com.linkedin.pinot.query.aggregation.groupby;

import com.linkedin.pinot.common.response.broker.GroupByResult;
import com.linkedin.pinot.core.query.aggregation.function.AggregationFunction;
import com.linkedin.pinot.core.query.aggregation.function.AggregationFunctionFactory;
import com.linkedin.pinot.core.query.aggregation.groupby.AggregationGroupByTrimmingService;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class AggregationGroupByTrimmingServiceTest {
  private static final long RANDOM_SEED = System.currentTimeMillis();
  private static final Random RANDOM = new Random(RANDOM_SEED);
  private static final String ERROR_MESSAGE = "Random seed: " + RANDOM_SEED;

  private static final AggregationFunction SUM = AggregationFunctionFactory.getAggregationFunction("SUM");
  private static final AggregationFunction DISTINCTCOUNT =
      AggregationFunctionFactory.getAggregationFunction("DISTINCTCOUNT");
  private static final AggregationFunction[] AGGREGATION_FUNCTIONS = {SUM, DISTINCTCOUNT};
  private static final int NUM_GROUP_KEYS = 3;
  private static final int GROUP_BY_TOP_N = 100;
  private static final int NUM_GROUPS = 50000;
  private static final int MAX_SIZE_OF_SET = 50;

  private List<String> _groups;
  private AggregationGroupByTrimmingService _trimmingService;

  @BeforeClass
  public void setUp() {
    // Generate a list of random groups.
    Set<String> groupSet = new HashSet<>(NUM_GROUPS);
    while (groupSet.size() < NUM_GROUPS) {
      List<String> group = new ArrayList<>(NUM_GROUP_KEYS);
      for (int i = 0; i < NUM_GROUP_KEYS; i++) {
        // Randomly generate group key without GROUP_KEY_DELIMITER
        group.add(RandomStringUtils.random(RANDOM.nextInt(10))
            .replace(AggregationGroupByTrimmingService.GROUP_KEY_DELIMITER, ""));
      }
      groupSet.add(buildGroupString(group));
    }
    _groups = new ArrayList<>(groupSet);

    // Explicitly set an empty group
    StringBuilder emptyGroupBuilder = new StringBuilder();
    for (int i = 1; i < NUM_GROUP_KEYS; i++) {
      emptyGroupBuilder.append(AggregationGroupByTrimmingService.GROUP_KEY_DELIMITER);
    }
    _groups.set(NUM_GROUPS - 1, emptyGroupBuilder.toString());

    _trimmingService = new AggregationGroupByTrimmingService(AGGREGATION_FUNCTIONS, GROUP_BY_TOP_N);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testTrimming() {
  }

  private static String buildGroupString(List<String> group) {
    StringBuilder groupStringBuilder = new StringBuilder();
    for (int i = 0; i < NUM_GROUP_KEYS; i++) {
      if (i != 0) {
        groupStringBuilder.append(AggregationGroupByTrimmingService.GROUP_KEY_DELIMITER);
      }
      groupStringBuilder.append(group.get(i));
    }
    return groupStringBuilder.toString();
  }
}
