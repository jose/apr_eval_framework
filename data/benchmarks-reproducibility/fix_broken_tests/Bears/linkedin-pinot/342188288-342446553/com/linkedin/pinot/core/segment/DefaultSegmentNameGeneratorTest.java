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
package com.linkedin.pinot.core.segment;

import com.linkedin.pinot.core.indexsegment.generator.SegmentGeneratorConfig;
import com.linkedin.pinot.core.segment.creator.SegmentIndexCreationDriver;
import com.linkedin.pinot.core.segment.creator.impl.SegmentCreationDriverFactory;
import com.linkedin.pinot.core.segment.index.ColumnMetadataTest;
import com.linkedin.pinot.segments.v1.creator.SegmentTestUtils;
import com.linkedin.pinot.util.TestUtils;
import java.io.File;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class DefaultSegmentNameGeneratorTest {

  private static final String AVRO_DATA = "data/test_data-mv.avro";
  private static final File INDEX_DIR = new File(ColumnMetadataTest.class.toString());

  @BeforeMethod
  public void setUp() throws Exception {
    FileUtils.deleteQuietly(INDEX_DIR);
  }

  @AfterMethod
  public void tearDown() {
    FileUtils.deleteQuietly(INDEX_DIR);
  }

  public SegmentGeneratorConfig CreateSegmentConfigWithoutCreator()
      throws Exception {
    final String filePath =
        TestUtils.getFileFromResourceUrl(DefaultSegmentNameGeneratorTest.class.getClassLoader().getResource(AVRO_DATA));
    // Intentionally changed this to TimeUnit.Hours to make it non-default for testing.
    SegmentGeneratorConfig config =
        SegmentTestUtils.getSegmentGenSpecWithSchemAndProjectedColumns(new File(filePath), INDEX_DIR, "daysSinceEpoch",
            TimeUnit.HOURS, "testTable");
    config.setSegmentNamePostfix("1");
    config.setTimeColumnName("daysSinceEpoch");
    return config;
  }

  @Test
  public void testPostfix() throws Exception {
  }

  @Test
  public void testAlreadyNamedSegment() throws Exception {
  }

  @Test
  public void testNullTimeColumn() throws Exception {
  }

  @Test
  public void testNullTimeColumnThroughDefaultSegment() throws Exception {
  }

  @Test
  public void testNullPostfix() throws Exception {
  }

  @Test
  public void testNullPostfixWithNonNegSequenceId() throws Exception {
  }

  @Test
  public void testOnlyTableName() throws Exception {
  }
}
