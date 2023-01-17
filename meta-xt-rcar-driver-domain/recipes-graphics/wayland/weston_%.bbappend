FILESEXTRAPATHS:append := ":${THISDIR}/${PN}"

SRC_URI:append = "file://weston-seats.rules \
"

FILES:${PN} += " \
    ${sysconfdir}/udev/rules.d/weston-seats.rules \
"

do_install:append() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/weston-seats.rules ${D}${sysconfdir}/udev/rules.d/weston-seats.rules
}
