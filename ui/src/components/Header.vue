<script>
export default {
  name: "Header"
}
</script>

<template>
  <header id="home" class="header-bg relative shadow-2xl rounded-b-3xl overflow-hidden" style="background: linear-gradient(135deg, #091717 0%, #0d1b16 100%);">
    <div class="header-content text-center relative z-10 px-6 py-12 md:py-20">

      <h1 class="text-5xl md:text-7xl font-extrabold text-white mb-4 animate-pulse drop-shadow-2xl tracking-tight">
        Shohanur Rahman
      </h1>
      <p class="text-2xl md:text-3xl text-cyan-200 mb-8 font-mono tracking-wide">
        Software Engineer <span class="mx-2 text-cyan-400">|</span> Crafting Code with Passion
      </p>
      <div class="flex flex-col md:flex-row justify-center items-center md:space-x-8 space-y-2 md:space-y-0 mb-8">
        <a class="text-white hover:text-cyan-300 flex items-center space-x-2 bg-cyan-900/60 px-4 py-2 rounded-lg shadow transition hover:scale-105">
          <i class="fas fa-envelope text-cyan-300 text-xl"></i>
          <span class="font-semibold">john.doe@example.com</span>
        </a>
        <a class="text-white hover:text-cyan-300 flex items-center space-x-2 bg-cyan-900/60 px-4 py-2 rounded-lg shadow transition hover:scale-105">
          <i class="fas fa-phone text-cyan-300 text-xl"></i>
          <span class="font-semibold">+1 (234) 567-890</span>
        </a>
        <a class="text-white hover:text-cyan-300 flex items-center space-x-2 bg-cyan-900/60 px-4 py-2 rounded-lg shadow transition hover:scale-105">
          <i class="fas fa-briefcase text-cyan-300 text-xl"></i>
          <span class="font-semibold">5+ Years Experience</span>
        </a>
      </div>
      <div class="flex justify-center space-x-8 mb-8">
        <a class="group" href="https://github.com" aria-label="GitHub">
                    <span class="inline-block bg-black/40 p-4 rounded-full shadow-lg transition group-hover:scale-110 group-hover:bg-cyan-900/80">
                        <i class="fab fa-github text-3xl text-pink-400 group-hover:text-cyan-400"></i>
                    </span>
        </a>
        <a class="group" href="https://linkedin.com" aria-label="LinkedIn">
                    <span class="inline-block bg-black/40 p-4 rounded-full shadow-lg transition group-hover:scale-110 group-hover:bg-cyan-900/80">
                        <i class="fab fa-linkedin text-3xl text-blue-400 group-hover:text-cyan-400"></i>
                    </span>
        </a>
        <a class="group" href="https://leetcode.com" aria-label="LeetCode">
                    <span class="inline-block bg-black/40 p-4 rounded-full shadow-lg transition group-hover:scale-110 group-hover:bg-cyan-900/80">
                        <i class="fas fa-code text-3xl text-green-400 group-hover:text-cyan-400"></i>
                    </span>
        </a>
      </div>
      <div class="flex flex-col md:flex-row justify-center items-center space-y-2 md:space-y-0 md:space-x-6">
        <a class="btn bg-gradient-to-r from-cyan-500 to-blue-600 text-white px-10 py-4 rounded-xl font-bold shadow-lg hover:from-cyan-400 hover:to-blue-400 transition text-lg tracking-wide"
           href="/resume.pdf">
          <i class="fas fa-download mr-2"></i>Get Resume
        </a>
      </div>
    </div>
  </header>
</template>

<style scoped>

</style>