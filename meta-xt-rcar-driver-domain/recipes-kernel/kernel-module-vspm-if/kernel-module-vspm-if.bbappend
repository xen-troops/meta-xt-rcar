KERNEL_MODULE_AUTOLOAD_append = " vspm_if"

FILES_${PN}_append = " \
    ${sysconfdir}/modules-load.d \
"
