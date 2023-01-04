KERNEL_MODULE_AUTOLOAD_append = " uvcs_drv"

FILES_${PN}_append = " \
    ${sysconfdir}/modules-load.d \
"