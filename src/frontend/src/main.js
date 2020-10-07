import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false

Vue.prototype.$showTip = function(e, id) {
  console.log(e)
  console.log(id)
  var targetElement = document.getElementById(id);
  if (e.target.classList.contains('active')) {
    targetElement.classList.remove('show')
    e.target.classList.remove('active')
  } else {
    targetElement.classList.add('show')
    e.target.classList.add('active')
  }
}

new Vue({
  render: h => h(App),
}).$mount('#app')
