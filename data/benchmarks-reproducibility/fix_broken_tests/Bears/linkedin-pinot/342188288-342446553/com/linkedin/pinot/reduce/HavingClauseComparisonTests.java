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
package com.linkedin.pinot.reduce;

import com.linkedin.pinot.common.request.BrokerRequest;
import com.linkedin.pinot.common.request.FilterOperator;
import com.linkedin.pinot.core.query.reduce.BetweenComparison;
import com.linkedin.pinot.core.query.reduce.EqualComparison;
import com.linkedin.pinot.core.query.reduce.GreaterEqualComparison;
import com.linkedin.pinot.core.query.reduce.GreaterThanComparison;
import com.linkedin.pinot.core.query.reduce.HavingClauseComparisonTree;
import com.linkedin.pinot.core.query.reduce.InAndNotInComparison;
import com.linkedin.pinot.core.query.reduce.LessEqualComparison;
import com.linkedin.pinot.core.query.reduce.LessThanComparison;
import com.linkedin.pinot.core.query.reduce.NotEqualComparison;
import com.linkedin.pinot.pql.parsers.Pql2Compiler;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.testng.Assert;
import org.testng.annotations.Test;


public class HavingClauseComparisonTests {
  @Test
  public void testBetweenComparison() {
  }

  @Test
  public void testEqualComparison() {
  }

  @Test
  public void testGreaterEqualComparison() {
  }

  @Test
  public void testGreaterComparison() {
  }

  @Test
  public void testLessEqualComparison() {
  }

  @Test
  public void testLessLessThanComparison() {
  }

  @Test
  public void testNotEqualComparison() {
  }

  @Test
  public void testInAndNotINComparison() {
  }

  @Test
  public void testComplexHavingPredicate() {
  }
}
