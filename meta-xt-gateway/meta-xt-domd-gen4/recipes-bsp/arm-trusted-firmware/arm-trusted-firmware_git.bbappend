FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Start cores in EL2 mode
SRC_URI += "\
    file://0001-HACK-s4-boot-U-Boot-in-EL2-mode.patch \
    file://0002-HACK-s4-Configure-IPMMU-registers.patch \
    file://0003-HACK-s4-perform-direct-reset.patch \
"

do_deploy:append() {
    if [ -f "${DEPLOYDIR}/bl31-${MACHINE}.srec" ]; then
        ln -sfr "${DEPLOYDIR}/bl31-${MACHINE}.srec" "${DEPLOYDIR}/bl31.srec"
    fi
}
