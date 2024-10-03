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
package com.linkedin.pinot.core.data.extractors;

import com.linkedin.pinot.common.data.FieldSpec.DataType;
import com.linkedin.pinot.common.data.Schema;
import com.linkedin.pinot.core.data.GenericRow;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.Test;


public class PlainFieldExtractorTest {
  private static final DataType[] ALL_TYPES =
      {DataType.INT, DataType.LONG, DataType.FLOAT, DataType.DOUBLE, DataType.STRING};
  // All types have single/multi-value version.
  private static final int NUMBER_OF_TYPES = 2 * ALL_TYPES.length;
  private static final int INDEX_OF_STRING_TYPE = NUMBER_OF_TYPES - 2;
  private static final String TEST_COLUMN = "testColumn";
  private static final Schema[] ALL_TYPE_SCHEMAS = new Schema[NUMBER_OF_TYPES];

  static {
    int i = 0;
    for (DataType dataType : ALL_TYPES) {
      ALL_TYPE_SCHEMAS[i++] = new Schema.SchemaBuilder().setSchemaName("testSchema")
          .addSingleValueDimension(TEST_COLUMN, dataType)
          .build();
      ALL_TYPE_SCHEMAS[i++] = new Schema.SchemaBuilder().setSchemaName("testSchema")
          .addMultiValueDimension(TEST_COLUMN, dataType)
          .build();
    }
  }

  private class AnyClassWithToString {
    @Override
    public String toString() {
      return "AnyClass";
    }
  }

  @Test
  public void simpleTest() {
  }

  @Test
  public void nullValueTest() {
  }

  @Test
  public void classWithToStringTest() {
  }

  @Test
  public void automatedTest() {
  }

  @Test
  public void timeSpecStringTest() {
  }

  @Test
  public void differentIncomingOutgoingTimeSpecTest() {
  }
}
