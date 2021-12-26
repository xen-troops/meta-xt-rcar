FILESEXTRAPATHS_append := ":${THISDIR}/files"

SRC_URI_append = " \
   file://0001-Set-default-environment.patch \
"

# Move u-boot into 'firmware' folder
inherit collect_firmware
