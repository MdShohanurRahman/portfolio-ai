<script>
export default {
  name: "Header",
  props: {
    data: {
      type: Object,
      required: true
    }
  }
}
</script>

<template>
  <div id="home" class="relative overflow-hidden bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900">
    <!-- Animated background elements -->
    <div class="absolute inset-0 z-0 opacity-20">
      <div class="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMDAlIiBoZWlnaHQ9IjEwMCUiPjxkZWZzPjxwYXR0ZXJuIGlkPSJwYXR0ZXJuIiB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHBhdHRlcm5Vbml0cz0idXNlclNwYWNlT25Vc2UiIHBhdHRlcm5UcmFuc2Zvcm09InJvdGF0ZSg0NSkiPjxyZWN0IHdpZHRoPSIyMCIgaGVpZ2h0PSIyMCIgZmlsbD0icmdiYSgyMCwgMTg0LCAyNTUsIDAuMDUpIj48L3JlY3Q+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI3BhdHRlcm4pIj48L3JlY3Q+PC9zdmc+')]"></div>
      <div class="absolute inset-0 bg-gradient-to-t from-cyan-900/10 to-transparent"></div>
    </div>

    <div class="header-content relative z-10 px-6 py-16 md:py-28 lg:py-32 text-center">
      <!-- Name with animated gradient -->
      <h1 class="text-5xl md:text-7xl lg:text-8xl font-extrabold mb-4 tracking-tight">
      <span class="bg-clip-text text-transparent bg-gradient-to-r from-cyan-400 via-blue-400 to-purple-400 animate-gradient">
        {{ data.name }}
      </span>
      </h1>

      <!-- Title with typing animation effect -->
      <div class="typewriter-container mb-8">
        <p class="text-2xl md:text-3xl font-mono text-cyan-300">
          <span>{{ data.title }}</span>
          <span class="text-cyan-400 mx-3">|</span>
          <span>{{ data.titleTag }}</span>
        </p>
      </div>

      <!-- Contact info chips -->
      <div class="flex flex-col md:flex-row justify-center items-center gap-3 mb-8">
        <a class="contact-chip group" :href="data.email">
          <i class="fas fa-envelope text-cyan-300 group-hover:text-white"></i>
          <span class="ml-2">{{data.email}}</span>
        </a>
        <a class="contact-chip group" :href="`https://wa.me/${data.phone}`" target="_blank">
          <i class="fas fa-phone text-cyan-300 group-hover:text-white"></i>
          <span class="ml-2">{{ data.phone }}</span>
        </a>
        <div class="contact-chip">
          <i class="fas fa-briefcase text-cyan-300"></i>
          <span class="ml-2"> {{data.yearOfExperience}}+ Years Experience</span>
        </div>
      </div>

      <!-- Social links with hover glow -->
      <div class="flex justify-center gap-6 mb-10">
        <a class="social-icon github" :href="data.socialLinks.github" aria-label="GitHub">
          <i class="fab fa-github text-2xl"></i>
        </a>
        <a class="social-icon linkedin" :href="data.socialLinks.linkedin" aria-label="LinkedIn">
          <i class="fab fa-linkedin-in text-2xl" style="color: #60a5fa;"></i>
        </a>
        <a class="social-icon leetcode text-2xl" :href="data.socialLinks.leetcode" aria-label="LeetCode">
          <i class="fas fa-code" style="color: #10b981;"></i>
        </a>
      </div>

      <!-- CTA button with pulse animation -->
      <div class="animate-pulse hover:animate-none">
        <a class="cta-button" :href="data?.resumeLinks?.pdf" target="_blank">
          <i class="fas fa-download mr-2"></i>Download Resume
        </a>
      </div>
    </div>

    <!-- Scroll indicator -->
    <div class="absolute bottom-8 left-0 right-0 flex justify-center animate-bounce">
      <a href="#about" class="text-white text-2xl">
        <i class="fas fa-chevron-down"></i>
      </a>
    </div>
  </div>


</template>

<style scoped>
/* Animation for gradient text */
@keyframes gradient {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.animate-gradient {
  background-size: 200% 200%;
  animation: gradient 6s ease infinite;
}

/* Typewriter effect */
.typewriter-text {
  display: inline-block;
  overflow: hidden;
  border-right: 2px solid #22d3ee;
  white-space: nowrap;
  animation: typing 3.5s steps(40, end), blink-caret 0.75s step-end infinite;
}

.delay-1000 {
  animation-delay: 1s;
}

@keyframes typing {
  from { width: 0 }
  to { width: 100% }
}

@keyframes blink-caret {
  from, to { border-color: transparent }
  50% { border-color: #22d3ee; }
}

/* Contact chips */
.contact-chip {
  @apply flex items-center space-x-2 bg-gray-800/60 hover:bg-cyan-700/60 px-4 py-2 rounded-full text-white transition-all duration-300 hover:scale-105 border border-gray-700 hover:border-cyan-400;
}

/* Social icons */
.social-icon {
  @apply w-12 h-12 rounded-full flex items-center justify-center text-xl bg-gray-800/60 border border-gray-700 text-white transition-all duration-300 hover:scale-110 shadow-lg;
}

.social-icon.github {
  @apply hover:bg-[#333] hover:text-white hover:shadow-purple-500/20;
}
.social-icon.linkedin {
  @apply hover:bg-[#0077B5] hover:text-white hover:shadow-blue-500/20;
}
.social-icon.leetcode {
  @apply hover:bg-[#FFA116] hover:text-white hover:shadow-yellow-500/20;
}

/* CTA button */
.cta-button {
  @apply inline-flex items-center bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 text-white px-8 py-3 md:px-10 md:py-4 rounded-xl font-bold shadow-lg transition-all duration-300 transform hover:scale-105 text-lg tracking-wide;
}
</style>