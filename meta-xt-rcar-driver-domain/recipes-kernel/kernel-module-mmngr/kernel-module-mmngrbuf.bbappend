KERNEL_MODULE_AUTOLOAD:append = " mmngrbuf"

FILES:${PN}:append = " \
    ${sysconfdir}/modules-load.d \
"
