FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RENESAS_BSP_URL = "git://github.com/xen-troops/linux.git"

BRANCH = "v5.10/rcar-5.0.0.rc4-xt0.1"
SRCREV = "${AUTOREV}"
LINUX_VERSION = "5.10.0"

SRC_URI_append = " \
    file://defconfig \
    file://xen-chosen.dtsi;subdir=git/arch/${ARCH}/boot/dts/renesas \
"

# M3
ADDITIONAL_DEVICE_TREES_r8a7796 = " \
    r8a77960-salvator-x-xen.dts \
    r8a77960-salvator-x-domd.dts \
    r8a77961-salvator-xs-2x4g-xen.dts \
    r8a77961-salvator-xs-2x4g-domd.dts \
"

# Ignore in-tree defconfig
KBUILD_DEFCONFIG = ""

# Add ADDITIONAL_DEVICE_TREES to SRC_URIs and to KERNEL_DEVICETREEs
python __anonymous () {
    for dts in (d.getVar("ADDITIONAL_DEVICE_TREES") or "").split():
        d.appendVar("SRC_URI", " file://%s;subdir=git/arch/${ARCH}/boot/dts/renesas"%dts)
        dtb = dts[:-3] + "dtb"
        d.appendVar("KERNEL_DEVICETREE", " renesas/%s"%dtb)
}
