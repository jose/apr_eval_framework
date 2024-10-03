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
package com.linkedin.pinot.core.segment.index.converter;

import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.core.indexsegment.IndexSegment;
import com.linkedin.pinot.core.indexsegment.generator.SegmentGeneratorConfig;
import com.linkedin.pinot.core.indexsegment.generator.SegmentVersion;
import com.linkedin.pinot.core.segment.creator.SegmentIndexCreationDriver;
import com.linkedin.pinot.core.segment.creator.impl.SegmentCreationDriverFactory;
import com.linkedin.pinot.core.segment.creator.impl.V1Constants;
import com.linkedin.pinot.core.segment.index.SegmentMetadataImpl;
import com.linkedin.pinot.core.segment.index.loader.IndexLoadingConfig;
import com.linkedin.pinot.core.segment.index.loader.Loaders;
import com.linkedin.pinot.core.segment.store.SegmentDirectoryPaths;
import com.linkedin.pinot.segments.v1.creator.SegmentTestUtils;
import com.linkedin.pinot.util.TestUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class SegmentV1V2ToV3FormatConverterTest {
  private static final String AVRO_DATA = "data/test_data-mv.avro";

  private File _indexDir;
  private File _segmentDirectory;
  private IndexLoadingConfig _v3IndexLoadingConfig;

  @BeforeMethod
  public void setUp()
      throws Exception {

    _indexDir = Files.createTempDirectory(SegmentV1V2ToV3FormatConverter.class.getName() + "_segmentDir").toFile();

    final String filePath =
        TestUtils.getFileFromResourceUrl(SegmentV1V2ToV3FormatConverter.class.getClassLoader().getResource(AVRO_DATA));

    // intentionally changed this to TimeUnit.Hours to make it non-default for testing
    final SegmentGeneratorConfig config =
        SegmentTestUtils.getSegmentGenSpecWithSchemAndProjectedColumns(new File(filePath), _indexDir, "daysSinceEpoch",
            TimeUnit.HOURS, "testTable");
    config.setSegmentNamePostfix("1");
    config.setTimeColumnName("daysSinceEpoch");
    final SegmentIndexCreationDriver driver = SegmentCreationDriverFactory.get(null);
    driver.init(config);
    driver.build();
    _segmentDirectory = new File(_indexDir, driver.getSegmentName());
    File starTreeFile = new File(_segmentDirectory, V1Constants.STAR_TREE_INDEX_FILE);
    FileUtils.touch(starTreeFile);
    FileUtils.writeStringToFile(starTreeFile, "This is a star tree index");

    _v3IndexLoadingConfig = new IndexLoadingConfig();
    _v3IndexLoadingConfig.setReadMode(ReadMode.mmap);
    _v3IndexLoadingConfig.setSegmentVersion(SegmentVersion.v3);
  }

  @AfterMethod
  public void tearDown()
      throws Exception {
    if (_indexDir != null) {
      FileUtils.deleteQuietly(_indexDir);
    }
  }

  @Test
  public void testConvert()
      throws Exception {
  }
}
