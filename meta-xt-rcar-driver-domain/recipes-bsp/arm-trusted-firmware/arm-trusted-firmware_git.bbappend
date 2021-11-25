FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Start cores in EL2 mode
# Due to curent implementation of setting of build parameters,
# we need to use ADDITIONAL_ATFW_OPT to specify execution mode.
# Main reason is that ADDITIONAL_ATFW_OPT is used by
# do_compile and do_ipl_opt_compile.
ADDITIONAL_ATFW_OPT_append = ' RCAR_BL33_EXECUTION_EL=1'

SRC_URI += "\
    file://0001-rcar-Use-UART-instead-of-Secure-DRAM-area-for-loggin.patch \
    file://0002-tools-Produce-two-cert_header_sa6-images.patch \
    file://0003-rcar-Add-BOARD_SALVATOR_X-case-in-ddr_rank_judge.patch \
"

do_deploy_append () {
    install -m 0644 ${S}/tools/renesas/rcar_layout_create/bootparam_sa0.bin ${DEPLOYDIR}/bootparam_sa0.bin
    install -m 0644 ${S}/tools/renesas/rcar_layout_create/cert_header_sa6.bin ${DEPLOYDIR}/cert_header_sa6.bin
    install -m 0644 ${S}/tools/renesas/rcar_layout_create/cert_header_sa6_emmc.bin ${DEPLOYDIR}/cert_header_sa6_emmc.bin
    install -m 0644 ${S}/tools/renesas/rcar_layout_create/cert_header_sa6_emmc.srec ${DEPLOYDIR}/cert_header_sa6_emmc.srec
}

