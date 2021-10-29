#Make a pseudo to ignore the ${KERNELSRC}/include
#It is necessary because some of the do_install methods from different components
#modify the directory ${KERNELSRC}/include, it bring the pseudo error and stop the build.

PSEUDO_IGNORE_PATHS .= ",${KERNELSRC}/include"
