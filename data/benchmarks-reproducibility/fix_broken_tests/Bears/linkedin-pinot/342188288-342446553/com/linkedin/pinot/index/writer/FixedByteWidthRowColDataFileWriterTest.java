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
package com.linkedin.pinot.index.writer;

import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.util.Random;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.linkedin.pinot.core.io.reader.impl.FixedByteSingleValueMultiColReader;
import com.linkedin.pinot.core.io.writer.impl.FixedByteSingleValueMultiColWriter;
import com.linkedin.pinot.core.segment.creator.impl.V1Constants;


@Test
public class FixedByteWidthRowColDataFileWriterTest {
  @Test
  public void testSingleColInt() throws Exception {
  }

  @Test
  public void testSingleColFloat() throws Exception {
  }

  @Test
  public void testSingleColDouble() throws Exception {
  }

  @Test
  public void testSingleColLong() throws Exception {
  }

  @Test
  public void testMultiCol() throws Exception {
  }

  @Test
  public void testSpecialCharsForStringReaderWriter() throws Exception {
  }

  @Test
  public void testSpecialPaddingCharsForStringReaderWriter() throws Exception {
  }
}
