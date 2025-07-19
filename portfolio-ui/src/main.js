import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import VueLazyload from 'vue-lazyload';

createApp(App)
    .use(VueLazyload)
    .mount('#app')
