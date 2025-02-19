FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Start cores in EL2 mode
SRC_URI += "\
    file://0001-HACK-s4-boot-U-Boot-in-EL2-mode.patch \
    file://0003-HACK-s4-perform-direct-reset.patch \
"

SRC_URI:append:r8a779f0 = " \
    file://0002-HACK-s4-Configure-IPMMU-registers.patch \
"

SRC_URI:append:r8a779g0 = " \
    file://0001-HACK-v4h-Configure-IPMMU-registers.patch \
"
