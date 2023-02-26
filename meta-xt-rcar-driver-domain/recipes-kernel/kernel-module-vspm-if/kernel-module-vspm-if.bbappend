KERNEL_MODULE_AUTOLOAD:append = " vspm_if"

FILES:${PN}:append = " \
    ${sysconfdir}/modules-load.d \
"
