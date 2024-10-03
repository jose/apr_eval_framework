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
package com.linkedin.pinot.query.selection;

import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.common.request.Selection;
import com.linkedin.pinot.common.request.SelectionSort;
import com.linkedin.pinot.common.response.broker.SelectionResults;
import com.linkedin.pinot.common.utils.DataSchema;
import com.linkedin.pinot.common.utils.DataTable;
import com.linkedin.pinot.core.query.selection.SelectionOperatorService;
import com.linkedin.pinot.core.query.selection.SelectionOperatorUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The <code>SelectionOperatorServiceTest</code> class provides unit tests for {@link SelectionOperatorUtils} and
 * {@link SelectionOperatorService}.
 */
public class SelectionOperatorServiceTest {
  private final String[] _columnNames =
      {"int", "long", "float", "double", "string", "int_array", "long_array", "float_array", "double_array", "string_array"};
  private final FieldSpec.DataType[] _dataTypes =
      {FieldSpec.DataType.INT, FieldSpec.DataType.LONG, FieldSpec.DataType.FLOAT, FieldSpec.DataType.DOUBLE, FieldSpec.DataType.STRING, FieldSpec.DataType.INT_ARRAY, FieldSpec.DataType.LONG_ARRAY, FieldSpec.DataType.FLOAT_ARRAY, FieldSpec.DataType.DOUBLE_ARRAY, FieldSpec.DataType.STRING_ARRAY};
  private final DataSchema _dataSchema = new DataSchema(_columnNames, _dataTypes);
  private final FieldSpec.DataType[] _compatibleDataTypes =
      {FieldSpec.DataType.LONG, FieldSpec.DataType.FLOAT, FieldSpec.DataType.DOUBLE, FieldSpec.DataType.INT, FieldSpec.DataType.STRING, FieldSpec.DataType.LONG_ARRAY, FieldSpec.DataType.FLOAT_ARRAY, FieldSpec.DataType.DOUBLE_ARRAY, FieldSpec.DataType.INT_ARRAY, FieldSpec.DataType.STRING_ARRAY};
  private final DataSchema _compatibleDataSchema =
      new DataSchema(_columnNames, _compatibleDataTypes);
  private final FieldSpec.DataType[] _upgradedDataTypes =
      new FieldSpec.DataType[]{FieldSpec.DataType.LONG, FieldSpec.DataType.DOUBLE, FieldSpec.DataType.DOUBLE, FieldSpec.DataType.DOUBLE, FieldSpec.DataType.STRING, FieldSpec.DataType.LONG_ARRAY, FieldSpec.DataType.DOUBLE_ARRAY, FieldSpec.DataType.DOUBLE_ARRAY, FieldSpec.DataType.DOUBLE_ARRAY, FieldSpec.DataType.STRING_ARRAY};
  private final DataSchema _upgradedDataSchema =
      new DataSchema(_columnNames, _upgradedDataTypes);
  private final Serializable[] _row1 =
      {0, 1L, 2.0F, 3.0, "4", new int[]{5}, new long[]{6L}, new float[]{7.0F}, new double[]{8.0}, new String[]{"9"}};
  private final Serializable[] _row2 =
      {10, 11L, 12.0F, 13.0, "14", new int[]{15}, new long[]{16L}, new float[]{17.0F}, new double[]{18.0}, new String[]{"19"}};
  private final Serializable[] _compatibleRow1 =
      {1L, 2.0F, 3.0, 4, "5", new long[]{6L}, new float[]{7.0F}, new double[]{8.0}, new int[]{9}, new String[]{"10"}};
  private final Serializable[] _compatibleRow2 =
      {11L, 12.0F, 13.0, 14, "15", new long[]{16L}, new float[]{17.0F}, new double[]{18.0}, new int[]{19}, new String[]{"20"}};
  private final Selection _selectionOrderBy = new Selection();

  @BeforeClass
  public void setUp() {
    // SELECT * FROM table ORDER BY int DESC LIMIT 1, 2.
    _selectionOrderBy.setSelectionColumns(Arrays.asList(_columnNames));
    SelectionSort selectionSort = new SelectionSort();
    selectionSort.setColumn("int");
    selectionSort.setIsAsc(false);
    _selectionOrderBy.setSelectionSortSequence(Collections.singletonList(selectionSort));
    _selectionOrderBy.setSize(2);
    _selectionOrderBy.setOffset(1);
  }

  @Test
  public void testGetSelectionColumns() {
  }

  @Test
  public void testCompatibleRowsMergeWithoutOrdering() {
  }

  @Test
  public void testCompatibleRowsMergeWithOrdering() {
  }

  @Test
  public void testCompatibleRowsDataTableTransformation()
      throws Exception {
  }

  @Test
  public void testCompatibleRowsRenderSelectionResultsWithoutOrdering() {
  }

  @Test
  public void testCompatibleRowsRenderSelectionResultsWithOrdering() {
  }
}
