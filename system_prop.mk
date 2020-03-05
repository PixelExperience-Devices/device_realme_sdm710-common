# Audio
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
   ro.config.vc_call_vol_steps=7 \
   ro.config.media_vol_steps=25

# Bluetooth
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
   vendor.bluetooth.soc=cherokee

# Data Modules
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    persist.vendor.data.mode=concurrent

# Graphics
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    debug.sf.enable_hwc_vds=1 \
    debug.sf.enable_gl_backpressure=1

# Media
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    media.settings.xml=/system/etc/media_profiles_vendor.xml

# Netflix
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    ro.netflix.bsp_rev=Q670-14477-1

# RIL
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    ro.telephony.default_network=22,20 \
    persist.radio.add_power_save=1 \
    persist.vendor.radio.lte_vrte_ltd=1 \
    persist.vendor.radio.cs_srv_type=1 \
    persist.vendor.radio.rat_on=combine \
    persist.vendor.radio.force_on_dc=true \
    persist.vendor.radio.redir_party_num=1

# UI
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    sys.use_fifo_ui=1
