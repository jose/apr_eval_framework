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

import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.common.utils.DataSchema;
import com.linkedin.pinot.core.operator.ExecutionStatistics;
import com.linkedin.pinot.core.operator.blocks.IntermediateResultsBlock;
import com.linkedin.pinot.core.operator.query.MSelectionOnlyOperator;
import com.linkedin.pinot.core.operator.query.MSelectionOrderByOperator;
import java.io.Serializable;
import java.util.List;
import java.util.Queue;
import org.testng.Assert;
import org.testng.annotations.Test;


@SuppressWarnings("ConstantConditions")
public class InnerSegmentSelectionMultiValueQueriesTest extends BaseMultiValueQueriesTest {
  private static final String SELECTION = " column1, column5, column6";
  private static final String ORDER_BY = " ORDER BY column5, column9";

  @Test
  public void testSelectStar() {
  }

  @Test
  public void testSelectionOnly() {
  }

  @Test
  public void testSelectionOrderBy() {
  }
}
