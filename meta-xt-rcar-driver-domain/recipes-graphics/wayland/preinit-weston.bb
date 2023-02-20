DESCRIPTION = "Weston preinit service"
SECTION = "extras"
LICENSE = "GPL-2.0-only"
PR = "r0"

inherit systemd

FILESEXTRAPATHS:prepend := "${THISDIR}/weston:"

SYSTEMD_SERVICE:${PN} = "preinit-weston.service"

SRC_URI = " \
    file://preinit-weston.service \
    file://preinit-weston.sh \
"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

FILES:${PN} += " \
    ${systemd_system_unitdir}/preinit-weston.service \
    ${bindir}/preinit-weston.sh \
"

do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/preinit-weston.service ${D}${systemd_system_unitdir}
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/preinit-weston.sh ${D}${bindir}
}
