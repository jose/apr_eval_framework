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
package com.linkedin.pinot.core.common.docidsets;

import com.linkedin.pinot.common.utils.Pairs;
import com.linkedin.pinot.common.utils.Pairs.IntPair;
import com.linkedin.pinot.core.common.BlockDocIdIterator;
import com.linkedin.pinot.core.common.Constants;
import com.linkedin.pinot.core.operator.docidsets.SortedDocIdSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.Assert;
import org.testng.annotations.Test;


public class SortedDocIdSetTest {
  @Test
  public void testEmpty() {
  }

  @Test
  public void testPairWithSameStartAndEnd() {
  }

  @Test
  public void testOnePair() {
  }

  @Test
  public void testTwoPair() {
  }

  @Test
  public void testCustom() {
  }

  private void testCustomRange(String rangeString) {
    String trim =
        rangeString.replace('[', ' ').replace(']', ' ').replace('(', ' ').replace(')', ' ').replaceAll("[\\s]+", "");
    String[] splits = trim.split(",");
    Assert.assertTrue(splits.length % 2 == 0);
    List<Integer> expectedList = new ArrayList<Integer>();
    List<IntPair> pairs = new ArrayList<IntPair>();

    for (int i = 0; i < splits.length; i += 2) {
      int start = Integer.parseInt(splits[i]);
      int end = Integer.parseInt(splits[i + 1]);
      for (int val = start; val <= end; val++) {
        expectedList.add(val);
      }
      pairs.add(Pairs.intPair(start, end));
    }
    SortedDocIdSet sortedDocIdSet = new SortedDocIdSet("Datasource-testCol", pairs);
    BlockDocIdIterator iterator = sortedDocIdSet.iterator();
    List<Integer> result = new ArrayList<Integer>();
    int docId;
    while ((docId = iterator.next()) != Constants.EOF) {
      result.add(docId);
    }
    Assert.assertEquals(result.size(), expectedList.size());
    Assert.assertEquals(result, expectedList);
  }
}
