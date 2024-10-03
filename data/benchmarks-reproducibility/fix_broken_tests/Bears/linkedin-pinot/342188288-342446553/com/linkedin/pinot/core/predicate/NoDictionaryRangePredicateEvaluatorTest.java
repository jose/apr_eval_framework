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
package com.linkedin.pinot.core.predicate;

import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.core.common.predicate.RangePredicate;
import com.linkedin.pinot.core.operator.filter.predicate.PredicateEvaluator;
import com.linkedin.pinot.core.operator.filter.predicate.RangePredicateEvaluatorFactory;
import java.util.Collections;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Unit test for no-dictionary based range predicate evaluators.
 */
public class NoDictionaryRangePredicateEvaluatorTest {
  private static final String COLUMN_NAME = "column";

  @Test
  public void testIntPredicateEvaluator() {
  }

  @Test
  public void testLongPredicateEvaluator() {
  }

  @Test
  public void testFloatPredicateEvaluator() {
  }

  @Test
  public void testDoublePredicateEvaluator() {
  }

  @Test
  public void testStringPredicateEvaluator() {
  }

  private PredicateEvaluator buildRangePredicate(String rangeString, FieldSpec.DataType dataType) {
    RangePredicate predicate = new RangePredicate(COLUMN_NAME, Collections.singletonList(rangeString));
    return RangePredicateEvaluatorFactory.newRawValueBasedEvaluator(predicate, dataType);
  }
}
