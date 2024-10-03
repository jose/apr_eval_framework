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
package com.linkedin.pinot.core.util;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.linkedin.pinot.common.utils.Pairs;
import com.linkedin.pinot.common.utils.Pairs.IntPair;


/**
 * Tests for SortedRangeIntersection utility class.
 *
 * We hardcoded or random generated some lists of sorted doc range pairs (inclusive), and inside each list, there is no
 * overlap between two pairs. Then we compare the SortedRangeIntersection results with our simple set based brute force
 * solution results to verify the correctness of SortedRangeIntersection utility class.
 */
public class SortedRangeIntersectionTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(SortedRangeIntersectionTest.class);

  @Test
  public void testSimple() {
  }

  @Test
  public void testComplex() {
  }

  List<IntPair> constructRangeSet(String formattedString) {
    formattedString = formattedString.replace('[', ' ');
    formattedString = formattedString.replace(']', ' ');
    String[] splits = formattedString.split(",");
    int length = splits.length;
    Preconditions.checkState(length % 2 == 0);

    List<IntPair> pairs = new ArrayList<>();
    for (int i = 0; i < length; i += 2) {
      int start = Integer.parseInt(splits[i].trim());
      int end = Integer.parseInt(splits[i + 1].trim());
      pairs.add(Pairs.intPair(start, end));
    }
    return pairs;
  }

  @Test
  public void testRandom() {
  }
}
