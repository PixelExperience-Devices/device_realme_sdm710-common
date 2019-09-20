# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_p.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Get non-open-source specific aspects
$(call inherit-product-if-exists, vendor/realme/sdm710-common/sdm710-common-vendor.mk)

# Boot animation
TARGET_SCREEN_HEIGHT := 2244
TARGET_SCREEN_WIDTH := 1080

# Device uses high-density artwork where available
PRODUCT_AAPT_CONFIG := normal
PRODUCT_AAPT_PREF_CONFIG := xxhdpi

# Overlays
DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay
ifneq ($(findstring lineage, $(TARGET_PRODUCT)),)
DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay-lineage
endif

# Audio
PRODUCT_PACKAGES += \
    audio.a2dp.default \
    tinymix

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/audio/audio_policy_configuration.xml:system/etc/audio_policy_configuration.xml

# Bluetooth
PRODUCT_PACKAGES += \
    BluetoothResCommon

# Camera
PRODUCT_PACKAGES += \
    Snap

# Common init scripts
PRODUCT_PACKAGES += \
    init.target.rc

# Display
PRODUCT_PACKAGES += \
    libvulkan \
    vendor.display.config@1.0

# Doze
PRODUCT_PACKAGES += \
    OppoDoze

# IMS
PRODUCT_PACKAGES += \
    ims-ext-common

# Lights
PRODUCT_PACKAGES += \
    android.hardware.light@2.0-service.realme_sdm710

# Net
PRODUCT_PACKAGES += \
    netutils-wrapper-1.0

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.fingerprint.xml:system/etc/permissions/android.hardware.fingerprint.xml

# RCS
PRODUCT_PACKAGES += \
    rcs_service_aidl \
    rcs_service_aidl.xml \
    rcs_service_api \
    rcs_service_api.xml

# QTI
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/qti_whitelist.xml:system/etc/sysconfig/qti_whitelist.xml \
    $(LOCAL_PATH)/permissions/privapp-permissions-qti.xml:system/etc/permissions/privapp-permissions-qti.xml

# Soong namespaces
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH)

# System properties
-include $(LOCAL_PATH)/system_prop.mk

# Telephony
PRODUCT_PACKAGES += \
    telephony-ext

PRODUCT_BOOT_JARS += \
    telephony-ext

# TextClassifier
PRODUCT_PACKAGES += \
    textclassifier.bundle1

# Trust HAL
PRODUCT_PACKAGES += \
    lineage.trust@1.0-service

# WiFi Display
PRODUCT_BOOT_JARS += \
    WfdCommon
