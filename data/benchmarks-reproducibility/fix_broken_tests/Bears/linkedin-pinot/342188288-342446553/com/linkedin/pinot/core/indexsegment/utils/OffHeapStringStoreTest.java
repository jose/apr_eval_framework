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

import com.linkedin.pinot.core.io.readerwriter.PinotDataBufferMemoryManager;
import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.linkedin.pinot.core.io.writer.impl.DirectMemoryManager;
import com.linkedin.pinot.core.io.writer.impl.OffHeapStringStore;


public class OffHeapStringStoreTest {
  private static Random RANDOM = new Random();
  private PinotDataBufferMemoryManager _memoryManager;

  @BeforeClass
  public void setUp() {
    _memoryManager = new DirectMemoryManager(OffHeapStringStoreTest.class.getName());
  }

  @AfterClass
  public void tearDown() throws Exception {
    _memoryManager.close();
  }

  @Test
  public void maxValueTest() throws Exception {
  }

  @Test
  public void overflowTest() throws Exception {
  }

  // Generates a ascii displayable string of given length
  private String generateRandomString(final int len) {
    byte[] bytes = new byte[len];
    for (int i = 0; i < len; i++) {
      bytes[i] = (byte)(RANDOM.nextInt(92) + 32);
    }
    return new String(bytes);
  }
}
