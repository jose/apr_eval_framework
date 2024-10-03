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
package com.linkedin.pinot.index.readerwriter;

import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.core.io.compression.ChunkCompressorFactory;
import com.linkedin.pinot.core.io.reader.impl.ChunkReaderContext;
import com.linkedin.pinot.core.io.reader.impl.v1.VarByteChunkSingleValueReader;
import com.linkedin.pinot.core.io.writer.impl.v1.VarByteChunkSingleValueWriter;
import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Unit test for {@link VarByteChunkSingleValueReader} and {@link VarByteChunkSingleValueWriter} classes.
 */
public class VarByteChunkSingleValueReaderWriteTest {
  private static final Charset UTF_8 = Charset.forName("UTF-8");

  private static final int NUM_STRINGS = 5003;
  private static final int NUM_DOCS_PER_CHUNK = 1009;
  private static final int MAX_STRING_LENGTH = 101;
  private static final String TEST_FILE = System.getProperty("java.io.tmpdir") + File.separator + "varByteSVRTest";

  /**
   * This test writes {@link #NUM_STRINGS} using {@link VarByteChunkSingleValueWriter}. It then reads
   * the strings using {@link VarByteChunkSingleValueReader}, and asserts that what was written is the same as
   * what was read in.
   *
   * Number of docs and docs per chunk are chosen to generate complete as well partial chunks.
   *
   * @throws Exception
   */
  @Test
  public void test()
      throws Exception {
  }

  /**
   * This test ensures that the reader can read in an data file from version 1.
   * @throws IOException
   */
  @Test
  public void testBackwardCompatibility()
      throws IOException {
  }
}
