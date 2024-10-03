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
import com.linkedin.pinot.common.utils.StringUtil;
import com.linkedin.pinot.core.common.predicate.InPredicate;
import com.linkedin.pinot.core.common.predicate.NotInPredicate;
import com.linkedin.pinot.core.operator.filter.predicate.InPredicateEvaluatorFactory;
import com.linkedin.pinot.core.operator.filter.predicate.NotInPredicateEvaluatorFactory;
import com.linkedin.pinot.core.operator.filter.predicate.PredicateEvaluator;
import it.unimi.dsi.fastutil.doubles.DoubleOpenHashSet;
import it.unimi.dsi.fastutil.doubles.DoubleSet;
import it.unimi.dsi.fastutil.floats.FloatOpenHashSet;
import it.unimi.dsi.fastutil.floats.FloatSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * Unit test for all implementations of no-dictionary based predicate evaluators.
 */
public class NoDictionaryInPredicateEvaluatorTest {

  private static final String COLUMN_NAME = "column";
  private static final int NUM_PREDICATE_VALUES = 100;
  private static final int NUM_MULTI_VALUES = 10;
  private static final int MAX_STRING_LENGTH = 100;
  private Random _random;

  @BeforeClass
  public void setup() {
    _random = new Random();
  }

  @Test
  public void testIntPredicateEvaluators() {
  }

  @Test
  public void testLongPredicateEvaluators() {
  }

  @Test
  public void testFloatPredicateEvaluators() {
  }

  @Test
  public void testDoublePredicateEvaluators() {
  }

  @Test
  public void testStringPredicateEvaluators() {
  }
}
