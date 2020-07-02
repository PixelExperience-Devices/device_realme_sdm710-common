LOCAL_PATH := vendor/qcom/opensource/power

include $(CLEAR_VARS)
LOCAL_MODULE := android.hardware.power@1.2-service-sdm710
LOCAL_MODULE_STEM := android.hardware.power@1.2-service-qti
LOCAL_MODULE_PATH := $(TARGET_OUT_PRODUCT)/vendor_overlay/$(PRODUCT_TARGET_VNDK_VERSION)/bin
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_RELATIVE_PATH := hw
LOCAL_REQUIRED_MODULES := android.hardware.power@1.2-service-qti.rc

LOCAL_SHARED_LIBRARIES := \
    liblog \
    libcutils \
    libdl \
    libbase \
    libhidlbase \
    libhidltransport \
    libhwbinder \
    libutils \
    android.hardware.power@1.2

LOCAL_HEADER_LIBRARIES := \
    libhardware_headers

LOCAL_SRC_FILES := \
    power-common.c \
    metadata-parser.c \
    utils.c \
    list.c \
    hint-data.c \
    power-710.c \
    service.cpp \
    Power.cpp

LOCAL_CFLAGS += -Wall -Wextra -Werror

ifneq ($(TARGET_TAP_TO_WAKE_NODE),)
    LOCAL_CFLAGS += -DTAP_TO_WAKE_NODE=\"$(TARGET_TAP_TO_WAKE_NODE)\"
endif

ifeq ($(TARGET_USES_INTERACTION_BOOST),true)
    LOCAL_CFLAGS += -DINTERACTION_BOOST
endif
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_MODULE := android.hardware.power@1.2-service-qti.rc
LOCAL_MODULE_STEM := android.hardware.power@1.0-service.rc
LOCAL_MODULE_PATH := $(TARGET_OUT_PRODUCT)/vendor_overlay/$(PLATFORM_VNDK_VERSION)/etc/init
LOCAL_MODULE_TAGS  := optional
LOCAL_MODULE_CLASS := ETC
LOCAL_SRC_FILES := $(LOCAL_MODULE)
include $(BUILD_PREBUILT)
