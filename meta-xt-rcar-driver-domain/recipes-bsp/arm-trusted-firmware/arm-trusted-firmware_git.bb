DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

require include/multimedia-control.inc

S = "${WORKDIR}/git"

BRANCH = "rcar_gen3_v2.3"
SRC_URI = "git://github.com/renesas-rcar/arm-trusted-firmware.git;branch=${BRANCH}"
SRCREV = "7638cfbe68760358a4bbad2d12a7b1b93a02e9f5"

PV = "v2.3+renesas+git${SRCPV}"

# Following four patches were copied from
# XT's 'meta-renesas' repo
# branch 'Renesas-Yocto-v5.1-patched'
# commit 7c24aff7924b68b78be047759acb572d037e571e
SRC_URI_append = " \
   file://0001-Revert-rcar_gen3-plat-Delete-FDT-function-calls.patch \
   file://0002-rcar_gen3-plat-Fix-DRAM-size-judgment-by-PRR-registe.patch \
   file://0003-rcar_gen3-plat-Factor-out-DT-memory-node-generation.patch \
   file://0004-rcar_gen3-plat-Generate-two-memory-nodes-for-larger-.patch \
"

# patches from xen-troops
SRC_URI += "\
    file://0001-rcar-Use-UART-instead-of-Secure-DRAM-area-for-loggin.patch \
    file://0002-tools-Produce-two-cert_header_sa6-images.patch \
    file://0003-rcar-Add-BOARD_SALVATOR_X-case-in-ddr_rank_judge.patch \
"

COMPATIBLE_MACHINE = "(salvator-x|ulcb|ebisu|draak)"
PLATFORM = "rcar"

####
# Build options for ATF are divided into few parts:
#  - SOC specific, like ATFW_OPT_r8a779*;
#  - board specific, like _append_ulcb;
#  - application specific, like RCAR_LOSSY_ENABLE;
#  - memory specific. See explanation bellow near MEMORY_VARIANTS;

# SOC specific build options
ATFW_OPT_r8a7795  = "LSI=H3"
ATFW_OPT_r8a7796  = "LSI=M3 RCAR_DRAM_SPLIT=2"
ATFW_OPT_r8a77965 = "LSI=M3N"
ATFW_OPT_r8a77990 = "LSI=E3 RCAR_SA0_SIZE=0 RCAR_AVS_SETTING_ENABLE=0"
ATFW_OPT_r8a77995 = "LSI=D3 RCAR_SA0_SIZE=0 RCAR_AVS_SETTING_ENABLE=0 PMIC_ROHM_BD9571=0 RCAR_SYSTEM_SUSPEND=0 DEBUG=0 "

# Board specific build options
ATFW_OPT_append_ulcb = " RCAR_GEN3_ULCB=1 PMIC_LEVEL_MODE=0"
ATFW_OPT_append_ulcb = " RCAR_RPC_HYPERFLASH_LOCKED=0"

# Application specific build options
ATFW_OPT_LOSSY = "${@oe.utils.conditional("USE_MULTIMEDIA", "1", "RCAR_LOSSY_ENABLE=1", "", d)}"
ATFW_OPT_append   = " ${ATFW_OPT_LOSSY}"
# Run BL33 in EL2 mode. This is required for Xen.
ATFW_OPT_append = " RCAR_BL33_EXECUTION_EL=1"

# Memory specific build options
# To understand following code we need to mention two important points:
#  - these options are binded to specific SOC
#  - these options are implented as variable flags
# But we can't use override syntax with varFlags, so it's impossible to do
# MEMORY_VARIANTS_r8a7795[4x2g] = 'RCAR_DRAM_SPLIT=1'
# That's why we use prepend to do_build_all_memory_variants with SOC specific override.
# And inside of this prepend we set varFlags that we need.

# H3 related memory variants
python do_build_all_memory_variants_prepend_r8a7795() {
    d.setVarFlag('MEMORY_VARIANTS', 'default', 'RCAR_DRAM_SPLIT=1 RCAR_DRAM_LPDDR4_MEMCONF=0')
    d.setVarFlag('MEMORY_VARIANTS', '2x2g',    'RCAR_DRAM_SPLIT=2 RCAR_DRAM_CHANNEL=5')
    d.setVarFlag('MEMORY_VARIANTS', '4x2g',    'RCAR_DRAM_SPLIT=1')
}

# E3 related memory variants
python do_build_all_memory_variants_prepend_r8a77990() {
    d.setVarFlag('MEMORY_VARIANTS', 'default', 'RCAR_DRAM_DDR3L_MEMCONF=0 RCAR_DRAM_DDR3L_MEMDUAL=0')
    d.setVarFlag('MEMORY_VARIANTS', '4d'     ' 'RCAR_DRAM_DDR3L_MEMCONF=1 RCAR_DRAM_DDR3L_MEMDUAL=1')
}


# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"


# Default do_compile need to be disabled to avoid build with incorrect build options
do_compile[noexec] = "1"

do_build_all_memory_variants[dirs] = "${B}"
addtask do_build_all_memory_variants before do_compile after do_configure

# This function need to be 'BitBake-Style Python' to work with variable flags
python do_build_all_memory_variants() {
    # get all memory variants
    mem_dict = d.getVarFlags('MEMORY_VARIANTS')
    # get 'names' of memory variants (like 2x2g, 4x2g, 4d, etc)
    # they are used as suffixes for build products
    mem_conf_list = list(mem_dict.keys())

    for mem_conf in mem_conf_list:
        bb.plain(mem_conf + ': ' + mem_dict[mem_conf])
        if (mem_conf == 'default'):
            d.setVar('MEM_SUFFIX', '')
        else:
            d.setVar('MEM_SUFFIX', '-' + mem_conf)

        d.setVar('MEM_OPTIONS', mem_dict[mem_conf])

        bb.build.exec_func('ipl_make_and_move', d)
}

# dedicated folder for built IPL's
FIRMWARE_DEPLOY_DIR = "${DEPLOYDIR}/firmware"

# This function need to be 'shell'-ish to call `oe_runmake` and `install`
ipl_make_and_move() {
    oe_runmake distclean
    oe_runmake bl2 bl31 rcar_layout_tool rcar_srecord PLAT=${PLATFORM} SPD=opteed MBEDTLS_COMMON_MK=1 ${ATFW_OPT} ${MEM_OPTIONS}

    install -d ${FIRMWARE_DEPLOY_DIR}
    # Copy IPL to deploy folder
    # There is no '-' before MEM_SUFFIX because MEM_SUFFIX either is empty or contains '-'
    install -m 0644 ${S}/build/${PLATFORM}/release/bl2/bl2.elf   ${FIRMWARE_DEPLOY_DIR}/bl2-${MACHINE}${MEM_SUFFIX}.elf
    install -m 0644 ${S}/build/${PLATFORM}/release/bl2.bin       ${FIRMWARE_DEPLOY_DIR}/bl2-${MACHINE}${MEM_SUFFIX}.bin
    install -m 0644 ${S}/build/${PLATFORM}/release/bl2.srec      ${FIRMWARE_DEPLOY_DIR}/bl2-${MACHINE}${MEM_SUFFIX}.srec
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31/bl31.elf ${FIRMWARE_DEPLOY_DIR}/bl31-${MACHINE}${MEM_SUFFIX}.elf
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31.bin      ${FIRMWARE_DEPLOY_DIR}/bl31-${MACHINE}${MEM_SUFFIX}.bin
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31.srec     ${FIRMWARE_DEPLOY_DIR}/bl31-${MACHINE}${MEM_SUFFIX}.srec
    if [ -z ${MEM_SUFFIX} ]; then
        # sa0 and sa6 doesn't change for different momory variants, so we build them for default variant only
        install -m 0644 ${S}/tools/renesas/rcar_layout_create/bootparam_sa0.bin  ${FIRMWARE_DEPLOY_DIR}/bootparam_sa0.bin
        install -m 0644 ${S}/tools/renesas/rcar_layout_create/bootparam_sa0.srec ${FIRMWARE_DEPLOY_DIR}/bootparam_sa0.srec
        install -m 0644 ${S}/tools/renesas/rcar_layout_create/cert_header_sa6.bin  ${FIRMWARE_DEPLOY_DIR}/cert_header_sa6.bin
        install -m 0644 ${S}/tools/renesas/rcar_layout_create/cert_header_sa6.srec ${FIRMWARE_DEPLOY_DIR}/cert_header_sa6.srec
        install -m 0644 ${S}/tools/renesas/rcar_layout_create/cert_header_sa6_emmc.bin  ${FIRMWARE_DEPLOY_DIR}/cert_header_sa6_emmc.bin
        install -m 0644 ${S}/tools/renesas/rcar_layout_create/cert_header_sa6_emmc.srec ${FIRMWARE_DEPLOY_DIR}/cert_header_sa6_emmc.srec
    fi
}

# We need to provide empty do_deploy() for proper
# copy of binaries from DEPLOYDIR to DEPLOY_DIR_IMAGE
addtask deploy before do_build after do_build_all_memory_variants
do_deploy() {
  :
}
