FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

RENESAS_BSP_URL = "git://github.com/xen-troops/linux.git"
BRANCH = "v5.10.41/rcar-5.1.7.rc11.2-xt"
SRCREV = "769ab722739878c3c1aaa1571f6ca996b135f8f6"

SRC_URI:append = "\
    file://defconfig \
"

SRC_URI:append = " \
    file://rswitch.cfg \
    file://dmatest.cfg \
    file://gpio.cfg \
    file://l3offload.cfg \
"

SRC_URI:append:r8a779f0 = " file://r8a779f0.cfg"
SRC_URI:append:r8a779g0 = " file://r8a779g0.cfg"

ADDITIONAL_DEVICE_TREES = "${XT_DEVICE_TREES}"

# Ignore in-tree defconfig
KBUILD_DEFCONFIG = ""

# Don't build defaul DTBs
KERNEL_DEVICETREE = ""

# Add ADDITIONAL_DEVICE_TREES to SRC_URIs and to KERNEL_DEVICETREEs
python __anonymous () {
    for fname in (d.getVar("ADDITIONAL_DEVICE_TREES") or "").split():
        dts = fname[:-3] + "dts"
        d.appendVar("SRC_URI", " file://%s;subdir=git/arch/${ARCH}/boot/dts/renesas"%dts)
        dtb = fname[:-3] + "dtb"
        d.appendVar("KERNEL_DEVICETREE", " renesas/%s"%dtb)
}

