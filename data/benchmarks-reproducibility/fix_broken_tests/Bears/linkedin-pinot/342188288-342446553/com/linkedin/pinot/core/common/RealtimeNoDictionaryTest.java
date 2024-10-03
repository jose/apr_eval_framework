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

package com.linkedin.pinot.core.common;

import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.common.data.MetricFieldSpec;
import com.linkedin.pinot.core.io.readerwriter.PinotDataBufferMemoryManager;
import com.linkedin.pinot.core.io.readerwriter.impl.FixedByteSingleColumnSingleValueReaderWriter;
import com.linkedin.pinot.core.io.writer.impl.DirectMemoryManager;
import com.linkedin.pinot.core.operator.BaseOperator;
import com.linkedin.pinot.core.segment.index.data.source.ColumnDataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class RealtimeNoDictionaryTest {
  private static final String INT_COL_NAME = "intcol";
  private static final String LONG_COL_NAME = "longcol";
  private static final String FLOAT_COL_NAME = "floatcol";
  private static final String DOUBLE_COL_NAME = "doublecol";
  private static final int NUM_ROWS = 1000;
  private int[] _intVals = new int[NUM_ROWS];
  private long[] _longVals = new long[NUM_ROWS];
  private float[] _floatVals = new float[NUM_ROWS];
  private double[] _doubleVals = new double[NUM_ROWS];
  private Random _random;

  private PinotDataBufferMemoryManager _memoryManager;

  @BeforeClass
  public void setUp() {
    _memoryManager = new DirectMemoryManager(RealtimeNoDictionaryTest.class.getName());
  }

  @AfterClass
  public void tearDown() throws Exception {
    _memoryManager.close();
  }

  @SuppressWarnings("Duplicates")
  private DataFetcher makeDataFetcher(long seed) {
    FieldSpec intSpec = new MetricFieldSpec(INT_COL_NAME, FieldSpec.DataType.INT);
    FieldSpec longSpec = new MetricFieldSpec(LONG_COL_NAME, FieldSpec.DataType.LONG);
    FieldSpec floatSpec = new MetricFieldSpec(FLOAT_COL_NAME, FieldSpec.DataType.FLOAT);
    FieldSpec doubleSpec = new MetricFieldSpec(DOUBLE_COL_NAME, FieldSpec.DataType.DOUBLE);
    _random = new Random(seed);

    FixedByteSingleColumnSingleValueReaderWriter intRawIndex = new FixedByteSingleColumnSingleValueReaderWriter(
        _random.nextInt(NUM_ROWS)+1, Integer.SIZE/8, _memoryManager, "int");
    FixedByteSingleColumnSingleValueReaderWriter longRawIndex = new FixedByteSingleColumnSingleValueReaderWriter(
        _random.nextInt(NUM_ROWS)+1, Long.SIZE/8, _memoryManager, "long");
    FixedByteSingleColumnSingleValueReaderWriter floatRawIndex = new FixedByteSingleColumnSingleValueReaderWriter(
        _random.nextInt(NUM_ROWS)+1, Float.SIZE/8, _memoryManager, "float");
    FixedByteSingleColumnSingleValueReaderWriter doubleRawIndex = new FixedByteSingleColumnSingleValueReaderWriter(
        _random.nextInt(NUM_ROWS)+1, Double.SIZE/8, _memoryManager, "double");

    for (int i = 0; i < NUM_ROWS; i++) {
      _intVals[i] = _random.nextInt();
      intRawIndex.setInt(i, _intVals[i]);
      _longVals[i]  = _random.nextLong();
      longRawIndex.setLong(i, _longVals[i]);
      _floatVals[i] = _random.nextFloat();
      floatRawIndex.setFloat(i, _floatVals[i]);
      _doubleVals[i] = _random.nextDouble();
      doubleRawIndex.setDouble(i, _doubleVals[i]);
    }

    Map<String, BaseOperator> dataSourceBlock = new HashMap<>();
    dataSourceBlock.put(INT_COL_NAME, new ColumnDataSource(intSpec, NUM_ROWS, 0, intRawIndex, null, null));
    dataSourceBlock.put(LONG_COL_NAME, new ColumnDataSource(longSpec, NUM_ROWS, 0, longRawIndex, null, null));
    dataSourceBlock.put(FLOAT_COL_NAME, new ColumnDataSource(floatSpec, NUM_ROWS, 0, floatRawIndex, null, null));
    dataSourceBlock.put(DOUBLE_COL_NAME, new ColumnDataSource(doubleSpec, NUM_ROWS, 0, doubleRawIndex, null, null));

    return new DataFetcher(dataSourceBlock);
  }

  @Test
  public void testIntColumn() throws Exception {
  }

  @Test
  public void testLongValues() throws Exception {
  }

  @Test
  public void testFloatValues() throws Exception {
  }

  @Test
  public void testDoubleValues() throws Exception {
  }
}
