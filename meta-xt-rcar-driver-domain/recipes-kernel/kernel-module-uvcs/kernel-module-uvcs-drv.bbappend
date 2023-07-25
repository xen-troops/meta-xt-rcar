KERNEL_MODULE_AUTOLOAD:append = " uvcs_drv"

FILES:${PN}:append = " \
    ${sysconfdir}/modules-load.d \
"
