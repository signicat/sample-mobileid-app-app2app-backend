<template>
  <div id="app">
    <header class="signicat-header sticky">
      <div class="header-wrapper">
        <div class="menu-toggler" @click="toggleMenu">
          <i class="material-icons open-menu-icon" v-if="!menuOpen">menu</i>
          <i class="material-icons close-menu-icon" v-if="menuOpen">close</i>
          <span>Menu</span>
        </div>
        <a class="logo" @click="openSignicatSiteInNewTab">
          <img src="signicat-logo-black.svg" data-bind="attr: {src: logo}" />
        </a>
        <nav v-bind:class="{'slide-in': menuOpen}">
          <h2>MobileID Sample</h2>
          <a v-bind:class="{ active: activeMenuPoint === 'disclaimer'}" @click="setMenuPoint('disclaimer')">Disclaimer</a>
        </nav>
      </div>
    </header>
    <div class="content-wrapper">
      <Disclaimer v-if="activeMenuPoint === 'disclaimer'" />
    </div>
  </div>
</template>

<script>
import Disclaimer from "./components/Disclaimer.vue";

export default {
  name: "App",
  components: {
    Disclaimer
  },
  data() {
    return {
      activeMenuPoint: 'disclaimer',
      menuOpen: false
    }
  },
  methods: {
    setMenuPoint: function(point) {
      this.activeMenuPoint = point;
    },
    toggleMenu: function() {
      this.menuOpen = !this.menuOpen;
    },
    openSignicatSiteInNewTab: function () {
      window.open('https://www.signicat.com', '_blank');
    }
  }
};
</script>

<style lang="less">
html body {
  background-image: url('/background-image/connecting-dots.png');
  background-position: 100% 100%;
  background-size: 300px;
  .signicat-header {
    background-color: white;
    .menu-toggler {
      color: #3f505f;
    }
  }

  h1 {
    margin-top: 0;
  }
  h3 {
    margin-top: 30px;
  }
  h2 + h3 {
    margin-top: 20px;
  }
  p {
    font-size: 16px;
    color: #60768a;
  }
  p.header-description {
    margin-bottom:10px;
    color:#60768a;
    font-size: 16px;
  }
  .content-wrapper {
    max-width: 900px;
    p {
      line-height: normal;
    }
  }
  .question-mark-button {
    width: 30px;
    height: 30px;
    background-image: url("/icons/help-normal.svg");
    background-size: contain;
    background-position: 50% 50%;
    float: right;

    &.active {
      background-image: url("/icons/invalid-name.svg");
    }
  }
  .info-text-box {
    border-radius: 4px;
    background-color: #f0f4f8;
    display: none;
    &.show {
      display: block;
    }
    p {
      color: #3f505f;
      padding: 20px;
    }
  }
}
</style>
