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
package com.linkedin.pinot.core.common.datatable;

import com.linkedin.pinot.common.data.FieldSpec.DataType;
import com.linkedin.pinot.common.exception.QueryException;
import com.linkedin.pinot.common.response.ProcessingException;
import com.linkedin.pinot.common.utils.DataSchema;
import com.linkedin.pinot.common.utils.DataTable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Unit test for {@link DataTable} serialization/de-serialization.
 */
public class DataTableSerDeTest {
  private static final long RANDOM_SEED = System.currentTimeMillis();
  private static final Random RANDOM = new Random(RANDOM_SEED);
  private static final String ERROR_MESSAGE = "Random seed: " + RANDOM_SEED;

  private static final int NUM_ROWS = 100;

  @Test
  public void testException()
      throws IOException {
  }

  @Test
  public void testAllDataTypes()
      throws IOException {
  }
}
