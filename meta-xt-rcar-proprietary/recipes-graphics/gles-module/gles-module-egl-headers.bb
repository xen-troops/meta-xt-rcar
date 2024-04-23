
PVRUM_URL ?= "git://git@gitpct.epam.com/epmd-aepr/pvr_um_vgpu_img.git"
PVRUM_BRANCH = "${@bb.utils.contains('XT_USE_DDK1_11', '1', '1.11/5516664_5.1.0', '1.15/6052913_5.9.0', d)}"
SRCREV = "${@bb.utils.contains('XT_USE_DDK1_11', '1', \
            '4ed61e604d925bfce8ab16645f48425a581496c7', \
            'e172e94c7d61186fe5b64db68fdef949c9ee2189', \
            d)}"

SRC_URI = " \
    ${PVRUM_URL};protocol=ssh;branch=${PVRUM_BRANCH} \
"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(r8a7795|r8a7796)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

DESCRIPTION = "PowerVR GPU user EGL header files"
LICENSE = "CLOSED"

PN = "gles-module-egl-headers"
PR = "r1"

do_compile[noexec] = "1"

do_configure() {
    :
}

do_install() {
    # Install header files
    if [ -d ${S}/include/khronos/CL ]; then
        install -d ${D}/${includedir}/CL
        install -m 644 ${S}/include/khronos/CL/*.h ${D}/${includedir}/CL/
    fi
    install -d ${D}/${includedir}/EGL
    install -m 644 ${S}/include/khronos/EGL/*.h ${D}/${includedir}/EGL/
    install -d ${D}/${includedir}/GLES2
    install -m 644 ${S}/include/khronos/GLES2/*.h ${D}/${includedir}/GLES2/
    install -d ${D}/${includedir}/GLES3
    install -m 644 ${S}/include/khronos/GLES3/*.h ${D}/${includedir}/GLES3/
    install -d ${D}/${includedir}/KHR
    install -m 644 ${S}/include/khronos/KHR/khrplatform.h ${D}/${includedir}/KHR/khrplatform.h
    install -m 644 ${S}/include/khronos/drv/EGL/eglext_REL.h ${D}/${includedir}/EGL/
}

RDEPENDS:${PN}-dev = ""
