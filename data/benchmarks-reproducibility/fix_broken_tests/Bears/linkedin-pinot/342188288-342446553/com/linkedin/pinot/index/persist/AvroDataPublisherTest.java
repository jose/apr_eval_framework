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
package com.linkedin.pinot.index.persist;

import com.linkedin.pinot.common.data.FieldSpec.DataType;
import com.linkedin.pinot.common.data.Schema;
import com.linkedin.pinot.core.data.GenericRow;
import com.linkedin.pinot.core.data.readers.AvroRecordReader;
import com.linkedin.pinot.core.data.readers.FileFormat;
import com.linkedin.pinot.core.data.readers.RecordReaderFactory;
import com.linkedin.pinot.core.indexsegment.generator.SegmentGeneratorConfig;
import com.linkedin.pinot.core.indexsegment.generator.SegmentVersion;
import com.linkedin.pinot.core.util.AvroUtils;
import com.linkedin.pinot.util.TestUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;


public class AvroDataPublisherTest {

  private final String AVRO_DATA = "data/test_sample_data.avro";
  private final String JSON_DATA = "data/test_sample_data.json";
  private final String AVRO_MULTI_DATA = "data/test_sample_data_multi_value.avro";

  @Test
  public void TestReadAvro() throws Exception {
  }

  @Test
  public void TestReadPartialAvro() throws Exception {
  }

  @Test
  public void TestReadMultiValueAvro() throws Exception {
  }
}
