FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://0001-waylandws_server-Add-support-for-YUYV-buffers.patch \
"

DEPENDS:append = " virtual/egl"
