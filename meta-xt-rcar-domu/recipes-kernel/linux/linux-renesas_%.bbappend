FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

RENESAS_BSP_URL = "git://github.com/xen-troops/linux.git"

BRANCH = "v5.10.41/rcar-5.1.4.1-xt0.1"
SRCREV = "${AUTOREV}"
LINUX_VERSION = "5.10.41"

KBUILD_DEFCONFIG_rcar = ""
SRC_URI:append = " \
	file://defconfig \
	file://0001-Revert-tee-optee-Added-optee_rcar_suspend_sync-and-o.patch \
	file://0002-Revert-tee-optee-replace-use-of-ioremap_nocache-with.patch \
	file://0003-Revert-tee-optee-add-SMC-of-START_DLOG_OUTPUT-to-rca.patch \
	file://0004-Revert-tee-optee-Modify-duration-of-spinlock-for-lis.patch \
	file://0005-Revert-tee-optee-Change-wait-to-interruptible.patch \
	file://0006-Revert-tee-optee-Change-workqueue-to-kthread-in-debu.patch \
	file://0007-Revert-tee-Add-a-macro-to-provide-a-register-functio.patch \
	file://0008-Revert-tee-optee-add-r-car-original-function.patch \
	file://salvator-generic-domu.dts;subdir=git/arch/${ARCH}/boot/dts/renesas \
"

KERNEL_DEVICETREE:append = " renesas/salvator-generic-domu.dtb"

KERNEL_FEATURES:remove = "cfg/virtio.scc"

# Ignore in-tree defconfig
KBUILD_DEFCONFIG = ""

do_deploy:append() {
    find ${D}/boot -iname "vmlinux*" -exec tar -cJvf ${STAGING_KERNEL_BUILDDIR}/vmlinux.tar.xz {} \;
}

