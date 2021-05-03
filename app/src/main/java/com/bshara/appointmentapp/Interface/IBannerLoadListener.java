package com.bshara.appointmentapp.Interface;


import com.bshara.appointmentapp.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {

    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);

}

