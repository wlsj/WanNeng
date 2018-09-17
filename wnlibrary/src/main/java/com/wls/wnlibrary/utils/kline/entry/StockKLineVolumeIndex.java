package com.wls.wnlibrary.utils.kline.entry;

/**
 * <p>StockKLineVolumeIndex</p>
 */

public class StockKLineVolumeIndex extends StockIndex {

    public StockKLineVolumeIndex() {
        super(STANDARD_HEIGHT);
    }

    public StockKLineVolumeIndex(int height) {
        super(height);

        setExtremumYScale(1f);
    }

    @Override
    public void computeMinMax(int currentIndex, Entry entry) {
        if (entry.getVolume() < getMinY()) {
            setMinY(entry.getVolume());
        }
        if (entry.getVolumeMa5() < getMinY()) {
            setMinY((float) entry.getVolumeMa5());
        }
        if (entry.getVolumeMa10() < getMinY()) {
            setMinY((float) entry.getVolumeMa10());
        }

        if (entry.getVolume() > getMaxY()) {
            setMaxY(entry.getVolume());
        }
        if (entry.getVolumeMa5() > getMaxY()) {
            setMaxY((float) entry.getVolumeMa5());
        }
        if (entry.getVolumeMa10() > getMaxY()) {
            setMaxY((float) entry.getVolumeMa10());
        }
    }
}
