DESCRIPTION = "OP-TEE OS"

LICENSE = "BSD-2-Clause & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

inherit deploy python3native
DEPENDS = "python3-pycryptodome-native python3-pyelftools-native"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/xen-troops/optee_os.git;branch=3.9-xt-linux;protocol=https"
PV = "git${SRCPV}"
SRCREV = "${AUTOREV}"

COMPATIBLE_MACHINE ?= "(salvator-x|h3ulcb|m3ulcb|m3nulcb)"
OPTEEMACHINE = "rcar"
PLATFORM = "rcar"

OPTEEFLAVOR = "${XT_OP_TEE_FLAVOUR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
export CROSS_COMPILE64="${TARGET_PREFIX}"

# Let the Makefile handle setting up the flags as it is a standalone application
LD[unexport] = "1"
LDFLAGS[unexport] = "1"
export CCcore="${CC}"
export LDcore="${LD}"
libdir[unexport] = "1"

EXTRA_OEMAKE = "-e MAKEFLAGS= \
		   PLATFORM=rcar \
		   PLATFORM_FLAVOR=${OPTEEFLAVOR} \
		   CFG_ARM64_core=y \
		   CFG_VIRTUALIZATION=y \
		   CROSS_COMPILE_core=${HOST_PREFIX} \
		   CROSS_COMPILE_ta_arm64=${HOST_PREFIX} \
		   ta-targets=ta_arm64 \
		   CFLAGS64=--sysroot=${STAGING_DIR_HOST} \
		   CFG_SYSTEM_PTA=y \
	       "

# Enable Android-specific features if we are building Android guest
ANDROID_EXTRA_OEMAKE = " \
	       CFG_ASN1_PARSER=y \
	       CFG_CORE_MBEDTLS_MPI=y \
	       CFG_RPMB_FS=y \
	       CFG_RPMB_WRITE_KEY=y \
	       CFG_EARLY_TA=y \
	       CFG_IN_TREE_EARLY_TAS=avb/023f8f1a-292a-432b-8fc4-de8471358067 \
	       "

EXTRA_OEMAKE += "${@bb.utils.contains('XT_GUEST_INSTALL', 'doma', '${ANDROID_EXTRA_OEMAKE}', '', d)}"

OPTEE_ARCH:aarch64 = "arm64"

do_configure() {
}

do_install () {
    install -d ${D}/usr/include/optee/export-user_ta_${OPTEE_ARCH}/

    for f in ${S}/out/arm-plat-rcar/export-ta_${OPTEE_ARCH}/*; do
        cp -aR $f ${D}/usr/include/optee/export-user_ta_${OPTEE_ARCH}/
    done
}

do_deploy() {
    # Create deploy folder
    install -d ${DEPLOYDIR}

    # Copy TEE OS to deploy folder
    install -m 0644 ${S}/out/arm-plat-${PLATFORM}/core/tee.elf ${DEPLOYDIR}/tee-${MACHINE}.elf
    install -m 0644 ${S}/out/arm-plat-${PLATFORM}/core/tee-raw.bin ${DEPLOYDIR}/tee-${MACHINE}.bin
    install -m 0644 ${S}/out/arm-plat-${PLATFORM}/core/tee.srec ${DEPLOYDIR}/tee-${MACHINE}.srec
}
addtask deploy before do_build after do_compile

FILES:${PN} = ""
FILES:${PN}-staticdev = "/usr/include/optee/"

# Move tee.bin into 'firmware' folder
inherit collect_firmware
