KERNEL_MODULE_AUTOLOAD_append = " mmngrbuf"

FILES_${PN}_append = " \
    ${sysconfdir}/modules-load.d \
"