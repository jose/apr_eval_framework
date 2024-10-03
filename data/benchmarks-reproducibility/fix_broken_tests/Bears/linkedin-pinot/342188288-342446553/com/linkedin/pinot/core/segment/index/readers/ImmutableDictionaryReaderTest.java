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
package com.linkedin.pinot.core.segment.index.readers;

import com.linkedin.pinot.common.data.DimensionFieldSpec;
import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.core.segment.creator.impl.SegmentDictionaryCreator;
import com.linkedin.pinot.core.segment.creator.impl.V1Constants;
import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import it.unimi.dsi.fastutil.doubles.DoubleOpenHashSet;
import it.unimi.dsi.fastutil.floats.FloatOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.io.File;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class ImmutableDictionaryReaderTest {
  private static final File TEMP_DIR = new File(FileUtils.getTempDirectory(), "ImmutableDictionaryReaderTest");
  private static final Random RANDOM = new Random();
  private static final String INT_COLUMN_NAME = "intColumn";
  private static final String LONG_COLUMN_NAME = "longColumn";
  private static final String FLOAT_COLUMN_NAME = "floatColumn";
  private static final String DOUBLE_COLUMN_NAME = "doubleColumn";
  private static final String STRING_COLUMN_NAME = "stringColumn";
  private static final int NUM_VALUES = 1000;
  private static final int MAX_STRING_LENGTH = 100;

  private int[] _intValues;
  private long[] _longValues;
  private float[] _floatValues;
  private double[] _doubleValues;
  private String[] _stringValues;

  private int _numBytesPerStringValue;

  @BeforeClass
  public void setUp() throws Exception {
    FileUtils.deleteQuietly(TEMP_DIR);

    IntOpenHashSet intSet = new IntOpenHashSet();
    while (intSet.size() < NUM_VALUES) {
      intSet.add(RANDOM.nextInt());
    }
    _intValues = intSet.toIntArray();
    Arrays.sort(_intValues);

    LongOpenHashSet longSet = new LongOpenHashSet();
    while (longSet.size() < NUM_VALUES) {
      longSet.add(RANDOM.nextLong());
    }
    _longValues = longSet.toLongArray();
    Arrays.sort(_longValues);

    FloatOpenHashSet floatSet = new FloatOpenHashSet();
    while (floatSet.size() < NUM_VALUES) {
      floatSet.add(RANDOM.nextFloat());
    }
    _floatValues = floatSet.toFloatArray();
    Arrays.sort(_floatValues);

    DoubleOpenHashSet doubleSet = new DoubleOpenHashSet();
    while (doubleSet.size() < NUM_VALUES) {
      doubleSet.add(RANDOM.nextDouble());
    }
    _doubleValues = doubleSet.toDoubleArray();
    Arrays.sort(_doubleValues);

    Set<String> stringSet = new HashSet<>();
    while (stringSet.size() < NUM_VALUES) {
      stringSet.add(RandomStringUtils.random(RANDOM.nextInt(MAX_STRING_LENGTH)).replace('\0', ' '));
    }
    _stringValues = stringSet.toArray(new String[NUM_VALUES]);
    Arrays.sort(_stringValues);

    try (SegmentDictionaryCreator dictionaryCreator = new SegmentDictionaryCreator(false, _intValues,
        new DimensionFieldSpec(INT_COLUMN_NAME, FieldSpec.DataType.INT, true), TEMP_DIR)) {
      dictionaryCreator.build();
    }

    try (SegmentDictionaryCreator dictionaryCreator = new SegmentDictionaryCreator(false, _longValues,
        new DimensionFieldSpec(LONG_COLUMN_NAME, FieldSpec.DataType.LONG, true), TEMP_DIR)) {
      dictionaryCreator.build();
    }

    try (SegmentDictionaryCreator dictionaryCreator = new SegmentDictionaryCreator(false, _floatValues,
        new DimensionFieldSpec(FLOAT_COLUMN_NAME, FieldSpec.DataType.FLOAT, true), TEMP_DIR)) {
      dictionaryCreator.build();
    }

    try (SegmentDictionaryCreator dictionaryCreator = new SegmentDictionaryCreator(false, _doubleValues,
        new DimensionFieldSpec(DOUBLE_COLUMN_NAME, FieldSpec.DataType.DOUBLE, true), TEMP_DIR)) {
      dictionaryCreator.build();
    }

    try (SegmentDictionaryCreator dictionaryCreator = new SegmentDictionaryCreator(false, _stringValues,
        new DimensionFieldSpec(STRING_COLUMN_NAME, FieldSpec.DataType.STRING, true), TEMP_DIR)) {
      dictionaryCreator.build();
      _numBytesPerStringValue = dictionaryCreator.getStringColumnMaxLength();
    }
  }

  @Test
  public void testIntDictionary() throws Exception {
  }

  @Test
  public void testLongDictionary() throws Exception {
  }

  @Test
  public void testFloatDictionary() throws Exception {
  }

  @Test
  public void testDoubleDictionary() throws Exception {
  }

  @Test
  public void testStringDictionary() throws Exception {
  }

  @Test
  public void testOnHeapStringDictionary() throws Exception {
  }

  private void testStringDictionary(ImmutableDictionaryReader stringDictionary) {
    for (int i = 0; i < NUM_VALUES; i++) {
      Assert.assertEquals(stringDictionary.get(i), _stringValues[i]);
      Assert.assertEquals(stringDictionary.getStringValue(i), _stringValues[i]);

      Assert.assertEquals(stringDictionary.indexOf(_stringValues[i]), i);

      // Test String longer than MAX_STRING_LENGTH
      String randomString = RandomStringUtils.random(RANDOM.nextInt(2 * MAX_STRING_LENGTH)).replace('\0', ' ');
      Assert.assertEquals(stringDictionary.insertionIndexOf(randomString),
          Arrays.binarySearch(_stringValues, randomString));
    }
  }

  @AfterClass
  public void tearDown() {
    FileUtils.deleteQuietly(TEMP_DIR);
  }
}
