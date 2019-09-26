# Assertive display
PRODUCT_PROPERTY_OVERRIDES += \
   ro.vendor.display.ad=1 \
   ro.vendor.display.sensortype=2 \
   ro.vendor.display.ad.sdr_calib_data=/vendor/etc/OPPO_OLED_AD_calib.cfg

# Audio
PRODUCT_PROPERTY_OVERRIDES += \
   ro.config.vc_call_vol_steps=7 \
   ro.config.media_vol_steps=25

# Camera
PRODUCT_PROPERTY_OVERRIDES += \
   persist.camera.HAL3.enabled=1

# CNE
PRODUCT_PROPERTY_OVERRIDES += \
    persist.vendor.cne.feature=1

# Data Modules
PRODUCT_PROPERTY_OVERRIDES += \
    persist.vendor.data.mode=concurrent \
    ro.vendor.use_data_netmgrd=true

# Graphics
PRODUCT_PROPERTY_OVERRIDES += \
    debug.sf.enable_hwc_vds=1

# IMS
PRODUCT_PROPERTY_OVERRIDES += \
    persist.dbg.volte_avail_ovr=1 \
    persist.dbg.vt_avail_ovr=1 \
    persist.dbg.wfc_avail_ovr=1

# Netflix
PRODUCT_PROPERTY_OVERRIDES += \
    ro.netflix.bsp_rev=Q670-14477-1

# Perf
PRODUCT_PROPERTY_OVERRIDES += \
    ro.vendor.qti.core_ctl_min_cpu=4 \
    ro.vendor.qti.core_ctl_max_cpu=6 \
    vendor.iop.enable_prefetch_ofr=0 \
    vendor.iop.enable_uxe=0

# RIL
PRODUCT_PROPERTY_OVERRIDES += \
    ro.telephony.default_network=9,0

# UI
PRODUCT_PROPERTY_OVERRIDES += \
    sys.use_fifo_ui=1
