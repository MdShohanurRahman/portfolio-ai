<script>
export default {
  name: "Skills"
}
</script>

<template>
  <section id="skills" class="py-12">
    <h2 class="text-3xl font-bold text-cyan-400 mb-8 text-center">// Skills & Proficiency</h2>
    <div class="code-block p-8 rounded-xl shadow-lg bg-gradient-to-br from-gray-800 to-gray-900">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <!-- Languages -->
        <div>
          <h3 class="text-xl font-semibold text-cyan-400 mb-4 text-center">Languages</h3>
          <ul class="space-y-3">
            <li class="skill-item flex items-center justify-between bg-gray-700 px-4 py-2 rounded-lg shadow">
              <span>Java</span>
              <span class="text-cyan-400 text-sm">Expert, 7 Years</span>
            </li>
            <li class="skill-item flex items-center justify-between bg-gray-700 px-4 py-2 rounded-lg shadow">
              <span>Python</span>
              <span class="text-cyan-400 text-sm">Advanced, 5 Years</span>
            </li>
            <li class="skill-item flex items-center justify-between bg-gray-700 px-4 py-2 rounded-lg shadow">
              <span>JavaScript</span>
              <span class="text-cyan-400 text-sm">Expert, 6 Years</span>
            </li>
          </ul>
        </div>
        <!-- Frameworks -->
        <div>
          <h3 class="text-xl font-semibold text-cyan-400 mb-4 text-center">Frameworks</h3>
          <ul class="space-y-3">
            <li class="skill-item flex items-center justify-between bg-gray-700 px-4 py-2 rounded-lg shadow">
              <span>React</span>
              <span class="text-cyan-400 text-sm">Advanced, 4 Years</span>
            </li>
            <li class="skill-item flex items-center justify-between bg-gray-700 px-4 py-2 rounded-lg shadow">
              <span>Django</span>
              <span class="text-cyan-400 text-sm">Intermediate, 3 Years</span>
            </li>
          </ul>
        </div>
        <!-- Tools -->
        <div>
          <h3 class="text-xl font-semibold text-cyan-400 mb-4 text-center">Tools</h3>
          <ul class="space-y-3">
            <li class="skill-item flex items-center justify-between bg-gray-700 px-4 py-2 rounded-lg shadow">
              <span>AWS</span>
              <span class="text-cyan-400 text-sm">Advanced, 4 Years</span>
            </li>
            <li class="skill-item flex items-center justify-between bg-gray-700 px-4 py-2 rounded-lg shadow">
              <span>Docker</span>
              <span class="text-cyan-400 text-sm">Intermediate, 2 Years</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>

</style>