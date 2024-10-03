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
package com.linkedin.pinot.core.segment.index.loader;

import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.common.utils.TarGzCompressionUtils;
import com.linkedin.pinot.core.indexsegment.IndexSegment;
import com.linkedin.pinot.core.indexsegment.generator.SegmentGeneratorConfig;
import com.linkedin.pinot.core.indexsegment.generator.SegmentVersion;
import com.linkedin.pinot.core.segment.creator.SegmentIndexCreationDriver;
import com.linkedin.pinot.core.segment.creator.impl.SegmentCreationDriverFactory;
import com.linkedin.pinot.core.segment.creator.impl.V1Constants;
import com.linkedin.pinot.core.segment.index.ColumnMetadata;
import com.linkedin.pinot.core.segment.index.SegmentMetadataImpl;
import com.linkedin.pinot.core.segment.index.converter.SegmentV1V2ToV3FormatConverter;
import com.linkedin.pinot.core.segment.index.readers.StringDictionary;
import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import com.linkedin.pinot.core.segment.store.ColumnIndexType;
import com.linkedin.pinot.core.segment.store.SegmentDirectory;
import com.linkedin.pinot.core.segment.store.SegmentDirectoryPaths;
import com.linkedin.pinot.segments.v1.creator.SegmentTestUtils;
import com.linkedin.pinot.util.TestUtils;
import java.io.File;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class LoadersTest {
  private static final File INDEX_DIR = new File(LoadersTest.class.getName());
  private static final String AVRO_DATA = "data/test_data-mv.avro";
  private static final String PADDING_OLD = "data/paddingOld.tar.gz";
  private static final String PADDING_PERCENT = "data/paddingPercent.tar.gz";
  private static final String PADDING_NULL = "data/paddingNull.tar.gz";

  private File _avroFile;
  private File _indexDir;
  private IndexLoadingConfig _v1IndexLoadingConfig;
  private IndexLoadingConfig _v3IndexLoadingConfig;

  @BeforeClass
  public void setUp() throws Exception {
    FileUtils.deleteQuietly(INDEX_DIR);

    URL resourceUrl = getClass().getClassLoader().getResource(AVRO_DATA);
    Assert.assertNotNull(resourceUrl);
    _avroFile = new File(resourceUrl.getFile());

    _v1IndexLoadingConfig = new IndexLoadingConfig();
    _v1IndexLoadingConfig.setReadMode(ReadMode.mmap);
    _v1IndexLoadingConfig.setSegmentVersion(SegmentVersion.v1);

    _v3IndexLoadingConfig = new IndexLoadingConfig();
    _v3IndexLoadingConfig.setReadMode(ReadMode.mmap);
    _v3IndexLoadingConfig.setSegmentVersion(SegmentVersion.v3);
  }

  private void constructV1Segment() throws Exception {
    FileUtils.deleteQuietly(INDEX_DIR);

    SegmentGeneratorConfig segmentGeneratorConfig =
        SegmentTestUtils.getSegmentGeneratorConfigWithoutTimeColumn(_avroFile, INDEX_DIR, "testTable");
    segmentGeneratorConfig.setSegmentVersion(SegmentVersion.v1);
    SegmentIndexCreationDriver driver = SegmentCreationDriverFactory.get(null);
    driver.init(segmentGeneratorConfig);
    driver.build();

    _indexDir = new File(INDEX_DIR, driver.getSegmentName());
  }

  @Test
  public void testLoad() throws Exception {
  }

  /**
   * Format converter will leave stale directory around if there were conversion failures. This test checks loading in
   * that scenario.
   */
  @Test
  public void testLoadWithStaleConversionDir() throws Exception {
  }

  private void testConversion() throws Exception {
    // Do not set segment version, should not convert the segment
    IndexSegment indexSegment = Loaders.IndexSegment.load(_indexDir, ReadMode.mmap);
    Assert.assertEquals(indexSegment.getSegmentMetadata().getVersion(), SegmentVersion.v1.toString());
    Assert.assertFalse(SegmentDirectoryPaths.segmentDirectoryFor(_indexDir, SegmentVersion.v3).exists());

    // Set segment version to v1, should not convert the segment
    indexSegment = Loaders.IndexSegment.load(_indexDir, _v1IndexLoadingConfig);
    Assert.assertEquals(indexSegment.getSegmentMetadata().getVersion(), SegmentVersion.v1.toString());
    Assert.assertFalse(SegmentDirectoryPaths.segmentDirectoryFor(_indexDir, SegmentVersion.v3).exists());

    // Set segment version to v3, should convert the segment to v3
    indexSegment = Loaders.IndexSegment.load(_indexDir, _v3IndexLoadingConfig);
    Assert.assertEquals(indexSegment.getSegmentMetadata().getVersion(), SegmentVersion.v3.toString());
    Assert.assertTrue(SegmentDirectoryPaths.segmentDirectoryFor(_indexDir, SegmentVersion.v3).exists());
  }

  @Test
  public void testPadding() throws Exception {
  }

  @AfterClass
  public void tearDown() {
    FileUtils.deleteQuietly(INDEX_DIR);
  }
}
