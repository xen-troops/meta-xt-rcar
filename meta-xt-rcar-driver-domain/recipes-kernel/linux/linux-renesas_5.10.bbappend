FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RENESAS_BSP_URL = "git://github.com/xen-troops/linux.git"

BRANCH = "v5.10/rcar-5.0.0.rc4-xt0.1"
SRCREV = "${AUTOREV}"
LINUX_VERSION = "5.10.0"

SRC_URI_append = " \
    file://defconfig \
    file://xen-chosen.dtsi;subdir=git/arch/${ARCH}/boot/dts/renesas \
    file://ulcb-ab.dtsi;subdir=git/arch/${ARCH}/boot/dts/renesas \
"

# Salavotor-X(S) M3
ADDITIONAL_DEVICE_TREES_r8a7796 = " \
    r8a77960-salvator-x-xen.dts \
    r8a77960-salvator-x-domd.dts \
    r8a77961-salvator-xs-2x4g-xen.dts \
    r8a77961-salvator-xs-2x4g-domd.dts \
"

# Salavotor-X(S) H3
ADDITIONAL_DEVICE_TREES_r8a7795 = " \
    r8a7795-salvator-x-4x2g-domd.dts \
    r8a7795-salvator-x-4x2g-xen.dts \
    r8a7795-salvator-x-domd.dts \
    r8a7795-salvator-xs-4x2g-domd.dts \
    r8a7795-salvator-xs-4x2g-xen.dts \
    r8a7795-salvator-xs-domd.dts \
    r8a7795-salvator-xs-xen.dts \
    r8a7795-salvator-x-xen.dts \
"

# H3ULCB
ADDITIONAL_DEVICE_TREES_h3ulcb = " \
    r8a77951-h3ulcb-4x2g-domd.dts \
    r8a77951-h3ulcb-4x2g-xen.dts \
    r8a77951-h3ulcb-4x2g-kf-domd.dts \
    r8a77951-h3ulcb-4x2g-kf-xen.dts \
    r8a77951-h3ulcb-4x2g-ab-domd.dts \
    r8a77951-h3ulcb-4x2g-ab-xen.dts \
    r8a77951-h3ulcb-domd.dts \
    r8a77951-h3ulcb-xen.dts \
"

# M3ULCB
ADDITIONAL_DEVICE_TREES_m3ulcb = " \
   r8a7796-m3ulcb-domd.dts \
   r8a7796-m3ulcb-xen.dts \
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
