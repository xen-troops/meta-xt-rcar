FILESEXTRAPATHS:append := ":${THISDIR}/${PN}"

SRC_URI:append = "file://weston-seats.rules \
    file://weston \
"

FILES:${PN} += " \
    ${sysconfdir}/udev/rules.d/weston-seats.rules \
    ${sysconfdir}/default/weston \
"

do_install:append() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/weston-seats.rules ${D}${sysconfdir}/udev/rules.d/weston-seats.rules
    install -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/weston ${D}${sysconfdir}/default/weston
}
