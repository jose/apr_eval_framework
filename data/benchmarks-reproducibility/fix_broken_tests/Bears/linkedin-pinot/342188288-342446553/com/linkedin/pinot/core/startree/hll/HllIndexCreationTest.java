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
package com.linkedin.pinot.core.startree.hll;

import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.core.common.DataBlockCache;
import com.linkedin.pinot.core.common.DataFetcher;
import com.linkedin.pinot.core.indexsegment.IndexSegment;
import com.linkedin.pinot.core.indexsegment.columnar.ColumnarSegmentLoader;
import com.linkedin.pinot.core.indexsegment.generator.SegmentVersion;
import com.linkedin.pinot.core.operator.BaseOperator;
import com.linkedin.pinot.core.segment.creator.SegmentIndexCreationDriver;
import com.linkedin.pinot.core.segment.creator.impl.V1Constants;
import com.linkedin.pinot.core.segment.index.SegmentMetadataImpl;
import com.linkedin.pinot.core.segment.index.converter.SegmentV1V2ToV3FormatConverter;
import com.linkedin.pinot.core.segment.index.loader.IndexLoadingConfig;
import com.linkedin.pinot.core.segment.index.loader.Loaders;
import com.linkedin.pinot.core.segment.store.SegmentDirectoryPaths;
import com.linkedin.pinot.startree.hll.HllConfig;
import com.linkedin.pinot.startree.hll.HllConstants;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Dictionary Index Size for Hll Field is roughly 10 times of the corresponding index for Long field.
 */
public class HllIndexCreationTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(HllIndexCreationTest.class);
  private static final String hllDeriveColumnSuffix = HllConstants.DEFAULT_HLL_DERIVE_COLUMN_SUFFIX;

  // change this to change the columns that need to create hll index on
  private static final Set<String> columnsToDeriveHllFields =
      new HashSet<>(Arrays.asList("column1", "column2", "column3",
          "count", "weeksSinceEpochSunday", "daysSinceEpoch",
          "column17", "column18"));
  private static final String AVRO_DATA = "data/test_data-sv.avro";
  private static final String timeColumnName = "daysSinceEpoch";
  private static final TimeUnit timeUnit = TimeUnit.DAYS;

  private static final int hllLog2m = HllConstants.DEFAULT_LOG2M;

  private IndexLoadingConfig v3LoadingConfig;

  private HllConfig hllConfig;

  @BeforeMethod
  public void setUp() throws Exception {
    hllConfig = new HllConfig(hllLog2m, columnsToDeriveHllFields, hllDeriveColumnSuffix);

    v3LoadingConfig = new IndexLoadingConfig();
    v3LoadingConfig.setReadMode(ReadMode.mmap);
    v3LoadingConfig.setSegmentVersion(SegmentVersion.v3);
  }

  @AfterMethod
  public void tearDown() throws Exception {}

  @Test
  public void testColumnStatsWithoutStarTree() {
  }

  @Test
  public void testColumnStatsWithStarTree() throws Exception {
  }

  @Test
  public void testConvert() throws Exception {
  }

}
