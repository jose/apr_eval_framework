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
package com.linkedin.pinot.query.pruner;

import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.common.request.BrokerRequest;
import com.linkedin.pinot.common.utils.request.FilterQueryTree;
import com.linkedin.pinot.common.utils.request.RequestUtils;
import com.linkedin.pinot.core.query.pruner.ColumnValueSegmentPruner;
import com.linkedin.pinot.core.segment.index.ColumnMetadata;
import com.linkedin.pinot.pql.parsers.Pql2Compiler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import junit.framework.Assert;
import org.testng.annotations.Test;


/**
 * Unit tests for {@link ColumnValueSegmentPruner} class.
 */
public class ColumnValueSegmentPrunerTest {
  private static final Pql2Compiler COMPILER = new Pql2Compiler();
  private static final Map<String, ColumnMetadata> COLUMN_METADATA_MAP = new HashMap<>();

  static {
    COLUMN_METADATA_MAP.put("time", new ColumnMetadata.Builder().setColumnName("time")
        .setFieldType(FieldSpec.FieldType.TIME)
        .setDataType(FieldSpec.DataType.INT)
        .setTimeUnit(TimeUnit.DAYS)
        .setMinValue(10)
        .setMaxValue(20)
        .build());
    COLUMN_METADATA_MAP.put("foo", new ColumnMetadata.Builder().setColumnName("foo")
        .setFieldType(FieldSpec.FieldType.DIMENSION)
        .setDataType(FieldSpec.DataType.STRING)
        .build());
  }

  @Test
  public void test() {
  }

  private boolean runPruner(String query) {
    BrokerRequest brokerRequest = COMPILER.compileToBrokerRequest(query);
    FilterQueryTree filterQueryTree = RequestUtils.generateFilterQueryTree(brokerRequest);
    ColumnValueSegmentPruner pruner = new ColumnValueSegmentPruner();
    return pruner.pruneSegment(filterQueryTree, COLUMN_METADATA_MAP);
  }
}
