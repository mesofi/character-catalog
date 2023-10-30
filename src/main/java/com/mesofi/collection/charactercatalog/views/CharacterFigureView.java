package com.mesofi.collection.charactercatalog.views;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.mesofi.collection.charactercatalog.model.Distribution;
import com.mesofi.collection.charactercatalog.model.GalleryImage;
import com.mesofi.collection.charactercatalog.model.Group;
import com.mesofi.collection.charactercatalog.model.LineUp;
import com.mesofi.collection.charactercatalog.model.Series;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterFigureView {
    private String id;
    private String originalName;
    private String baseName;
    private String displayableName;
    private LineUp lineUp;
    private Group group;

    private BigDecimal basePriceJPY;
    private BigDecimal releasePriceJPY;
    private LocalDate firstAnnouncementDateJPY;
    private LocalDate preorderDateJPY;
    private boolean preorderConfirmationDayJPY;
    private LocalDate releaseDateJPY;
    private boolean releaseConfirmationDayJPY;

    private BigDecimal basePriceMXN;
    private BigDecimal releasePriceMXN;
    private LocalDate firstAnnouncementDateMXN;
    private LocalDate preorderDateMXN;
    private boolean preorderConfirmationDayMXN;
    private LocalDate releaseDateMXN;
    private boolean releaseConfirmationDayMXN;

    private Series series;
    private Distribution distribution;
    private String url;

    private boolean metalBody;
    private boolean oce;
    private boolean revival;
    private boolean plainCloth;
    private boolean brokenCloth;
    private boolean bronzeToGold;
    private boolean gold;
    private boolean hongKongVersion;
    private boolean manga;
    private boolean surplice;
    private boolean set;
    private String remarks;

    private List<GalleryImage> images;
}
