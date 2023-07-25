FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://dm-m3.cfg \
    file://dm-h3.cfg \
    file://dm-ulcb.cfg \
    file://dm-m3n.cfg \
"

DM_CONFIG:r8a7796 = "dm-m3.cfg"
DM_CONFIG:r8a7795 = "dm-h3.cfg"
DM_CONFIG:ulcb = "dm-ulcb.cfg"
DM_CONFIG:r8a77965 = "dm-m3n.cfg"
