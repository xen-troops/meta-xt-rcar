DESCRIPTION = "Prebuilt package of kernel module of PowerVR GPU"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    ${@bb.utils.contains('XT_USE_DDK1_11', '1', 'file://GPL-COPYING;md5=60422928ba677faaa13d6ab5f5baaa1e', 'file://GPL-COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263', d)} \
    file://MIT-COPYING;md5=8c2810fa6bfdc5ae5c15a0c1ade34054 \
"

COMPATIBLE_MACHINE = "(r8a7795|r8a7796)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

PVRKM_URL ??= "git://git@gitpct.epam.com/epmd-aepr/pvr_km_vgpu_img.git"
PVRKM_BRANCH ??= "${@bb.utils.contains('XT_USE_DDK1_11', '1', '1.11/5516664_5.1.0', '1.15/6052913_5.9.0', d)}"
PVRKM_SRCREV ??= "${@bb.utils.contains('XT_USE_DDK1_11', '1', \
                    '6994a4be87075975665fcef1fd95c087b0a007c8', \
                    '487a9326d5446bf4ed2808980ab0b12d8fd3f9e9', \
                    d)}"
SRCREV = "${PVRKM_SRCREV}"

SRC_URI = "${PVRKM_URL};protocol=ssh;branch=${PVRKM_BRANCH}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

RESULT_NAME = "^^???-NOT-SUPPORTED-???^^"
RESULT_NAME:r8a7795 = "GSX_KM_H3.tar.bz2"
RESULT_NAME:r8a7796 = "GSX_KM_M3.tar.bz2"

ASSEMBLE_FOLDER = "rogue_km"

do_install() {
    install -d ${D}/${ASSEMBLE_FOLDER}

    # copy files from root
    install -m 644 ${WORKDIR}/git/GPL-COPYING ${D}/${ASSEMBLE_FOLDER}
    install -m 644 ${WORKDIR}/git/INSTALL     ${D}/${ASSEMBLE_FOLDER}
    install -m 644 ${WORKDIR}/git/Makefile    ${D}/${ASSEMBLE_FOLDER}
    install -m 644 ${WORKDIR}/git/MIT-COPYING ${D}/${ASSEMBLE_FOLDER}
    install -m 644 ${WORKDIR}/git/README      ${D}/${ASSEMBLE_FOLDER}
    # we use `cp` instead of `install` due to need
    # to recursively copy lots of nested subfolders
    cp --recursive --no-preserve=ownership ${WORKDIR}/git/build     ${D}/${ASSEMBLE_FOLDER}
    cp --recursive --no-preserve=ownership ${WORKDIR}/git/generated ${D}/${ASSEMBLE_FOLDER}
    cp --recursive --no-preserve=ownership ${WORKDIR}/git/hwdefs    ${D}/${ASSEMBLE_FOLDER}
    cp --recursive --no-preserve=ownership ${WORKDIR}/git/include   ${D}/${ASSEMBLE_FOLDER}
    cp --recursive --no-preserve=ownership ${WORKDIR}/git/kernel    ${D}/${ASSEMBLE_FOLDER}
    cp --recursive --no-preserve=ownership ${WORKDIR}/git/services  ${D}/${ASSEMBLE_FOLDER}
}

inherit deploy nopackages

do_deploy() {
    install -d ${DEPLOYDIR}/prebuilt/${XT_DOM_NAME}
    tar -C ${D} -cjf ${DEPLOYDIR}/prebuilt/${XT_DOM_NAME}/${RESULT_NAME} ${ASSEMBLE_FOLDER}
    rm -r ${D}/${ASSEMBLE_FOLDER}
}

addtask do_deploy after do_install
