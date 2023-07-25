FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://0001-Use-first-reserved-addr-from-IOMEM-area-as-MM_LOSSY_.patch \
"

KERNEL_MODULE_AUTOLOAD:append = " mmngr"

FILES:${PN}:append = " \
    ${sysconfdir}/modules-load.d \
"
