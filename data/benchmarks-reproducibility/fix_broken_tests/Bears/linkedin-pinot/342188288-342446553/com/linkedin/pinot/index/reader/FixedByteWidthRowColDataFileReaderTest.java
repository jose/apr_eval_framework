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
package com.linkedin.pinot.index.reader;

import com.linkedin.pinot.common.segment.ReadMode;
import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.linkedin.pinot.core.io.reader.impl.FixedByteSingleValueMultiColReader;


@Test
public class FixedByteWidthRowColDataFileReaderTest {

  @Test
  void testSingleCol() throws Exception {
  }

  @Test
  void testMultiCol() throws Exception {
  }
}
