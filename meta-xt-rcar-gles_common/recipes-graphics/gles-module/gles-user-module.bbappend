python () {
    if d.getVar('XT_PREBUILT_GSX_DIR', True):
        # We use prebuilt package provided by user
        d.appendVar('FILESEXTRAPATHS', '${XT_PREBUILT_GSX_DIR}/${XT_DOM_NAME}:')
    else:
        # We prepare package by gles-um-compile
        d.appendVar('FILESEXTRAPATHS', '${DEPLOY_DIR_IMAGE}/prebuilt/${XT_DOM_NAME}:')
        d.appendVarFlag('do_fetch', 'depends', ' gles-um-compile:do_deploy ')
}
