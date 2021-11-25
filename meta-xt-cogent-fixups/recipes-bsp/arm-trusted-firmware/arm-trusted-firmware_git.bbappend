

# Override the do_ipl_opt_compile from cogent's layer to add the ${ADDITIONAL_ATFW_OPT} option
do_ipl_opt_compile () {
    oe_runmake distclean
    oe_runmake bl2 bl31 rcar_layout_tool rcar_srecord PLAT=${PLATFORM} SPD=opteed MBEDTLS_COMMON_MK=1 ${EXTRA_ATFW_OPT} ${ATFW_OPT_LOSSY} ${ATFW_OPT_RPC} ${ADDITIONAL_ATFW_OPT}
}

