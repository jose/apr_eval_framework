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
package com.linkedin.pinot.query.transform;

import com.linkedin.pinot.core.operator.transform.function.DivisionTransform;
import com.linkedin.pinot.core.operator.transform.function.TransformFunction;
import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Test for {@link DivisionTransform} class.
 */
public class DivisionTransformTest {

  private static final int NUM_ROWS = 1000;
  private static final double EPSILON = 1e-5;

  /**
   * Tests that division of two columns as returned by the {@link DivisionTransform}
   * is as expected.
   */
  @Test
  public void test() {
  }
}
