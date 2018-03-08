# Assertive display
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
   ro.vendor.display.ad=1 \
   ro.vendor.display.sensortype=2 \
   ro.vendor.display.ad.sdr_calib_data=/vendor/etc/OPPO_OLED_AD_calib.cfg

# Audio
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
   ro.config.vc_call_vol_steps=7 \
   ro.config.media_vol_steps=25

# Camera
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
   persist.camera.HAL3.enabled=1

# CNE
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    persist.vendor.cne.feature=1

# Data Modules
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    persist.vendor.data.mode=concurrent \
    ro.vendor.use_data_netmgrd=true

# Graphics
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    debug.sf.enable_hwc_vds=1

# IMS
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    persist.dbg.volte_avail_ovr=1 \
    persist.dbg.vt_avail_ovr=1 \
    persist.dbg.wfc_avail_ovr=1

# Netflix
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    ro.netflix.bsp_rev=Q670-14477-1

# Perf
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    ro.vendor.qti.core_ctl_min_cpu=4 \
    ro.vendor.qti.core_ctl_max_cpu=6 \
    vendor.iop.enable_prefetch_ofr=0 \
    vendor.iop.enable_uxe=0

# RIL
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    ro.telephony.default_network=22,18 \
    persist.radio.add_power_save=1

# UI
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    sys.use_fifo_ui=1
