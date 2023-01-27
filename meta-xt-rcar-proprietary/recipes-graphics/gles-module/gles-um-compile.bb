DESCRIPTION = "PowerVR GPU user module built from sources"
LICENSE = "CLOSED"

inherit deploy nopackages pkgconfig

PV = "1.11"

PVRUM_URL ?= "git://git@gitpct.epam.com/epmd-aepr/pvr_um_vgpu_img.git"
PVRUM_BRANCH = "1.11/5516664_5.1.0"
SRCREV = "${AUTOREV}"

COMPATIBLE_MACHINE = "(r8a7795|r8a7796)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPILER_URL = "${TOPDIR}/../proprietary/rcar/meta/META_Embedded_Toolkit_2.8.1.0.3.1.zip"
PVRUM_DISCIMAGE = "${D}"
BUILD = "release"
S = "${WORKDIR}/git"

PVRUM_BUILD_DIR = "^^???-NOT-SUPPORTED-???^^"
PVRUM_BUILD_DIR:r8a7795 = "r8a7795_linux"
PVRUM_BUILD_DIR:r8a7796 = "r8a7796_linux"

RESULT_NAME = "^^???-NOT-SUPPORTED-???^^"
RESULT_NAME:r8a7795 = "r8a77951_linux_gsx_binaries_gles.tar.bz2"
RESULT_NAME:r8a7796 = "r8a77960_linux_gsx_binaries_gles.tar.bz2"

DEPENDS = " \
    wayland-native \
    bison-native \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'libgbm wayland-kms', '', d)} \
    virtual/kernel \
    clang-native \
    python-native \
    python-clang-native \
    wayland-protocols \
"

DEPENDS:remove = " \
    binutils-cross-aarch64 \
"

RDEPENDS:${PN} = " \
    python \
    python3-core \
"

SRC_URI = " \
    ${PVRUM_URL};protocol=ssh;branch=${PVRUM_BRANCH} \
    file://${COMPILER_URL} \
    file://pvr-addons/etc/powervr.ini \
    file://pvr-addons/lib/pkgconfig/egl.pc \
    file://pvr-addons/lib/pkgconfig/glesv2.pc \
    file://rc.pvr.service \
"

# FIXME: because of LLVM this recipe behaves much better if not run with -jMAX
PARALLEL_MAKE = "-j ${@oe.utils.cpu_count() - 1}"
EXTRA_OEMAKE += "CROSS_COMPILE=${TARGET_PREFIX}"
EXTRA_OEMAKE += "PVR_BUILD_DIR=${PVRUM_BUILD_DIR}"
EXTRA_OEMAKE += "DISCIMAGE=${PVRUM_DISCIMAGE}"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_BUILDDIR}"
EXTRA_OEMAKE += "METAG_INST_ROOT=${S}/metag/2.8"
EXTRA_OEMAKE += "LLVM_BUILD_DIR=${STAGING_LIBDIR}/llvm_build_dir"
EXTRA_OEMAKE += "PVRSRV_VZ_NUM_OSID=${XT_PVR_NUM_OSID}"
EXTRA_OEMAKE += "BIN_DESTDIR=${bindir}"
EXTRA_OEMAKE += "SHARE_DESTDIR=${datadir}"
EXCLUDED_APIS = ""
EXTRA_OEMAKE += "EXCLUDED_APIS='${EXCLUDED_APIS}'"
EXTRA_OEMAKE += "${@bb.utils.contains('XT_GUEST_INSTALL', 'doma', '', 'EXCLUDE_FENCE_SYNC_SUPPORT:=1', d)}"
EXTRA_OEMAKE += "LIBCLANG_PATH=${STAGING_LIBDIR_NATIVE}/libclang.so"
EXTRA_OEMAKE:remove = "LLVM_BUILD_DIR=${STAGING_LIBDIR}/llvm_build_dir"

do_configure:prepend() {
    if [ -f ${WORKDIR}/Meta_Embedded_Toolkit-2.8.1.CentOS-5.tar.gz ]
    then
        tar -xzf ${WORKDIR}/Meta_Embedded_Toolkit-2.8.1.CentOS-5.tar.gz
        rm -f ${WORKDIR}/Meta_Embedded_Toolkit-2.8.1.CentOS-5.tar.gz
        rm -f ${WORKDIR}/*.pdf
        ./Meta_Embedded_Toolkit-2.8.1.CentOS-5/install.sh x64 ${S}
        rm -rf ${S}/Meta_Embedded_Toolkit-2.8.1.CentOS-5
    fi
}

do_compile:prepend() {
    export PATH="$PATH:${WORKDIR}/recipe-sysroot-native/usr/bin/python-native"
}

# It is not enough just adding dependency on virtual/kernel
# https://stackoverflow.com/questions/34793697/how-to-write-a-bitbake-driver-recipe-which-requires-kernel-source-header-files
do_compile[depends] += "linux-renesas:do_shared_workdir"

# We disable "already-stripped" QA check to suppress
# multiple warnings regarding stripped so-libraries.
INSANE_SKIP:${PN} += "already-stripped"

ASSEMBLE_FOLDER = "rogue"

# we use do_install to run IMG's install.sh under fakeroot
do_install() {
    oe_runmake install
    sed -i 's/soc/passthrough/g' ${D}/etc/udev/rules.d/72-pvr-seat.rules

    install -d ${D}/${ASSEMBLE_FOLDER}

    # etc/
    install -d ${D}/${ASSEMBLE_FOLDER}${sysconfdir}
    install -m 644 ${WORKDIR}/pvr-addons/etc/powervr.ini ${D}/${ASSEMBLE_FOLDER}${sysconfdir}
    install -d ${D}/${ASSEMBLE_FOLDER}${sysconfdir}/init.d
    install -m 755 ${D}${sysconfdir}/init.d/rc.pvr  ${D}/${ASSEMBLE_FOLDER}${sysconfdir}/init.d/
    install -d ${D}/${ASSEMBLE_FOLDER}${sysconfdir}/udev/rules.d
    install -m 644 ${D}${sysconfdir}/udev/rules.d/72-pvr-seat.rules ${D}/${ASSEMBLE_FOLDER}${sysconfdir}/udev/rules.d/

    # lib/
    install -d ${D}/${ASSEMBLE_FOLDER}/lib/firmware
    install -m 644 ${D}/lib/firmware/* ${D}/${ASSEMBLE_FOLDER}/lib/firmware/

    # usr/include/
    install -d ${D}/${ASSEMBLE_FOLDER}${includedir}/EGL
    install -m 644 ${S}/include/khronos/EGL/*.h ${D}/${ASSEMBLE_FOLDER}${includedir}/EGL/
    install -d ${D}/${ASSEMBLE_FOLDER}${includedir}/GLES2
    install -m 644 ${S}/include/khronos/GLES2/*.h ${D}/${ASSEMBLE_FOLDER}${includedir}/GLES2/
    install -d ${D}/${ASSEMBLE_FOLDER}${includedir}/GLES3
    install -m 644 ${S}/include/khronos/GLES3/*.h ${D}/${ASSEMBLE_FOLDER}${includedir}/GLES3/
    install -d ${D}/${ASSEMBLE_FOLDER}${includedir}/KHR
    install -m 644 ${S}/include/khronos/KHR/khrplatform.h ${D}/${ASSEMBLE_FOLDER}${includedir}/KHR/khrplatform.h
    install -m 644 ${S}/include/khronos/drv/EGL/eglext_REL.h ${D}/${ASSEMBLE_FOLDER}${includedir}/EGL/

    # usr/lib/
    install -d ${D}/${ASSEMBLE_FOLDER}${libdir}
    install -m 755 ${D}${libdir}/*.so ${D}/${ASSEMBLE_FOLDER}${libdir}/
    install -d ${D}/${ASSEMBLE_FOLDER}${libdir}/pkgconfig
    install -m 644 ${WORKDIR}/pvr-addons/lib/pkgconfig/*.pc ${D}/${ASSEMBLE_FOLDER}${libdir}/pkgconfig/

    # usr/local/bin
    install -d ${D}/${ASSEMBLE_FOLDER}/usr/local/bin/
    install -m 755 ${D}/usr/bin/dlcsrv_REL ${D}/${ASSEMBLE_FOLDER}/usr/local/bin/
}

# We use do_deploy to put packed archive to ${DEPLOY_DIR_IMAGE}
# where it can be used by user and by gles-user-module.bb
do_deploy() {
    install -d ${DEPLOYDIR}/prebuilt/${XT_DOM_NAME}
    tar -C ${D} -cjf ${DEPLOYDIR}/prebuilt/${XT_DOM_NAME}/${RESULT_NAME} ${ASSEMBLE_FOLDER}
    rm -r ${D}/${ASSEMBLE_FOLDER}
}

addtask do_deploy after do_install

