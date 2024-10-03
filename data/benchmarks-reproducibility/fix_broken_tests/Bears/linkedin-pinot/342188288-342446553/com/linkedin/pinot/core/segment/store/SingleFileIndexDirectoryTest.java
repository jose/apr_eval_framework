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
package com.linkedin.pinot.core.segment.store;

import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.core.indexsegment.generator.SegmentVersion;
import com.linkedin.pinot.core.segment.index.SegmentMetadataImpl;
import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SingleFileIndexDirectoryTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(SingleFileIndexDirectoryTest.class);
  private static final File TEST_DIRECTORY = new File(SingleFileIndexDirectoryTest.class.toString());

  private File segmentDir;
  private SegmentMetadataImpl segmentMetadata;
  static ColumnIndexType[] indexTypes;
  static final long ONE_KB = 1024L;
  static final long ONE_MB = ONE_KB * ONE_KB;
  static final long ONE_GB = ONE_MB * ONE_KB;

  static {
    indexTypes = ColumnIndexType.values();
  }

  @BeforeMethod
  public void setUpTest()
      throws IOException, ConfigurationException {
    segmentDir = new File(TEST_DIRECTORY, "segmentDirectory");

    if (segmentDir.exists()) {
      FileUtils.deleteQuietly(segmentDir);
    }
    segmentDir.mkdirs();
    segmentDir.deleteOnExit();
    writeMetadata();
  }

  @AfterMethod
  public void tearDownTest()
      throws IOException {
  }

  @AfterClass
  public void tearDownClass() {
    FileUtils.deleteQuietly(TEST_DIRECTORY);
  }

  void writeMetadata() throws ConfigurationException {
    SegmentMetadataImpl meta = mock(SegmentMetadataImpl.class);
    when(meta.getVersion()).thenReturn(SegmentVersion.v3.toString());
    segmentMetadata = meta;
  }

  @Test
  public void testWithEmptyDir()
      throws Exception {
  }

  @Test
  public void testMmapLargeBuffer()
      throws Exception {
  }

  @Test
  public void testLargeRWDirectBuffer()
      throws Exception {
  }

  @Test
  public void testModeChange()
      throws Exception {
  }

  private void testMultipleRW(ReadMode readMode, int numIter, long size)
      throws Exception {

    try (SingleFileIndexDirectory columnDirectory = new SingleFileIndexDirectory(segmentDir, segmentMetadata, readMode)) {
      ColumnIndexDirectoryTestHelper.performMultipleWrites(columnDirectory, "foo", size, numIter);
    }

    // now read and validate data
    try(ColumnIndexDirectory columnDirectory =
          new SingleFileIndexDirectory(segmentDir, segmentMetadata, readMode) ) {
      ColumnIndexDirectoryTestHelper.verifyMultipleReads(columnDirectory, "foo", numIter);
    }
  }


  @Test //(expectedExceptions = RuntimeException.class)
  public void testWriteExisting()
      throws Exception {
  }

  @Test //(expectedExceptions = RuntimeException.class)
  public void testMissingIndex()
      throws IOException, ConfigurationException {
  }

  @Test //(expectedExceptions =  UnsupportedOperationException.class)
  public void testRemoveIndex()
      throws IOException, ConfigurationException {
  }


}
