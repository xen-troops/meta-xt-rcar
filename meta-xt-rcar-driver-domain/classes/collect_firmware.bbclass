# Implementation note.
# Why do we move files inside DEPLOY_DIR_IMAGE and in separate task?
# If we put code into regular deploy:append() there is no
# guarantee that our append will be the last append. As result,
# we may meet a situation that some code wants to work (create link
# for example) with files that are already moved to other folder.
# To avoid such confusion we add a dedicated task after do_deploy()
# and we handle files directly in DEPLOY_DIR_IMAGE.

# Variable FIRMWARE_SUBDIR is used to specify destination subfolder
# for following items under DEPLOY_DIR_IMAGE:
#  - ATF (bl2, bl31, sa0, sa6)
#  - OP-TEE OS (tee.bin)
#  - u-boot
FIRMWARE_SUBDIR ??= 'firmware'

do_collect_firmware() {
    install -d ${DEPLOY_DIR_IMAGE}/${FIRMWARE_SUBDIR}

    # This task is used after do_deploy, so we can iterate
    # through DEPLOYDIR to identify files need to be relocated
    # inside DEPLOY_DIR_IMAGE.
    for filename in ${DEPLOYDIR}/*; do
        mv ${DEPLOY_DIR_IMAGE}/`basename ${filename}` ${DEPLOY_DIR_IMAGE}/${FIRMWARE_SUBDIR}
    done
}
addtask do_collect_firmware after do_deploy before do_build
