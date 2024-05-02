FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://config_unsupp.cfg"

do_configure:append() {
    if [ ${XEN_TARGET_ARCH} = "arm64" -o ${XEN_TARGET_ARCH} = "arm" ]; then
        ${S}/xen/tools/kconfig/merge_config.sh -m -O \
            ${S}/xen ${S}/xen/.config ${S}/xen/arch/arm/configs/xt_defconfig
    fi
}
