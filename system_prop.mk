# Audio
PRODUCT_PROPERTY_OVERRIDES += \
   ro.config.vc_call_vol_steps=7 \
   ro.config.media_vol_steps=25

# CNE
PRODUCT_PROPERTY_OVERRIDES += \
    persist.vendor.cne.feature=1

# IMS
PRODUCT_PROPERTY_OVERRIDES += \
    persist.dbg.volte_avail_ovr=1 \
    persist.dbg.vt_avail_ovr=1 \
    persist.dbg.wfc_avail_ovr=1

# Perf
PRODUCT_PROPERTY_OVERRIDES += \
    ro.vendor.qti.core_ctl_max_cpu=4 \
    ro.vendor.qti.core_ctl_min_cpu=2

# UI
PRODUCT_PROPERTY_OVERRIDES += \
    sys.use_fifo_ui=1
