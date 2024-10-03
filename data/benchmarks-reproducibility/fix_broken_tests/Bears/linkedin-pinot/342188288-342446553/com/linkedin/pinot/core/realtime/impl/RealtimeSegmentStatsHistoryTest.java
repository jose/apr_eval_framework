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

package com.linkedin.pinot.core.realtime.impl;

import com.linkedin.pinot.util.TestUtils;
import java.io.File;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;


public class RealtimeSegmentStatsHistoryTest {
  private static final String STATS_FILE_NAME = RealtimeSegmentStatsHistoryTest.class.getSimpleName() + ".ser";
  private static final String COL1 = "col1";
  private static final String COL2 = "col2";

  private void addSegmentStats(int segmentId, RealtimeSegmentStatsHistory history) {
    RealtimeSegmentStatsHistory.SegmentStats segmentStats = new RealtimeSegmentStatsHistory.SegmentStats();
    segmentStats.setMemUsedBytes(segmentId);
    segmentStats.setNumSeconds(segmentId);
    segmentStats.setNumRowsConsumed(segmentId);
    for (int i = 0; i < 2; i++) {
      RealtimeSegmentStatsHistory.ColumnStats columnStats = new RealtimeSegmentStatsHistory.ColumnStats();
      columnStats.setAvgColumnSize(segmentId*100 + i);
      columnStats.setCardinality(segmentId*100 + i);
      segmentStats.setColumnStats(String.valueOf(i), columnStats);
    }
    history.addSegmentStats(segmentStats);
  }

  @Test
  public void zeroStatTest() throws Exception {
  }

  @Test
  public void serdeTest() throws Exception {
  }

  @Test
  public void testMultiThreadedUse() throws Exception {
  }

  // This test attempts to ensure that future modifications to RealtimeSegmentStatsHistory does not prevent the software
  // from reading data serialized by earlier versions. The serialized data has one segment, with 2 columns -- "v1col1" and
  // "v1col2".
  @Test
  public void testVersion1() throws Exception {
  }

  private static class StatsUpdater implements Runnable {
    private final RealtimeSegmentStatsHistory _statsHistory;
    private final int _numIterations;
    private final long _avgSleepTimeMs;
    private final int _sleepVariationMs;
    private final Random _random = new Random();

    private static final int MAX_AVGLEN = 200;
    private static final int MAX_CARDINALITY = 50000;

    private StatsUpdater(RealtimeSegmentStatsHistory statsHistory, int numInterations, long avgSleepTimeMs){
      _statsHistory = statsHistory;
      _numIterations = numInterations;
      _avgSleepTimeMs = avgSleepTimeMs;
      _sleepVariationMs = (int)_avgSleepTimeMs/10;
    }

    @Override
    public void run() {
      for (int i = 0; i < _numIterations; i++) {
        try {
          Thread.sleep(_avgSleepTimeMs - _sleepVariationMs + _random.nextInt(2 * _sleepVariationMs));
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        RealtimeSegmentStatsHistory.SegmentStats segmentStats = new RealtimeSegmentStatsHistory.SegmentStats();
        RealtimeSegmentStatsHistory.ColumnStats columnStats;

        columnStats = new RealtimeSegmentStatsHistory.ColumnStats();
        columnStats.setAvgColumnSize(_random.nextInt(MAX_AVGLEN));
        columnStats.setCardinality(_random.nextInt(MAX_CARDINALITY));
        segmentStats.setColumnStats(COL1, columnStats);

        columnStats = new RealtimeSegmentStatsHistory.ColumnStats();
        columnStats.setAvgColumnSize(_random.nextInt(MAX_AVGLEN));
        columnStats.setCardinality(_random.nextInt(MAX_CARDINALITY));
        segmentStats.setColumnStats(COL2, columnStats);

        _statsHistory.addSegmentStats(segmentStats);
        _statsHistory.save();
      }
    }
  }
}
