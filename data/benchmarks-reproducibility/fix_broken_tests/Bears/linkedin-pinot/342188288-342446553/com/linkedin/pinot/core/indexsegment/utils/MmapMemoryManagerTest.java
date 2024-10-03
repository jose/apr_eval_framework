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

package com.linkedin.pinot.core.indexsegment.utils;

import com.linkedin.pinot.common.utils.MmapUtils;
import com.linkedin.pinot.core.io.readerwriter.PinotDataBufferMemoryManager;
import com.linkedin.pinot.core.io.writer.impl.MmapMemoryManager;
import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class MmapMemoryManagerTest {

  private String _tmpDir;

  @BeforeClass
  public void setUp() {
    _tmpDir = System.getProperty("java.io.tmpdir") + "/" + MmapMemoryManagerTest.class.getSimpleName();
    File dir = new File(_tmpDir);
    FileUtils.deleteQuietly(dir);
    dir.mkdir();
    dir.deleteOnExit();
  }

  @AfterClass
  public void tearDown() {
    new File(_tmpDir).delete();
  }

  @Test
  public void testLargeBlocks() throws Exception {
  }

  @Test
  public void testSmallBlocksForSameColumn() throws Exception {
  }

  @Test
  public void testCornerConditions() throws Exception {
  }
}
