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
package com.linkedin.pinot.core.realtime;

import com.linkedin.pinot.common.data.FieldSpec.FieldType;
import com.linkedin.pinot.common.data.Schema;
import com.linkedin.pinot.common.metadata.segment.RealtimeSegmentZKMetadata;
import com.linkedin.pinot.common.metrics.ServerMetrics;
import com.linkedin.pinot.core.common.Block;
import com.linkedin.pinot.core.common.BlockDocIdIterator;
import com.linkedin.pinot.core.common.BlockSingleValIterator;
import com.linkedin.pinot.core.common.Constants;
import com.linkedin.pinot.core.common.DataSource;
import com.linkedin.pinot.core.common.Predicate;
import com.linkedin.pinot.core.common.predicate.EqPredicate;
import com.linkedin.pinot.core.common.predicate.NEqPredicate;
import com.linkedin.pinot.core.common.predicate.RangePredicate;
import com.linkedin.pinot.core.data.GenericRow;
import com.linkedin.pinot.core.data.readers.FileFormat;
import com.linkedin.pinot.core.io.writer.impl.DirectMemoryManager;
import com.linkedin.pinot.core.operator.filter.BitmapBasedFilterOperator;
import com.linkedin.pinot.core.operator.filter.ScanBasedFilterOperator;
import com.linkedin.pinot.core.operator.filter.predicate.PredicateEvaluator;
import com.linkedin.pinot.core.operator.filter.predicate.PredicateEvaluatorProvider;
import com.linkedin.pinot.core.realtime.impl.FileBasedStreamProviderConfig;
import com.linkedin.pinot.core.realtime.impl.FileBasedStreamProviderImpl;
import com.linkedin.pinot.core.realtime.impl.RealtimeSegmentConfig;
import com.linkedin.pinot.core.realtime.impl.RealtimeSegmentImpl;
import com.linkedin.pinot.core.realtime.impl.RealtimeSegmentStatsHistory;
import com.linkedin.pinot.segments.v1.creator.SegmentTestUtils;
import com.yammer.metrics.core.MetricsRegistry;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;


public class RealtimeSegmentTest {
  private static final String AVRO_DATA = "data/test_data-mv.avro";
  private static String filePath;
  private static Map<String, FieldType> fieldTypeMap;
  private static Schema schema;
  private static RealtimeSegment segmentWithInvIdx;
  private static RealtimeSegment segmentWithoutInvIdx;

  @BeforeClass
  public static void before() throws Exception {
    filePath = RealtimeFileBasedReaderTest.class.getClassLoader().getResource(AVRO_DATA).getFile();
    fieldTypeMap = new HashMap<>();
    fieldTypeMap.put("column1", FieldType.DIMENSION);
    fieldTypeMap.put("column2", FieldType.DIMENSION);
    fieldTypeMap.put("column3", FieldType.DIMENSION);
    fieldTypeMap.put("column4", FieldType.DIMENSION);
    fieldTypeMap.put("column5", FieldType.DIMENSION);
    fieldTypeMap.put("column6", FieldType.DIMENSION);
    fieldTypeMap.put("column7", FieldType.DIMENSION);
    fieldTypeMap.put("column8", FieldType.DIMENSION);
    fieldTypeMap.put("column9", FieldType.DIMENSION);
    fieldTypeMap.put("column10", FieldType.DIMENSION);
    fieldTypeMap.put("weeksSinceEpochSunday", FieldType.DIMENSION);
    fieldTypeMap.put("daysSinceEpoch", FieldType.DIMENSION);
    fieldTypeMap.put("column13", FieldType.TIME);
    fieldTypeMap.put("count", FieldType.METRIC);
    schema = SegmentTestUtils.extractSchemaFromAvro(new File(filePath), fieldTypeMap, TimeUnit.MINUTES);

    StreamProviderConfig config = new FileBasedStreamProviderConfig(FileFormat.AVRO, filePath, schema);
    StreamProvider provider = new FileBasedStreamProviderImpl();
    final String tableName = RealtimeSegmentTest.class.getSimpleName() + ".noTable";
    provider.init(config, tableName, new ServerMetrics(new MetricsRegistry()));

    RealtimeSegmentStatsHistory statsHistory = mock(RealtimeSegmentStatsHistory.class);
    when(statsHistory.getEstimatedAvgColSize(any(String.class))).thenReturn(32);
    when(statsHistory.getEstimatedCardinality(any(String.class))).thenReturn(200);
    RealtimeSegmentConfig realtimeSegmentConfig = new RealtimeSegmentConfig.Builder().setSegmentName("noSegment")
        .setStreamName(AVRO_DATA)
        .setSchema(schema)
        .setCapacity(100000)
        .setAvgNumMultiValues(2)
        .setNoDictionaryColumns(Collections.<String>emptySet())
        .setInvertedIndexColumns(Collections.singleton("count"))
        .setRealtimeSegmentZKMetadata(new RealtimeSegmentZKMetadata())
        .setMemoryManager(new DirectMemoryManager("noSegment"))
        .setStatsHistory(statsHistory)
        .build();
    segmentWithInvIdx = new RealtimeSegmentImpl(realtimeSegmentConfig);
    segmentWithoutInvIdx = RealtimeSegmentTestUtils.createRealtimeSegmentImpl(schema, 100000, "noSegment", AVRO_DATA);
    GenericRow row = provider.next(new GenericRow());
    while (row != null) {
      segmentWithInvIdx.index(row);
      segmentWithoutInvIdx.index(row);
      row = GenericRow.createOrReuseRow(row);
      row = provider.next(row);
    }
    provider.shutdown();
  }

  @Test
  public void testMetricPredicateWithInvIdx() throws Exception {
  }

  @Test
  public void testMetricPredicateWithoutInvIdx() throws Exception {
  }

  @Test
  public void testNoMatchFilteringMetricPredicateWithInvIdx() throws Exception {
  }

  @Test
  public void testNoMatchFilteringMetricPredicateWithoutInvIdx() throws Exception {
  }

  @Test
  public void testRangeMatchFilteringMetricPredicateWithInvIdx() throws Exception {
  }

  @Test
  public void testRangeMatchFilteringMetricPredicateWithoutInvIdx() throws Exception {
  }

  @Test
  public void testNoRangeMatchFilteringMetricPredicateWithInvIdx() throws Exception {
  }

  @Test
  public void testNoRangeMatchFilteringMetricPredicateWithoutInvIdx() throws Exception {
  }
}
