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
package com.linkedin.pinot.core.segment.index.creator;

import com.linkedin.pinot.common.config.ColumnPartitionConfig;
import com.linkedin.pinot.common.config.SegmentPartitionConfig;
import com.linkedin.pinot.common.data.DimensionFieldSpec;
import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.common.data.Schema;
import com.linkedin.pinot.common.request.BrokerRequest;
import com.linkedin.pinot.common.request.FilterOperator;
import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.common.utils.request.FilterQueryTree;
import com.linkedin.pinot.common.utils.request.RequestUtils;
import com.linkedin.pinot.core.data.GenericRow;
import com.linkedin.pinot.core.data.readers.GenericRowRecordReader;
import com.linkedin.pinot.core.indexsegment.IndexSegment;
import com.linkedin.pinot.core.indexsegment.generator.SegmentGeneratorConfig;
import com.linkedin.pinot.core.query.pruner.PartitionSegmentPruner;
import com.linkedin.pinot.core.segment.creator.impl.SegmentIndexCreationDriverImpl;
import com.linkedin.pinot.core.segment.index.ColumnMetadata;
import com.linkedin.pinot.core.segment.index.SegmentMetadataImpl;
import com.linkedin.pinot.core.segment.index.loader.Loaders;
import com.linkedin.pinot.pql.parsers.Pql2Compiler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.IntRange;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * Unit test for Segment partitioning:
 * <ul>
 *   <li> Test to cover segment generation and metadata.</li>
 *   <li> Test to cover segment pruning during query execution. </li>
 * </ul>
 */
public class SegmentPartitionTest {
  private static final String SEGMENT_DIR_NAME =
      System.getProperty("java.io.tmpdir") + File.separator + "partitionTest";
  private static final String TABLE_NAME = "partitionTable";
  private static final String SEGMENT_NAME = "partition";
  private static final String SEGMENT_PATH = SEGMENT_DIR_NAME + File.separator + SEGMENT_NAME;

  private static final int NUM_ROWS = 1001;
  private static final String PARTITIONED_COLUMN_NAME = "partitionedColumn";

  private static final String NON_PARTITIONED_COLUMN_NAME = "nonPartitionedColumn";
  private static final int NUM_PARTITIONS = 20; // For modulo function
  private static final int PARTITION_DIVISOR = 5; // Allowed partition values
  private static final int MAX_PARTITION_VALUE = (PARTITION_DIVISOR - 1);
  private static final String EXPECTED_PARTITION_VALUE_STRING = "[0 " + MAX_PARTITION_VALUE + "]";
  private static final String EXPECTED_PARTITION_FUNCTION = "MoDuLo";
  private IndexSegment _segment;

  @BeforeClass
  public void init()
      throws Exception {
    buildSegment();
  }

  /**
   * Clean up after test
   */
  @AfterClass
  public void cleanup() {
    FileUtils.deleteQuietly(new File(SEGMENT_DIR_NAME));
  }

  /**
   * Unit test:
   * <ul>
   *   <li> Partitioning metadata is written out correctly for column where all values comply to partition scheme. </li>
   *   <li> Partitioning metadata is dropped for column that does not comply to partitioning scheme. </li>
   *   <li> Partitioning metadata is not written out for column for which the metadata was not specified. </li>
   * </ul>
   * @throws Exception
   */
  @Test
  public void testMetadata()
      throws Exception {
  }

  /**
   * Unit test for {@link PartitionSegmentPruner}.
   * <ul>
   *   <li> Generates queries with equality predicate on partitioned column with random values. </li>
   *   <li> Ensures that column values that are in partition range ([0 5]) do not prune the segment,
   *        whereas other values do. </li>
   *   <li> Ensures that predicates on non-partitioned columns do not prune the segment. </li>
   * </ul>
   */
  @Test
  public void testPruner() {
  }

  /**
   * Unit test for utility the converts String ranges into IntRanges and back.
   * <ul>
   *   <li> Generates a list of String ranges</li>
   *   <li> Ensures that conversion to IntRanges is as expected</li>
   *   <li> Ensures that the IntRanges when converted back to String ranges are as expected. </li>
   * </ul>
   */
  @Test
  public void testStringRangeConversions() {
  }

  /**
   * Unit test for {@link SegmentPartitionConfig} that tests the following:
   * <ul>
   *   <li> Conversion from/to JSON string. </li>
   *   <li> Function names, values and ranges are as expected. </li>
   * </ul>
   * @throws IOException
   */
  @Test
  public void testSegmentPartitionConfig()
      throws IOException {
  }

  private String buildQuery(String tableName, String columnName, int predicateValue) {
    return "select count(*) from " + tableName + " where " + columnName + " = " + predicateValue;
  }

  private String buildAndQuery(String tableName, String partitionColumn, int partitionedColumnValue,
      String nonPartitionColumn, int nonPartitionedColumnValue, FilterOperator operator) {
    return "select count(*) from " + tableName + " where " + partitionColumn + " = " + partitionedColumnValue + " "
        + operator + " " + nonPartitionColumn + " = " + nonPartitionedColumnValue;
  }

  private String buildRangeString(int start, int end) {
    return "[" + start + " " + end + "]";
  }

  /**
   * Helper method to build a segment for testing:
   * <ul>
   *   <li> First column is partitioned correctly as per the specification in the segment generation config. </li>
   *   <li> Second column is not partitioned as per the specification in the segment generation config. </li>
   *   <li> Third column does not have any partitioning config in the segment generation config. </li>
   * </ul>
   * @throws Exception
   */
  private void buildSegment()
      throws Exception {
    Schema schema = new Schema();
    schema.addField(new DimensionFieldSpec(PARTITIONED_COLUMN_NAME, FieldSpec.DataType.INT, true));
    schema.addField(new DimensionFieldSpec(NON_PARTITIONED_COLUMN_NAME, FieldSpec.DataType.INT, true));

    Random random = new Random();
    Map<String, ColumnPartitionConfig> partitionFunctionMap = new HashMap<>();

    partitionFunctionMap.put(PARTITIONED_COLUMN_NAME, new ColumnPartitionConfig(EXPECTED_PARTITION_FUNCTION,
        NUM_PARTITIONS));

    SegmentPartitionConfig segmentPartitionConfig = new SegmentPartitionConfig(partitionFunctionMap);
    SegmentGeneratorConfig config = new SegmentGeneratorConfig(schema);

    config.setOutDir(SEGMENT_DIR_NAME);
    config.setSegmentName(SEGMENT_NAME);
    config.setTableName(TABLE_NAME);
    config.setSegmentPartitionConfig(segmentPartitionConfig);

    List<GenericRow> rows = new ArrayList<>(NUM_ROWS);
    for (int i = 0; i < NUM_ROWS; i++) {
      HashMap<String, Object> map = new HashMap<>();

      int validPartitionedValue = random.nextInt(100) * 20 + random.nextInt(PARTITION_DIVISOR);
      map.put(PARTITIONED_COLUMN_NAME, validPartitionedValue);
      map.put(NON_PARTITIONED_COLUMN_NAME, validPartitionedValue);

      GenericRow genericRow = new GenericRow();
      genericRow.init(map);
      rows.add(genericRow);
    }

    SegmentIndexCreationDriverImpl driver = new SegmentIndexCreationDriverImpl();
    driver.init(config, new GenericRowRecordReader(rows, schema));
    driver.build();
    _segment = Loaders.IndexSegment.load(new File(SEGMENT_PATH), ReadMode.mmap);
  }
}
