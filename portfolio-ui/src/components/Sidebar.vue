<script>
export default {
  name: "Sidebar",
  data() {
    return {};
  },
}
</script>

<template>
  <nav class="sidebar md:flex flex-col items-center py-6 space-y-6 text-gray-400 hidden">
    <a class="flex items-center space-x-2 hover:text-cyan-400" href="#home">
      <i class="fas fa-home text-xl nav-icon" style="color: #22d3ee;"></i>
      <span class="nav-text">Home</span>
    </a>
    <a class="flex items-center space-x-2 hover:text-cyan-400" href="#about">
      <i class="fas fa-user text-xl nav-icon" style="color: #f472b6;"></i>
      <span class="nav-text">About</span>
    </a>
    <a class="flex items-center space-x-2 hover:text-cyan-400" href="#experience">
      <i class="fas fa-briefcase text-xl nav-icon" style="color: #facc15;"></i>
      <span class="nav-text">Experience</span>
    </a>
    <a class="flex items-center space-x-2 hover:text-cyan-400" href="#projects">
      <i class="fas fa-code text-xl nav-icon" style="color: #10b981;"></i>
      <span class="nav-text">Projects</span>
    </a>
    <a class="flex items-center space-x-2 hover:text-cyan-400" href="#education">
      <i class="fas fa-graduation-cap text-xl nav-icon" style="color: #a78bfa;"></i>
      <span class="nav-text">Education</span>
    </a>
    <a class="flex items-center space-x-2 hover:text-cyan-400" href="#skills">
      <i class="fas fa-tools text-xl nav-icon" style="color: #ef4444;"></i>
      <span class="nav-text">Skills</span>
    </a>
    <a class="flex items-center space-x-2 hover:text-cyan-400" href="#certificates">
      <i class="fas fa-certificate text-xl nav-icon" style="color: #60a5fa;"></i>
      <span class="nav-text">Certificates</span>
    </a>
    <a class="flex items-center space-x-2 hover:text-cyan-400" href="#blogs">
      <i class="fas fa-blog text-xl nav-icon" style="color: #f59e0b;"></i>
      <span class="nav-text">Blogs</span>
    </a>
  </nav>
  <nav class="md:hidden flex justify-around p-4 bg-gray-900 text-gray-400">
    <a class="hover:text-cyan-400" href="#home"><i class="fas fa-home"></i></a>
    <a class="hover:text-cyan-400" href="#about"><i class="fas fa-user"></i></a>
    <a class="hover:text-cyan-400" href="#experience"><i class="fas fa-briefcase"></i></a>
    <a class="hover:text-cyan-400" href="#projects"><i class="fas fa-code"></i></a>
    <a class="hover:text-cyan-400" href="#education"><i class="fas fa-graduation-cap"></i></a>
    <a class="hover:text-cyan-400" href="#skills"><i class="fas fa-tools"></i></a>
    <a class="hover:text-cyan-400" href="#certificates"><i class="fas fa-certificate"></i></a>
    <a class="hover:text-cyan-400" href="#blogs"><i class="fas fa-blog"></i></a>
  </nav>
</template>

<style>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 80px;
  background-color: #1a1a2e;
  transition: width 0.3s ease;
  z-index: 1000;
  overflow: hidden;
}

.sidebar:hover {
  width: 200px;
}

.group:has(.sidebar:hover) main {
  margin-left: 200px;
  transition: margin-left 0.3s ease;
}

main {
  margin-left: 80px;
  transition: margin-left 0.3s ease;
}

.nav-icon {
  min-width: 20px;
  text-align: center;
}

.nav-text {
  opacity: 0;
  transition: opacity 0.3s ease;
  margin-left: 10px;
}

.sidebar:hover .nav-text {
  opacity: 1;
}
</style>