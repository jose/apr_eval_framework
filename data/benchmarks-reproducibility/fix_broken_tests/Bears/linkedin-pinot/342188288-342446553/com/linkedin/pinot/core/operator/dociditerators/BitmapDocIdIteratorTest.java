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
package com.linkedin.pinot.core.operator.dociditerators;

import com.linkedin.pinot.core.common.BlockDocIdIterator;
import com.linkedin.pinot.core.common.Constants;
import org.roaringbitmap.buffer.MutableRoaringBitmap;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Test for the bitmap docid iterators.
 */
public class BitmapDocIdIteratorTest {
  @Test
  public void testIt() {
  }

  private void checkDocIdIterator(int[] expectedValues, BlockDocIdIterator docIdIterator) {
    int index = 0;
    for (int expected : expectedValues) {
      Assert.assertEquals(docIdIterator.next(), expected, "Call #" + (index + 1) + " to the iterator did not give the expected result" );
      ++index;
    }
  }
}
