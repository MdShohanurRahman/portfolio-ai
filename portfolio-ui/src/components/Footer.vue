<script>
export default {
  name: "Footer",
  props: {
    data: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      socialLinks: {
        github: "https://github.com/MdShohanurRahman",
        linkedin: "https://www.linkedin.com/in/shohanur-rahman-674897180/",
        leetcode: "https://leetcode.com/u/user1633lF/",
        facebook: "https://www.facebook.com/shohanur.rahman3/",
      }
    };
  }
}
</script>

<template>
  <div class="footer text-center">
    <div class="mb-4">
      <p class="text-gray-200">{{ data.name }} © {{ new Date().getFullYear() }}</p>
    </div>
    <div class="flex justify-center space-x-4 mb-4">
      <a class="text-gray-200 hover:text-cyan-400" :href="`mailto:${data.email}`">
        <i class="fas fa-envelope text-cyan-400"></i> {{ data.email }}
      </a>
      <a class="text-gray-200 hover:text-cyan-400" href="`https://wa.me/${data.phone}`" target="_blank">
        <i class="fas fa-phone text-cyan-400"></i> {{ data.phone }}
      </a>
    </div>
    <div class="flex justify-center space-x-4">
      <a class="text-gray-200 hover:text-cyan-400" :href="socialLinks.github" target="_blank">
        <i class="fab fa-github text-2xl"></i>
      </a>
      <a class="text-gray-200 hover:text-cyan-400" :href="socialLinks.linkedin" target="_blank">
        <i class="fab fa-linkedin text-2xl" style="color: #60a5fa;"></i>
      </a>
      <a class="text-gray-200 hover:text-cyan-400" :href="socialLinks.linkedin" target="_blank">
        <i class="fas fa-code text-2xl" style="color: #10b981;"></i>
      </a>
    </div>
  </div>
</template>

<style scoped>

</style>