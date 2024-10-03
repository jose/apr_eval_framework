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
package com.linkedin.pinot.core.minion;

import com.linkedin.pinot.common.data.DimensionFieldSpec;
import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.common.data.Schema;
import com.linkedin.pinot.core.data.GenericRow;
import com.linkedin.pinot.core.data.readers.GenericRowRecordReader;
import com.linkedin.pinot.core.data.readers.PinotSegmentRecordReader;
import com.linkedin.pinot.core.indexsegment.generator.SegmentGeneratorConfig;
import com.linkedin.pinot.core.segment.creator.impl.SegmentIndexCreationDriverImpl;
import com.linkedin.pinot.core.segment.index.SegmentMetadataImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class SegmentPurgerTest {
  private static final File TEMP_DIR = new File(FileUtils.getTempDirectory(), "SegmentPurgerTest");
  private static final File ORIGINAL_SEGMENT_DIR = new File(TEMP_DIR, "originalSegment");
  private static final File PURGED_SEGMENT_DIR = new File(TEMP_DIR, "purgedSegment");
  private static final Random RANDOM = new Random();

  private static final int NUM_ROWS = 10000;
  private static final String TABLE_NAME = "testTable";
  private static final String SEGMENT_NAME = "testSegment";
  private static final String D1 = "d1";
  private static final String D2 = "d2";

  private File _originalIndexDir;
  private int _expectedNumRecordsPurged;
  private int _expectedNumRecordsModified;

  @BeforeClass
  public void setUp() throws Exception {
    FileUtils.deleteDirectory(TEMP_DIR);

    Schema schema = new Schema();
    schema.addField(new DimensionFieldSpec(D1, FieldSpec.DataType.INT, true));
    schema.addField(new DimensionFieldSpec(D2, FieldSpec.DataType.INT, true));

    List<GenericRow> rows = new ArrayList<>(NUM_ROWS);
    for (int i = 0; i < NUM_ROWS; i++) {
      GenericRow row = new GenericRow();
      int value1 = RANDOM.nextInt(100);
      int value2 = RANDOM.nextInt(100);
      if (value1 == 0) {
        _expectedNumRecordsPurged++;
      } else if (value2 == 0) {
        _expectedNumRecordsModified++;
      }
      row.putField(D1, value1);
      row.putField(D2, value2);
      rows.add(row);
    }
    GenericRowRecordReader genericRowRecordReader = new GenericRowRecordReader(rows, schema);

    SegmentGeneratorConfig config = new SegmentGeneratorConfig(schema);
    config.setOutDir(ORIGINAL_SEGMENT_DIR.getPath());
    config.setTableName(TABLE_NAME);
    config.setSegmentName(SEGMENT_NAME);

    SegmentIndexCreationDriverImpl driver = new SegmentIndexCreationDriverImpl();
    driver.init(config, genericRowRecordReader);
    driver.build();
    _originalIndexDir = new File(ORIGINAL_SEGMENT_DIR, SEGMENT_NAME);
  }

  @Test
  public void testPurgeSegment() throws Exception {
  }

  @AfterClass
  public void tearDown() throws Exception {
    FileUtils.deleteDirectory(TEMP_DIR);
  }
}
