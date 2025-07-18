<script>
export default {
  name: "Footer"
}
</script>

<template>
  <footer class="footer text-center">
    <div class="mb-4">
      <p class="text-gray-200">John Doe © 2025</p>
      <p class="text-gray-400">Software Engineer | Full Stack Developer</p>
    </div>
    <div class="flex justify-center space-x-4 mb-4">
      <a class="text-gray-200 hover:text-cyan-400" href="mailto:john.doe@example.com">
        <i class="fas fa-envelope text-cyan-400"></i> john.doe@example.com
      </a>
      <a class="text-gray-200 hover:text-cyan-400" href="tel:+1234567890">
        <i class="fas fa-phone text-cyan-400"></i> +1 (234) 567-890
      </a>
    </div>
    <div class="flex justify-center space-x-4">
      <a class="text-gray-200 hover:text-cyan-400" href="https://github.com">
        <i class="fab fa-github text-2xl" style="color: #f472b6;"></i>
      </a>
      <a class="text-gray-200 hover:text-cyan-400" href="https://linkedin.com">
        <i class="fab fa-linkedin text-2xl" style="color: #60a5fa;"></i>
      </a>
      <a class="text-gray-200 hover:text-cyan-400" href="https://leetcode.com">
        <i class="fas fa-code text-2xl" style="color: #10b981;"></i>
      </a>
    </div>
  </footer>
</template>

<style scoped>

</style>