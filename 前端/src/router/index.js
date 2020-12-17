import Vue from 'vue'
import Router from 'vue-router'
import login from '@/views/Login'
import register from '@/views/Register'
import main from '@/views/index'
import downloadList from '@/views/downloadList.vue'
import FileVersionManger from '@/views/FileVersionManger.vue'
import Setting from '@/views/Setting.vue'
import Schedule from '@/views/Schedule.vue'
import video from '@/views/video.vue'

Vue.use(Router)
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default new Router({
  mode: "history",
  routes: [{
      path: '/',
      name: 'main',
      component: main,
      meta: {
        hidden: true,
        title: "我的网盘"
      }
    },
    {
      path: '/User/login',
      name: login,
      component: login,
      meta: {
        hidden: true,
        title: "登陆"
      }
    },
    {
      path: '/register',
      name: register,
      component: register,
      meta: {
        hidden: true,
        title: "注册"
      }
    },
    {
      path: '/storage',
      name: 'main',
      component: main,
      meta: {
        hidden: true,
        title: "我的网盘"
      }
    },
    {
      path: '/video',
      name: 'video',
      component: video
    },
    {
      path: '/storage/BtDownload',
      name: 'downloadList',
      component: downloadList
    },
    {
      path: '/storage/FileVersionManger',
      name: 'FileVersionManger',
      component: FileVersionManger,
      meta: {
        hidden: true,
        title: "版本控制"
      }
    },
    {
      path: '/storage/admin',
      name: 'Setting',
      component: Setting,
      meta: {
        hidden: true,
        title: "设置"
      }
    }, {
      path: '/storage/Schedule',
      name: 'Schedule',
      component: Schedule,
      meta: {
        hidden: true,
        title: "定时任务"
      }
    }
  ]
})
