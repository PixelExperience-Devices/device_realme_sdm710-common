LOCAL_PATH := hardware/custom/interfaces/usb/1.0-basic

include $(CLEAR_VARS)
LOCAL_MODULE := android.hardware.usb@1.0-service.realme_sdm710
LOCAL_MODULE_TAGS := optional

LOCAL_MODULE_PATH := $(TARGET_OUT_PRODUCT)/vendor_overlay/$(PRODUCT_TARGET_VNDK_VERSION)/bin
LOCAL_MODULE_RELATIVE_PATH := hw
LOCAL_MODULE_STEM := android.hardware.usb@1.0-service

LOCAL_REQUIRED_MODULES := android.hardware.usb@1.0-service.realme_sdm710.rc

LOCAL_SRC_FILES := service.cpp Usb.cpp

LOCAL_SHARED_LIBRARIES := \
    libbase \
    libcutils \
    libhidlbase \
    libhidltransport \
    libhwbinder \
    libutils \
    libhardware \
    android.hardware.usb@1.0

include $(BUILD_EXECUTABLE)
