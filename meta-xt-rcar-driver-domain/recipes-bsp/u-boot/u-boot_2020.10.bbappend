# Move u-boot into 'firmware' folder
inherit collect_firmware

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://0001-Revert-net-ravb-Fix-stop-RAVB-module-clock-before-OS.patch \
"
