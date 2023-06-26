# It actually provides libgles3, but Renesas recipe omits this for
# some reason. We need this for kmscube at least
PROVIDES += "virtual/libgles3"

python () {
    if d.getVar('XT_PREBUILT_GSX_DIR', True):
        # We use prebuilt package provided by user
        d.appendVar('FILESEXTRAPATHS', '${XT_PREBUILT_GSX_DIR}/${XT_DOM_NAME}:')
    else:
        # We prepare package by gles-um-compile
        d.appendVar('FILESEXTRAPATHS', '${DEPLOY_DIR_IMAGE}/prebuilt/${XT_DOM_NAME}:')
        d.appendVarFlag('do_fetch', 'depends', ' gles-um-compile:do_deploy ')
}
