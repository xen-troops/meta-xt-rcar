FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# We support only one Kingfisher based machine so far
SRC_URI_append = " file://gstvspfilter-h3ulcb-4x2g-kf.conf "

do_install_append() {
    install -Dm 644 ${WORKDIR}/gstvspfilter-h3ulcb-4x2g-kf.conf ${D}/${sysconfdir}/gstvspfilter.conf
}

