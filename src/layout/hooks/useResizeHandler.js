/*
 *                   ___====-_  _-====___
 *             _--^^^#####//      \\#####^^^--_
 *          _-^##########// (    ) \\##########^-_
 *         -############//  |\^^/|  \\############-
 *       _/############//   (@::@)   \############\_
 *      /#############((     \\//     ))#############\
 *     -###############\\    (oo)    //###############-
 *    -#################\\  / VV \  //#################-
 *   -###################\\/      \//###################-
 *  _#/|##########/\######(   /\   )######/\##########|\#_
 *  |/ |#/\#/\#/\/  \#/\##\  |  |  /##/\#/  \/\#/\#/\#| \|
 *  `  |/  V  V  `   V  \#\| |  | |/#/  V   '  V  V  \|  '
 *     `   `  `      `   / | |  | | \   '      '  '   '
 *                      (  | |  | |  )
 *                     __\ | |  | | /__
 *                    (vvv(VVV)(VVV)vvv)
 *
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *                神兽保佑            永无BUG
 *
 * @Descripttion:
 * @version:
 *
 * @LastEditors:  zsj
 *: 
 * @Author:   zsj

 */

import { storeToRefs } from 'pinia'
import { useApp } from '@/pinia/modules/app'
import { onBeforeMount, onBeforeUnmount, computed } from 'vue'

const WIDTH = 768
export const useResizeHandler = () => {
  const appStore = useApp()
  const { sidebar } = storeToRefs(appStore)
  const { setDevice, setCollapse } = appStore
  const collapse = computed(() => sidebar.value.collapse)

  const isMobile = () => {
    return window.innerWidth < WIDTH
  }

  const resizeHandler = () => {
    if (isMobile()) {
      setDevice('mobile')
      setCollapse(1)
    } else {
      setDevice('desktop')
      setCollapse(collapse.value)
    }
  }

  onBeforeMount(() => {
    resizeHandler()
    window.addEventListener('resize', resizeHandler)
  })

  onBeforeUnmount(() => {
    window.removeEventListener('resize', resizeHandler)
  })
}
